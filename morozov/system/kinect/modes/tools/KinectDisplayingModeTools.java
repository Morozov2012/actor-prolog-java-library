// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.tools;

import morozov.system.kinect.modes.*;

public class KinectDisplayingModeTools {
	//
	protected static final KinectFrameType defaultFrameType= KinectFrameType.COLORED_DEPTH_MAPS;
	protected static final KinectPeopleIndexMode defaultPeopleIndexMode= KinectPeopleIndexMode.PAINT_PEOPLE;
	protected static final KinectCircumscriptionMode defaultCircumscriptionMode= KinectCircumscriptionMode.SKELETON_PARALLELEPIPEDS;
	protected static final KinectCircumscriptionMode[] defaultCircumscriptionModes= new KinectCircumscriptionMode[]{defaultCircumscriptionMode};
	protected static final KinectSkeletonsMode defaultSkeletonsMode= KinectSkeletonsMode.DETECT_AND_TRACK_SKELETONS;
	//
	public static KinectFrameType getDefaultFrameType() {
		return defaultFrameType;
	}
	public static KinectPeopleIndexMode getDefaultPeopleIndexMode() {
		return defaultPeopleIndexMode;
	}
	public static KinectCircumscriptionMode getDefaultCircumscriptionMode() {
		return defaultCircumscriptionMode;
	}
	public static KinectCircumscriptionMode[] getDefaultCircumscriptionModes() {
		return defaultCircumscriptionModes;
	}
	public static KinectSkeletonsMode getDefaultSkeletonsMode() {
		return defaultSkeletonsMode;
	}
}
