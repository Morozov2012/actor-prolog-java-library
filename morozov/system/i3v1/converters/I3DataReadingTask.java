// (c) 2018 Alexei A. Morozov

package morozov.system.i3v1.converters;

import morozov.system.i3v1.converters.interfaces.*;
import morozov.system.frames.converters.*;
import morozov.system.modes.*;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class I3DataReadingTask extends DataFrameReadingTask implements I3DataReadingTaskInterface {
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
	public I3DataReadingTask() {
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
	public boolean delayIsNecessary(long currentRecordTime, DataArrayType dataArrayType) {
		boolean doDelayReading= true;
		if (currentRecordTime < 0) {
			doDelayReading= false;
		} else if (dataArrayType==DataArrayType.CAMERA_FLASH_FRAME) {
			doDelayReading= false;
		} else if (dataArrayType==DataArrayType.AUDIO_FRAME) {
			doDelayReading= false;
		} else if (quickReadingMode.get()) {
			doDelayReading= false;
			if (dataArrayType==DataArrayType.DOUBLE_FRAME) {
				if (numberOfFramesToBeRead.decrementAndGet() <= 0) {
					quickReadingMode.set(false);
					doDelayReading= true;
				}
			}
		} else if (timeSynchronizationMode.get()) {
			doDelayReading= false;
			if (dataArrayType==DataArrayType.DOUBLE_FRAME) {
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
}
