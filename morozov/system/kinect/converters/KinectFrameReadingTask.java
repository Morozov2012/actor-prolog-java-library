// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.converters;

import morozov.run.*;
import morozov.system.*;
import morozov.system.checker.signals.*;
import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.interfaces.*;
import morozov.system.kinect.modes.*;
import morozov.system.kinect.modes.interfaces.*;
import morozov.system.files.*;
import morozov.system.files.errors.*;

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.LinkedList;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class KinectFrameReadingTask extends Thread {
	//
	protected KinectBufferInterface owner;
	//
	protected AtomicBoolean stopThisThread= new AtomicBoolean(false);
	protected AtomicBoolean stopAfterSingleReading= new AtomicBoolean(false);
	protected AtomicReference<Double> slowMotionCoefficient= new AtomicReference<>(-1.0);
	protected AtomicBoolean inputIsOpen= new AtomicBoolean(false);
	protected AtomicReference<KinectDisplayingModeInterface> displayingMode= new AtomicReference<>();
	//
	protected long initialRecordTime= -1;
	protected long initialRealTime= -1;
	protected long totalNumberOfFrames= 0;
	//
	public static long defaultMaximalWaitingPeriod= 1000;
	public AtomicLong maximalWaitingPeriod= new AtomicLong(defaultMaximalWaitingPeriod);
	//
	protected LinkedList<KinectFrameInterface> history= new LinkedList<>();
	protected int defaultReadBufferSize= 0;
	protected AtomicInteger readBufferSize= new AtomicInteger(defaultReadBufferSize);
	//
	protected ExtendedFileName extendedFileName;
	protected InputStream inputStream;
	protected InputStream bufferedInputStream;
	protected ObjectInputStream objectInputStream;
	//
	protected ReentrantLock lock= new ReentrantLock();
	protected Condition condition= lock.newCondition();
	//
	protected long currentRecordTime;
	protected long currentRealTime;
	protected long recordTimeDelta;
	protected long currentTimeDelta;
	protected long waitingPeriod;
	protected long nanosTimeout;
	//
	///////////////////////////////////////////////////////////////
	//
	public KinectFrameReadingTask(KinectBufferInterface kinectBuffer) {
		owner= kinectBuffer;
		setDaemon(true);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public KinectDisplayingModeInterface getDisplayingMode() {
		return displayingMode.get();
	}
	public void setDisplayingMode(KinectDisplayingModeInterface mode) {
		displayingMode.set(mode);
	}
	//
	public static long getDefaultMaximalWaitingPeriod() {
		return defaultMaximalWaitingPeriod;
	}
	public long getMaximalWaitingPeriod() {
		return maximalWaitingPeriod.get();
	}
	public void setMaximalWaitingPeriod(long number) {
		maximalWaitingPeriod.set(number);
	}
	public void setMaximalWaitingPeriod(IntegerAttribute maximalFrameDelay) {
		maximalWaitingPeriod.set(maximalFrameDelay.getValue(getDefaultMaximalWaitingPeriod()));
	}
	//
	public int getReadBufferSize() {
		return readBufferSize.get();
	}
	public void setReadBufferSize(int length) {
		readBufferSize.set(length);
	}
	public void resetReadBufferSize() {
		readBufferSize.set(defaultReadBufferSize);
	}
	//
	public void setStopAfterSingleReading(boolean mode) {
		stopAfterSingleReading.set(mode);
	}
	//
	public void setSlowMotionCoefficient(NumericalValue coefficient) {
		if (coefficient != null) {
			slowMotionCoefficient.set(coefficient.toDouble());
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void startReading(ExtendedFileName fileName, int timeout, CharacterSet characterSet, KinectDisplayingModeInterface currentDisplayingMode, StaticContext staticContext) {
		synchronized (this) {
			if (!inputIsOpen.get()) {
				openReading(fileName,timeout,characterSet,currentDisplayingMode,staticContext);
			};
			resetCounters();
			stopThisThread.set(false);
			startProcessIfNecessary();
		}
	}
	//
	public void activateReading() {
		synchronized (this) {
			if (stopThisThread.get()) {
				resetCounters();
				stopThisThread.set(false);
				startProcessIfNecessary();
			}
		}
	}
	//
	protected void startProcessIfNecessary() {
		if (!isAlive()) {
			start();
		} else {
			notify();
		}
	}
	//
	public void suspendReading() {
		stopThisThread.set(true);
	}
	//
	public boolean isSuspended() {
		return inputIsOpen() && stopThisThread.get();
	}
	public boolean isNotSuspended() {
		return !isSuspended();
	}
	//
	public boolean inputIsOpen() {
		return inputIsOpen.get();
	}
	public boolean eof() {
		return !inputIsOpen.get();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void openReading(ExtendedFileName fileName, int timeout, CharacterSet characterSet, KinectDisplayingModeInterface currentDisplayingMode, StaticContext staticContext) {
		closeReading();
		synchronized (this) {
			inputIsOpen.set(false);
			resetCounters();
			extendedFileName= fileName;
			try {
				if (fileName.doesExist(false,timeout,characterSet,staticContext)) {
					try {
						inputStream= fileName.getInputStreamOfUniversalResource(timeout,staticContext);
						bufferedInputStream= new BufferedInputStream(inputStream);
						objectInputStream= new ObjectInputStream(bufferedInputStream);
						displayingMode.set(currentDisplayingMode);
						inputIsOpen.set(true);
					} catch (CannotRetrieveContent e) {
						throw new FileInputOutputError(fileName.toString(),e);
					};
				} else {
					throw new FileIsNotFound(fileName.toString());
				}
			} catch (EOFException e) {
			} catch (IOException e) {
				throw new FileInputOutputError(fileName.toString(),e);
			}
		}
	}
	//
	protected void resetCounters() {
		synchronized (this) {
			initialRecordTime= -1;
			initialRealTime= -1;
			totalNumberOfFrames= 0;
		};
		synchronized (history) {
			history.clear();
		}
	}
	//
	public void closeReading() {
		suspendReading();
		synchronized (this) {
			try {
				inputIsOpen.set(false);
				if (bufferedInputStream != null) {
					bufferedInputStream.close();
					bufferedInputStream= null;
				};
				if (objectInputStream != null) {
					objectInputStream.close();
					objectInputStream= null;
				};
			} catch (IOException e) {
				if (extendedFileName != null) {
					throw new FileInputOutputError(extendedFileName.toString(),e);
				} else {
					throw new FileInputOutputError("",e);
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void run() {
		while (true) {
			long numberOfFrameToBeAcquired= totalNumberOfFrames + 1;
			try {
				KinectFrameInterface frame;
				synchronized (this) {
					if (stopThisThread.get()) {
						wait();
						continue;
					};
					if (inputIsOpen.get()) {
						frame= (KinectFrameInterface)objectInputStream.readObject();
						KinectDataArrayType dataArrayType= frame.getDataArrayType();
						if (dataArrayType==KinectDataArrayType.MODE_FRAME) {
							owner.sendFrame(frame);
							continue;
						} else {
							KinectDisplayingModeInterface currentDisplayingMode= displayingMode.get();
							if (currentDisplayingMode != null) {
								if (!currentDisplayingMode.requiresFrameType(dataArrayType)) {
									continue;
								}
							}
						};
						currentRecordTime= frame.getActingFrameTime();
						if (currentRecordTime < 0) {
							owner.sendFrame(frame);
							continue;
						};
						delayReading(currentRecordTime);
					} else {
						stopThisThread.set(true);
						continue;
					}
				};
				if (stopAfterSingleReading.get()) {
					stopThisThread.set(true);
				};
				if (owner.sendFrame(frame)) {
					synchronized (history) {
						history.addLast(frame);
						if (history.size() > readBufferSize.get()) {
							history.removeFirst();
						}
					}
				} else {
					if (stopAfterSingleReading.get()) {
						stopThisThread.set(false);
					}
				}
			} catch (EOFException e) {
				closeReading();
				stopThisThread.set(true);
				owner.completeDataTransfer(totalNumberOfFrames);
				continue;
			} catch (IOException e) {
				closeReading();
				stopThisThread.set(true);
				owner.completeDataTransfer(numberOfFrameToBeAcquired,new FileInputOutputError(extendedFileName.toString(),e));
			} catch (ClassNotFoundException e) {
				closeReading();
				stopThisThread.set(true);
				owner.completeDataTransfer(numberOfFrameToBeAcquired,new FileInputOutputError(extendedFileName.toString(),e));
			} catch (InterruptedException e) {
				closeReading();
				stopThisThread.set(true);
				owner.completeDataTransfer(totalNumberOfFrames);
				return;
			}
		}
	}
	//
	protected void delayReading(long currentRecordTime) {
		double coefficient= slowMotionCoefficient.get();
		if (coefficient > 0.0) {
			currentRecordTime= (long)(currentRecordTime*coefficient);
		};
		currentRealTime= System.currentTimeMillis();
		totalNumberOfFrames++;
		if (totalNumberOfFrames <= 1) {
			initialRealTime= currentRealTime;
			initialRecordTime= currentRecordTime;
		} else {
			recordTimeDelta= currentRecordTime - initialRecordTime;
			currentTimeDelta= currentRealTime - initialRealTime;
			waitingPeriod= recordTimeDelta - currentTimeDelta;
			long waitingBound= maximalWaitingPeriod.get();
			if (coefficient > 0.0) {
				waitingBound= (long)(waitingBound * coefficient);
			};
			if (waitingPeriod > waitingBound) {
				initialRecordTime= initialRecordTime + waitingPeriod;
				waitingPeriod= 0;
			};
			if (waitingPeriod > 0) {
///////////////////////////////////////////////////////////////////////
try {
	nanosTimeout= waitingPeriod * 1_000_000;
	lock.lock();
	try {
		WaitingLoop: while (true) {
			nanosTimeout= condition.awaitNanos(nanosTimeout);
			if (nanosTimeout <= 0) {
				break WaitingLoop;
			}
		}
	} finally {
		lock.unlock();
	}
} catch (InterruptedException e) {
} catch (ThreadDeath e) {
	return;
}
///////////////////////////////////////////////////////////////////////
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean retrieveBufferedFrame(int number) {
		synchronized (history) {
			int relativeNumber= number - 1;
			if (relativeNumber < 0) {
				relativeNumber= 0;
			};
			int bufferSize= getReadBufferSize();
			int historySize= history.size();
			int maximalIndex= bufferSize - 1;
			if (historySize < bufferSize) {
				maximalIndex= historySize - 1;
				relativeNumber= relativeNumber * historySize / bufferSize;
			};
			if (relativeNumber > maximalIndex) {
				relativeNumber= maximalIndex;
			};
			int position= historySize-1-relativeNumber;
			KinectFrameInterface frame= history.get(relativeNumber);
			owner.transferBufferedFrame(frame,position);
		};
		return true;
	}
}