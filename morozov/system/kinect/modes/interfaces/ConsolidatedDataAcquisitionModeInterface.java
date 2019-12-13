// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.interfaces;

public interface ConsolidatedDataAcquisitionModeInterface {
	//
	public void setDepthMapsAreRequested(boolean value);
	public void setInfraredFramesAreRequested(boolean value);
	public void setLongExposureInfraredFramesAreRequested(boolean value);
	public void setMappedColorFramesAreRequested(boolean value);
	public void setEntirePointCloudsAreRequested(boolean value);
	public void setForegroundPointCloudsAreRequested(boolean value);
	public void setColorFramesAreRequested(boolean value);
	public void setSkeletonsAreRequested(boolean value);
	public void setCircumscriptionIsRequested(boolean value);
	public void setKinectDataAcquisitionMode(int value);
	//
	public boolean getDepthMapsAreRequested();
	public boolean getInfraredFramesAreRequested();
	public boolean getLongExposureInfraredFramesAreRequested();
	public boolean getMappedColorFramesAreRequested();
	public boolean getEntirePointCloudsAreRequested();
	public boolean getForegroundPointCloudsAreRequested();
	public boolean getColorFramesAreRequested();
	public boolean getSkeletonsAreRequested();
	public boolean getCircumscriptionIsRequested();
	public int getKinectDataAcquisitionMode();
	//
	public void refineKinectDataAcquisitionMode(int flags);
	public void complete();
	//
	public boolean equals(ConsolidatedDataAcquisitionModeInterface mode2);
}
