// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes;

import edu.ufl.digitalworlds.j4k.J4KSDK;

import morozov.system.kinect.modes.interfaces.*;

public enum KinectSkeletonsMode {
	//
	DETECT_SKELETONS {
		@Override
		public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode, KinectFrameType frameType) {
			consolidatedMode.setSkeletonsAreRequested(true);
			consolidatedMode.refineKinectDataAcquisitionMode(J4KSDK.SKELETON);
			if (frameType==KinectFrameType.COLOR) {
				consolidatedMode.refineKinectDataAcquisitionMode(J4KSDK.DEPTH | J4KSDK.UV);
			}
		}
	},
	DETECT_AND_TRACK_SKELETONS {
		@Override
		public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode, KinectFrameType frameType) {
			DETECT_SKELETONS.refineDataAcquisitionMode(consolidatedMode,frameType);
		}
	},
	NONE {
		@Override
		public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode, KinectFrameType frameType) {
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode, KinectFrameType frameType);
}
