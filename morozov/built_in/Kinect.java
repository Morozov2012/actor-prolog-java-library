// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.converters.*;
import morozov.system.kinect.*;
import morozov.system.kinect.converters.interfaces.*;
import morozov.system.kinect.errors.*;
import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.interfaces.*;
import morozov.system.kinect.modes.*;
import morozov.system.kinect.modes.converters.*;
import morozov.system.kinect.modes.interfaces.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public abstract class Kinect extends DataAbstraction implements KinectInterface, KinectFrameSupplier {
	//
	protected ExtendedCorrectionInterface horizontalCorrection;
	protected ExtendedCorrectionInterface verticalCorrection;
	//
	protected Integer skeletonReleaseTime;
	//
	protected ArrayList<KinectDeviceInterface> clientListeners= new ArrayList<>();
	protected AtomicReference<ConsolidatedDataAcquisitionModeInterface> currentKinectMode= new AtomicReference<>(null);
	protected boolean isActive= false;
	protected boolean providesExclusiveAccess= false;
	//
	protected FrameMappingTaskInterface frameMappingTask= new FrameMappingTask(this);
	protected KinectListenerInterface kinectListener= new KinectListener(frameMappingTask);
	//
	///////////////////////////////////////////////////////////////
	//
	public Kinect() {
	}
	public Kinect(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term getBuiltInSlot_E_skeleton_release_time();
	abstract public Term getBuiltInSlot_E_horizontal_correction();
	abstract public Term getBuiltInSlot_E_vertical_correction();
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set skeleton_release_time
	//
	public void setSkeletonReleaseTime1s(ChoisePoint iX, Term a1) {
		setSkeletonReleaseTime(GeneralConverters.argumentToSmallInteger(a1,iX));
	}
	public void setSkeletonReleaseTime(int value) {
		skeletonReleaseTime= value;
		frameMappingTask.setSkeletonReleaseTime(skeletonReleaseTime);
	}
	public void getSkeletonReleaseTime0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(getSkeletonReleaseTime(iX)));
	}
	public void getSkeletonReleaseTime0fs(ChoisePoint iX) {
	}
	public int getSkeletonReleaseTime(ChoisePoint iX) {
		if (skeletonReleaseTime != null) {
			return skeletonReleaseTime;
		} else {
			Term value= getBuiltInSlot_E_skeleton_release_time();
			return GeneralConverters.argumentToSmallInteger(value,iX);
		}
	}
	//
	// get/set horizontal_correction
	//
	public void setHorizontalCorrection1s(ChoisePoint iX, Term a1) {
		setHorizontalCorrection(ExtendedCorrectionConverters.argumentToExtendedCorrection(a1,iX));
	}
	public void setHorizontalCorrection(ExtendedCorrectionInterface value) {
		horizontalCorrection= value;
		kinectListener.setHorizontalCorrection(horizontalCorrection);
	}
	public void getHorizontalCorrection0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(ExtendedCorrectionConverters.toTerm(getHorizontalCorrection(iX)));
	}
	public void getHorizontalCorrection0fs(ChoisePoint iX) {
	}
	public ExtendedCorrectionInterface getHorizontalCorrection(ChoisePoint iX) {
		if (horizontalCorrection != null) {
			return horizontalCorrection;
		} else {
			Term value= getBuiltInSlot_E_horizontal_correction();
			return ExtendedCorrectionConverters.argumentToExtendedCorrection(value,iX);
		}
	}
	//
	// get/set vertical_correction
	//
	public void setVerticalCorrection1s(ChoisePoint iX, Term a1) {
		setVerticalCorrection(ExtendedCorrectionConverters.argumentToExtendedCorrection(a1,iX));
	}
	public void setVerticalCorrection(ExtendedCorrectionInterface value) {
		verticalCorrection= value;
		kinectListener.setVerticalCorrection(verticalCorrection);
	}
	public void getVerticalCorrection0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(ExtendedCorrectionConverters.toTerm(getVerticalCorrection(iX)));
	}
	public void getVerticalCorrection0fs(ChoisePoint iX) {
	}
	public ExtendedCorrectionInterface getVerticalCorrection(ChoisePoint iX) {
		if (verticalCorrection != null) {
			return verticalCorrection;
		} else {
			Term value= getBuiltInSlot_E_vertical_correction();
			return ExtendedCorrectionConverters.argumentToExtendedCorrection(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setCorrection2s(ChoisePoint iX, Term a1, Term a2) {
		ExtendedCorrection x= ExtendedCorrectionConverters.argumentToExtendedCorrection(a1,iX);
		ExtendedCorrection y= ExtendedCorrectionConverters.argumentToExtendedCorrection(a2,iX);
		horizontalCorrection= x;
		verticalCorrection= y;
		kinectListener.setCorrection(horizontalCorrection,verticalCorrection);
	}
	//
	public void getCorrection2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		Term x= ExtendedCorrectionConverters.toTerm(getHorizontalCorrection(iX));
		Term y= ExtendedCorrectionConverters.toTerm(getVerticalCorrection(iX));
		a1.setBacktrackableValue(x,iX);
		a2.setBacktrackableValue(y,iX);
	}
	//
	public void getCorrectionInPixels2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		int x= frameMappingTask.getHorizontalCorrection();
		int y= frameMappingTask.getVerticalCorrection();
		a1.setBacktrackableValue(new PrologInteger(x),iX);
		a2.setBacktrackableValue(new PrologInteger(y),iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void activate0s(ChoisePoint iX) {
		activate(iX);
	}
	//
	public void stop0s(ChoisePoint iX) {
		stop();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void initiateAuxiliaryProcedures(ChoisePoint iX) {
		int currentSkeletonReleaseTime= getSkeletonReleaseTime(iX);
		ExtendedCorrectionInterface currentHorizontalCorrection= getHorizontalCorrection(iX);
		ExtendedCorrectionInterface currentVerticalCorrection= getVerticalCorrection(iX);
		frameMappingTask.setSkeletonReleaseTime(currentSkeletonReleaseTime);
		kinectListener.setHorizontalCorrection(currentHorizontalCorrection);
		kinectListener.setVerticalCorrection(currentVerticalCorrection);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void registerListener(KinectDeviceInterface device, boolean requireExclusiveAccess, ChoisePoint iX) {
		boolean modeIsChanged;
		synchronized (clientListeners) {
			if (requireExclusiveAccess) {
				if (providesExclusiveAccess) {
					throw new KinectDeviceIsAlreadyInExclusiveAccessMode();
				} else if (clientListeners.size() > 0) {
					throw new KinectDeviceIsAlreadyInSharedAccessMode();
				} else {
					providesExclusiveAccess= true;
				}
			};
			boolean deviceIsFound= false;
			for (int n=0; n < clientListeners.size(); n++) {
				KinectDeviceInterface element= clientListeners.get(n);
				if (element.equals(device)) {
					// if (element.isSuspended()) {
					//	return;
					// } else {
					//	element.setIsSuspended(false);
					deviceIsFound= true;
					break;
					// }
				}
			};
			if (!deviceIsFound) {
				device.setIsSuspended(true);
				clientListeners.add(device);
			};
			modeIsChanged= changeDataAcquisitionMode();
		};
		if (modeIsChanged) {
			reactivateDevice(iX);
		}
	}
	//
	public void suspendListener(KinectDeviceInterface device, ChoisePoint iX) {
		// boolean modeIsChanged;
		synchronized (clientListeners) {
			boolean deviceIsFound= false;
			for (int n=0; n < clientListeners.size(); n++) {
				KinectDeviceInterface element= clientListeners.get(n);
				if (element.equals(device)) {
					if (element.isSuspended()) {
						return;
					} else {
						element.setIsSuspended(true);
						// deviceIsFound= true;
						break;
					}
				}
			}
			// if (!deviceIsFound) {
			//	return;
			// };
			// modeIsChanged= changeDataAcquisitionMode();
		}
		// if (modeIsChanged) {
		//	reactivateDevice(iX);
		// }
	}
	//
	public void cancelListener(KinectDeviceInterface device, ChoisePoint iX) {
		boolean modeIsChanged;
		synchronized (clientListeners) {
			boolean deviceIsFound= false;
			for (int n=0; n < clientListeners.size(); n++) {
				KinectDeviceInterface element= clientListeners.get(n);
				if (element.equals(device)) {
					// clientListeners.remove(n);
					element.setIsSuspended(true);
					deviceIsFound= true;
					break;
				}
			};
			if (!deviceIsFound) {
				return;
			};
			modeIsChanged= changeDataAcquisitionMode();
		};
		if (modeIsChanged) {
			reactivateDevice(iX);
		}
	}
	//
	protected boolean changeDataAcquisitionMode() {
		ConsolidatedDataAcquisitionModeInterface newMode= new ConsolidatedDataAcquisitionMode();
		for (int n=0; n < clientListeners.size(); n++) {
			clientListeners.get(n).refineDataAcquisitionMode(newMode);
		};
		newMode.complete();
		ConsolidatedDataAcquisitionModeInterface previousMode= currentKinectMode.get();
		currentKinectMode.set(newMode);
		if (isActive && previousMode != null && !previousMode.equals(newMode)) {
			return true;
		} else {
			return false;
		}
	}
	protected void reactivateDevice(ChoisePoint iX) {
		initiateAuxiliaryProcedures(iX);
		synchronized (kinectListener) {
			kinectListener.stop();
			if (clientListeners.size() > 0) {
				kinectListener.initializeDevice(currentKinectMode.get());
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void activate(ChoisePoint iX) {
		initiateAuxiliaryProcedures(iX);
		synchronized (kinectListener) {
			if (kinectListener.initializeDevice(currentKinectMode.get())) {
				isActive= true;
			}
		}
	}
	public void stop() {
		synchronized (kinectListener) {
			kinectListener.stop();
			isActive= false;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void sendDepthFrame(KinectDepthFrameInterface frame) {
		synchronized (clientListeners) {
			for (int n=0; n < clientListeners.size(); n++) {
				KinectDeviceInterface device= clientListeners.get(n);
				if (device.isSuspended()) {
					continue;
				};
				if (device.requiresFrameType(KinectDataArrayType.DEPTH_FRAME)) {
					device.sendFrame(frame);
				}
			}
		}
	}
	public void sendInfraredFrame(KinectInfraredFrameInterface frame) {
		synchronized (clientListeners) {
			for (int n=0; n < clientListeners.size(); n++) {
				KinectDeviceInterface device= clientListeners.get(n);
				if (device.isSuspended()) {
					continue;
				};
				if (device.requiresFrameType(KinectDataArrayType.INFRARED_FRAME)) {
					device.sendFrame(frame);
				}
			}
		}
	}
	public void sendLongExposureInfraredFrame(KinectLongExposureInfraredFrameInterface frame) {
		synchronized (clientListeners) {
			for (int n=0; n < clientListeners.size(); n++) {
				KinectDeviceInterface device= clientListeners.get(n);
				if (device.isSuspended()) {
					continue;
				};
				if (device.requiresFrameType(KinectDataArrayType.LONG_EXPOSURE_INFRARED_FRAME)) {
					device.sendFrame(frame);
				}
			}
		}
	}
	public void sendMappedColorFrame(KinectMappedColorFrameInterface frame) {
		synchronized (clientListeners) {
			for (int n=0; n < clientListeners.size(); n++) {
				KinectDeviceInterface device= clientListeners.get(n);
				if (device.isSuspended()) {
					continue;
				};
				if (device.requiresFrameType(KinectDataArrayType.MAPPED_COLOR_FRAME)) {
					device.sendFrame(frame);
				}
			}
		}
	}
	public void sendPointCloudsFrame(KinectPointCloudsFrameInterface frame) {
		synchronized (clientListeners) {
			for (int n=0; n < clientListeners.size(); n++) {
				KinectDeviceInterface device= clientListeners.get(n);
				if (device.isSuspended()) {
					continue;
				};
				if (device.requiresFrameType(KinectDataArrayType.POINT_CLOUDS_FRAME)) {
					device.sendFrame(frame);
				}
			}
		}
	}
	public void sendColorFrame(KinectColorFrameInterface frame) {
		synchronized (clientListeners) {
			for (int n=0; n < clientListeners.size(); n++) {
				KinectDeviceInterface device= clientListeners.get(n);
				if (device.isSuspended()) {
					continue;
				};
				if (device.requiresFrameType(KinectDataArrayType.COLOR_FRAME)) {
					device.sendFrame(frame);
				}
			}
		}
	}
}
