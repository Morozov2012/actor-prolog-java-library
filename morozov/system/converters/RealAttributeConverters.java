// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.converters;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.errors.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class RealAttributeConverters {
	//
	public static RealAttribute argumentToRealAttribute(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_default) {
				return new RealAttribute();
			} else {
				throw new WrongArgumentIsNotRealAttribute(value);
			}
		} catch (TermIsNotASymbol e) {
			double number= GeneralConverters.argumentToReal(value,iX);
			return new RealAttribute(number);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(RealAttribute attribute) {
		try {
			return new PrologReal(attribute.getValue());
		} catch (UseDefaultValue e) {
			return termDefault;
		}
	}
}
