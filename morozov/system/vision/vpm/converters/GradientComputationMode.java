// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.converters;

import target.*;

import morozov.run.*;
import morozov.system.vision.vpm.converters.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum GradientComputationMode {
	//
	MODULUS {
		public boolean isModulusMode() {
			return true;
		}
		public Term toTerm() {
			return term_MODULUS;
		}
	},
	DIRECTION {
		public boolean isModulusMode() {
			return false;
		}
		public Term toTerm() {
			return term_DIRECTION;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public static GradientComputationMode argumentToGradientComputationMode(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_MODULUS) {
				return GradientComputationMode.MODULUS;
			} else if (code==SymbolCodes.symbolCode_E_DIRECTION) {
				return GradientComputationMode.DIRECTION;
			} else {
				throw new WrongArgumentIsNotGradientComputationMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotGradientComputationMode(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term term_MODULUS= new PrologSymbol(SymbolCodes.symbolCode_E_MODULUS);
	protected static Term term_DIRECTION= new PrologSymbol(SymbolCodes.symbolCode_E_DIRECTION);
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public boolean isModulusMode();
	abstract public Term toTerm();
}
