// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.converters;

import target.*;

import morozov.run.*;
import morozov.system.kinect.modes.*;
import morozov.system.kinect.modes.converters.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class KinectSurfaceTypeConverters {
	//
	public static KinectSurfaceType argumentToKinectSurfaceType(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_CONVEX) {
				return KinectSurfaceType.CONVEX;
			} else if (code==SymbolCodes.symbolCode_E_CONCAVE) {
				return KinectSurfaceType.CONCAVE;
			} else if (code==SymbolCodes.symbolCode_E_default) {
				return KinectSurfaceType.DEFAULT;
			} else {
				throw new WrongArgumentIsNotKinectSurfaceType(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotKinectSurfaceType(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termConves= new PrologSymbol(SymbolCodes.symbolCode_E_CONVEX);
	protected static Term termConcave= new PrologSymbol(SymbolCodes.symbolCode_E_CONCAVE);
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(KinectSurfaceType mode) {
		switch (mode) {
		case CONVEX:
			return termConves;
		case CONCAVE:
			return termConcave;
		case DEFAULT:
			return termDefault;
		};
		return termDefault;
	}
}
