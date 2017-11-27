// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.converters;

import target.*;

import morozov.run.*;
import morozov.system.vision.vpm.converters.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum ErodingAlgorithm {
	//
	PLAIN_ERODING {
		public Term toTerm() {
			return term_PLAIN_ERODING;
		}
	},
	HIERARCHICAL_ERODING {
		public Term toTerm() {
			return term_HIERARCHICAL_ERODING;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public static ErodingAlgorithm argumentToErodingAlgorithm(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_PLAIN_ERODING) {
				return ErodingAlgorithm.PLAIN_ERODING;
			} else if (code==SymbolCodes.symbolCode_E_HIERARCHICAL_ERODING) {
				return ErodingAlgorithm.HIERARCHICAL_ERODING;
			} else {
				throw new WrongArgumentIsNotErodingAlgorithm(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotErodingAlgorithm(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term term_PLAIN_ERODING= new PrologSymbol(SymbolCodes.symbolCode_E_PLAIN_ERODING);
	protected static Term term_HIERARCHICAL_ERODING= new PrologSymbol(SymbolCodes.symbolCode_E_HIERARCHICAL_ERODING);
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term toTerm();
}
