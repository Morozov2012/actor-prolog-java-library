// (c) 2018 Alexei A. Morozov

package morozov.system.ip_camera.converters;

import morozov.system.ip_camera.converters.interfaces.*;
import morozov.system.frames.converters.*;
import morozov.system.modes.*;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class IPCameraFrameReadingTask extends DataFrameReadingTask implements IPCameraFrameReadingTaskInterface {
	//
	protected AtomicBoolean quickReadingMode= new AtomicBoolean(false);
	protected AtomicInteger numberOfColorFramesToBeRead= new AtomicInteger(0);
	//
	protected AtomicBoolean timeSynchronizationMode= new AtomicBoolean(false);
	protected AtomicLong targetTime= new AtomicLong(0);
	protected AtomicInteger numberOfExtraColorFramesToBeRead= new AtomicInteger(0);
	//
	///////////////////////////////////////////////////////////////
	//
	public IPCameraFrameReadingTask() {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void readGivenNumberOfColorFrames(int numberOfFrames) {
		numberOfColorFramesToBeRead.set(numberOfFrames);
		quickReadingMode.set(true);
		targetTime.set(-1);
		numberOfExtraColorFramesToBeRead.set(0);
		timeSynchronizationMode.set(false);
		activateReading();
	}
	//
	public void readFramesUntilGivenTime(long time, int numberOfExtraFrames) {
		targetTime.set(time);
		numberOfExtraColorFramesToBeRead.set(numberOfExtraFrames);
		timeSynchronizationMode.set(true);
		numberOfColorFramesToBeRead.set(0);
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
			if (dataArrayType==DataArrayType.IP_CAMERA_FRAME) {
				if (numberOfColorFramesToBeRead.decrementAndGet() <= 0) {
					quickReadingMode.set(false);
					doDelayReading= true;
				}
			}
		} else if (timeSynchronizationMode.get()) {
			doDelayReading= false;
			if (dataArrayType==DataArrayType.IP_CAMERA_FRAME) {
				if (currentRecordTime >= targetTime.get()) {
					timeSynchronizationMode.set(false);
					targetTime.set(-1);
					numberOfColorFramesToBeRead.set(numberOfExtraColorFramesToBeRead.get());
					quickReadingMode.set(true);
				}
			}
		};
		return doDelayReading;
	}
}
