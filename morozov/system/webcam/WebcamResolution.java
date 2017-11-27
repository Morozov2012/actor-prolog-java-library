// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.webcam;

import target.*;

import morozov.run.*;
import morozov.system.gui.*;
import morozov.system.webcam.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum WebcamResolution {
	//
	CIF(352,288) {
		public Term toTerm() {
			return termCIF;
		}
	},
	HD720(1280,720) {
		public Term toTerm() {
			return termHD720;
		}
	},
	HD720P(1280,720) {
		public Term toTerm() {
			return termHD720P;
		}
	},
	HVGA(480,400) {
		public Term toTerm() {
			return termHVGA;
		}
	},
	PAL(768,576) {
		public Term toTerm() {
			return termPAL;
		}
	},
	QQVGA(176,144) {
		public Term toTerm() {
			return termQQVGA;
		}
	},
	QVGA(320,240) {
		public Term toTerm() {
			return termQVGA;
		}
	},
	QXGA(2048,1536) {
		public Term toTerm() {
			return termQXGA;
		}
	},
	SVGA(800,600) {
		public Term toTerm() {
			return termSVGA;
		}
	},
	SXGA(1280,1024) {
		public Term toTerm() {
			return termSXGA;
		}
	},
	UXGA(1600,1200) {
		public Term toTerm() {
			return termUXGA;
		}
	},
	VGA(640,480) {
		public Term toTerm() {
			return termVGA;
		}
	},
	WXGA(1280,768) {
		public Term toTerm() {
			return termWXGA;
		}
	},
	XGA(1024,768) {
		public Term toTerm() {
			return termXGA;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	private int width;
	private int height;
	//
	private WebcamResolution(int w, int h) {
		width= w;
		height= h;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termCIF= new PrologSymbol(SymbolCodes.symbolCode_E_CIF);
	protected static Term termHD720= new PrologSymbol(SymbolCodes.symbolCode_E_HD720);
	protected static Term termHD720P= new PrologSymbol(SymbolCodes.symbolCode_E_HD720P);
	protected static Term termHVGA= new PrologSymbol(SymbolCodes.symbolCode_E_HVGA);
	protected static Term termPAL= new PrologSymbol(SymbolCodes.symbolCode_E_PAL);
	protected static Term termQQVGA= new PrologSymbol(SymbolCodes.symbolCode_E_QQVGA);
	protected static Term termQVGA= new PrologSymbol(SymbolCodes.symbolCode_E_QVGA);
	protected static Term termQXGA= new PrologSymbol(SymbolCodes.symbolCode_E_QXGA);
	protected static Term termSVGA= new PrologSymbol(SymbolCodes.symbolCode_E_SVGA);
	protected static Term termSXGA= new PrologSymbol(SymbolCodes.symbolCode_E_SXGA);
	protected static Term termUXGA= new PrologSymbol(SymbolCodes.symbolCode_E_UXGA);
	protected static Term termVGA= new PrologSymbol(SymbolCodes.symbolCode_E_VGA);
	protected static Term termWXGA= new PrologSymbol(SymbolCodes.symbolCode_E_WXGA);
	protected static Term termXGA= new PrologSymbol(SymbolCodes.symbolCode_E_XGA);
	//
	///////////////////////////////////////////////////////////////
	//
	public static WebcamResolution argumentToWebcamResolution(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_CIF) {
				return WebcamResolution.CIF;
			} else if (code==SymbolCodes.symbolCode_E_HD720) {
				return WebcamResolution.HD720;
			} else if (code==SymbolCodes.symbolCode_E_HD720P) {
				return WebcamResolution.HD720P;
			} else if (code==SymbolCodes.symbolCode_E_HVGA) {
				return WebcamResolution.HVGA;
			} else if (code==SymbolCodes.symbolCode_E_PAL) {
				return WebcamResolution.PAL;
			} else if (code==SymbolCodes.symbolCode_E_QQVGA) {
				return WebcamResolution.QQVGA;
			} else if (code==SymbolCodes.symbolCode_E_QVGA) {
				return WebcamResolution.QVGA;
			} else if (code==SymbolCodes.symbolCode_E_QXGA) {
				return WebcamResolution.QXGA;
			} else if (code==SymbolCodes.symbolCode_E_SVGA) {
				return WebcamResolution.SVGA;
			} else if (code==SymbolCodes.symbolCode_E_SXGA) {
				return WebcamResolution.SXGA;
			} else if (code==SymbolCodes.symbolCode_E_UXGA) {
				return WebcamResolution.UXGA;
			} else if (code==SymbolCodes.symbolCode_E_VGA) {
				return WebcamResolution.VGA;
			} else if (code==SymbolCodes.symbolCode_E_WXGA) {
				return WebcamResolution.WXGA;
			} else if (code==SymbolCodes.symbolCode_E_XGA) {
				return WebcamResolution.XGA;
			} else {
				throw new WrongArgumentIsNotWebcamResolution(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotWebcamResolution(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	//
	public ExtendedSize getExtendedWidth() {
		return new ExtendedSize(getWidth());
	}
	public ExtendedSize getExtendedHeight() {
		return new ExtendedSize(getHeight());
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term toTerm();
}
