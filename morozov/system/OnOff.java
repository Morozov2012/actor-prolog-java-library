// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system;

import target.*;

import morozov.run.*;
import morozov.system.errors.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum OnOff {
	//
	ON {
		public boolean toBoolean() {
			return true;
		}
		public Term toTerm() {
			return termOn;
		}
	},
	OFF {
		public boolean toBoolean() {
			return false;
		}
		public Term toTerm() {
			return termOff;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public boolean toBoolean();
	abstract public Term toTerm();
	//
	///////////////////////////////////////////////////////////////
	//
	protected static String stringOn= "on";
	protected static String stringOff= "off";
	protected static Term termOn= new PrologSymbol(SymbolCodes.symbolCode_E_on);
	protected static Term termOff= new PrologSymbol(SymbolCodes.symbolCode_E_off);
	//
	///////////////////////////////////////////////////////////////
	//
	public static OnOff term2OnOff(Term value, ChoisePoint iX) {
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
	///////////////////////////////////////////////////////////////
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
}
