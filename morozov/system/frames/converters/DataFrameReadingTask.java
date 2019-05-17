// (c) 2018 Alexei A. Morozov

package morozov.system.frames.converters;

import morozov.run.*;
import morozov.system.*;
import morozov.system.checker.signals.*;
import morozov.system.converters.*;
import morozov.system.datum.*;
import morozov.system.files.*;
import morozov.system.files.errors.*;
import morozov.system.frames.converters.interfaces.*;
import morozov.system.frames.interfaces.*;
import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.modes.*;
import morozov.system.kinect.modes.interfaces.*;
import morozov.system.modes.*;

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.io.EOFException;
import java.io.IOException;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class DataFrameReadingTask extends Thread {
	//
	protected DataFrameConsumerInterface dataConsumer;
	//
	protected AtomicBoolean stopAfterSingleReading= new AtomicBoolean(false);
	protected AtomicReference<Double> slowMotionCoefficient= new AtomicReference<>(-1.0);
	//
	protected static long defaultMaximalFrameDelay= 1000;
	protected AtomicLong maximalFrameDelay= new AtomicLong(defaultMaximalFrameDelay);
	//
	protected AtomicReference<KinectDisplayingModeInterface> displayingMode= new AtomicReference<>();
	//
	protected ExtendedFileName extendedFileName;
	protected InputStream inputStream;
	protected InputStream bufferedInputStream;
	protected ObjectInputStream objectInputStream;
	//
	protected AtomicBoolean stopThisThread= new AtomicBoolean(true);
	protected AtomicBoolean inputIsOpen= new AtomicBoolean(false);
	//
	protected long initialRecordTime= -1;
	protected long initialRealTime= -1;
	protected long totalNumberOfFrames= 0;
	//
	protected ReentrantLock lock= new ReentrantLock();
	protected Condition condition= lock.newCondition();
	//
	///////////////////////////////////////////////////////////////
	//
	protected AtomicBoolean quickReadingMode= new AtomicBoolean(false);
	protected AtomicInteger numberOfFramesToBeRead= new AtomicInteger(0);
	//
	protected AtomicBoolean timeSynchronizationMode= new AtomicBoolean(false);
	protected AtomicLong targetTime= new AtomicLong(0);
	protected AtomicInteger numberOfExtraFramesToBeRead= new AtomicInteger(0);
	//
	///////////////////////////////////////////////////////////////
	//
	public DataFrameReadingTask() {
		setPriority(Thread.MAX_PRIORITY);
		setDaemon(true);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setDataConsumer(DataFrameConsumerInterface consumer) {
		dataConsumer= consumer;
	}
	//
	public void setStopAfterSingleReading(boolean mode) {
		stopAfterSingleReading.set(mode);
	}
	//
	public void setSlowMotionCoefficient(NumericalValue coefficient) {
		if (coefficient != null) {
			slowMotionCoefficient.set(NumericalValueConverters.toDouble(coefficient));
		}
	}
	//
	public static long getDefaultMaximalFrameDelay() {
		return defaultMaximalFrameDelay;
	}
	public long getMaximalFrameDelay() {
		return maximalFrameDelay.get();
	}
	public void setMaximalFrameDelay(long number) {
		maximalFrameDelay.set(number);
	}
	public void setMaximalFrameDelay(IntegerAttribute delay) {
		maximalFrameDelay.set(delay.getValue(getDefaultMaximalFrameDelay()));
	}
	//
	public KinectDisplayingModeInterface getDisplayingMode() {
		return displayingMode.get();
	}
	public void setDisplayingMode(KinectDisplayingModeInterface mode) {
		displayingMode.set(mode);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void startReading(boolean doActivateReading, ExtendedFileName fileName, int timeout, CharacterSet characterSet, StaticContext staticContext) {
		synchronized (this) {
			if (!inputIsOpen.get()) {
				openReading(fileName,timeout,characterSet,staticContext);
			};
			resetCounters();
			if (doActivateReading) {
				stopThisThread.set(false);
				startProcessIfNecessary();
			}
		}
	}
	//
	public void activateReading() {
		if (stopThisThread.get()) {
			synchronized (this) {
				if (stopThisThread.get()) {
					resetCounters();
					stopThisThread.set(false);
					startProcessIfNecessary();
				}
			}
		}
	}
	//
	protected void startProcessIfNecessary() {
		if (!isAlive()) {
			start();
		} else {
			notifyAll();
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
	public boolean stopThisThread() {
		return stopThisThread.get();
	}
	public boolean eof() {
		return !inputIsOpen.get();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void openReading(ExtendedFileName fileName, int timeout, CharacterSet characterSet, StaticContext staticContext) {
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
						objectInputStream= new DataStoreInputStream(bufferedInputStream);
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
	public void resetCounters() {
		synchronized (this) {
			initialRecordTime= -1;
			initialRealTime= -1;
			totalNumberOfFrames= 0;
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
				}
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
///////////////////////////////////////////////////////////////////////
DataFrameInterface dataFrame= null;
CompoundFrameInterface compoundFrame= null;
KinectFrameInterface kinectFrame= null;
synchronized (this) {
	if (stopThisThread.get()) {
		wait();
		continue;
	};
	if (inputIsOpen.get()) {
		try {
			Object object= objectInputStream.readObject();
			if (object instanceof DataFrameInterface) {
				dataFrame= (DataFrameInterface)object;
			} else if (object instanceof CompoundFrameInterface) {
				compoundFrame= (CompoundFrameInterface)object;
			} else if (object instanceof KinectFrameInterface) {
				kinectFrame= (KinectFrameInterface)object;
			} else {
				System.err.printf("Unexpected object type error: %s\n",object);
				continue;
			}
		} catch (EOFException e) {
			throw e;
		} catch (Throwable e) {
			System.err.printf("Object reading error: %s\n",e);
			throw e;
		};
		DataArrayType dataArrayType= null;
		CompoundArrayType compoundArrayType= null;
		KinectDataArrayType kinectArrayType= null;
		long currentRecordTime;
		if (dataFrame != null) {
			dataArrayType= dataFrame.getDataArrayType();
			if (dataArrayType==DataArrayType.CAMERA_FLASH_FRAME) {
				if (dataConsumer != null) {
					dataConsumer.sendDataFrame(dataFrame);
				};
				continue;
			};
			currentRecordTime= dataFrame.getTime();
		} else if (compoundFrame != null) {
			compoundArrayType= compoundFrame.getCompoundArrayType();
			if (compoundArrayType==CompoundArrayType.DESCRIPTION_FRAME) {
				if (dataConsumer != null) {
					dataConsumer.sendCompoundFrame(compoundFrame);
				};
				continue;
			};
			currentRecordTime= compoundFrame.getTime();
		} else if (kinectFrame != null) {
			kinectArrayType= kinectFrame.getDataArrayType();
			if (kinectArrayType==KinectDataArrayType.MODE_FRAME) {
				if (dataConsumer != null) {
					dataConsumer.sendKinectFrame(kinectFrame);
				};
				continue;
			} else {
				KinectDisplayingModeInterface currentDisplayingMode= displayingMode.get();
				if (currentDisplayingMode != null) {
					if (!currentDisplayingMode.requiresFrameType(kinectArrayType)) {
						continue;
					}
				}
			};
			currentRecordTime= kinectFrame.getActingFrameTime();
		} else {
			System.err.printf("Unexpected object type error\n");
			continue;
		};
		boolean doDelayReading= delayIsNecessary(currentRecordTime,dataArrayType,compoundArrayType,kinectArrayType);
		if (doDelayReading) {
			delayReading(currentRecordTime);
		}
	} else { // NO inputIsOpen.get()
		stopThisThread.set(true);
		continue;
	}
};
if (dataConsumer != null) {
	if (stopAfterSingleReading.get()) {
		stopThisThread.set(true);
	};
	boolean frameIsAccepted= false;
	if (dataFrame != null) {
		frameIsAccepted= dataConsumer.sendDataFrame(dataFrame);
	} else if (compoundFrame != null) {
		frameIsAccepted= dataConsumer.sendCompoundFrame(compoundFrame);
	} else if (kinectFrame != null) {
		frameIsAccepted= dataConsumer.sendKinectFrame(kinectFrame);
	};
	if (!frameIsAccepted) {
		if (stopAfterSingleReading.get()) {
			stopThisThread.set(false);
		}
	}
}
///////////////////////////////////////////////////////////////////////
			} catch (EOFException e) {
				closeReading();
				stopThisThread.set(true);
				if (dataConsumer != null) {
					dataConsumer.completeDataReading(totalNumberOfFrames);
				};
				continue;
			} catch (IOException e) {
				closeReading();
				stopThisThread.set(true);
				if (dataConsumer != null) {
					dataConsumer.completeDataReading(numberOfFrameToBeAcquired,new FileInputOutputError(extendedFileName.toString(),e));
				}
			} catch (ClassNotFoundException e) {
				closeReading();
				stopThisThread.set(true);
				if (dataConsumer != null) {
					dataConsumer.completeDataReading(numberOfFrameToBeAcquired,new FileInputOutputError(extendedFileName.toString(),e));
				}
			} catch (InterruptedException e) {
				closeReading();
				stopThisThread.set(true);
				if (dataConsumer != null) {
					dataConsumer.completeDataReading(totalNumberOfFrames);
				};
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
		long currentRealTime= System.currentTimeMillis();
		totalNumberOfFrames++;
		if (totalNumberOfFrames <= 1) {
			initialRealTime= currentRealTime;
			initialRecordTime= currentRecordTime;
		} else {
			long recordTimeDelta= currentRecordTime - initialRecordTime;
			long currentTimeDelta= currentRealTime - initialRealTime;
			long waitingPeriod= recordTimeDelta - currentTimeDelta;
			long waitingBound= maximalFrameDelay.get();
			if (coefficient > 0.0) {
				waitingBound= (long)(waitingBound * coefficient);
			};
			if (waitingPeriod > waitingBound) {
				initialRecordTime= initialRecordTime + waitingPeriod;
				waitingPeriod= 0;
			};
			if (waitingPeriod > 0) {
//===================================================================//
try {
	long nanosTimeout= waitingPeriod * 1_000_000;
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
//===================================================================//
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void readGivenNumberOfFrames(int numberOfFrames) {
		numberOfFramesToBeRead.set(numberOfFrames);
		quickReadingMode.set(true);
		targetTime.set(-1);
		numberOfExtraFramesToBeRead.set(0);
		timeSynchronizationMode.set(false);
		activateReading();
	}
	//
	public void readFramesUntilGivenTime(long time, int numberOfExtraFrames) {
		targetTime.set(time);
		numberOfExtraFramesToBeRead.set(numberOfExtraFrames);
		timeSynchronizationMode.set(true);
		numberOfFramesToBeRead.set(0);
		quickReadingMode.set(false);
		activateReading();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean delayIsNecessary(long currentRecordTime, DataArrayType dataArrayType, CompoundArrayType compoundArrayType, KinectDataArrayType kinectArrayType) {
		boolean doDelayReading= true;
		if (currentRecordTime < 0) {
			doDelayReading= false;
		} else if (isAuxiliaryArrayType(dataArrayType,compoundArrayType,kinectArrayType)) {
			doDelayReading= false;
		} else if (quickReadingMode.get()) {
			doDelayReading= false;
			if (isTargetArrayType(dataArrayType,compoundArrayType,kinectArrayType)) {
				if (numberOfFramesToBeRead.decrementAndGet() <= 0) {
					quickReadingMode.set(false);
					doDelayReading= true;
				}
			}
		} else if (timeSynchronizationMode.get()) {
			doDelayReading= false;
			if (isTargetArrayType(dataArrayType,compoundArrayType,kinectArrayType)) {
				if (currentRecordTime >= targetTime.get()) {
					timeSynchronizationMode.set(false);
					targetTime.set(-1);
					numberOfFramesToBeRead.set(numberOfExtraFramesToBeRead.get());
					quickReadingMode.set(true);
				}
			}
		};
		return doDelayReading;
	}
	//
	protected boolean isAuxiliaryArrayType(DataArrayType dataArrayType, CompoundArrayType compoundArrayType, KinectDataArrayType kinectArrayType) {
		boolean answer= false;
		if (dataArrayType != null) {
			if (dataArrayType==DataArrayType.CAMERA_FLASH_FRAME) {
				answer= true;
			} else if (dataArrayType==DataArrayType.AUDIO_FRAME) {
				answer= true;
			}
		} else if (compoundArrayType != null) {
			if (compoundArrayType==CompoundArrayType.DESCRIPTION_FRAME) {
				answer= true;
			}
		} else if (kinectArrayType != null) {
			if (kinectArrayType==KinectDataArrayType.MODE_FRAME) {
				answer= true;
			}
		};
		return answer;
	}
	//
	protected boolean isTargetArrayType(DataArrayType dataArrayType, CompoundArrayType compoundArrayType, KinectDataArrayType kinectArrayType) {
		return true;
	}
}
