// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.converters;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.errors.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class OnOffConverters {
	//
	public static OnOff argument2OnOff(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_on) {
				return OnOff.ON;
			} else if (code==SymbolCodes.symbolCode_E_off) {
				return OnOff.OFF;
			} else {
				throw new WrongArgumentIsNotTheOnOffSwitch(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotTheOnOffSwitch(value);
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
				if (code==SymbolCodes.symbolCode_E_on) {
					return termOn;
				} else if (code==SymbolCodes.symbolCode_E_off) {
					return termOff;
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
	protected static String stringOn= "on";
	protected static String stringOff= "off";
	//
	///////////////////////////////////////////////////////////////
	//
	public static String boolean2StringOnOff(boolean value) {
		if (value) {
			return stringOn;
		} else {
			return stringOff;
		}
	}
	//
	public static Term boolean2TermOnOff(boolean value) {
		if (value) {
			return termOn;
		} else {
			return termOff;
		}
	}
	//
	public static boolean termOnOff2Boolean(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_on) {
				return true;
			} else if (code==SymbolCodes.symbolCode_E_off) {
				return false;
			} else {
				throw new WrongArgumentIsNotTheOnOffSwitch(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotTheOnOffSwitch(value);
		}
	}
	//
	public static boolean termOnOffDefault2Boolean(Term value, ChoisePoint iX) throws TermIsSymbolDefault {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_on) {
				return true;
			} else if (code==SymbolCodes.symbolCode_E_off) {
				return false;
			} else if (code==SymbolCodes.symbolCode_E_default) {
				throw TermIsSymbolDefault.instance;
			} else {
				throw new WrongArgumentIsNotTheOnOffDefaultSwitch(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotTheOnOffDefaultSwitch(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termOn= new PrologSymbol(SymbolCodes.symbolCode_E_on);
	protected static Term termOff= new PrologSymbol(SymbolCodes.symbolCode_E_off);
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(OnOff value) {
		switch (value) {
		case ON:
			return termOn;
		case OFF:
			return termOff;
		};
		throw new UnknownOnOff(value);
	}
}
