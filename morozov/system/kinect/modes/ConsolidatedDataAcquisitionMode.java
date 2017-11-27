// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes;

import edu.ufl.digitalworlds.j4k.J4KSDK;

import morozov.system.kinect.modes.interfaces.*;

public class ConsolidatedDataAcquisitionMode implements ConsolidatedDataAcquisitionModeInterface {
	//
	protected boolean depthMapsAreRequested= false;
	protected boolean infraredFramesAreRequested= false;
	protected boolean longExposureInfraredFramesAreRequested= false;
	protected boolean mappedColorFramesAreRequested= false;
	protected boolean pointCloudsAreRequested= false;
	protected boolean colorFramesAreRequested= false;
	protected boolean skeletonsAreRequested= false;
	protected boolean circumscriptionIsRequested= false;
	protected boolean backgroundIsNotRequested= false;
	protected int kinectDataAcquisitionMode= 0;
	//
	///////////////////////////////////////////////////////////////
	//
	public void setDepthMapsAreRequested(boolean value) {
		depthMapsAreRequested= value;
	}
	public void setInfraredFramesAreRequested(boolean value) {
		infraredFramesAreRequested= value;
	}
	public void setLongExposureInfraredFramesAreRequested(boolean value) {
		longExposureInfraredFramesAreRequested= value;
	}
	public void setMappedColorFramesAreRequested(boolean value) {
		mappedColorFramesAreRequested= value;
	}
	public void setPointCloudsAreRequested(boolean value) {
		pointCloudsAreRequested= value;
	}
	public void setColorFramesAreRequested(boolean value) {
		colorFramesAreRequested= value;
	}
	public void setSkeletonsAreRequested(boolean value) {
		skeletonsAreRequested= value;
	}
	public void setCircumscriptionIsRequested(boolean value) {
		circumscriptionIsRequested= value;
	}
	public void setBackgroundIsNotRequested(boolean value) {
		backgroundIsNotRequested= value;
	}
	public void setKinectDataAcquisitionMode(int value) {
		kinectDataAcquisitionMode= value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean getDepthMapsAreRequested() {
		return depthMapsAreRequested;
	}
	public boolean getInfraredFramesAreRequested() {
		return infraredFramesAreRequested;
	}
	public boolean getLongExposureInfraredFramesAreRequested() {
		return longExposureInfraredFramesAreRequested;
	}
	public boolean getMappedColorFramesAreRequested() {
		return mappedColorFramesAreRequested;
	}
	public boolean getPointCloudsAreRequested() {
		return pointCloudsAreRequested;
	}
	public boolean getColorFramesAreRequested() {
		return colorFramesAreRequested;
	}
	public boolean getSkeletonsAreRequested() {
		return skeletonsAreRequested;
	}
	public boolean getCircumscriptionIsRequested() {
		return circumscriptionIsRequested;
	}
	public boolean getBackgroundIsNotRequested() {
		return backgroundIsNotRequested;
	}
	public int getKinectDataAcquisitionMode() {
		return kinectDataAcquisitionMode;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void refineKinectDataAcquisitionMode(int flags) {
		kinectDataAcquisitionMode= kinectDataAcquisitionMode | flags;
	}
	//
	public void complete() {
		if (	(0 <= (kinectDataAcquisitionMode & J4KSDK.DEPTH)) &&
			(0 <= (kinectDataAcquisitionMode & J4KSDK.INFRARED)) &&
			(0 <= (kinectDataAcquisitionMode & J4KSDK.LONG_EXPOSURE_INFRARED)) &&
			(0 <= (kinectDataAcquisitionMode & J4KSDK.COLOR))) {
			depthMapsAreRequested= true;
			kinectDataAcquisitionMode= kinectDataAcquisitionMode | J4KSDK.DEPTH;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean equals(ConsolidatedDataAcquisitionModeInterface mode2) {
		if (kinectDataAcquisitionMode != mode2.getKinectDataAcquisitionMode()) {
			return false;
		} else if (depthMapsAreRequested != mode2.getDepthMapsAreRequested()) {
			return false;
		} else if (infraredFramesAreRequested != mode2.getInfraredFramesAreRequested()) {
			return false;
		} else if (longExposureInfraredFramesAreRequested != mode2.getLongExposureInfraredFramesAreRequested()) {
			return false;
		} else if (mappedColorFramesAreRequested != mode2.getMappedColorFramesAreRequested()) {
			return false;
		} else if (pointCloudsAreRequested != mode2.getPointCloudsAreRequested()) {
			return false;
		} else if (colorFramesAreRequested != mode2.getColorFramesAreRequested()) {
			return false;
		} else if (skeletonsAreRequested != mode2.getSkeletonsAreRequested()) {
			return false;
		} else if (circumscriptionIsRequested != mode2.getCircumscriptionIsRequested()) {
			return false;
		} else if (backgroundIsNotRequested != mode2.getBackgroundIsNotRequested()) {
			return false;
		} else {
			return true;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public String toString() {
		return
			"(" +
			Boolean.toString(getDepthMapsAreRequested()) + ";" +
			Boolean.toString(getInfraredFramesAreRequested()) + ";" +
			Boolean.toString(getLongExposureInfraredFramesAreRequested()) + ";" +
			Boolean.toString(getMappedColorFramesAreRequested()) + ";" +
			Boolean.toString(getPointCloudsAreRequested()) + ";" +
			Boolean.toString(getColorFramesAreRequested()) + ";" +
			Boolean.toString(getSkeletonsAreRequested()) + ";" +
			Boolean.toString(getCircumscriptionIsRequested()) + ";" +
			Boolean.toString(getBackgroundIsNotRequested()) + ";" +
			Integer.toString(getKinectDataAcquisitionMode()) + ")";
	}
}
