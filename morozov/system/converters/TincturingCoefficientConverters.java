// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.converters;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.errors.*;
import morozov.system.signals.*;
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
			double coefficient= GeneralConverters.argumentToReal(value,iX);
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
	public static Term toTerm(TincturingCoefficient tincturingCoefficient) {
		try {
			return new PrologReal(tincturingCoefficient.getValue());
		} catch (UseDefaultCoefficient e) {
			return termDefault;
		}
	}
}
