// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avcodec;

import target.*;

import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum FFmpeg_IDCT_AlgorithmName {
	//
	FF_IDCT_AUTO {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_IDCT_AUTO;
		}
		public Term toTerm() {
			return term_FF_IDCT_AUTO;
		}
	},
	FF_IDCT_INT {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_IDCT_INT;
		}
		public Term toTerm() {
			return term_FF_IDCT_INT;
		}
	},
	FF_IDCT_SIMPLE {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_IDCT_SIMPLE;
		}
		public Term toTerm() {
			return term_FF_IDCT_SIMPLE;
		}
	},
	FF_IDCT_SIMPLEMMX {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_IDCT_SIMPLEMMX;
		}
		public Term toTerm() {
			return term_FF_IDCT_SIMPLEMMX;
		}
	},
	FF_IDCT_ARM {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_IDCT_ARM;
		}
		public Term toTerm() {
			return term_FF_IDCT_ARM;
		}
	},
	FF_IDCT_ALTIVEC {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_IDCT_ALTIVEC;
		}
		public Term toTerm() {
			return term_FF_IDCT_ALTIVEC;
		}
	},
	FF_IDCT_SH4 {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_IDCT_SH4;
		}
		public Term toTerm() {
			return term_FF_IDCT_SH4;
		}
	},
	FF_IDCT_SIMPLEARM {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_IDCT_SIMPLEARM;
		}
		public Term toTerm() {
			return term_FF_IDCT_SIMPLEARM;
		}
	},
	FF_IDCT_IPP {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_IDCT_IPP;
		}
		public Term toTerm() {
			return term_FF_IDCT_IPP;
		}
	},
	FF_IDCT_XVID {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_IDCT_XVID;
		}
		public Term toTerm() {
			return term_FF_IDCT_XVID;
		}
	},
	FF_IDCT_XVIDMMX {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_IDCT_XVIDMMX;
		}
		public Term toTerm() {
			return term_FF_IDCT_XVIDMMX;
		}
	},
	FF_IDCT_SIMPLEARMV5TE {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_IDCT_SIMPLEARMV5TE;
		}
		public Term toTerm() {
			return term_FF_IDCT_SIMPLEARMV5TE;
		}
	},
	FF_IDCT_SIMPLEARMV6 {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_IDCT_SIMPLEARMV6;
		}
		public Term toTerm() {
			return term_FF_IDCT_SIMPLEARMV6;
		}
	},
	FF_IDCT_SIMPLEVIS {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_IDCT_SIMPLEVIS;
		}
		public Term toTerm() {
			return term_FF_IDCT_SIMPLEVIS;
		}
	},
	FF_IDCT_FAAN {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_IDCT_FAAN;
		}
		public Term toTerm() {
			return term_FF_IDCT_FAAN;
		}
	},
	FF_IDCT_SIMPLENEON {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_IDCT_SIMPLENEON;
		}
		public Term toTerm() {
			return term_FF_IDCT_SIMPLENEON;
		}
	},
	FF_IDCT_SIMPLEALPHA {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_IDCT_SIMPLEALPHA;
		}
		public Term toTerm() {
			return term_FF_IDCT_SIMPLEALPHA;
		}
	},
	FF_IDCT_SIMPLEAUTO {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_IDCT_SIMPLEAUTO;
		}
		public Term toTerm() {
			return term_FF_IDCT_SIMPLEAUTO;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpeg_IDCT_AlgorithmName argumentToFFmpeg_IDCT_AlgorithmName(Term value, ChoisePoint iX) throws TermIsNotFFmpeg_IDCT_AlgorithmName {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_FF_IDCT_AUTO) {
				return FFmpeg_IDCT_AlgorithmName.FF_IDCT_AUTO;
			} else if (code==SymbolCodes.symbolCode_E_FF_IDCT_INT) {
				return FFmpeg_IDCT_AlgorithmName.FF_IDCT_INT;
			} else if (code==SymbolCodes.symbolCode_E_FF_IDCT_SIMPLE) {
				return FFmpeg_IDCT_AlgorithmName.FF_IDCT_SIMPLE;
			} else if (code==SymbolCodes.symbolCode_E_FF_IDCT_SIMPLEMMX) {
				return FFmpeg_IDCT_AlgorithmName.FF_IDCT_SIMPLEMMX;
			} else if (code==SymbolCodes.symbolCode_E_FF_IDCT_ARM) {
				return FFmpeg_IDCT_AlgorithmName.FF_IDCT_ARM;
			} else if (code==SymbolCodes.symbolCode_E_FF_IDCT_ALTIVEC) {
				return FFmpeg_IDCT_AlgorithmName.FF_IDCT_ALTIVEC;
			} else if (code==SymbolCodes.symbolCode_E_FF_IDCT_SH4) {
				return FFmpeg_IDCT_AlgorithmName.FF_IDCT_SH4;
			} else if (code==SymbolCodes.symbolCode_E_FF_IDCT_SIMPLEARM) {
				return FFmpeg_IDCT_AlgorithmName.FF_IDCT_SIMPLEARM;
			} else if (code==SymbolCodes.symbolCode_E_FF_IDCT_IPP) {
				return FFmpeg_IDCT_AlgorithmName.FF_IDCT_IPP;
			} else if (code==SymbolCodes.symbolCode_E_FF_IDCT_XVID) {
				return FFmpeg_IDCT_AlgorithmName.FF_IDCT_XVID;
			} else if (code==SymbolCodes.symbolCode_E_FF_IDCT_XVIDMMX) {
				return FFmpeg_IDCT_AlgorithmName.FF_IDCT_XVIDMMX;
			} else if (code==SymbolCodes.symbolCode_E_FF_IDCT_SIMPLEARMV5TE) {
				return FFmpeg_IDCT_AlgorithmName.FF_IDCT_SIMPLEARMV5TE;
			} else if (code==SymbolCodes.symbolCode_E_FF_IDCT_SIMPLEARMV6) {
				return FFmpeg_IDCT_AlgorithmName.FF_IDCT_SIMPLEARMV6;
			} else if (code==SymbolCodes.symbolCode_E_FF_IDCT_SIMPLEVIS) {
				return FFmpeg_IDCT_AlgorithmName.FF_IDCT_SIMPLEVIS;
			} else if (code==SymbolCodes.symbolCode_E_FF_IDCT_FAAN) {
				return FFmpeg_IDCT_AlgorithmName.FF_IDCT_FAAN;
			} else if (code==SymbolCodes.symbolCode_E_FF_IDCT_SIMPLENEON) {
				return FFmpeg_IDCT_AlgorithmName.FF_IDCT_SIMPLENEON;
			} else if (code==SymbolCodes.symbolCode_E_FF_IDCT_SIMPLEALPHA) {
				return FFmpeg_IDCT_AlgorithmName.FF_IDCT_SIMPLEALPHA;
			} else if (code==SymbolCodes.symbolCode_E_FF_IDCT_SIMPLEAUTO) {
				return FFmpeg_IDCT_AlgorithmName.FF_IDCT_SIMPLEAUTO;
			} else {
				throw TermIsNotFFmpeg_IDCT_AlgorithmName.instance;
			}
		} catch (TermIsNotASymbol e) {
			throw TermIsNotFFmpeg_IDCT_AlgorithmName.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term term_FF_IDCT_AUTO= new PrologSymbol(SymbolCodes.symbolCode_E_FF_IDCT_AUTO);
	protected static Term term_FF_IDCT_INT= new PrologSymbol(SymbolCodes.symbolCode_E_FF_IDCT_INT);
	protected static Term term_FF_IDCT_SIMPLE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_IDCT_SIMPLE);
	protected static Term term_FF_IDCT_SIMPLEMMX= new PrologSymbol(SymbolCodes.symbolCode_E_FF_IDCT_SIMPLEMMX);
	protected static Term term_FF_IDCT_ARM= new PrologSymbol(SymbolCodes.symbolCode_E_FF_IDCT_ARM);
	protected static Term term_FF_IDCT_ALTIVEC= new PrologSymbol(SymbolCodes.symbolCode_E_FF_IDCT_ALTIVEC);
	protected static Term term_FF_IDCT_SH4= new PrologSymbol(SymbolCodes.symbolCode_E_FF_IDCT_SH4);
	protected static Term term_FF_IDCT_SIMPLEARM= new PrologSymbol(SymbolCodes.symbolCode_E_FF_IDCT_SIMPLEARM);
	protected static Term term_FF_IDCT_IPP= new PrologSymbol(SymbolCodes.symbolCode_E_FF_IDCT_IPP);
	protected static Term term_FF_IDCT_XVID= new PrologSymbol(SymbolCodes.symbolCode_E_FF_IDCT_XVID);
	protected static Term term_FF_IDCT_XVIDMMX= new PrologSymbol(SymbolCodes.symbolCode_E_FF_IDCT_XVIDMMX);
	protected static Term term_FF_IDCT_SIMPLEARMV5TE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_IDCT_SIMPLEARMV5TE);
	protected static Term term_FF_IDCT_SIMPLEARMV6= new PrologSymbol(SymbolCodes.symbolCode_E_FF_IDCT_SIMPLEARMV6);
	protected static Term term_FF_IDCT_SIMPLEVIS= new PrologSymbol(SymbolCodes.symbolCode_E_FF_IDCT_SIMPLEVIS);
	protected static Term term_FF_IDCT_FAAN= new PrologSymbol(SymbolCodes.symbolCode_E_FF_IDCT_FAAN);
	protected static Term term_FF_IDCT_SIMPLENEON= new PrologSymbol(SymbolCodes.symbolCode_E_FF_IDCT_SIMPLENEON);
	protected static Term term_FF_IDCT_SIMPLEALPHA= new PrologSymbol(SymbolCodes.symbolCode_E_FF_IDCT_SIMPLEALPHA);
	protected static Term term_FF_IDCT_SIMPLEAUTO= new PrologSymbol(SymbolCodes.symbolCode_E_FF_IDCT_SIMPLEAUTO);
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public int toInteger();
	abstract public Term toTerm();
}
