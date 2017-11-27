// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.converters;

import target.*;

import morozov.run.*;
import morozov.system.vision.vpm.converters.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum DilationAlgorithm {
	//
	PLAIN_DILATION {
		public Term toTerm() {
			return term_PLAIN_DILATION;
		}
	},
	HIERARCHICAL_DILATION {
		public Term toTerm() {
			return term_HIERARCHICAL_DILATION;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public static DilationAlgorithm argumentToDilationAlgorithm(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_PLAIN_DILATION) {
				return DilationAlgorithm.PLAIN_DILATION;
			} else if (code==SymbolCodes.symbolCode_E_HIERARCHICAL_DILATION) {
				return DilationAlgorithm.HIERARCHICAL_DILATION;
			} else {
				throw new WrongArgumentIsNotDilationAlgorithm(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotDilationAlgorithm(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term term_PLAIN_DILATION= new PrologSymbol(SymbolCodes.symbolCode_E_PLAIN_DILATION);
	protected static Term term_HIERARCHICAL_DILATION= new PrologSymbol(SymbolCodes.symbolCode_E_HIERARCHICAL_DILATION);
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term toTerm();
}
