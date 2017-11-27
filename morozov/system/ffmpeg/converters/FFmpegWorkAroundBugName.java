// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avcodec;

import target.*;

import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum FFmpegWorkAroundBugName {
	//
	FF_BUG_AUTODETECT {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_BUG_AUTODETECT;
		}
		public Term toTerm() {
			return term_FF_BUG_AUTODETECT;
		}
	},
	FF_BUG_OLD_MSMPEG4 {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_BUG_OLD_MSMPEG4;
		}
		public Term toTerm() {
			return term_FF_BUG_OLD_MSMPEG4;
		}
	},
	FF_BUG_XVID_ILACE {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_BUG_XVID_ILACE;
		}
		public Term toTerm() {
			return term_FF_BUG_XVID_ILACE;
		}
	},
	FF_BUG_UMP4 {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_BUG_UMP4;
		}
		public Term toTerm() {
			return term_FF_BUG_UMP4;
		}
	},
	FF_BUG_NO_PADDING {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_BUG_NO_PADDING;
		}
		public Term toTerm() {
			return term_FF_BUG_NO_PADDING;
		}
	},
	FF_BUG_AMV {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_BUG_AMV;
		}
		public Term toTerm() {
			return term_FF_BUG_AMV;
		}
	},
	FF_BUG_QPEL_CHROMA {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_BUG_QPEL_CHROMA;
		}
		public Term toTerm() {
			return term_FF_BUG_QPEL_CHROMA;
		}
	},
	FF_BUG_STD_QPEL {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_BUG_STD_QPEL;
		}
		public Term toTerm() {
			return term_FF_BUG_STD_QPEL;
		}
	},
	FF_BUG_QPEL_CHROMA2 {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_BUG_QPEL_CHROMA2;
		}
		public Term toTerm() {
			return term_FF_BUG_QPEL_CHROMA2;
		}
	},
	FF_BUG_DIRECT_BLOCKSIZE {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_BUG_DIRECT_BLOCKSIZE;
		}
		public Term toTerm() {
			return term_FF_BUG_DIRECT_BLOCKSIZE;
		}
	},
	FF_BUG_EDGE {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_BUG_EDGE;
		}
		public Term toTerm() {
			return term_FF_BUG_EDGE;
		}
	},
	FF_BUG_HPEL_CHROMA {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_BUG_HPEL_CHROMA;
		}
		public Term toTerm() {
			return term_FF_BUG_HPEL_CHROMA;
		}
	},
	FF_BUG_DC_CLIP {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_BUG_DC_CLIP;
		}
		public Term toTerm() {
			return term_FF_BUG_DC_CLIP;
		}
	},
	FF_BUG_MS {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_BUG_MS;
		}
		public Term toTerm() {
			return term_FF_BUG_MS;
		}
	},
	FF_BUG_TRUNCATED {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_BUG_TRUNCATED;
		}
		public Term toTerm() {
			return term_FF_BUG_TRUNCATED;
		}
	},
	FF_BUG_IEDGE {
		public int toInteger() {
			return avcodec.AVCodecContext.FF_BUG_IEDGE;
		}
		public Term toTerm() {
			return term_FF_BUG_IEDGE;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegWorkAroundBugName argumentToFFmpegWorkAroundBugName(Term value, ChoisePoint iX) throws TermIsNotFFmpegWorkAroundBugName {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_FF_BUG_AUTODETECT) {
				return FFmpegWorkAroundBugName.FF_BUG_AUTODETECT;
			} else if (code==SymbolCodes.symbolCode_E_FF_BUG_OLD_MSMPEG4) {
				return FFmpegWorkAroundBugName.FF_BUG_OLD_MSMPEG4;
			} else if (code==SymbolCodes.symbolCode_E_FF_BUG_XVID_ILACE) {
				return FFmpegWorkAroundBugName.FF_BUG_XVID_ILACE;
			} else if (code==SymbolCodes.symbolCode_E_FF_BUG_UMP4) {
				return FFmpegWorkAroundBugName.FF_BUG_UMP4;
			} else if (code==SymbolCodes.symbolCode_E_FF_BUG_NO_PADDING) {
				return FFmpegWorkAroundBugName.FF_BUG_NO_PADDING;
			} else if (code==SymbolCodes.symbolCode_E_FF_BUG_AMV) {
				return FFmpegWorkAroundBugName.FF_BUG_AMV;
			} else if (code==SymbolCodes.symbolCode_E_FF_BUG_QPEL_CHROMA) {
				return FFmpegWorkAroundBugName.FF_BUG_QPEL_CHROMA;
			} else if (code==SymbolCodes.symbolCode_E_FF_BUG_STD_QPEL) {
				return FFmpegWorkAroundBugName.FF_BUG_STD_QPEL;
			} else if (code==SymbolCodes.symbolCode_E_FF_BUG_QPEL_CHROMA2) {
				return FFmpegWorkAroundBugName.FF_BUG_QPEL_CHROMA2;
			} else if (code==SymbolCodes.symbolCode_E_FF_BUG_DIRECT_BLOCKSIZE) {
				return FFmpegWorkAroundBugName.FF_BUG_DIRECT_BLOCKSIZE;
			} else if (code==SymbolCodes.symbolCode_E_FF_BUG_EDGE) {
				return FFmpegWorkAroundBugName.FF_BUG_EDGE;
			} else if (code==SymbolCodes.symbolCode_E_FF_BUG_HPEL_CHROMA) {
				return FFmpegWorkAroundBugName.FF_BUG_HPEL_CHROMA;
			} else if (code==SymbolCodes.symbolCode_E_FF_BUG_DC_CLIP) {
				return FFmpegWorkAroundBugName.FF_BUG_DC_CLIP;
			} else if (code==SymbolCodes.symbolCode_E_FF_BUG_MS) {
				return FFmpegWorkAroundBugName.FF_BUG_MS;
			} else if (code==SymbolCodes.symbolCode_E_FF_BUG_TRUNCATED) {
				return FFmpegWorkAroundBugName.FF_BUG_TRUNCATED;
			} else if (code==SymbolCodes.symbolCode_E_FF_BUG_IEDGE) {
				return FFmpegWorkAroundBugName.FF_BUG_IEDGE;
			} else {
				throw TermIsNotFFmpegWorkAroundBugName.instance;
			}
		} catch (TermIsNotASymbol e) {
			throw TermIsNotFFmpegWorkAroundBugName.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term term_FF_BUG_AUTODETECT= new PrologSymbol(SymbolCodes.symbolCode_E_FF_BUG_AUTODETECT);
	protected static Term term_FF_BUG_OLD_MSMPEG4= new PrologSymbol(SymbolCodes.symbolCode_E_FF_BUG_OLD_MSMPEG4);
	protected static Term term_FF_BUG_XVID_ILACE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_BUG_XVID_ILACE);
	protected static Term term_FF_BUG_UMP4= new PrologSymbol(SymbolCodes.symbolCode_E_FF_BUG_UMP4);
	protected static Term term_FF_BUG_NO_PADDING= new PrologSymbol(SymbolCodes.symbolCode_E_FF_BUG_NO_PADDING);
	protected static Term term_FF_BUG_AMV= new PrologSymbol(SymbolCodes.symbolCode_E_FF_BUG_AMV);
	protected static Term term_FF_BUG_QPEL_CHROMA= new PrologSymbol(SymbolCodes.symbolCode_E_FF_BUG_QPEL_CHROMA);
	protected static Term term_FF_BUG_STD_QPEL= new PrologSymbol(SymbolCodes.symbolCode_E_FF_BUG_STD_QPEL);
	protected static Term term_FF_BUG_QPEL_CHROMA2= new PrologSymbol(SymbolCodes.symbolCode_E_FF_BUG_QPEL_CHROMA2);
	protected static Term term_FF_BUG_DIRECT_BLOCKSIZE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_BUG_DIRECT_BLOCKSIZE);
	protected static Term term_FF_BUG_EDGE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_BUG_EDGE);
	protected static Term term_FF_BUG_HPEL_CHROMA= new PrologSymbol(SymbolCodes.symbolCode_E_FF_BUG_HPEL_CHROMA);
	protected static Term term_FF_BUG_DC_CLIP= new PrologSymbol(SymbolCodes.symbolCode_E_FF_BUG_DC_CLIP);
	protected static Term term_FF_BUG_MS= new PrologSymbol(SymbolCodes.symbolCode_E_FF_BUG_MS);
	protected static Term term_FF_BUG_TRUNCATED= new PrologSymbol(SymbolCodes.symbolCode_E_FF_BUG_TRUNCATED);
	protected static Term term_FF_BUG_IEDGE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_BUG_IEDGE);
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public int toInteger();
	abstract public Term toTerm();
}
