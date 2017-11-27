// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes;

import edu.ufl.digitalworlds.j4k.J4KSDK;

import morozov.system.kinect.modes.interfaces.*;

public enum KinectFrameType {
	//
	DEPTH_MAPS {
		public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode) {
			consolidatedMode.setDepthMapsAreRequested(true);
			consolidatedMode.refineKinectDataAcquisitionMode(J4KSDK.DEPTH);
		}
		public boolean requiresFrameType(KinectDataArrayType proposedFrameType) {
			return (proposedFrameType == KinectDataArrayType.DEPTH_FRAME);
		}
		public boolean requiresColorFrameResolution() {
			return false;
		}
	},
	COLORED_DEPTH_MAPS {
		public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode) {
			consolidatedMode.setDepthMapsAreRequested(true);
			consolidatedMode.refineKinectDataAcquisitionMode(J4KSDK.DEPTH);
		}
		public boolean requiresFrameType(KinectDataArrayType proposedFrameType) {
			return (proposedFrameType == KinectDataArrayType.DEPTH_FRAME);
		}
	},
	INFRARED {
		public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode) {
			consolidatedMode.setInfraredFramesAreRequested(true);
			consolidatedMode.refineKinectDataAcquisitionMode(J4KSDK.INFRARED);
		}
		public boolean requiresFrameType(KinectDataArrayType proposedFrameType) {
			return (proposedFrameType == KinectDataArrayType.INFRARED_FRAME);
		}
	},
	LONG_EXPOSURE_INFRARED {
		public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode) {
			consolidatedMode.setLongExposureInfraredFramesAreRequested(true);
			consolidatedMode.refineKinectDataAcquisitionMode(J4KSDK.LONG_EXPOSURE_INFRARED);
		}
		public boolean requiresFrameType(KinectDataArrayType proposedFrameType) {
			return (proposedFrameType == KinectDataArrayType.LONG_EXPOSURE_INFRARED_FRAME);
		}
	},
	MAPPED_COLOR {
		public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode) {
			consolidatedMode.setMappedColorFramesAreRequested(true);
			consolidatedMode.refineKinectDataAcquisitionMode(J4KSDK.DEPTH | J4KSDK.UV | J4KSDK.COLOR);
		}
		public boolean requiresFrameType(KinectDataArrayType proposedFrameType) {
			return (proposedFrameType == KinectDataArrayType.MAPPED_COLOR_FRAME);
		}
	},
	POINT_CLOUDS {
		public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode) {
			consolidatedMode.setPointCloudsAreRequested(true);
			consolidatedMode.refineKinectDataAcquisitionMode(J4KSDK.DEPTH | J4KSDK.XYZ | J4KSDK.UV | J4KSDK.COLOR);
		}
		public boolean requiresFrameType(KinectDataArrayType proposedFrameType) {
			return (proposedFrameType == KinectDataArrayType.POINT_CLOUDS_FRAME);
		}
	},
	COLOR {
		public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode) {
			consolidatedMode.setColorFramesAreRequested(true);
			consolidatedMode.refineKinectDataAcquisitionMode(J4KSDK.COLOR);
		}
		public boolean requiresFrameType(KinectDataArrayType proposedFrameType) {
			return (proposedFrameType == KinectDataArrayType.COLOR_FRAME);
		}
		public boolean requiresColorFrameResolution() {
			return true;
		}
	},
	DEVICE_TUNING {
		public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode) {
			consolidatedMode.setPointCloudsAreRequested(true);
			consolidatedMode.refineKinectDataAcquisitionMode(J4KSDK.DEPTH | J4KSDK.XYZ | J4KSDK.UV | J4KSDK.COLOR);
		}
		public boolean requiresFrameType(KinectDataArrayType proposedFrameType) {
			return (proposedFrameType == KinectDataArrayType.POINT_CLOUDS_FRAME);
		}
	},
	NONE {
		public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode) {
		}
		public boolean requiresFrameType(KinectDataArrayType proposedFrameType) {
			return (proposedFrameType == KinectDataArrayType.DEPTH_FRAME);
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode);
	abstract public boolean requiresFrameType(KinectDataArrayType proposedFrameType);
	//
	public boolean requiresColorFrameResolution() {
		return false;
	}
}
