// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes;

import edu.ufl.digitalworlds.j4k.J4KSDK;

import morozov.system.kinect.modes.interfaces.*;

public enum KinectPeopleIndexMode {
	//
	TINCTURE_PEOPLE {
	},
	PAINT_PEOPLE {
	},
	EXTRACT_PEOPLE {
		public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode) {
			consolidatedMode.setBackgroundIsNotRequested(true);
			consolidatedMode.refineKinectDataAcquisitionMode(J4KSDK.DEPTH | J4KSDK.PLAYER_INDEX);
		}
	},
	ADAPTIVELY_EXTRACT_PEOPLE {
		public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode) {
			consolidatedMode.setBackgroundIsNotRequested(true);
			consolidatedMode.refineKinectDataAcquisitionMode(J4KSDK.DEPTH | J4KSDK.PLAYER_INDEX);
		}
	},
	PROJECT_PEOPLE {
		public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode) {
			consolidatedMode.setBackgroundIsNotRequested(false);
			consolidatedMode.refineKinectDataAcquisitionMode(J4KSDK.DEPTH | J4KSDK.UV | J4KSDK.COLOR | J4KSDK.PLAYER_INDEX);
		}
	},
	NONE {
		public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode) {
			consolidatedMode.setBackgroundIsNotRequested(false);
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode) {
		consolidatedMode.setBackgroundIsNotRequested(false);
		consolidatedMode.refineKinectDataAcquisitionMode(J4KSDK.DEPTH | J4KSDK.PLAYER_INDEX);
	}
}
