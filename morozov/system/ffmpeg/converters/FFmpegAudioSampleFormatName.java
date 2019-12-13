// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avutil;

import target.*;

import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum FFmpegAudioSampleFormatName {
	//
	AV_SAMPLE_FMT_NONE {
		@Override
		public int toInteger() {
			return avutil.AV_SAMPLE_FMT_NONE;
		}
		@Override
		public Term toTerm() {
			return term_AV_SAMPLE_FMT_NONE;
		}
	},
	AV_SAMPLE_FMT_U8 {
		@Override
		public int toInteger() {
			return avutil.AV_SAMPLE_FMT_U8;
		}
		@Override
		public Term toTerm() {
			return term_AV_SAMPLE_FMT_U8;
		}
	},
	AV_SAMPLE_FMT_S16 {
		@Override
		public int toInteger() {
			return avutil.AV_SAMPLE_FMT_S16;
		}
		@Override
		public Term toTerm() {
			return term_AV_SAMPLE_FMT_S16;
		}
	},
	AV_SAMPLE_FMT_S32 {
		@Override
		public int toInteger() {
			return avutil.AV_SAMPLE_FMT_S32;
		}
		@Override
		public Term toTerm() {
			return term_AV_SAMPLE_FMT_S32;
		}
	},
	AV_SAMPLE_FMT_FLT {
		@Override
		public int toInteger() {
			return avutil.AV_SAMPLE_FMT_FLT;
		}
		@Override
		public Term toTerm() {
			return term_AV_SAMPLE_FMT_FLT;
		}
	},
	AV_SAMPLE_FMT_DBL {
		@Override
		public int toInteger() {
			return avutil.AV_SAMPLE_FMT_DBL;
		}
		@Override
		public Term toTerm() {
			return term_AV_SAMPLE_FMT_DBL;
		}
	},
	AV_SAMPLE_FMT_U8P {
		@Override
		public int toInteger() {
			return avutil.AV_SAMPLE_FMT_U8P;
		}
		@Override
		public Term toTerm() {
			return term_AV_SAMPLE_FMT_U8P;
		}
	},
	AV_SAMPLE_FMT_S16P {
		@Override
		public int toInteger() {
			return avutil.AV_SAMPLE_FMT_S16P;
		}
		@Override
		public Term toTerm() {
			return term_AV_SAMPLE_FMT_S16P;
		}
	},
	AV_SAMPLE_FMT_S32P {
		@Override
		public int toInteger() {
			return avutil.AV_SAMPLE_FMT_S32P;
		}
		@Override
		public Term toTerm() {
			return term_AV_SAMPLE_FMT_S32P;
		}
	},
	AV_SAMPLE_FMT_FLTP {
		@Override
		public int toInteger() {
			return avutil.AV_SAMPLE_FMT_FLTP;
		}
		@Override
		public Term toTerm() {
			return term_AV_SAMPLE_FMT_FLTP;
		}
	},
	AV_SAMPLE_FMT_DBLP {
		@Override
		public int toInteger() {
			return avutil.AV_SAMPLE_FMT_DBLP;
		}
		@Override
		public Term toTerm() {
			return term_AV_SAMPLE_FMT_DBLP;
		}
	},
	AV_SAMPLE_FMT_S64 {
		@Override
		public int toInteger() {
			return avutil.AV_SAMPLE_FMT_S64;
		}
		@Override
		public Term toTerm() {
			return term_AV_SAMPLE_FMT_S64;
		}
	},
	AV_SAMPLE_FMT_S64P {
		@Override
		public int toInteger() {
			return avutil.AV_SAMPLE_FMT_S64P;
		}
		@Override
		public Term toTerm() {
			return term_AV_SAMPLE_FMT_S64P;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegAudioSampleFormatName argumentToFFmpegAudioSampleFormatName(Term value, ChoisePoint iX) throws TermIsNotFFmpegAudioSampleFormatName {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_AV_SAMPLE_FMT_NONE) {
				return FFmpegAudioSampleFormatName.AV_SAMPLE_FMT_NONE;
			} else if (code==SymbolCodes.symbolCode_E_AV_SAMPLE_FMT_U8) {
				return FFmpegAudioSampleFormatName.AV_SAMPLE_FMT_U8;
			} else if (code==SymbolCodes.symbolCode_E_AV_SAMPLE_FMT_S16) {
				return FFmpegAudioSampleFormatName.AV_SAMPLE_FMT_S16;
			} else if (code==SymbolCodes.symbolCode_E_AV_SAMPLE_FMT_S32) {
				return FFmpegAudioSampleFormatName.AV_SAMPLE_FMT_S32;
			} else if (code==SymbolCodes.symbolCode_E_AV_SAMPLE_FMT_FLT) {
				return FFmpegAudioSampleFormatName.AV_SAMPLE_FMT_FLT;
			} else if (code==SymbolCodes.symbolCode_E_AV_SAMPLE_FMT_DBL) {
				return FFmpegAudioSampleFormatName.AV_SAMPLE_FMT_DBL;
			} else if (code==SymbolCodes.symbolCode_E_AV_SAMPLE_FMT_U8P) {
				return FFmpegAudioSampleFormatName.AV_SAMPLE_FMT_U8P;
			} else if (code==SymbolCodes.symbolCode_E_AV_SAMPLE_FMT_S16P) {
				return FFmpegAudioSampleFormatName.AV_SAMPLE_FMT_S16P;
			} else if (code==SymbolCodes.symbolCode_E_AV_SAMPLE_FMT_S32P) {
				return FFmpegAudioSampleFormatName.AV_SAMPLE_FMT_S32P;
			} else if (code==SymbolCodes.symbolCode_E_AV_SAMPLE_FMT_FLTP) {
				return FFmpegAudioSampleFormatName.AV_SAMPLE_FMT_FLTP;
			} else if (code==SymbolCodes.symbolCode_E_AV_SAMPLE_FMT_DBLP) {
				return FFmpegAudioSampleFormatName.AV_SAMPLE_FMT_DBLP;
			} else if (code==SymbolCodes.symbolCode_E_AV_SAMPLE_FMT_S64) {
				return FFmpegAudioSampleFormatName.AV_SAMPLE_FMT_S64;
			} else if (code==SymbolCodes.symbolCode_E_AV_SAMPLE_FMT_S64P) {
				return FFmpegAudioSampleFormatName.AV_SAMPLE_FMT_S64P;
			} else {
				throw TermIsNotFFmpegAudioSampleFormatName.instance;
			}
		} catch (TermIsNotASymbol e) {
			throw TermIsNotFFmpegAudioSampleFormatName.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term term_AV_SAMPLE_FMT_NONE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_SAMPLE_FMT_NONE);
	protected static Term term_AV_SAMPLE_FMT_U8= new PrologSymbol(SymbolCodes.symbolCode_E_AV_SAMPLE_FMT_U8);
	protected static Term term_AV_SAMPLE_FMT_S16= new PrologSymbol(SymbolCodes.symbolCode_E_AV_SAMPLE_FMT_S16);
	protected static Term term_AV_SAMPLE_FMT_S32= new PrologSymbol(SymbolCodes.symbolCode_E_AV_SAMPLE_FMT_S32);
	protected static Term term_AV_SAMPLE_FMT_FLT= new PrologSymbol(SymbolCodes.symbolCode_E_AV_SAMPLE_FMT_FLT);
	protected static Term term_AV_SAMPLE_FMT_DBL= new PrologSymbol(SymbolCodes.symbolCode_E_AV_SAMPLE_FMT_DBL);
	protected static Term term_AV_SAMPLE_FMT_U8P= new PrologSymbol(SymbolCodes.symbolCode_E_AV_SAMPLE_FMT_U8P);
	protected static Term term_AV_SAMPLE_FMT_S16P= new PrologSymbol(SymbolCodes.symbolCode_E_AV_SAMPLE_FMT_S16P);
	protected static Term term_AV_SAMPLE_FMT_S32P= new PrologSymbol(SymbolCodes.symbolCode_E_AV_SAMPLE_FMT_S32P);
	protected static Term term_AV_SAMPLE_FMT_FLTP= new PrologSymbol(SymbolCodes.symbolCode_E_AV_SAMPLE_FMT_FLTP);
	protected static Term term_AV_SAMPLE_FMT_DBLP= new PrologSymbol(SymbolCodes.symbolCode_E_AV_SAMPLE_FMT_DBLP);
	protected static Term term_AV_SAMPLE_FMT_S64= new PrologSymbol(SymbolCodes.symbolCode_E_AV_SAMPLE_FMT_S64);
	protected static Term term_AV_SAMPLE_FMT_S64P= new PrologSymbol(SymbolCodes.symbolCode_E_AV_SAMPLE_FMT_S64P);
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public int toInteger();
	abstract public Term toTerm();
}
