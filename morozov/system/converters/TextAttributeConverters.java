// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.converters;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.errors.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class TextAttributeConverters {
	//
	public static TextAttribute argumentToTextAttribute(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_default) {
				return new TextAttribute();
			} else {
				throw new WrongArgumentIsNotTextAttribute(value);
			}
		} catch (TermIsNotASymbol e) {
			String text= GeneralConverters.argumentToString(value,iX);
			return new TextAttribute(text);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(TextAttribute attribute) {
		try {
			return new PrologString(attribute.getValue());
		} catch (UseDefaultValue e) {
			return termDefault;
		}
	}
}
