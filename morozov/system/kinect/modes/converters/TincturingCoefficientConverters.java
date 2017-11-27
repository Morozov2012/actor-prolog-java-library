// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.converters;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.kinect.modes.*;
import morozov.system.kinect.modes.converters.errors.*;
import morozov.system.kinect.modes.interfaces.*;
import morozov.system.kinect.modes.interfaces.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class TincturingCoefficientConverters {
	//
	public static TincturingCoefficient argumentToTincturingCoefficient(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_default) {
				return new TincturingCoefficient();
			} else {
				throw new WrongArgumentIsNotTincturingCoefficient(value);
			}
		} catch (TermIsNotASymbol e) {
			double coefficient= Converters.argumentToReal(value,iX);
			return new TincturingCoefficient(coefficient);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(TincturingCoefficientInterface coefficient) {
		try {
			return new PrologReal(coefficient.getValue());
		} catch (UseDefaultCoefficient e) {
			return termDefault;
		}
	}
}
