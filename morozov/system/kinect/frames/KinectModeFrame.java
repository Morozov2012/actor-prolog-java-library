// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames;

import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.frames.text.*;
import morozov.system.kinect.frames.tools.*;
import morozov.system.kinect.modes.*;

import java.io.BufferedWriter;
import java.io.IOException;

import java.util.Locale;

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
	public boolean isLightweightFrame() {
		return true;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public KinectFrameType getKinectFrameType() {
		return kinectFrameType;
	}
	public KinectSkeletonsMode getSkeletonsMode() {
		return skeletonsMode;
	}
	public KinectPeopleIndexMode getPeopleIndexMode() {
		return peopleIndexMode;
	}
	public KinectCircumscriptionMode[] getCircumscriptionModes() {
		return circumscriptionModes;
	}
	public KinectFrameType[] getDataAcquisitionMode() {
		return dataAcquisitionMode;
	}
	//
	public boolean depthIsSelected() {
		return depthIsSelected;
	}
	public boolean peopleIndexIsSelected() {
		return peopleIndexIsSelected;
	}
	public boolean xyzIsSelected() {
		return xyzIsSelected;
	}
	public boolean uvIsSelected() {
		return uvIsSelected;
	}
	public boolean infraredIsSelected() {
		return infraredIsSelected;
	}
	public boolean longExposureInfraredIsSelected() {
		return longExposureInfraredIsSelected;
	}
	public boolean colorIsSelected() {
		return colorIsSelected;
	}
	public boolean skeletonsAreSelected() {
		return skeletonsAreSelected;
	}
	//
	public String getDescription() {
		return description;
	}
	public void setDescription(String value) {
		description= value;
	}
	public String getCopyright() {
		return copyright;
	}
	public void setCopyright(String value) {
		copyright= value;
	}
	public String getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(String value) {
		registrationDate= value;
	}
	public String getRegistrationTime() {
		return registrationTime;
	}
	public void setRegistrationTime(String value) {
		registrationTime= value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public String toString() {
		return KinectModeFrameText.toString(this);
	}
}
