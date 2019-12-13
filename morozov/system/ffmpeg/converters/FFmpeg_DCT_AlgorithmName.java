// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avcodec;

import target.*;

import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum FFmpeg_DCT_AlgorithmName {
	//
	FF_DCT_AUTO {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DCT_AUTO;
		}
		@Override
		public Term toTerm() {
			return term_FF_DCT_AUTO;
		}
	},
	FF_DCT_FASTINT {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DCT_FASTINT;
		}
		@Override
		public Term toTerm() {
			return term_FF_DCT_FASTINT;
		}
	},
	FF_DCT_INT {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DCT_INT;
		}
		@Override
		public Term toTerm() {
			return term_FF_DCT_INT;
		}
	},
	FF_DCT_MMX {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DCT_MMX;
		}
		@Override
		public Term toTerm() {
			return term_FF_DCT_MMX;
		}
	},
	FF_DCT_ALTIVEC {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DCT_ALTIVEC;
		}
		@Override
		public Term toTerm() {
			return term_FF_DCT_ALTIVEC;
		}
	},
	FF_DCT_FAAN {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DCT_FAAN;
		}
		@Override
		public Term toTerm() {
			return term_FF_DCT_FAAN;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpeg_DCT_AlgorithmName argumentToFFmpeg_DCT_AlgorithmName(Term value, ChoisePoint iX) throws TermIsNotFFmpeg_DCT_AlgorithmName {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_FF_DCT_AUTO) {
				return FFmpeg_DCT_AlgorithmName.FF_DCT_AUTO;
			} else if (code==SymbolCodes.symbolCode_E_FF_DCT_FASTINT) {
				return FFmpeg_DCT_AlgorithmName.FF_DCT_FASTINT;
			} else if (code==SymbolCodes.symbolCode_E_FF_DCT_INT) {
				return FFmpeg_DCT_AlgorithmName.FF_DCT_INT;
			} else if (code==SymbolCodes.symbolCode_E_FF_DCT_MMX) {
				return FFmpeg_DCT_AlgorithmName.FF_DCT_MMX;
			} else if (code==SymbolCodes.symbolCode_E_FF_DCT_ALTIVEC) {
				return FFmpeg_DCT_AlgorithmName.FF_DCT_ALTIVEC;
			} else if (code==SymbolCodes.symbolCode_E_FF_DCT_FAAN) {
				return FFmpeg_DCT_AlgorithmName.FF_DCT_FAAN;
			} else {
				throw TermIsNotFFmpeg_DCT_AlgorithmName.instance;
			}
		} catch (TermIsNotASymbol e) {
			throw TermIsNotFFmpeg_DCT_AlgorithmName.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term term_FF_DCT_AUTO= new PrologSymbol(SymbolCodes.symbolCode_E_FF_DCT_AUTO);
	protected static Term term_FF_DCT_FASTINT= new PrologSymbol(SymbolCodes.symbolCode_E_FF_DCT_FASTINT);
	protected static Term term_FF_DCT_INT= new PrologSymbol(SymbolCodes.symbolCode_E_FF_DCT_INT);
	protected static Term term_FF_DCT_MMX= new PrologSymbol(SymbolCodes.symbolCode_E_FF_DCT_MMX);
	protected static Term term_FF_DCT_ALTIVEC= new PrologSymbol(SymbolCodes.symbolCode_E_FF_DCT_ALTIVEC);
	protected static Term term_FF_DCT_FAAN= new PrologSymbol(SymbolCodes.symbolCode_E_FF_DCT_FAAN);
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public int toInteger();
	abstract public Term toTerm();
}
