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
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_PICT_INFO;
		}
		@Override
		public Term toTerm() {
			return term_FF_DEBUG_PICT_INFO;
		}
	},
	FF_DEBUG_RC {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_RC;
		}
		@Override
		public Term toTerm() {
			return term_FF_DEBUG_RC;
		}
	},
	FF_DEBUG_BITSTREAM {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_BITSTREAM;
		}
		@Override
		public Term toTerm() {
			return term_FF_DEBUG_BITSTREAM;
		}
	},
	FF_DEBUG_MB_TYPE {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_MB_TYPE;
		}
		@Override
		public Term toTerm() {
			return term_FF_DEBUG_MB_TYPE;
		}
	},
	FF_DEBUG_QP {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_QP;
		}
		@Override
		public Term toTerm() {
			return term_FF_DEBUG_QP;
		}
	},
	FF_DEBUG_DCT_COEFF {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_DCT_COEFF;
		}
		@Override
		public Term toTerm() {
			return term_FF_DEBUG_DCT_COEFF;
		}
	},
	FF_DEBUG_SKIP {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_SKIP;
		}
		@Override
		public Term toTerm() {
			return term_FF_DEBUG_SKIP;
		}
	},
	FF_DEBUG_STARTCODE {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_STARTCODE;
		}
		@Override
		public Term toTerm() {
			return term_FF_DEBUG_STARTCODE;
		}
	},
	FF_DEBUG_PTS {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_PTS;
		}
		@Override
		public Term toTerm() {
			return term_FF_DEBUG_PTS;
		}
	},
	FF_DEBUG_ER {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_ER;
		}
		@Override
		public Term toTerm() {
			return term_FF_DEBUG_ER;
		}
	},
	FF_DEBUG_MMCO {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_MMCO;
		}
		@Override
		public Term toTerm() {
			return term_FF_DEBUG_MMCO;
		}
	},
	FF_DEBUG_BUGS {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_BUGS;
		}
		@Override
		public Term toTerm() {
			return term_FF_DEBUG_BUGS;
		}
	},
	FF_DEBUG_BUFFERS {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_BUFFERS;
		}
		@Override
		public Term toTerm() {
			return term_FF_DEBUG_BUFFERS;
		}
	},
	FF_DEBUG_THREADS {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_THREADS;
		}
		@Override
		public Term toTerm() {
			return term_FF_DEBUG_THREADS;
		}
	},
	FF_DEBUG_GREEN_MD {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_GREEN_MD;
		}
		@Override
		public Term toTerm() {
			return term_FF_DEBUG_GREEN_MD;
		}
	},
	FF_DEBUG_NOMC {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_DEBUG_NOMC;
		}
		@Override
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
