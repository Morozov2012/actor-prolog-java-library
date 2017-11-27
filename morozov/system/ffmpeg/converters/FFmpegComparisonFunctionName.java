// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avcodec;

import target.*;

import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum FFmpegComparisonFunctionName {
	//
	FF_CMP_SAD {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_CMP_SAD;
		}
		public Term toTerm() {
			return term_FF_CMP_SAD;
		}
	},
	FF_CMP_SSE {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_CMP_SSE;
		}
		public Term toTerm() {
			return term_FF_CMP_SSE;
		}
	},
	FF_CMP_SATD {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_CMP_SATD;
		}
		public Term toTerm() {
			return term_FF_CMP_SATD;
		}
	},
	FF_CMP_DCT {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_CMP_DCT;
		}
		public Term toTerm() {
			return term_FF_CMP_DCT;
		}
	},
	FF_CMP_PSNR {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_CMP_PSNR;
		}
		public Term toTerm() {
			return term_FF_CMP_PSNR;
		}
	},
	FF_CMP_BIT {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_CMP_BIT;
		}
		public Term toTerm() {
			return term_FF_CMP_BIT;
		}
	},
	FF_CMP_RD {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_CMP_RD;
		}
		public Term toTerm() {
			return term_FF_CMP_RD;
		}
	},
	FF_CMP_ZERO {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_CMP_ZERO;
		}
		public Term toTerm() {
			return term_FF_CMP_ZERO;
		}
	},
	FF_CMP_VSAD {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_CMP_VSAD;
		}
		public Term toTerm() {
			return term_FF_CMP_VSAD;
		}
	},
	FF_CMP_VSSE {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_CMP_VSSE;
		}
		public Term toTerm() {
			return term_FF_CMP_VSSE;
		}
	},
	FF_CMP_NSSE {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_CMP_NSSE;
		}
		public Term toTerm() {
			return term_FF_CMP_NSSE;
		}
	},
	FF_CMP_W53 {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_CMP_W53;
		}
		public Term toTerm() {
			return term_FF_CMP_W53;
		}
	},
	FF_CMP_W97 {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_CMP_W97;
		}
		public Term toTerm() {
			return term_FF_CMP_W97;
		}
	},
	FF_CMP_DCTMAX {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_CMP_DCTMAX;
		}
		public Term toTerm() {
			return term_FF_CMP_DCTMAX;
		}
	},
	FF_CMP_DCT264 {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_CMP_DCT264;
		}
		public Term toTerm() {
			return term_FF_CMP_DCT264;
		}
	},
	FF_CMP_MEDIAN_SAD {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_CMP_MEDIAN_SAD;
		}
		public Term toTerm() {
			return term_FF_CMP_MEDIAN_SAD;
		}
	},
	FF_CMP_CHROMA {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_CMP_CHROMA;
		}
		public Term toTerm() {
			return term_FF_CMP_CHROMA;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegComparisonFunctionName argumentToFFmpegComparisonFunctionName(Term value, ChoisePoint iX) throws TermIsNotFFmpegComparisonFunctionName {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_FF_CMP_SAD) {
				return FFmpegComparisonFunctionName.FF_CMP_SAD;
			} else if (code==SymbolCodes.symbolCode_E_FF_CMP_SSE) {
				return FFmpegComparisonFunctionName.FF_CMP_SSE;
			} else if (code==SymbolCodes.symbolCode_E_FF_CMP_SATD) {
				return FFmpegComparisonFunctionName.FF_CMP_SATD;
			} else if (code==SymbolCodes.symbolCode_E_FF_CMP_DCT) {
				return FFmpegComparisonFunctionName.FF_CMP_DCT;
			} else if (code==SymbolCodes.symbolCode_E_FF_CMP_PSNR) {
				return FFmpegComparisonFunctionName.FF_CMP_PSNR;
			} else if (code==SymbolCodes.symbolCode_E_FF_CMP_BIT) {
				return FFmpegComparisonFunctionName.FF_CMP_BIT;
			} else if (code==SymbolCodes.symbolCode_E_FF_CMP_RD) {
				return FFmpegComparisonFunctionName.FF_CMP_RD;
			} else if (code==SymbolCodes.symbolCode_E_FF_CMP_ZERO) {
				return FFmpegComparisonFunctionName.FF_CMP_ZERO;
			} else if (code==SymbolCodes.symbolCode_E_FF_CMP_VSAD) {
				return FFmpegComparisonFunctionName.FF_CMP_VSAD;
			} else if (code==SymbolCodes.symbolCode_E_FF_CMP_VSSE) {
				return FFmpegComparisonFunctionName.FF_CMP_VSSE;
			} else if (code==SymbolCodes.symbolCode_E_FF_CMP_NSSE) {
				return FFmpegComparisonFunctionName.FF_CMP_NSSE;
			} else if (code==SymbolCodes.symbolCode_E_FF_CMP_W53) {
				return FFmpegComparisonFunctionName.FF_CMP_W53;
			} else if (code==SymbolCodes.symbolCode_E_FF_CMP_W97) {
				return FFmpegComparisonFunctionName.FF_CMP_W97;
			} else if (code==SymbolCodes.symbolCode_E_FF_CMP_DCTMAX) {
				return FFmpegComparisonFunctionName.FF_CMP_DCTMAX;
			} else if (code==SymbolCodes.symbolCode_E_FF_CMP_DCT264) {
				return FFmpegComparisonFunctionName.FF_CMP_DCT264;
			} else if (code==SymbolCodes.symbolCode_E_FF_CMP_MEDIAN_SAD) {
				return FFmpegComparisonFunctionName.FF_CMP_MEDIAN_SAD;
			} else if (code==SymbolCodes.symbolCode_E_FF_CMP_CHROMA) {
				return FFmpegComparisonFunctionName.FF_CMP_CHROMA;
			} else {
				throw TermIsNotFFmpegComparisonFunctionName.instance;
			}
		} catch (TermIsNotASymbol e) {
			throw TermIsNotFFmpegComparisonFunctionName.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term term_FF_CMP_SAD= new PrologSymbol(SymbolCodes.symbolCode_E_FF_CMP_SAD);
	protected static Term term_FF_CMP_SSE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_CMP_SSE);
	protected static Term term_FF_CMP_SATD= new PrologSymbol(SymbolCodes.symbolCode_E_FF_CMP_SATD);
	protected static Term term_FF_CMP_DCT= new PrologSymbol(SymbolCodes.symbolCode_E_FF_CMP_DCT);
	protected static Term term_FF_CMP_PSNR= new PrologSymbol(SymbolCodes.symbolCode_E_FF_CMP_PSNR);
	protected static Term term_FF_CMP_BIT= new PrologSymbol(SymbolCodes.symbolCode_E_FF_CMP_BIT);
	protected static Term term_FF_CMP_RD= new PrologSymbol(SymbolCodes.symbolCode_E_FF_CMP_RD);
	protected static Term term_FF_CMP_ZERO= new PrologSymbol(SymbolCodes.symbolCode_E_FF_CMP_ZERO);
	protected static Term term_FF_CMP_VSAD= new PrologSymbol(SymbolCodes.symbolCode_E_FF_CMP_VSAD);
	protected static Term term_FF_CMP_VSSE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_CMP_VSSE);
	protected static Term term_FF_CMP_NSSE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_CMP_NSSE);
	protected static Term term_FF_CMP_W53= new PrologSymbol(SymbolCodes.symbolCode_E_FF_CMP_W53);
	protected static Term term_FF_CMP_W97= new PrologSymbol(SymbolCodes.symbolCode_E_FF_CMP_W97);
	protected static Term term_FF_CMP_DCTMAX= new PrologSymbol(SymbolCodes.symbolCode_E_FF_CMP_DCTMAX);
	protected static Term term_FF_CMP_DCT264= new PrologSymbol(SymbolCodes.symbolCode_E_FF_CMP_DCT264);
	protected static Term term_FF_CMP_MEDIAN_SAD= new PrologSymbol(SymbolCodes.symbolCode_E_FF_CMP_MEDIAN_SAD);
	protected static Term term_FF_CMP_CHROMA= new PrologSymbol(SymbolCodes.symbolCode_E_FF_CMP_CHROMA);
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public int toInteger();
	abstract public Term toTerm();
}
