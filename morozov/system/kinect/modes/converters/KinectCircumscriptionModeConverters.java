// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.converters;

import edu.ufl.digitalworlds.j4k.J4KSDK;

import target.*;

import morozov.run.*;
import morozov.system.kinect.modes.*;
import morozov.system.kinect.modes.converters.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class KinectCircumscriptionModeConverters {
	//
	public static KinectCircumscriptionMode argumentToKinectCircumscriptionMode(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_TOTAL_RECTANGLES) {
				return KinectCircumscriptionMode.TOTAL_RECTANGLES;
			} else if (code==SymbolCodes.symbolCode_E_SKELETON_RECTANGLES) {
				return KinectCircumscriptionMode.SKELETON_RECTANGLES;
			} else if (code==SymbolCodes.symbolCode_E_TOTAL_PARALLELEPIPEDS) {
				return KinectCircumscriptionMode.TOTAL_PARALLELEPIPEDS;
			} else if (code==SymbolCodes.symbolCode_E_SKELETON_PARALLELEPIPEDS) {
				return KinectCircumscriptionMode.SKELETON_PARALLELEPIPEDS;
			} else if (code==SymbolCodes.symbolCode_E_NONE) {
				return KinectCircumscriptionMode.NONE;
			} else {
				throw new WrongArgumentIsNotKinectCircumscriptionMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotKinectCircumscriptionMode(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termTotalRectangles= new PrologSymbol(SymbolCodes.symbolCode_E_TOTAL_RECTANGLES);
	protected static Term termSkeletonRectangles= new PrologSymbol(SymbolCodes.symbolCode_E_SKELETON_RECTANGLES);
	protected static Term termTotalParallelepipeds= new PrologSymbol(SymbolCodes.symbolCode_E_TOTAL_PARALLELEPIPEDS);
	protected static Term termSkeletonParallelepipeds= new PrologSymbol(SymbolCodes.symbolCode_E_SKELETON_PARALLELEPIPEDS);
	protected static Term termNone= new PrologSymbol(SymbolCodes.symbolCode_E_NONE);
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(KinectCircumscriptionMode mode) {
		switch (mode) {
		case TOTAL_RECTANGLES:
			return termTotalRectangles;
		case SKELETON_RECTANGLES:
			return termSkeletonRectangles;
		case TOTAL_PARALLELEPIPEDS:
			return termTotalParallelepipeds;
		case SKELETON_PARALLELEPIPEDS:
			return termSkeletonParallelepipeds;
		case NONE:
			return termNone;
		};
		return termNone;
	}
}
