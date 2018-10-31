// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.converters;

import target.*;

import morozov.run.*;
import morozov.system.kinect.modes.*;
import morozov.system.kinect.modes.converters.errors.*;
import morozov.system.kinect.modes.interfaces.*;
import morozov.system.kinect.modes.tools.*;
import morozov.terms.*;
import morozov.terms.errors.*;
import morozov.terms.signals.*;

import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

public class KinectDisplayingModeConverters {
	//
	public static KinectDisplayingMode argumentToKinectDisplayingMode(Term attributes, ChoisePoint iX) {
		try {
			long code= attributes.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_default) {
				return new KinectDisplayingMode();
			} else {
				throw new WrongArgumentIsNotKinectDisplayingMode(attributes);
			}
		} catch (TermIsNotASymbol e) {
			HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
			Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
			setEnd= setEnd.dereferenceValue(iX);
			if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
				KinectFrameType frameType= KinectDisplayingModeTools.getDefaultFrameType();
				KinectPeopleIndexMode peopleIndexMode= KinectDisplayingModeTools.getDefaultPeopleIndexMode();
				KinectCircumscriptionMode[] circumscriptionModes= KinectDisplayingModeTools.getDefaultCircumscriptionModes();
				KinectSkeletonsMode skeletonsMode= KinectDisplayingModeTools.getDefaultSkeletonsMode();
				Set<Long> nameList= setPositiveMap.keySet();
				Iterator<Long> iterator= nameList.iterator();
				while(iterator.hasNext()) {
					long key= iterator.next();
					long pairName= - key;
					Term pairValue= setPositiveMap.get(key);
					if (pairName==SymbolCodes.symbolCode_E_frame_type) {
						frameType= KinectFrameTypeConverters.argumentToKinectFrameType(pairValue,iX);
					} else if (pairName==SymbolCodes.symbolCode_E_people_index) {
						peopleIndexMode= KinectPeopleIndexModeConverters.argumentToKinectPeopleIndexMode(pairValue,iX);
					} else if (pairName==SymbolCodes.symbolCode_E_circumscription) {
						circumscriptionModes= KinectCircumscriptionModesConverters.argumentToKinectCircumscriptionModes(pairValue,iX);
					} else if (pairName==SymbolCodes.symbolCode_E_skeletons) {
						skeletonsMode= KinectSkeletonsModeConverters.argumentToKinectSkeletonsMode(pairValue,iX);
					} else {
						throw new WrongArgumentIsUnknownKinectDisplayingModeAttribute(key);
					}
				};
				return new KinectDisplayingMode(frameType,peopleIndexMode,circumscriptionModes,skeletonsMode);
			} else {
				throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(KinectDisplayingModeInterface mode) {
		if (mode.getUseDefaultMode()) {
			return termDefault;
		} else {
			KinectFrameType frameType= mode.getLocalFrameType();
			KinectPeopleIndexMode peopleIndexMode= mode.getLocalPeopleIndexMode();
			KinectCircumscriptionMode[] circumscriptionModes= mode.getLocalCircumscriptionModes();
			KinectSkeletonsMode skeletonsMode= mode.getLocalSkeletonsMode();
			Term value= PrologEmptySet.instance;
			if (skeletonsMode != null) {
				value= new PrologSet(-SymbolCodes.symbolCode_E_skeletons,KinectSkeletonsModeConverters.toTerm(skeletonsMode),value);
			};
			if (circumscriptionModes != null) {
				value= new PrologSet(-SymbolCodes.symbolCode_E_circumscription,KinectCircumscriptionModesConverters.toTerm(circumscriptionModes),value);
			};
			if (peopleIndexMode != null) {
				value= new PrologSet(-SymbolCodes.symbolCode_E_people_index,KinectPeopleIndexModeConverters.toTerm(peopleIndexMode),value);
			};
			if (frameType != null) {
				value= new PrologSet(-SymbolCodes.symbolCode_E_frame_type,KinectFrameTypeConverters.toTerm(frameType),value);
			};
			return value;
		}
	}
}
