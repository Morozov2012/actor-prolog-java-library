// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.interfaces;

import morozov.system.kinect.modes.*;

public interface KinectDisplayingModeInterface {
	//
	public boolean getUseDefaultMode();
	public KinectFrameType getLocalFrameType();
	public KinectPeopleIndexMode getLocalPeopleIndexMode();
	public KinectCircumscriptionMode[] getLocalCircumscriptionModes();
	public KinectSkeletonsMode getLocalSkeletonsMode();
	//
	public KinectFrameType getActingFrameType();
	public KinectPeopleIndexMode getActingPeopleIndexMode();
	public KinectCircumscriptionMode[] getActingCircumscriptionModes();
	public KinectSkeletonsMode getActingSkeletonsMode();
	//
	public boolean requiresFrameType(KinectFrameType type);
	public boolean requiresFrameType(KinectDataArrayType type);
	public boolean requiresPeopleIndexMode(KinectPeopleIndexMode mode);
	public boolean requiresCircumscriptionMode(KinectCircumscriptionMode mode);
	public boolean requiresSkeletonsMode(KinectSkeletonsMode mode);
}
