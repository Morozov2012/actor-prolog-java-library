// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import target.*;

import morozov.run.*;
import morozov.system.gui.space2d.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum Canvas2DScalingFactor {
	//
	MIN {
		public int computeScalingCoefficient(int width, int height) {
			return StrictMath.min(width,height);
		}
		public Term toTerm() {
			return termMin;
		}
	},
	MAX {
		public int computeScalingCoefficient(int width, int height) {
			return StrictMath.max(width,height);
		}
		public Term toTerm() {
			return termMax;
		}
	},
	WIDTH {
		public int computeScalingCoefficient(int width, int height) {
			return width;
		}
		public Term toTerm() {
			return termWidth;
		}
	},
	HEIGHT {
		public int computeScalingCoefficient(int width, int height) {
			return height;
		}
		public Term toTerm() {
			return termHeight;
		}
	},
	INDEPENDENT {
		public int computeScalingCoefficient(int width, int height) {
			return -1;
		}
		public Term toTerm() {
			return termIndependent;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termMin= new PrologSymbol(SymbolCodes.symbolCode_E_MIN);
	protected static Term termMax= new PrologSymbol(SymbolCodes.symbolCode_E_MAX);
	protected static Term termWidth= new PrologSymbol(SymbolCodes.symbolCode_E_WIDTH);
	protected static Term termHeight= new PrologSymbol(SymbolCodes.symbolCode_E_HEIGHT);
	protected static Term termIndependent= new PrologSymbol(SymbolCodes.symbolCode_E_INDEPENDENT);
	//
	///////////////////////////////////////////////////////////////
	//
	public static Canvas2DScalingFactor argumentToScalingFactor(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_WIDTH) {
				return Canvas2DScalingFactor.WIDTH;
			} else if (code==SymbolCodes.symbolCode_E_HEIGHT) {
				return Canvas2DScalingFactor.HEIGHT;
			} else if (code==SymbolCodes.symbolCode_E_MIN) {
				return Canvas2DScalingFactor.MIN;
			} else if (code==SymbolCodes.symbolCode_E_MAX) {
				return Canvas2DScalingFactor.MAX;
			} else if (code==SymbolCodes.symbolCode_E_INDEPENDENT) {
				return Canvas2DScalingFactor.INDEPENDENT;
			} else {
				throw new WrongArgumentIsNotScalingFactor2D(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotScalingFactor2D(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public int computeScalingCoefficient(int width, int height);
	abstract public Term toTerm();
}
