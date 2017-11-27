// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.converters;

import target.*;

import morozov.run.*;
import morozov.system.vision.vpm.converters.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum PushOperationMode {
	//
	SELECT_ALL {
		public Term toTerm() {
			return term_SELECT_ALL;
		}
	},
	SELECT_NOTHING {
		public Term toTerm() {
			return term_SELECT_NOTHING;
		}
	},
	DUPLICATE_FOREGROUND {
		public Term toTerm() {
			return term_DUPLICATE_FOREGROUND;
		}
	},
	FLIP_FOREGROUND {
		public Term toTerm() {
			return term_FLIP_FOREGROUND;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public static PushOperationMode argumentToPushOperationMode(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_SELECT_ALL) {
				return PushOperationMode.SELECT_ALL;
			} else if (code==SymbolCodes.symbolCode_E_SELECT_NOTHING) {
				return PushOperationMode.SELECT_NOTHING;
			} else if (code==SymbolCodes.symbolCode_E_DUPLICATE_FOREGROUND) {
				return PushOperationMode.DUPLICATE_FOREGROUND;
			} else if (code==SymbolCodes.symbolCode_E_FLIP_FOREGROUND) {
				return PushOperationMode.FLIP_FOREGROUND;
			} else {
				throw new WrongArgumentIsNotPushOperationMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotPushOperationMode(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term term_SELECT_ALL= new PrologSymbol(SymbolCodes.symbolCode_E_SELECT_ALL);
	protected static Term term_SELECT_NOTHING= new PrologSymbol(SymbolCodes.symbolCode_E_SELECT_NOTHING);
	protected static Term term_DUPLICATE_FOREGROUND= new PrologSymbol(SymbolCodes.symbolCode_E_DUPLICATE_FOREGROUND);
	protected static Term term_FLIP_FOREGROUND= new PrologSymbol(SymbolCodes.symbolCode_E_FLIP_FOREGROUND);
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term toTerm();
}
