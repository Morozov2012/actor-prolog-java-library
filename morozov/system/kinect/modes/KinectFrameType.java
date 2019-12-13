// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes;

import edu.ufl.digitalworlds.j4k.J4KSDK;

import morozov.system.kinect.modes.interfaces.*;

public enum KinectFrameType {
	//
	DEPTH_MAPS {
		@Override
		public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode, boolean extractPeople) {
			consolidatedMode.setDepthMapsAreRequested(true);
			consolidatedMode.refineKinectDataAcquisitionMode(J4KSDK.DEPTH);
		}
		@Override
		public boolean requiresFrameType(KinectDataArrayType proposedFrameType) {
			return (proposedFrameType == KinectDataArrayType.DEPTH_FRAME);
		}
		@Override
		public boolean requiresColorFrameResolution() {
			return false;
		}
	},
	COLORED_DEPTH_MAPS {
		@Override
		public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode, boolean extractPeople) {
			consolidatedMode.setDepthMapsAreRequested(true);
			consolidatedMode.refineKinectDataAcquisitionMode(J4KSDK.DEPTH);
		}
		@Override
		public boolean requiresFrameType(KinectDataArrayType proposedFrameType) {
			return (proposedFrameType == KinectDataArrayType.DEPTH_FRAME);
		}
	},
	INFRARED {
		@Override
		public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode, boolean extractPeople) {
			consolidatedMode.setInfraredFramesAreRequested(true);
			consolidatedMode.refineKinectDataAcquisitionMode(J4KSDK.INFRARED);
		}
		@Override
		public boolean requiresFrameType(KinectDataArrayType proposedFrameType) {
			return (proposedFrameType == KinectDataArrayType.INFRARED_FRAME);
		}
	},
	LONG_EXPOSURE_INFRARED {
		@Override
		public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode, boolean extractPeople) {
			consolidatedMode.setLongExposureInfraredFramesAreRequested(true);
			consolidatedMode.refineKinectDataAcquisitionMode(J4KSDK.LONG_EXPOSURE_INFRARED);
		}
		@Override
		public boolean requiresFrameType(KinectDataArrayType proposedFrameType) {
			return (proposedFrameType == KinectDataArrayType.LONG_EXPOSURE_INFRARED_FRAME);
		}
	},
	MAPPED_COLOR {
		@Override
		public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode, boolean extractPeople) {
			consolidatedMode.setMappedColorFramesAreRequested(true);
			consolidatedMode.refineKinectDataAcquisitionMode(J4KSDK.DEPTH | J4KSDK.UV | J4KSDK.COLOR);
		}
		@Override
		public boolean requiresFrameType(KinectDataArrayType proposedFrameType) {
			return (proposedFrameType == KinectDataArrayType.MAPPED_COLOR_FRAME);
		}
	},
	POINT_CLOUDS {
		@Override
		public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode, boolean extractPeople) {
			if (extractPeople) {
				consolidatedMode.setForegroundPointCloudsAreRequested(true);
			} else {
				consolidatedMode.setEntirePointCloudsAreRequested(true);
			};
			consolidatedMode.refineKinectDataAcquisitionMode(J4KSDK.DEPTH | J4KSDK.XYZ | J4KSDK.UV | J4KSDK.COLOR);
		}
		@Override
		public boolean requiresFrameType(KinectDataArrayType proposedFrameType) {
			return (proposedFrameType == KinectDataArrayType.POINT_CLOUDS_FRAME);
		}
	},
	COLOR {
		@Override
		public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode, boolean extractPeople) {
			consolidatedMode.setColorFramesAreRequested(true);
			consolidatedMode.refineKinectDataAcquisitionMode(J4KSDK.COLOR);
		}
		@Override
		public boolean requiresFrameType(KinectDataArrayType proposedFrameType) {
			return (proposedFrameType == KinectDataArrayType.COLOR_FRAME);
		}
		@Override
		public boolean requiresColorFrameResolution() {
			return true;
		}
	},
	DEVICE_TUNING {
		@Override
		public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode, boolean extractPeople) {
			consolidatedMode.setEntirePointCloudsAreRequested(true);
			consolidatedMode.refineKinectDataAcquisitionMode(J4KSDK.DEPTH | J4KSDK.XYZ | J4KSDK.UV | J4KSDK.COLOR);
		}
		@Override
		public boolean requiresFrameType(KinectDataArrayType proposedFrameType) {
			return (proposedFrameType == KinectDataArrayType.POINT_CLOUDS_FRAME);
		}
	},
	NONE {
		@Override
		public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode, boolean extractPeople) {
		}
		@Override
		public boolean requiresFrameType(KinectDataArrayType proposedFrameType) {
			return (proposedFrameType == KinectDataArrayType.DEPTH_FRAME);
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode, boolean extractPeople);
	abstract public boolean requiresFrameType(KinectDataArrayType proposedFrameType);
	//
	public boolean requiresColorFrameResolution() {
		return false;
	}
}
