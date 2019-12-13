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
		@Override
		public int computeScalingCoefficient(int width, int height) {
			return StrictMath.min(width,height);
		}
		@Override
		public Term toTerm() {
			return termMin;
		}
	},
	MAX {
		@Override
		public int computeScalingCoefficient(int width, int height) {
			return StrictMath.max(width,height);
		}
		@Override
		public Term toTerm() {
			return termMax;
		}
	},
	WIDTH {
		@Override
		public int computeScalingCoefficient(int width, int height) {
			return width;
		}
		@Override
		public Term toTerm() {
			return termWidth;
		}
	},
	HEIGHT {
		@Override
		public int computeScalingCoefficient(int width, int height) {
			return height;
		}
		@Override
		public Term toTerm() {
			return termHeight;
		}
	},
	INDEPENDENT {
		@Override
		public int computeScalingCoefficient(int width, int height) {
			return -1;
		}
		@Override
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
