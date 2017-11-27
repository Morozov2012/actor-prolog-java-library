// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avcodec;

import target.*;

import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum FFmpegDebugOptionName {
	//
	FF_DEBUG_PICT_INFO {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_PICT_INFO;
		}
		public Term toTerm() {
			return term_FF_DEBUG_PICT_INFO;
		}
	},
	FF_DEBUG_RC {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_RC;
		}
		public Term toTerm() {
			return term_FF_DEBUG_RC;
		}
	},
	FF_DEBUG_BITSTREAM {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_BITSTREAM;
		}
		public Term toTerm() {
			return term_FF_DEBUG_BITSTREAM;
		}
	},
	FF_DEBUG_MB_TYPE {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_MB_TYPE;
		}
		public Term toTerm() {
			return term_FF_DEBUG_MB_TYPE;
		}
	},
	FF_DEBUG_QP {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_QP;
		}
		public Term toTerm() {
			return term_FF_DEBUG_QP;
		}
	},
	FF_DEBUG_DCT_COEFF {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_DCT_COEFF;
		}
		public Term toTerm() {
			return term_FF_DEBUG_DCT_COEFF;
		}
	},
	FF_DEBUG_SKIP {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_SKIP;
		}
		public Term toTerm() {
			return term_FF_DEBUG_SKIP;
		}
	},
	FF_DEBUG_STARTCODE {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_STARTCODE;
		}
		public Term toTerm() {
			return term_FF_DEBUG_STARTCODE;
		}
	},
	FF_DEBUG_PTS {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_PTS;
		}
		public Term toTerm() {
			return term_FF_DEBUG_PTS;
		}
	},
	FF_DEBUG_ER {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_ER;
		}
		public Term toTerm() {
			return term_FF_DEBUG_ER;
		}
	},
	FF_DEBUG_MMCO {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_MMCO;
		}
		public Term toTerm() {
			return term_FF_DEBUG_MMCO;
		}
	},
	FF_DEBUG_BUGS {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_BUGS;
		}
		public Term toTerm() {
			return term_FF_DEBUG_BUGS;
		}
	},
	FF_DEBUG_BUFFERS {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_BUFFERS;
		}
		public Term toTerm() {
			return term_FF_DEBUG_BUFFERS;
		}
	},
	FF_DEBUG_THREADS {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_THREADS;
		}
		public Term toTerm() {
			return term_FF_DEBUG_THREADS;
		}
	},
	FF_DEBUG_GREEN_MD {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_GREEN_MD;
		}
		public Term toTerm() {
			return term_FF_DEBUG_GREEN_MD;
		}
	},
	FF_DEBUG_NOMC {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_NOMC;
		}
		public Term toTerm() {
			return term_FF_DEBUG_NOMC;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegDebugOptionName argumentToFFmpegDebugOptionName(Term value, ChoisePoint iX) throws TermIsNotFFmpegDebugOptionName {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_FF_DEBUG_PICT_INFO) {
				return FFmpegDebugOptionName.FF_DEBUG_PICT_INFO;
			} else if (code==SymbolCodes.symbolCode_E_FF_DEBUG_RC) {
				return FFmpegDebugOptionName.FF_DEBUG_RC;
			} else if (code==SymbolCodes.symbolCode_E_FF_DEBUG_BITSTREAM) {
				return FFmpegDebugOptionName.FF_DEBUG_BITSTREAM;
			} else if (code==SymbolCodes.symbolCode_E_FF_DEBUG_MB_TYPE) {
				return FFmpegDebugOptionName.FF_DEBUG_MB_TYPE;
			} else if (code==SymbolCodes.symbolCode_E_FF_DEBUG_QP) {
				return FFmpegDebugOptionName.FF_DEBUG_QP;
			} else if (code==SymbolCodes.symbolCode_E_FF_DEBUG_DCT_COEFF) {
				return FFmpegDebugOptionName.FF_DEBUG_DCT_COEFF;
			} else if (code==SymbolCodes.symbolCode_E_FF_DEBUG_SKIP) {
				return FFmpegDebugOptionName.FF_DEBUG_SKIP;
			} else if (code==SymbolCodes.symbolCode_E_FF_DEBUG_STARTCODE) {
				return FFmpegDebugOptionName.FF_DEBUG_STARTCODE;
			} else if (code==SymbolCodes.symbolCode_E_FF_DEBUG_PTS) {
				return FFmpegDebugOptionName.FF_DEBUG_PTS;
			} else if (code==SymbolCodes.symbolCode_E_FF_DEBUG_ER) {
				return FFmpegDebugOptionName.FF_DEBUG_ER;
			} else if (code==SymbolCodes.symbolCode_E_FF_DEBUG_MMCO) {
				return FFmpegDebugOptionName.FF_DEBUG_MMCO;
			} else if (code==SymbolCodes.symbolCode_E_FF_DEBUG_BUGS) {
				return FFmpegDebugOptionName.FF_DEBUG_BUGS;
			} else if (code==SymbolCodes.symbolCode_E_FF_DEBUG_BUFFERS) {
				return FFmpegDebugOptionName.FF_DEBUG_BUFFERS;
			} else if (code==SymbolCodes.symbolCode_E_FF_DEBUG_THREADS) {
				return FFmpegDebugOptionName.FF_DEBUG_THREADS;
			} else if (code==SymbolCodes.symbolCode_E_FF_DEBUG_GREEN_MD) {
				return FFmpegDebugOptionName.FF_DEBUG_GREEN_MD;
			} else if (code==SymbolCodes.symbolCode_E_FF_DEBUG_NOMC) {
				return FFmpegDebugOptionName.FF_DEBUG_NOMC;
			} else {
				throw TermIsNotFFmpegDebugOptionName.instance;
			}
		} catch (TermIsNotASymbol e) {
			throw TermIsNotFFmpegDebugOptionName.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term term_FF_DEBUG_PICT_INFO= new PrologSymbol(SymbolCodes.symbolCode_E_FF_DEBUG_PICT_INFO);
	protected static Term term_FF_DEBUG_RC= new PrologSymbol(SymbolCodes.symbolCode_E_FF_DEBUG_RC);
	protected static Term term_FF_DEBUG_BITSTREAM= new PrologSymbol(SymbolCodes.symbolCode_E_FF_DEBUG_BITSTREAM);
	protected static Term term_FF_DEBUG_MB_TYPE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_DEBUG_MB_TYPE);
	protected static Term term_FF_DEBUG_QP= new PrologSymbol(SymbolCodes.symbolCode_E_FF_DEBUG_QP);
	protected static Term term_FF_DEBUG_DCT_COEFF= new PrologSymbol(SymbolCodes.symbolCode_E_FF_DEBUG_DCT_COEFF);
	protected static Term term_FF_DEBUG_SKIP= new PrologSymbol(SymbolCodes.symbolCode_E_FF_DEBUG_SKIP);
	protected static Term term_FF_DEBUG_STARTCODE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_DEBUG_STARTCODE);
	protected static Term term_FF_DEBUG_PTS= new PrologSymbol(SymbolCodes.symbolCode_E_FF_DEBUG_PTS);
	protected static Term term_FF_DEBUG_ER= new PrologSymbol(SymbolCodes.symbolCode_E_FF_DEBUG_ER);
	protected static Term term_FF_DEBUG_MMCO= new PrologSymbol(SymbolCodes.symbolCode_E_FF_DEBUG_MMCO);
	protected static Term term_FF_DEBUG_BUGS= new PrologSymbol(SymbolCodes.symbolCode_E_FF_DEBUG_BUGS);
	protected static Term term_FF_DEBUG_BUFFERS= new PrologSymbol(SymbolCodes.symbolCode_E_FF_DEBUG_BUFFERS);
	protected static Term term_FF_DEBUG_THREADS= new PrologSymbol(SymbolCodes.symbolCode_E_FF_DEBUG_THREADS);
	protected static Term term_FF_DEBUG_GREEN_MD= new PrologSymbol(SymbolCodes.symbolCode_E_FF_DEBUG_GREEN_MD);
	protected static Term term_FF_DEBUG_NOMC= new PrologSymbol(SymbolCodes.symbolCode_E_FF_DEBUG_NOMC);
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public int toInteger();
	abstract public Term toTerm();
}
