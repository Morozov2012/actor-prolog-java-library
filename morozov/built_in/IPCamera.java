// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.frames.converters.*;
import morozov.system.frames.converters.interfaces.*;
import morozov.system.frames.data.interfaces.*;
import morozov.system.frames.errors.*;
import morozov.system.frames.interfaces.*;
import morozov.system.frames.tools.*;
import morozov.system.ip_camera.*;
import morozov.system.ip_camera.converters.*;
import morozov.system.ip_camera.converters.interfaces.*;
import morozov.system.ip_camera.errors.*;
import morozov.system.ip_camera.frames.*;
import morozov.system.ip_camera.frames.interfaces.*;
import morozov.system.ip_camera.interfaces.*;
import morozov.system.modes.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicLong;

public abstract class IPCamera extends VideoBuffer implements IPCameraDataConsumerInterface, DataFrameConsumerInterface, DataFrameProviderInterface {
	//
	protected String cameraURL;
	//
	protected IPCameraDataAcquisition ipCameraDataAcquisition= new IPCameraDataAcquisition();
	//
	protected AtomicLong counterOfAcquiredIPCameraFrames= new AtomicLong(-1);
	protected AtomicLong numberOfRecentIPCameraFrame= new AtomicLong(-1);
	//
	protected long numberOfRepeatedColorFrame= -1;
	//
	protected java.awt.image.BufferedImage committedIPCameraFrameImage;
	//
	protected long committedColorFrameNumber= -1;
	protected long committedColorFrameTime= -1;
	protected long firstCommittedColorFrameNumber= -1;
	protected long firstCommittedColorFrameTime= -1;
	//
	protected LinkedList<EnumeratedFrame> history= new LinkedList<>();
	//
	///////////////////////////////////////////////////////////////
	//
	public IPCamera() {
		super(	new IPCameraFrameReadingTask(),
			new DataFrameRecordingTask());
		connectIPCameraClassInstance();
	}
	public IPCamera(GlobalWorldIdentifier id) {
		super(	id,
			new IPCameraFrameReadingTask(),
			new DataFrameRecordingTask());
		connectIPCameraClassInstance();
	}
	//
	protected void connectIPCameraClassInstance() {
		ipCameraDataAcquisition.setDataConsumer(this);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term getBuiltInSlot_E_camera_url();
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set camera_url
	//
	public void setCameraURL1s(ChoisePoint iX, Term a1) {
		String text= GeneralConverters.argumentToString(a1,iX);
		setCameraURL(text);
	}
	public void setCameraURL(String value) {
		cameraURL= value;
	}
	public void getCameraURL0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologString(getCameraURL(iX)));
	}
	public void getCameraURL0fs(ChoisePoint iX) {
	}
	public String getCameraURL(ChoisePoint iX) {
		if (cameraURL != null) {
			return cameraURL;
		} else {
			Term value= getBuiltInSlot_E_camera_url();
			return GeneralConverters.argumentToString(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void resetCounters() {
		synchronized (numberOfRecentReceivedFrame) {
			super.resetCounters();
			counterOfAcquiredIPCameraFrames.set(-1);
			numberOfRecentIPCameraFrame.set(-1);
			numberOfRepeatedColorFrame= -1;
			committedIPCameraFrameImage= null;
			committedColorFrameNumber= -1;
			committedColorFrameTime= -1;
			firstCommittedColorFrameNumber= -1;
			firstCommittedColorFrameTime= -1;
			synchronized (history) {
				history.clear();
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void activateDataAcquisition(ChoisePoint iX) {
		ActionPeriod period= getOpeningAttemptPeriod(iX);
		int givenOpeningAttemptDelay= period.toMillisecondsOrDefault(defaultDeviceOpeningAttemptDelay);
		ipCameraDataAcquisition.setServerAttributes(
			getCameraURL(iX),
			givenOpeningAttemptDelay);
		ipCameraDataAcquisition.activateDataTransfer();
	}
	//
	protected void readGivenNumberOfTargetFrames(int number) {
		((IPCameraFrameReadingTask)frameReadingTask).readGivenNumberOfColorFrames(number);
	}
	//
	protected void suspendDataAcquisition() {
		ipCameraDataAcquisition.suspendDataTransfer();
	}
	//
	protected void stopDataAcquisition() {
		super.stopDataAcquisition();
		ipCameraDataAcquisition.stopDataTransfer();
	}
	//
	protected boolean dataAcquisitionIsActive() {
		return ipCameraDataAcquisition.isNotSuspended();
	}
	//
	protected boolean dataAcquisitionIsSuspended() {
		return ipCameraDataAcquisition.isSuspended();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void commit0s(ChoisePoint iX) throws Backtracking {
		synchronized (numberOfRecentReceivedFrame) {
			if (recentFrame==null) {
				throw Backtracking.instance;
			};
			commit();
		}
	}
	//
	protected void commit() {
		synchronized (numberOfRecentReceivedFrame) {
			super.commit();
			committedFrame= recentFrame;
			committedIPCameraFrameImage= null;
			if (!recentFrameIsRepeated) {
				committedColorFrameNumber= numberOfRecentIPCameraFrame.get();
			} else {
				committedColorFrameNumber= numberOfRepeatedColorFrame;
			};
			if (committedFrame != null) {
				committedColorFrameTime= committedFrame.getTime();
			} else {
				committedColorFrameTime= -1;
			};
			if (firstCommittedColorFrameTime < 0) {
				firstCommittedColorFrameNumber= committedColorFrameNumber;
				firstCommittedColorFrameTime= committedColorFrameTime;
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getRecentFrameNumber1s(ChoisePoint iX, PrologVariable a1) {
		long frameNumber;
		synchronized (numberOfRecentReceivedFrame) {
			frameNumber= committedColorFrameNumber;
		};
		a1.setBacktrackableValue(new PrologInteger(frameNumber),iX);
	}
	//
	public void getRecentFrameTime1s(ChoisePoint iX, PrologVariable a1) {
		long frameTime;
		synchronized (numberOfRecentReceivedFrame) {
			frameTime= committedColorFrameTime;
		};
		a1.setBacktrackableValue(new PrologInteger(frameTime),iX);
	}
	//
	public void getRecentFrameRelativeTime1s(ChoisePoint iX, PrologVariable a1) {
		long deltaTime;
		synchronized (numberOfRecentReceivedFrame) {
			deltaTime= committedColorFrameTime - firstCommittedColorFrameTime;
		};
		a1.setBacktrackableValue(new PrologInteger(deltaTime),iX);
	}
	//
	public void getRecentFrameRate1s(ChoisePoint iX, PrologVariable a1) {
		long deltaN;
		long deltaTime;
		synchronized (numberOfRecentReceivedFrame) {
			deltaN= committedColorFrameNumber - firstCommittedColorFrameNumber;
			deltaTime= committedColorFrameTime - firstCommittedColorFrameTime;
		};
		double rate= computeFrameRate(deltaN,deltaTime);
		a1.setBacktrackableValue(new PrologReal(rate),iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getRecentImage1s(ChoisePoint iX, Term value) {
		java.awt.image.BufferedImage nativeImage;
		DataFrameBaseAttributesInterface attributes;
		synchronized (numberOfRecentReceivedFrame) {
			if (committedFrame != null) {
				nativeImage= getNativeColorImage();
				attributes= committedFrame.getBaseAttributes();
			} else {
				throw new DataFrameIsNotCommitted();
			}
		};
		if (nativeImage==null) {
			throw new DataFrameContainsNoImage();
		};
		YesNo doNotControlZooming= getUseRecordedZoomingCommands(iX);
		YesNo doZoomImage= getZoomImage(iX);
		NumericalValue numericalZoomingCoefficient= getZoomingCoefficient(iX);
		boolean zoomIt;
		double zCoefficient;
		if (doNotControlZooming.toBoolean()) {
			zoomIt= attributes.isZoomingMode();
			zCoefficient= attributes.getZoomingCoefficient();
		} else {
			zoomIt= doZoomImage.toBoolean();
			zCoefficient= numericalZoomingCoefficient.toDouble();
		};
		nativeImage= DataFrameTools.zoomImage(
			nativeImage,
			zoomIt,
			zCoefficient);
		modifyImage(value,nativeImage,iX);
	}
	//
	protected java.awt.image.BufferedImage getNativeColorImage() {
		if (committedIPCameraFrameImage != null)  {
			return committedIPCameraFrameImage;
		};
		byte[] byteArray= ((IPCameraFrameInterface)committedFrame).getByteArray();
		try {
			java.awt.image.BufferedImage nativeImage= IPCameraDataAcquisition.byteArrayToBufferedImage(byteArray);
			committedIPCameraFrameImage= nativeImage;
			return nativeImage;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getImageSizeInPixels2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		java.awt.image.BufferedImage nativeColorImage= null;
		synchronized (numberOfRecentReceivedFrame) {
			if (committedFrame != null) {
				nativeColorImage= getNativeColorImage();
			}
		};
		int width= -1;
		int height= -1;
		if (nativeColorImage != null) {
			width= nativeColorImage.getWidth();
			height= nativeColorImage.getHeight();
		};
		a1.setBacktrackableValue(new PrologInteger(width),iX);
		a2.setBacktrackableValue(new PrologInteger(height),iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setIPCameraData(byte[] array, long time) {
		dataAcquisitionError.set(null);
		IPCameraFrame frame= new IPCameraFrame(
			counterOfAcquiredIPCameraFrames.incrementAndGet(),
			time,
			array,
			recentAttributes.get());
		sendFrame(frame);
	}
	// public void setAudioData(byte[] buffer, long time) {
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	protected long updateRecentFrame(DataFrameInterface frame) {
		if (frame.isLightweightFrame()) {
			return -1;
		};
		synchronized (numberOfRecentReceivedFrame) {
			if (frame instanceof IPCameraFrameInterface) {
				recentFrame= frame;
				numberOfRecentIPCameraFrame.set(recentFrame.getSerialNumber());
				updateHistory((IPCameraFrameInterface)frame);
			};
			long currentFrameNumber= numberOfRecentReceivedFrame.incrementAndGet();
			recentFrameIsRepeated= false;
			numberOfRepeatedColorFrame= -1;
			return currentFrameNumber;
		}
	}
	//
	protected void updateHistory(IPCameraFrameInterface ipCameraFrame) {
		if (ipCameraFrame==null) {
			return;
		};
		synchronized (history) {
			history.addLast(new EnumeratedFrame(
				ipCameraFrame,
				numberOfRecentIPCameraFrame.get()));
			if (history.size() > actingReadBufferSize.get()) {
				history.removeFirst();
			}
		};
		containsNewFrame.set(true);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void retrieveBufferedFrame1s(ChoisePoint iX, Term a1) {
		int number= GeneralConverters.argumentToSmallInteger(a1,iX);
		int relativeNumber= number - 1;
		if (relativeNumber < 0) {
			relativeNumber= 0;
		};
		synchronized (numberOfRecentReceivedFrame) {
			EnumeratedFrame enumeratedFrame;
			synchronized (history) {
				int bufferSize= actingReadBufferSize.get();
				int historySize= history.size();
				int maximalIndex= bufferSize - 1;
				if (historySize < bufferSize) {
					maximalIndex= historySize - 1;
					relativeNumber= relativeNumber * historySize / bufferSize;
				};
				if (relativeNumber > maximalIndex) {
					relativeNumber= maximalIndex;
				};
				if (relativeNumber < 0 || relativeNumber >= historySize) {
					return;
				};
				enumeratedFrame= history.get(relativeNumber);
			};
			recentFrame= enumeratedFrame.getFrame();
			recentFrameIsRepeated= true;
			numberOfRepeatedColorFrame= enumeratedFrame.getNumberOfFrame();
		};
		sendFrameObtained();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void retrieveTimedFrame1s(ChoisePoint iX, Term a1) throws Backtracking {
		TimeInterval timeInterval= TimeInterval.argumentMillisecondsToTimeInterval(a1,iX);
		if (!retrieveTimedFrame(timeInterval.toMillisecondsLong())) {
			throw Backtracking.instance;
		}
	}
	//
	protected boolean retrieveTimedFrame(long targetTime) {
		while (true) {
			VideoBufferOperatingMode currentOperatingMode= actingVideoBufferOperatingMode.get();
			int numberOfColorFramesToBeRead= 0;
			synchronized (numberOfRecentReceivedFrame) {
				synchronized (history) {
					int bufferSize= actingReadBufferSize.get();
					int historySize= history.size();
					if (historySize <= 1) {
						if (currentOperatingMode==VideoBufferOperatingMode.SPECULATIVE_READING) {
							((IPCameraFrameReadingTaskInterface)frameReadingTask).readGivenNumberOfColorFrames(bufferSize);
						};
						continue;
					};
					EnumeratedFrame firstEnumeratedFrame= history.getFirst();
					EnumeratedFrame lastEnumeratedFrame= history.getLast();
					long minimalTime= firstEnumeratedFrame.getTime();
					long maximalTime= lastEnumeratedFrame.getTime();
					if (targetTime >= minimalTime) {
						if(targetTime <= maximalTime) {
///////////////////////////////////////////////////////////////////////
ListIterator<EnumeratedFrame> iterator= history.listIterator(0);
int relativeNumber= 0;
EnumeratedFrame selectedFrame= firstEnumeratedFrame;
long delay1= targetTime - minimalTime;
if (delay1 < 0) {
	delay1= -delay1;
};
while (iterator.hasNext()) {
	EnumeratedFrame currentFrame= iterator.next();
	long time2= currentFrame.getTime();
	long delay2= targetTime - time2;
	if (delay2 < 0) {
		delay2= -delay2;
	};
	if (time2 >= targetTime) {
		if (delay2 < delay1) {
			selectedFrame= currentFrame;
		};
		break;
	} else {
		selectedFrame= currentFrame;
		delay1= delay2;
	}
};
IPCameraFrameInterface colorFrame= (IPCameraFrameInterface)selectedFrame.getFrame();
committedFrame= colorFrame;
committedIPCameraFrameImage= null;
committedColorFrameNumber= numberOfRepeatedColorFrame;
committedColorFrameTime= committedFrame.getTime();
if (firstCommittedColorFrameTime < 0) {
	firstCommittedColorFrameNumber= committedColorFrameNumber;
	firstCommittedColorFrameTime= committedColorFrameTime;
};
if (currentOperatingMode==VideoBufferOperatingMode.SPECULATIVE_READING) {
	((IPCameraFrameReadingTaskInterface)frameReadingTask).readGivenNumberOfColorFrames(numberOfColorFramesToBeRead);
};
return true;
///////////////////////////////////////////////////////////////////////
						} else { // Read several frames
							if (currentOperatingMode==VideoBufferOperatingMode.SPECULATIVE_READING) {
								((IPCameraFrameReadingTaskInterface)frameReadingTask).readFramesUntilGivenTime(targetTime,bufferSize);
							}
						}
					} else { // Suspend reading of the frames
						if (currentOperatingMode==VideoBufferOperatingMode.SPECULATIVE_READING) {
							frameReadingTask.suspendReading();
						};
						return false;
					}
				}
			};
			if (currentOperatingMode != VideoBufferOperatingMode.SPECULATIVE_READING) {
				break;
			}
		};
		return false;
	}
}
