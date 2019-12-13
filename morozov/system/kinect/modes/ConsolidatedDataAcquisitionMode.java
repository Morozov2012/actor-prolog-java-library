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
	protected boolean entirePointCloudsAreRequested= false;
	protected boolean foregroundPointCloudsAreRequested= false;
	protected boolean colorFramesAreRequested= false;
	protected boolean skeletonsAreRequested= false;
	protected boolean circumscriptionIsRequested= false;
	protected int kinectDataAcquisitionMode= 0;
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setDepthMapsAreRequested(boolean value) {
		depthMapsAreRequested= value;
	}
	@Override
	public void setInfraredFramesAreRequested(boolean value) {
		infraredFramesAreRequested= value;
	}
	@Override
	public void setLongExposureInfraredFramesAreRequested(boolean value) {
		longExposureInfraredFramesAreRequested= value;
	}
	@Override
	public void setMappedColorFramesAreRequested(boolean value) {
		mappedColorFramesAreRequested= value;
	}
	@Override
	public void setEntirePointCloudsAreRequested(boolean value) {
		entirePointCloudsAreRequested= value;
	}
	@Override
	public void setForegroundPointCloudsAreRequested(boolean value) {
		foregroundPointCloudsAreRequested= value;
	}
	@Override
	public void setColorFramesAreRequested(boolean value) {
		colorFramesAreRequested= value;
	}
	@Override
	public void setSkeletonsAreRequested(boolean value) {
		skeletonsAreRequested= value;
	}
	@Override
	public void setCircumscriptionIsRequested(boolean value) {
		circumscriptionIsRequested= value;
	}
	@Override
	public void setKinectDataAcquisitionMode(int value) {
		kinectDataAcquisitionMode= value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean getDepthMapsAreRequested() {
		return depthMapsAreRequested;
	}
	@Override
	public boolean getInfraredFramesAreRequested() {
		return infraredFramesAreRequested;
	}
	@Override
	public boolean getLongExposureInfraredFramesAreRequested() {
		return longExposureInfraredFramesAreRequested;
	}
	@Override
	public boolean getMappedColorFramesAreRequested() {
		return mappedColorFramesAreRequested;
	}
	@Override
	public boolean getEntirePointCloudsAreRequested() {
		return entirePointCloudsAreRequested;
	}
	@Override
	public boolean getForegroundPointCloudsAreRequested() {
		return foregroundPointCloudsAreRequested;
	}
	@Override
	public boolean getColorFramesAreRequested() {
		return colorFramesAreRequested;
	}
	@Override
	public boolean getSkeletonsAreRequested() {
		return skeletonsAreRequested;
	}
	@Override
	public boolean getCircumscriptionIsRequested() {
		return circumscriptionIsRequested;
	}
	@Override
	public int getKinectDataAcquisitionMode() {
		return kinectDataAcquisitionMode;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void refineKinectDataAcquisitionMode(int flags) {
		kinectDataAcquisitionMode= kinectDataAcquisitionMode | flags;
	}
	//
	@Override
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
	@Override
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
		} else if (entirePointCloudsAreRequested != mode2.getEntirePointCloudsAreRequested()) {
			return false;
		} else if (foregroundPointCloudsAreRequested != mode2.getForegroundPointCloudsAreRequested()) {
			return false;
		} else if (colorFramesAreRequested != mode2.getColorFramesAreRequested()) {
			return false;
		} else if (skeletonsAreRequested != mode2.getSkeletonsAreRequested()) {
			return false;
		} else if (circumscriptionIsRequested != mode2.getCircumscriptionIsRequested()) {
			return false;
		} else {
			return true;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public String toString() {
		return
			"(" +
			Boolean.toString(getDepthMapsAreRequested()) + ";" +
			Boolean.toString(getInfraredFramesAreRequested()) + ";" +
			Boolean.toString(getLongExposureInfraredFramesAreRequested()) + ";" +
			Boolean.toString(getMappedColorFramesAreRequested()) + ";" +
			Boolean.toString(getEntirePointCloudsAreRequested()) + ";" +
			Boolean.toString(getForegroundPointCloudsAreRequested()) + ";" +
			Boolean.toString(getColorFramesAreRequested()) + ";" +
			Boolean.toString(getSkeletonsAreRequested()) + ";" +
			Boolean.toString(getCircumscriptionIsRequested()) + ";" +
			Integer.toString(getKinectDataAcquisitionMode()) + ")";
	}
}
