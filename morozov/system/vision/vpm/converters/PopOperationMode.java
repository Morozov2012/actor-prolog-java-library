// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.converters;

import target.*;

import morozov.run.*;
import morozov.system.vision.vpm.converters.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum PopOperationMode {
	//
	AND {
		@Override
		public Term toTerm() {
			return term_AND;
		}
	},
	OR {
		@Override
		public Term toTerm() {
			return term_OR;
		}
	},
	XOR {
		@Override
		public Term toTerm() {
			return term_XOR;
		}
	},
	ASSIGN_FOREGROUND {
		@Override
		public Term toTerm() {
			return term_ASSIGN_FOREGROUND;
		}
	},
	FORGET_FOREGROUND {
		@Override
		public Term toTerm() {
			return term_FORGET_FOREGROUND;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public static PopOperationMode argumentToPopOperationMode(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_AND) {
				return PopOperationMode.AND;
			} else if (code==SymbolCodes.symbolCode_E_OR) {
				return PopOperationMode.OR;
			} else if (code==SymbolCodes.symbolCode_E_XOR) {
				return PopOperationMode.XOR;
			} else if (code==SymbolCodes.symbolCode_E_ASSIGN_FOREGROUND) {
				return PopOperationMode.ASSIGN_FOREGROUND;
			} else if (code==SymbolCodes.symbolCode_E_FORGET_FOREGROUND) {
				return PopOperationMode.FORGET_FOREGROUND;
			} else {
				throw new WrongArgumentIsNotPopOperationMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotPopOperationMode(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term term_AND= new PrologSymbol(SymbolCodes.symbolCode_E_AND);
	protected static Term term_OR= new PrologSymbol(SymbolCodes.symbolCode_E_OR);
	protected static Term term_XOR= new PrologSymbol(SymbolCodes.symbolCode_E_XOR);
	protected static Term term_ASSIGN_FOREGROUND= new PrologSymbol(SymbolCodes.symbolCode_E_ASSIGN_FOREGROUND);
	protected static Term term_FORGET_FOREGROUND= new PrologSymbol(SymbolCodes.symbolCode_E_FORGET_FOREGROUND);
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term toTerm();
}
