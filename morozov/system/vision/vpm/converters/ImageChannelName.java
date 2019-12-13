// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.converters;

import target.*;

import morozov.run.*;
import morozov.system.vision.vpm.converters.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum ImageChannelName {
	//
	GRAYSCALE {
		@Override
		public Term toTerm() {
			return term_GRAYSCALE;
		}
	},
	HUE {
		@Override
		public Term toTerm() {
			return term_HUE;
		}
	},
	SATURATION {
		@Override
		public Term toTerm() {
			return term_SATURATION;
		}
	},
	BRIGHTNESS {
		@Override
		public Term toTerm() {
			return term_BRIGHTNESS;
		}
	},
	RED {
		@Override
		public Term toTerm() {
			return term_RED;
		}
	},
	GREEN {
		@Override
		public Term toTerm() {
			return term_GREEN;
		}
	},
	BLUE {
		@Override
		public Term toTerm() {
			return term_BLUE;
		}
	},
	ALL {
		@Override
		public Term toTerm() {
			return term_ALL;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public static ImageChannelName argumentToImageChannelName(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_GRAYSCALE) {
				return ImageChannelName.GRAYSCALE;
			} else if (code==SymbolCodes.symbolCode_E_HUE) {
				return ImageChannelName.HUE;
			} else if (code==SymbolCodes.symbolCode_E_SATURATION) {
				return ImageChannelName.SATURATION;
			} else if (code==SymbolCodes.symbolCode_E_BRIGHTNESS) {
				return ImageChannelName.BRIGHTNESS;
			} else if (code==SymbolCodes.symbolCode_E_RED) {
				return ImageChannelName.RED;
			} else if (code==SymbolCodes.symbolCode_E_GREEN) {
				return ImageChannelName.GREEN;
			} else if (code==SymbolCodes.symbolCode_E_BLUE) {
				return ImageChannelName.BLUE;
			} else if (code==SymbolCodes.symbolCode_E_ALL) {
				return ImageChannelName.ALL;
			} else {
				throw new WrongArgumentIsNotImageChannelName(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotImageChannelName(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term term_GRAYSCALE= new PrologSymbol(SymbolCodes.symbolCode_E_GRAYSCALE);
	protected static Term term_HUE= new PrologSymbol(SymbolCodes.symbolCode_E_HUE);
	protected static Term term_SATURATION= new PrologSymbol(SymbolCodes.symbolCode_E_SATURATION);
	protected static Term term_BRIGHTNESS= new PrologSymbol(SymbolCodes.symbolCode_E_BRIGHTNESS);
	protected static Term term_RED= new PrologSymbol(SymbolCodes.symbolCode_E_RED);
	protected static Term term_GREEN= new PrologSymbol(SymbolCodes.symbolCode_E_GREEN);
	protected static Term term_BLUE= new PrologSymbol(SymbolCodes.symbolCode_E_BLUE);
	protected static Term term_ALL= new PrologSymbol(SymbolCodes.symbolCode_E_ALL);
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term toTerm();
}
