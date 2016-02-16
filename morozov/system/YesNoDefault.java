// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system;

import target.*;

import morozov.run.*;
import morozov.system.errors.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum YesNoDefault {
	//
	YES {
		public boolean toBoolean(boolean value) {
			return true;
		}
		public Term toTerm() {
			return termYes;
		}
	},
	NO {
		public boolean toBoolean(boolean value) {
			return false;
		}
		public Term toTerm() {
			return termNo;
		}
	},
	DEFAULT {
		public boolean toBoolean(boolean value) {
			return value;
		}
		public Term toTerm() {
			return termDefault;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termYes= new PrologSymbol(SymbolCodes.symbolCode_E_yes);
	protected static Term termNo= new PrologSymbol(SymbolCodes.symbolCode_E_no);
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	///////////////////////////////////////////////////////////////
	//
	public static YesNoDefault term2YesNoDefault(Term value, ChoisePoint iX) {
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
	abstract public boolean toBoolean(boolean value);
	abstract public Term toTerm();
}
