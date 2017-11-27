// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.interfaces;

import morozov.system.kinect.modes.*;

public interface KinectModeFrameInterface extends KinectFrameInterface {
	//
	public KinectFrameType getKinectFrameType();
	public KinectSkeletonsMode getSkeletonsMode();
	public KinectPeopleIndexMode getPeopleIndexMode();
	public KinectCircumscriptionMode[] getCircumscriptionModes();
	public KinectFrameType[] getDataAcquisitionMode();
	//
	public boolean depthIsSelected();
	public boolean peopleIndexIsSelected();
	public boolean xyzIsSelected();
	public boolean uvIsSelected();
	public boolean infraredIsSelected();
	public boolean longExposureInfraredIsSelected();
	public boolean colorIsSelected();
	public boolean skeletonsAreSelected();
	//
	public String getDescription();
	public void setDescription(String value);
	public String getCopyright();
	public void setCopyright(String value);
	public String getRegistrationDate();
	public void setRegistrationDate(String value);
	public String getRegistrationTime();
	public void setRegistrationTime(String value);
}
