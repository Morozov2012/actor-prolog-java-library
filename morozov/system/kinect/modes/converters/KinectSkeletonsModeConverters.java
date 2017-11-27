// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.converters;

import edu.ufl.digitalworlds.j4k.J4KSDK;

import target.*;

import morozov.run.*;
import morozov.system.kinect.modes.*;
import morozov.system.kinect.modes.converters.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class KinectSkeletonsModeConverters {
	//
	public static KinectSkeletonsMode argumentToKinectSkeletonsMode(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_DETECT_SKELETONS) {
				return KinectSkeletonsMode.DETECT_SKELETONS;
			} else if (code==SymbolCodes.symbolCode_E_DETECT_AND_TRACK_SKELETONS) {
				return KinectSkeletonsMode.DETECT_AND_TRACK_SKELETONS;
			} else if (code==SymbolCodes.symbolCode_E_NONE) {
				return KinectSkeletonsMode.NONE;
			} else {
				throw new WrongArgumentIsNotKinectSkeletonsMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotKinectSkeletonsMode(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termDetectSkeletons= new PrologSymbol(SymbolCodes.symbolCode_E_DETECT_SKELETONS);
	protected static Term termDetectAndTrackSkeletons= new PrologSymbol(SymbolCodes.symbolCode_E_DETECT_AND_TRACK_SKELETONS);
	protected static Term termNone= new PrologSymbol(SymbolCodes.symbolCode_E_NONE);
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(KinectSkeletonsMode mode) {
		switch (mode) {
		case DETECT_SKELETONS:
			return termDetectSkeletons;
		case DETECT_AND_TRACK_SKELETONS:
			return termDetectAndTrackSkeletons;
		case NONE:
			return termNone;
		};
		return termNone;
	}
}
