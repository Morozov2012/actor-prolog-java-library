// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.converters;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.errors.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class YesNoDefaultConverters {
	//
	public static YesNoDefault argument2YesNoDefault(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_yes) {
				return YesNoDefault.YES;
			} else if (code==SymbolCodes.symbolCode_E_no) {
				return YesNoDefault.NO;
			} else if (code==SymbolCodes.symbolCode_E_default) {
				return YesNoDefault.DEFAULT;
			} else {
				throw new WrongArgumentIsNotTheYesNoDefaultSwitch(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotTheYesNoDefaultSwitch(value);
		}
	}
	//
	public static Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
		value= value.dereferenceValue(iX);
		if (value.thisIsUnknownValue()) {
			return termDefault;
		} else {
			try {
				long code= value.getSymbolValue(iX);
				if (code==SymbolCodes.symbolCode_E_yes) {
					return termYes;
				} else if (code==SymbolCodes.symbolCode_E_no) {
					return termNo;
				} else if (code==SymbolCodes.symbolCode_E_default) {
					return termDefault;
				} else {
					throw RejectValue.instance;
				}
			} catch (TermIsNotASymbol e) {
				throw RejectValue.instance;
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termYes= new PrologSymbol(SymbolCodes.symbolCode_E_yes);
	protected static Term termNo= new PrologSymbol(SymbolCodes.symbolCode_E_no);
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(YesNoDefault value) {
		switch (value) {
		case YES:
			return termYes;
		case NO:
			return termNo;
		case DEFAULT:
			return termDefault;
		};
		throw new UnknownYesNoDefault(value);
	}
}
