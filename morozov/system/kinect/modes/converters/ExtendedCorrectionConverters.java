// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.converters;

import target.*;

import morozov.run.*;
import morozov.system.kinect.modes.*;
import morozov.system.kinect.modes.converters.errors.*;
import morozov.system.kinect.modes.interfaces.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class ExtendedCorrectionConverters {
	//
	public static ExtendedCorrection argumentToExtendedCorrection(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_default) {
				return new ExtendedCorrection();
			} else {
				throw new WrongArgumentIsNotExtendedCorrection(value);
			}
		} catch (TermIsNotASymbol e1) {
			try {
				return new ExtendedCorrection(value.getSmallIntegerValue(iX));
			} catch (TermIsNotAnInteger e2) {
				throw new WrongArgumentIsNotExtendedCorrection(value);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static String stringDefault= "default";
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(ExtendedCorrectionInterface correction) {
		if (correction.getUseDefaultCorrection()) {
			return termDefault;
		} else {
			return new PrologInteger(correction.getValue());
		}
	}
}
