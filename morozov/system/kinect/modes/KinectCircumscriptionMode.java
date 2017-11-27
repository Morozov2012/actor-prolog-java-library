// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes;

import edu.ufl.digitalworlds.j4k.J4KSDK;

import morozov.system.kinect.modes.interfaces.*;

public enum KinectCircumscriptionMode {
	//
	TOTAL_RECTANGLES {
		public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode, KinectFrameType frameType) {
			consolidatedMode.setCircumscriptionIsRequested(true);
			consolidatedMode.refineKinectDataAcquisitionMode(J4KSDK.DEPTH | J4KSDK.PLAYER_INDEX);
			if (frameType==KinectFrameType.COLOR) {
				consolidatedMode.refineKinectDataAcquisitionMode(J4KSDK.DEPTH | J4KSDK.UV);
			}
		}
	},
	SKELETON_RECTANGLES {
		public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode, KinectFrameType frameType) {
			consolidatedMode.setCircumscriptionIsRequested(true);
			consolidatedMode.refineKinectDataAcquisitionMode(J4KSDK.DEPTH | J4KSDK.PLAYER_INDEX | J4KSDK.SKELETON);
			if (frameType==KinectFrameType.COLOR) {
				consolidatedMode.refineKinectDataAcquisitionMode(J4KSDK.DEPTH | J4KSDK.UV);
			}
		}
	},
	TOTAL_PARALLELEPIPEDS {
		public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode, KinectFrameType frameType) {
			consolidatedMode.setCircumscriptionIsRequested(true);
			consolidatedMode.refineKinectDataAcquisitionMode(J4KSDK.DEPTH | J4KSDK.XYZ | J4KSDK.PLAYER_INDEX);
			if (frameType==KinectFrameType.COLOR) {
				consolidatedMode.refineKinectDataAcquisitionMode(J4KSDK.DEPTH | J4KSDK.UV);
			}
		}
	},
	SKELETON_PARALLELEPIPEDS {
		public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode, KinectFrameType frameType) {
			consolidatedMode.setCircumscriptionIsRequested(true);
			consolidatedMode.refineKinectDataAcquisitionMode(J4KSDK.DEPTH | J4KSDK.PLAYER_INDEX | J4KSDK.SKELETON);
			if (frameType==KinectFrameType.COLOR) {
				consolidatedMode.refineKinectDataAcquisitionMode(J4KSDK.DEPTH | J4KSDK.UV);
			}
		}
	},
	NONE {
		public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode, KinectFrameType frameType) {
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode, KinectFrameType frameType);
}
