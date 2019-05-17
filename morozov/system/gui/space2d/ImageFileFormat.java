// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import target.*;

import morozov.run.*;
import morozov.system.gui.space2d.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import javax.imageio.ImageTypeSpecifier;

public enum ImageFileFormat {
	//
	JPEG {
		public Space2DWriter createWriter(String defaultFormatName, ImageTypeSpecifier its) {
			return new Space2D_JPEG_Writer(its);
		}
		public Term toTerm() {
			return termJPEG;
		}
	},
	PNG {
		public Space2DWriter createWriter(String defaultFormatName, ImageTypeSpecifier its) {
			return new Space2D_PNG_Writer(its);
		}
		public Term toTerm() {
			return termPNG;
		}
	},
	GIF {
		public Space2DWriter createWriter(String defaultFormatName, ImageTypeSpecifier its) {
			return new Space2D_GIF_Writer(its);
		}
		public Term toTerm() {
			return termGIF;
		}
	},
	BMP {
		public Space2DWriter createWriter(String defaultFormatName, ImageTypeSpecifier its) {
			return new Space2D_BMP_Writer(its);
		}
		public Term toTerm() {
			return termBMP;
		}
	},
	WBMP {
		public Space2DWriter createWriter(String defaultFormatName, ImageTypeSpecifier its) {
			return new Space2D_WBMP_Writer(its);
		}
		public Term toTerm() {
			return termWBMP;
		}
	},
	UNIVERSAL {
		public Space2DWriter createWriter(String defaultFormatName, ImageTypeSpecifier its) {
			ImageFileFormat format= recognizeFormat(defaultFormatName);
			if (format==UNIVERSAL) {
				return new Space2DWriter(defaultFormatName,its);
			} else {
				return format.createWriter(defaultFormatName,its);
			}
		}
		public Term toTerm() {
			return termDefault;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Space2DWriter createWriter(String defaultFormatName, ImageTypeSpecifier its);
	abstract public Term toTerm();
	//
	///////////////////////////////////////////////////////////////
	//
	protected static final Term termJPEG= new PrologSymbol(SymbolCodes.symbolCode_E_JPEG);
	protected static final Term termPNG= new PrologSymbol(SymbolCodes.symbolCode_E_PNG);
	protected static final Term termGIF= new PrologSymbol(SymbolCodes.symbolCode_E_GIF);
	protected static final Term termBMP= new PrologSymbol(SymbolCodes.symbolCode_E_BMP);
	protected static final Term termWBMP= new PrologSymbol(SymbolCodes.symbolCode_E_WBMP);
	protected static final Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	///////////////////////////////////////////////////////////////
	//
	public static ImageFileFormat argumentToImageFileFormat(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_default) {
				// throw TermIsSymbolDefault.instance;
				return ImageFileFormat.UNIVERSAL;
			} else if (code==SymbolCodes.symbolCode_E_JPEG) {
				return ImageFileFormat.JPEG;
			} else if (code==SymbolCodes.symbolCode_E_PNG) {
				return ImageFileFormat.PNG;
			} else if (code==SymbolCodes.symbolCode_E_GIF) {
				return ImageFileFormat.GIF;
			} else if (code==SymbolCodes.symbolCode_E_BMP) {
				return ImageFileFormat.BMP;
			} else if (code==SymbolCodes.symbolCode_E_WBMP) {
				return ImageFileFormat.WBMP;
			} else {
				throw new WrongArgumentIsNotImageFileFormat(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotImageFileFormat(value);
		}
	}
	//
	public static ImageFileFormat recognizeFormat(String name) {
		if (name.equalsIgnoreCase("jpg") || name.equalsIgnoreCase("jpeg")) {
			return JPEG;
		} else if (name.equalsIgnoreCase("png")) {
			return PNG;
		} else if (name.equalsIgnoreCase("gif")) {
			return GIF;
		} else if (name.equalsIgnoreCase("bmp")) {
			return BMP;
		} else if (name.equalsIgnoreCase("wbmp")) {
			return WBMP;
		} else {
			return UNIVERSAL;
		}
	}
}
