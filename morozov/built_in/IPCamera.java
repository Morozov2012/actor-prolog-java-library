// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.frames.converters.*;
import morozov.system.frames.data.interfaces.*;
import morozov.system.frames.errors.*;
import morozov.system.frames.interfaces.*;
import morozov.system.frames.tools.*;
import morozov.system.ip_camera.*;
import morozov.system.ip_camera.converters.*;
import morozov.system.ip_camera.frames.*;
import morozov.system.ip_camera.frames.interfaces.*;
import morozov.system.ip_camera.interfaces.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;

public abstract class IPCamera extends ZoomDataAcquisitionBuffer implements IPCameraDataConsumerInterface {
	//
	protected String cameraURL;
	//
	protected IPCameraDataAcquisition ipCameraDataAcquisition= new IPCameraDataAcquisition();
	//
	protected AtomicLong counterOfAcquiredIPCameraFrames= new AtomicLong(-1);
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
	@Override
	public void setOutputDebugInformation(BigInteger value) {
		super.setOutputDebugInformation(value);
		int mode= Arithmetic.toInteger(value);
		ipCameraDataAcquisition.setOutputDebugInformation(mode);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected void resetCounters() {
		synchronized (numberOfRecentReceivedFrame) {
			super.resetCounters();
			counterOfAcquiredIPCameraFrames.set(-1);
			numberOfRepeatedColorFrame= -1;
			committedIPCameraFrameImage= null;
		}
	}
	//
	@Override
	protected void resetFrameRate() {
		committedColorFrameNumber= -1;
		committedColorFrameTime= -1;
		firstCommittedColorFrameNumber= -1;
		firstCommittedColorFrameTime= -1;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected void activateDataAcquisition(boolean flushBuffers, ChoisePoint iX) {
		ActionPeriod period= getConnectionAttemptPeriod(iX);
		int givenConnectionAttemptPeriod= period.toMillisecondsOrDefault(defaultDeviceConnectionAttemptPeriod);
		ipCameraDataAcquisition.setServerAttributes(
			getCameraURL(iX),
			givenConnectionAttemptPeriod);
		int currentOutputDebugInformation= Arithmetic.toInteger(getOutputDebugInformation(iX));
		super.activateDataAcquisition(flushBuffers,iX);
		ipCameraDataAcquisition.activateDataTransfer(currentOutputDebugInformation);
	}
	//
	@Override
	protected void suspendRecording(ChoisePoint iX) {
		ipCameraDataAcquisition.suspendDataTransfer();
		super.suspendRecording(iX);
	}
	@Override
	protected void suspendListening(ChoisePoint iX) {
		ipCameraDataAcquisition.suspendDataTransfer();
		super.suspendListening(iX);
	}
	//
	@Override
	protected void stopRecording(ChoisePoint iX) {
		ipCameraDataAcquisition.stopDataTransfer();
		super.stopRecording(iX);
	}
	@Override
	protected void stopListening(ChoisePoint iX) {
		ipCameraDataAcquisition.stopDataTransfer();
		super.stopListening(iX);
	}
	//
	@Override
	protected boolean dataAcquisitionIsActive() {
		return !ipCameraDataAcquisition.isSuspended();
	}
	//
	@Override
	protected boolean dataAcquisitionIsSuspended() {
		return ipCameraDataAcquisition.isSuspended();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected void commit() {
		synchronized (numberOfRecentReceivedFrame) {
			super.commit();
			committedFrame= recentFrame;
			committedIPCameraFrameImage= null;
			if (!recentFrameIsRepeated) {
				committedColorFrameNumber= numberOfRecentReceivedFrame.get();
			} else {
				committedColorFrameNumber= numberOfRepeatedColorFrame;
			};
			updateCommittedFrameTime();
		}
	}
	//
	protected void updateCommittedFrameTime() {
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
		updateAttributesIfNecessary(iX);
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
			zCoefficient= NumericalValueConverters.toDouble(numericalZoomingCoefficient);
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
		int currentWidth= -1;
		int currentHeight= -1;
		if (nativeColorImage != null) {
			currentWidth= nativeColorImage.getWidth();
			currentHeight= nativeColorImage.getHeight();
		};
		a1.setBacktrackableValue(new PrologInteger(currentWidth),iX);
		a2.setBacktrackableValue(new PrologInteger(currentHeight),iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setIPCameraData(byte[] array, long time) {
		dataAcquisitionError.set(null);
		IPCameraFrame frame= new IPCameraFrame(
			counterOfAcquiredIPCameraFrames.incrementAndGet(),
			time,
			array,
			recentAttributes.get());
		flushAudioSystemIfNecessary();
		sendDataFrame(frame);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected long acceptFrame(DataFrameInterface frame, long currentFrameNumber) {
		if (frame instanceof IPCameraFrameInterface) {
			IPCameraFrameInterface ipCameraFrame= (IPCameraFrameInterface)frame;
			currentFrameNumber= numberOfRecentReceivedFrame.incrementAndGet();
			updateHistory(ipCameraFrame);
		};
		currentFrameNumber= super.acceptFrame(frame,currentFrameNumber);
		if (currentFrameNumber >= 0) {
			numberOfRepeatedColorFrame= -1;
		};
		return currentFrameNumber;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected void acceptRequestedFrame(EnumeratedFrame enumeratedFrame) {
		EnumeratedDataFrame selectedFrame= (EnumeratedDataFrame)enumeratedFrame;
		recentFrame= selectedFrame.getFrame();
		committedFrameWasAssignedDirectly.set(false);
		recentFrameIsRepeated= true;
		numberOfRepeatedColorFrame= selectedFrame.getNumberOfFrame();
	}
	//
	@Override
	protected void acceptRetrievedFrame(EnumeratedFrame enumeratedFrame) {
		EnumeratedDataFrame selectedFrame= (EnumeratedDataFrame)enumeratedFrame;
		DataFrameInterface colorFrame= selectedFrame.getFrame();
		committedFrame= colorFrame;
		committedFrameWasAssignedDirectly.set(true);
		committedIPCameraFrameImage= null;
		committedColorFrameNumber= selectedFrame.getNumberOfFrame();
		updateCommittedFrameTime();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void extractFrame(String key, CompoundFrameInterface container) {
		if (committedFrame != null) {
			EnumeratedDataFrame enumeratedFrame= new EnumeratedDataFrame(
				committedFrame,
				committedColorFrameNumber);
			container.insertComponent(key,enumeratedFrame);
		} else {
			throw new DataFrameIsNotCommitted();
		}
	}
	//
	@Override
	public void assignFrame(String key, CompoundFrameInterface container, ChoisePoint iX) {
		EnumeratedDataFrame enumeratedFrame= (EnumeratedDataFrame)container.getComponent(key);
		synchronized (numberOfRecentReceivedFrame) {
			committedFrame= enumeratedFrame.getFrame();
			committedFrameWasAssignedDirectly.set(true);
			committedIPCameraFrameImage= null;
			committedColorFrameNumber= numberOfRecentReceivedFrame.incrementAndGet();
			updateCommittedFrameTime();
		}
	}
}
