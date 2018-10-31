// (c) 2018 Alexei A. Morozov

package morozov.system.astrohn.converters;

import morozov.system.astrohn.converters.interfaces.*;
import morozov.system.frames.converters.*;
import morozov.system.modes.*;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AstrohnDataReadingTask extends DataFrameReadingTask implements AstrohnDataReadingTaskInterface {
	//
	protected AtomicBoolean quickReadingMode= new AtomicBoolean(false);
	protected AtomicInteger numberOfTerahertzFramesToBeRead= new AtomicInteger(0);
	//
	protected AtomicBoolean timeSynchronizationMode= new AtomicBoolean(false);
	protected AtomicLong targetTime= new AtomicLong(0);
	protected AtomicInteger numberOfExtraTerahertzFramesToBeRead= new AtomicInteger(0);
	//
	///////////////////////////////////////////////////////////////
	//
	public AstrohnDataReadingTask() {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void readGivenNumberOfTerahertzFrames(int numberOfTerahertzFrames) {
		numberOfTerahertzFramesToBeRead.set(numberOfTerahertzFrames);
		quickReadingMode.set(true);
		targetTime.set(-1);
		numberOfExtraTerahertzFramesToBeRead.set(0);
		timeSynchronizationMode.set(false);
		activateReading();
	}
	//
	public void readFramesUntilGivenTime(long time, int numberOfExtraTerahertzFrames) {
		targetTime.set(time);
		numberOfExtraTerahertzFramesToBeRead.set(numberOfExtraTerahertzFrames);
		timeSynchronizationMode.set(true);
		numberOfTerahertzFramesToBeRead.set(0);
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
			if (dataArrayType==DataArrayType.THZ_FRAME) {
				if (numberOfTerahertzFramesToBeRead.decrementAndGet() <= 0) {
					quickReadingMode.set(false);
					doDelayReading= true;
				}
			}
		} else if (timeSynchronizationMode.get()) {
			doDelayReading= false;
			if (dataArrayType==DataArrayType.THZ_FRAME) {
				if (currentRecordTime >= targetTime.get()) {
					timeSynchronizationMode.set(false);
					targetTime.set(-1);
					numberOfTerahertzFramesToBeRead.set(numberOfExtraTerahertzFramesToBeRead.get());
					quickReadingMode.set(true);
				}
			}
		};
		return doDelayReading;
	}
}
