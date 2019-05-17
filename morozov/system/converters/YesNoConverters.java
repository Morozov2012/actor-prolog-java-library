// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.converters;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.errors.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class YesNoConverters {
	//
	public static YesNo argument2YesNo(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_yes) {
				return YesNo.YES;
			} else if (code==SymbolCodes.symbolCode_E_no) {
				return YesNo.NO;
			} else {
				throw new WrongArgumentIsNotTheYesNoSwitch(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotTheYesNoSwitch(value);
		}
	}
	//
	public static Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
		value= value.dereferenceValue(iX);
		if (value.thisIsUnknownValue()) {
			throw RejectValue.instance;
		} else {
			try {
				long code= value.getSymbolValue(iX);
				if (code==SymbolCodes.symbolCode_E_yes) {
					return termYes;
				} else if (code==SymbolCodes.symbolCode_E_no) {
					return termNo;
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
	protected static String stringYes= "yes";
	protected static String stringNo= "no";
	//
	///////////////////////////////////////////////////////////////
	//
	public static String boolean2StringYesNo(boolean value) {
		if (value) {
			return stringYes;
		} else {
			return stringNo;
		}
	}
	//
	public static Term boolean2TermYesNo(boolean value) {
		if (value) {
			return termYes;
		} else {
			return termNo;
		}
	}
	//
	public static boolean termYesNo2Boolean(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_yes) {
				return true;
			} else if (code==SymbolCodes.symbolCode_E_no) {
				return false;
			} else {
				throw new WrongArgumentIsNotTheYesNoSwitch(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotTheYesNoSwitch(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termYes= new PrologSymbol(SymbolCodes.symbolCode_E_yes);
	protected static Term termNo= new PrologSymbol(SymbolCodes.symbolCode_E_no);
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(YesNo value) {
		switch (value) {
		case YES:
			return termYes;
		case NO:
			return termNo;
		};
		throw new UnknownYesNo(value);
	}
}
