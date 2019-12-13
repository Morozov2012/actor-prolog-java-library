// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.converters;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.errors.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class IntegerAttributeConverters {
	//
	public static IntegerAttribute argumentToIntegerAttribute(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_default) {
				return new IntegerAttribute();
			} else {
				throw new WrongArgumentIsNotIntegerAttribute(value);
			}
		} catch (TermIsNotASymbol e) {
			long number= GeneralConverters.argumentToLongInteger(value,iX);
			return new IntegerAttribute(number);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(IntegerAttribute attribute) {
		try {
			return new PrologInteger(attribute.getValue());
		} catch (UseDefaultValue e) {
			return termDefault;
		}
	}
}
