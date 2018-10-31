// (c) 2018 Alexei A. Morozov

package morozov.system.frames.converters;

import morozov.run.*;
import morozov.system.*;
import morozov.system.checker.signals.*;
import morozov.system.files.*;
import morozov.system.files.errors.*;
import morozov.system.frames.converters.interfaces.*;
import morozov.system.frames.interfaces.*;
import morozov.system.modes.*;

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.io.EOFException;
import java.io.IOException;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class DataFrameReadingTask extends Thread {
	//
	protected DataFrameConsumerInterface dataConsumer;
	//
	protected AtomicBoolean stopThisThread= new AtomicBoolean(false);
	protected AtomicBoolean stopAfterSingleReading= new AtomicBoolean(false);
	protected AtomicReference<Double> slowMotionCoefficient= new AtomicReference<>(-1.0);
	protected AtomicBoolean inputIsOpen= new AtomicBoolean(false);
	//
	protected long initialRecordTime= -1;
	protected long initialRealTime= -1;
	protected long totalNumberOfFrames= 0;
	//
	public static long defaultMaximalWaitingPeriod= 1000;
	public AtomicLong maximalWaitingPeriod= new AtomicLong(defaultMaximalWaitingPeriod);
	//
	protected ExtendedFileName extendedFileName;
	// protected Path extendedFileName;
	protected InputStream inputStream;
	protected InputStream bufferedInputStream;
	protected ObjectInputStream objectInputStream;
	//
	protected ReentrantLock lock= new ReentrantLock();
	protected Condition condition= lock.newCondition();
	//
	// protected static final FileSystem fileSystem= FileSystems.getDefault();
	//
	///////////////////////////////////////////////////////////////
	//
	public DataFrameReadingTask() {
		setDaemon(true);
	}
	//
	public void setDataConsumer(DataFrameConsumerInterface consumer) {
		dataConsumer= consumer;
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
	// protected void openReading(Path inputFilePath) {
	protected void openReading(ExtendedFileName fileName, int timeout, CharacterSet characterSet, StaticContext staticContext) {
		closeReading();
		synchronized (this) {
			inputIsOpen.set(false);
			resetCounters();
			extendedFileName= fileName;
			try {
				// Path inputFilePath= fileSystem.getPath(fileName);
				if (fileName.doesExist(false,timeout,characterSet,staticContext)) {
					try {
						inputStream= fileName.getInputStreamOfUniversalResource(timeout,staticContext);
						// inputStream= Files.newInputStream(inputFilePath);
						bufferedInputStream= new BufferedInputStream(inputStream);
						objectInputStream= new ObjectInputStream(bufferedInputStream);
						// displayingMode= currentDisplayingMode;
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
DataFrameInterface frame;
synchronized (this) {
	if (stopThisThread.get()) {
		wait();
		continue;
	};
	if (inputIsOpen.get()) {
		try {
			frame= (DataFrameInterface)objectInputStream.readObject();
		} catch (EOFException e) {
			throw e;
		} catch (Throwable e) {
			System.err.printf("Object reading error: %s\n",e);
			throw e;
		};
		DataArrayType dataArrayType= frame.getDataArrayType();
		if (dataArrayType==DataArrayType.CAMERA_FLASH_FRAME) {
			if (dataConsumer != null) {
				dataConsumer.sendFrame(frame);
			};
			continue;
		};
		long currentRecordTime= frame.getTime();
		boolean doDelayReading= delayIsNecessary(currentRecordTime,dataArrayType);
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
	if (!dataConsumer.sendFrame(frame)) {
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
					dataConsumer.completeDataTransfer(totalNumberOfFrames);
				};
				continue;
			} catch (IOException e) {
				closeReading();
				stopThisThread.set(true);
				if (dataConsumer != null) {
					dataConsumer.completeDataTransfer(numberOfFrameToBeAcquired,new FileInputOutputError(extendedFileName.toString(),e));
				}
			} catch (ClassNotFoundException e) {
				closeReading();
				stopThisThread.set(true);
				if (dataConsumer != null) {
					dataConsumer.completeDataTransfer(numberOfFrameToBeAcquired,new FileInputOutputError(extendedFileName.toString(),e));
				}
			} catch (InterruptedException e) {
				closeReading();
				stopThisThread.set(true);
				if (dataConsumer != null) {
					dataConsumer.completeDataTransfer(totalNumberOfFrames);
				};
				return;
			}
		}
	}
	//
	public boolean delayIsNecessary(long currentRecordTime, DataArrayType dataArrayType) {
		boolean doDelayReading= true;
		if (currentRecordTime < 0) {
			doDelayReading= false;
		} else if (dataArrayType==DataArrayType.CAMERA_FLASH_FRAME) {
			doDelayReading= false;
		} else if (dataArrayType==DataArrayType.AUDIO_FRAME) {
			doDelayReading= false;
		};
		return doDelayReading;
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
			long waitingBound= maximalWaitingPeriod.get();
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
}
