// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes;

import morozov.system.kinect.modes.interfaces.*;

public class KinectDisplayingMode implements KinectDisplayingModeInterface {
	//
	protected boolean useDefaultMode= false;
	protected KinectFrameType frameType;
	protected KinectPeopleIndexMode peopleIndexMode;
	protected KinectCircumscriptionMode[] circumscriptionModes;
	protected KinectSkeletonsMode skeletonsMode;
	//
	///////////////////////////////////////////////////////////////
	//
	public static final KinectFrameType defaultFrameType= KinectFrameType.COLORED_DEPTH_MAPS;
	public static final KinectPeopleIndexMode defaultPeopleIndexMode= KinectPeopleIndexMode.PAINT_PEOPLE;
	public static final KinectCircumscriptionMode defaultCircumscriptionMode= KinectCircumscriptionMode.SKELETON_PARALLELEPIPEDS;
	public static final KinectCircumscriptionMode[] defaultCircumscriptionModes= new KinectCircumscriptionMode[]{defaultCircumscriptionMode};
	public static final KinectSkeletonsMode defaultSkeletonsMode= KinectSkeletonsMode.DETECT_AND_TRACK_SKELETONS;
	//
	///////////////////////////////////////////////////////////////
	//
	public KinectDisplayingMode(KinectFrameType t, KinectPeopleIndexMode i, KinectCircumscriptionMode[] c, KinectSkeletonsMode s) {
		useDefaultMode= false;
		frameType= t;
		peopleIndexMode= i;
		circumscriptionModes= c;
		skeletonsMode= s;
	}
	public KinectDisplayingMode() {
		useDefaultMode= true;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean getUseDefaultMode() {
		return useDefaultMode;
	}
	@Override
	public KinectFrameType getLocalFrameType() {
		return frameType;
	}
	@Override
	public KinectPeopleIndexMode getLocalPeopleIndexMode() {
		return peopleIndexMode;
	}
	@Override
	public KinectCircumscriptionMode[] getLocalCircumscriptionModes() {
		return circumscriptionModes;
	}
	@Override
	public KinectSkeletonsMode getLocalSkeletonsMode() {
		return skeletonsMode;
	}
	//
	@Override
	public KinectFrameType getActingFrameType() {
		if (useDefaultMode) {
			return defaultFrameType;
		} else {
			return frameType;
		}
	}
	@Override
	public KinectPeopleIndexMode getActingPeopleIndexMode() {
		if (useDefaultMode) {
			return defaultPeopleIndexMode;
		} else {
			return peopleIndexMode;
		}
	}
	@Override
	public KinectCircumscriptionMode[] getActingCircumscriptionModes() {
		if (useDefaultMode) {
			return defaultCircumscriptionModes;
		} else {
			return circumscriptionModes;
		}
	}
	@Override
	public KinectSkeletonsMode getActingSkeletonsMode() {
		if (useDefaultMode) {
			return defaultSkeletonsMode;
		} else {
			return skeletonsMode;
		}
	}
	//
	@Override
	public boolean requiresFrameType(KinectFrameType type) {
		if (useDefaultMode) {
			return defaultFrameType == type;
		} else {
			return frameType == type;
		}
	}
	@Override
	public boolean requiresFrameType(KinectDataArrayType type) {
		if (useDefaultMode) {
			return defaultFrameType.requiresFrameType(type);
		} else {
			return frameType.requiresFrameType(type);
		}
	}
	@Override
	public boolean requiresPeopleIndexMode(KinectPeopleIndexMode mode) {
		if (useDefaultMode) {
			return defaultPeopleIndexMode == mode;
		} else {
			return peopleIndexMode == mode;
		}
	}
	@Override
	public boolean requiresCircumscriptionMode(KinectCircumscriptionMode mode) {
		if (useDefaultMode) {
			return defaultCircumscriptionMode == mode;
		} else {
			if (circumscriptionModes.length <= 0) {
				return true;
			} else {
				for (int n=0; n < circumscriptionModes.length; n++) {
					if (circumscriptionModes[n] == mode) {
						return true;
					}
				};
				return false;
			}
		}
	}
	@Override
	public boolean requiresSkeletonsMode(KinectSkeletonsMode mode) {
		if (useDefaultMode) {
			return defaultSkeletonsMode == mode;
		} else {
			return skeletonsMode == mode;
		}
	}
}
