// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames;

import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.frames.text.*;
import morozov.system.kinect.modes.*;

public class KinectModeFrame extends KinectFrame implements KinectModeFrameInterface {
	//
	protected KinectFrameType kinectFrameType;
	protected KinectSkeletonsMode skeletonsMode;
	protected KinectPeopleIndexMode peopleIndexMode;
	protected KinectCircumscriptionMode[] circumscriptionModes;
	protected KinectFrameType[] dataAcquisitionMode;
	//
	protected boolean depthIsSelected;
	protected boolean peopleIndexIsSelected;
	protected boolean xyzIsSelected;
	protected boolean uvIsSelected;
	protected boolean infraredIsSelected;
	protected boolean longExposureInfraredIsSelected;
	protected boolean colorIsSelected;
	protected boolean skeletonsAreSelected;
	//
	protected String description;
	protected String copyright;
	protected String registrationDate;
	protected String registrationTime;
	//
	private static final long serialVersionUID= 0xB8B17DAD003F62DAL; // -5138187517866056998L;
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.kinect.frames","KinectModeFrame");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public KinectModeFrame(
			long givenSerialNumber,
			long givenTargetFrameNumber,
			long givenColorFrameNumber,
			long givenSkeletonsFrameNumber,
			long givenTargetFrameTime,
			long givenColorFrameTime,
			long givenSkeletonsFrameTime,
			KinectFrameType givenFrameType,
			KinectSkeletonsMode givenSkeletonsMode,
			KinectPeopleIndexMode givenPeopleIndexMode,
			KinectCircumscriptionMode[] givenCircumscriptionModes,
			KinectFrameType[] givenDataAcquisitionMode,
			boolean givenDepthIsSelected,
			boolean givenPeopleIndexIsSelected,
			boolean givenXYZIsSelected,
			boolean givenUVIsSelected,
			boolean givenInfraredIsSelected,
			boolean givenLongExposureInfraredIsSelected,
			boolean givenColorIsSelected,
			boolean givenSkeletonsAreSelected,
			String givenDescription,
			String givenCopyright,
			String givenRegistrationDate,
			String givenRegistrationTime,
			KinectFrameBaseAttributesInterface givenAttributes) {
		super(	KinectDataArrayType.MODE_FRAME,
			givenSerialNumber,
			givenTargetFrameNumber,
			givenColorFrameNumber,
			givenSkeletonsFrameNumber,
			givenTargetFrameTime,
			givenColorFrameTime,
			givenSkeletonsFrameTime,
			null,
			null,
			null,
			null,
			null,
			null,
			givenAttributes);
		kinectFrameType= givenFrameType;
		skeletonsMode= givenSkeletonsMode;
		peopleIndexMode= givenPeopleIndexMode;
		circumscriptionModes= givenCircumscriptionModes;
		dataAcquisitionMode= givenDataAcquisitionMode;
		depthIsSelected= givenDepthIsSelected;
		peopleIndexIsSelected= givenPeopleIndexIsSelected;
		xyzIsSelected= givenXYZIsSelected;
		uvIsSelected= givenUVIsSelected;
		infraredIsSelected= givenInfraredIsSelected;
		longExposureInfraredIsSelected= givenLongExposureInfraredIsSelected;
		colorIsSelected= givenColorIsSelected;
		skeletonsAreSelected= givenSkeletonsAreSelected;
		description= givenDescription;
		copyright= givenCopyright;
		registrationDate= givenRegistrationDate;
		registrationTime= givenRegistrationTime;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean isLightweightFrame() {
		return true;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public KinectFrameType getKinectFrameType() {
		return kinectFrameType;
	}
	@Override
	public KinectSkeletonsMode getSkeletonsMode() {
		return skeletonsMode;
	}
	@Override
	public KinectPeopleIndexMode getPeopleIndexMode() {
		return peopleIndexMode;
	}
	@Override
	public KinectCircumscriptionMode[] getCircumscriptionModes() {
		return circumscriptionModes;
	}
	@Override
	public KinectFrameType[] getDataAcquisitionMode() {
		return dataAcquisitionMode;
	}
	//
	@Override
	public boolean depthIsSelected() {
		return depthIsSelected;
	}
	@Override
	public boolean peopleIndexIsSelected() {
		return peopleIndexIsSelected;
	}
	@Override
	public boolean xyzIsSelected() {
		return xyzIsSelected;
	}
	@Override
	public boolean uvIsSelected() {
		return uvIsSelected;
	}
	@Override
	public boolean infraredIsSelected() {
		return infraredIsSelected;
	}
	@Override
	public boolean longExposureInfraredIsSelected() {
		return longExposureInfraredIsSelected;
	}
	@Override
	public boolean colorIsSelected() {
		return colorIsSelected;
	}
	@Override
	public boolean skeletonsAreSelected() {
		return skeletonsAreSelected;
	}
	//
	@Override
	public String getDescription() {
		return description;
	}
	@Override
	public void setDescription(String value) {
		description= value;
	}
	@Override
	public String getCopyright() {
		return copyright;
	}
	@Override
	public void setCopyright(String value) {
		copyright= value;
	}
	@Override
	public String getRegistrationDate() {
		return registrationDate;
	}
	@Override
	public void setRegistrationDate(String value) {
		registrationDate= value;
	}
	@Override
	public String getRegistrationTime() {
		return registrationTime;
	}
	@Override
	public void setRegistrationTime(String value) {
		registrationTime= value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public String toString() {
		return KinectModeFrameText.toString(this);
	}
}
