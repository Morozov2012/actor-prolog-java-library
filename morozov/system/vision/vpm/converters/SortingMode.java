// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.converters;

import target.*;

import morozov.run.*;
import morozov.system.vision.vpm.converters.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum SortingMode {
	//
	DESCENDING_ORDER {
		public Term toTerm() {
			return term_DESCENDING_ORDER;
		}
	},
	ASCENDING_ORDER {
		public Term toTerm() {
			return term_ASCENDING_ORDER;
		}
	},
	DEFAULT {
		public Term toTerm() {
			return term_default;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public static SortingMode argumentToSortingMode(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_DESCENDING_ORDER) {
				return SortingMode.DESCENDING_ORDER;
			} else if (code==SymbolCodes.symbolCode_E_ASCENDING_ORDER) {
				return SortingMode.ASCENDING_ORDER;
			} else if (code==SymbolCodes.symbolCode_E_default) {
				return SortingMode.DEFAULT;
			} else {
				throw new WrongArgumentIsNotSortingMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotSortingMode(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term term_DESCENDING_ORDER= new PrologSymbol(SymbolCodes.symbolCode_E_DESCENDING_ORDER);
	protected static Term term_ASCENDING_ORDER= new PrologSymbol(SymbolCodes.symbolCode_E_ASCENDING_ORDER);
	protected static Term term_default= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term toTerm();
}
