// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes;

import morozov.system.kinect.modes.converters.*;
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
	public static KinectFrameType defaultFrameType= KinectFrameType.COLORED_DEPTH_MAPS;
	public static KinectPeopleIndexMode defaultPeopleIndexMode= KinectPeopleIndexMode.PAINT_PEOPLE;
	public static KinectCircumscriptionMode defaultCircumscriptionMode= KinectCircumscriptionMode.SKELETON_PARALLELEPIPEDS;
	public static KinectCircumscriptionMode[] defaultCircumscriptionModes= new KinectCircumscriptionMode[]{defaultCircumscriptionMode};
	public static KinectSkeletonsMode defaultSkeletonsMode= KinectSkeletonsMode.DETECT_AND_TRACK_SKELETONS;
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
	public boolean getUseDefaultMode() {
		return useDefaultMode;
	}
	public KinectFrameType getLocalFrameType() {
		return frameType;
	}
	public KinectPeopleIndexMode getLocalPeopleIndexMode() {
		return peopleIndexMode;
	}
	public KinectCircumscriptionMode[] getLocalCircumscriptionModes() {
		return circumscriptionModes;
	}
	public KinectSkeletonsMode getLocalSkeletonsMode() {
		return skeletonsMode;
	}
	//
	public KinectFrameType getActingFrameType() {
		if (useDefaultMode) {
			return defaultFrameType;
		} else {
			return frameType;
		}
	}
	public KinectPeopleIndexMode getActingPeopleIndexMode() {
		if (useDefaultMode) {
			return defaultPeopleIndexMode;
		} else {
			return peopleIndexMode;
		}
	}
	public KinectCircumscriptionMode[] getActingCircumscriptionModes() {
		if (useDefaultMode) {
			return defaultCircumscriptionModes;
		} else {
			return circumscriptionModes;
		}
	}
	public KinectSkeletonsMode getActingSkeletonsMode() {
		if (useDefaultMode) {
			return defaultSkeletonsMode;
		} else {
			return skeletonsMode;
		}
	}
	//
	public boolean requiresFrameType(KinectFrameType type) {
		if (useDefaultMode) {
			return defaultFrameType == type;
		} else {
			return frameType == type;
		}
	}
	public boolean requiresFrameType(KinectDataArrayType type) {
		if (useDefaultMode) {
			return defaultFrameType.requiresFrameType(type);
		} else {
			return frameType.requiresFrameType(type);
		}
	}
	public boolean requiresPeopleIndexMode(KinectPeopleIndexMode mode) {
		if (useDefaultMode) {
			return defaultPeopleIndexMode == mode;
		} else {
			return peopleIndexMode == mode;
		}
	}
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
	public boolean requiresSkeletonsMode(KinectSkeletonsMode mode) {
		if (useDefaultMode) {
			return defaultSkeletonsMode == mode;
		} else {
			return skeletonsMode == mode;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public String toString() {
		return KinectDisplayingModeConverters.toTerm(this).toString();
	}
}
