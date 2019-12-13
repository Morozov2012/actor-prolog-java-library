// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avutil;

import target.*;

import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum FFmpegMediaTypeName {
	//
	AVMEDIA_TYPE_UNKNOWN {
		@Override
		public int toInteger() {
			return avutil.AVMEDIA_TYPE_UNKNOWN;
		}
		@Override
		public Term toTerm() {
			return term_AVMEDIA_TYPE_UNKNOWN;
		}
	},
	AVMEDIA_TYPE_VIDEO {
		@Override
		public int toInteger() {
			return avutil.AVMEDIA_TYPE_VIDEO;
		}
		@Override
		public Term toTerm() {
			return term_AVMEDIA_TYPE_VIDEO;
		}
	},
	AVMEDIA_TYPE_AUDIO {
		@Override
		public int toInteger() {
			return avutil.AVMEDIA_TYPE_AUDIO;
		}
		@Override
		public Term toTerm() {
			return term_AVMEDIA_TYPE_AUDIO;
		}
	},
	AVMEDIA_TYPE_DATA {
		@Override
		public int toInteger() {
			return avutil.AVMEDIA_TYPE_DATA;
		}
		@Override
		public Term toTerm() {
			return term_AVMEDIA_TYPE_DATA;
		}
	},
	AVMEDIA_TYPE_SUBTITLE {
		@Override
		public int toInteger() {
			return avutil.AVMEDIA_TYPE_SUBTITLE;
		}
		@Override
		public Term toTerm() {
			return term_AVMEDIA_TYPE_SUBTITLE;
		}
	},
	AVMEDIA_TYPE_ATTACHMENT {
		@Override
		public int toInteger() {
			return avutil.AVMEDIA_TYPE_ATTACHMENT;
		}
		@Override
		public Term toTerm() {
			return term_AVMEDIA_TYPE_ATTACHMENT;
		}
	},
	AVMEDIA_TYPE_NB {
		@Override
		public int toInteger() {
			return avutil.AVMEDIA_TYPE_NB;
		}
		@Override
		public Term toTerm() {
			return term_AVMEDIA_TYPE_NB;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegMediaTypeName argumentToFFmpegMediaTypeName(Term value, ChoisePoint iX) throws TermIsNotFFmpegMediaTypeName {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_AVMEDIA_TYPE_UNKNOWN) {
				return FFmpegMediaTypeName.AVMEDIA_TYPE_UNKNOWN;
			} else if (code==SymbolCodes.symbolCode_E_AVMEDIA_TYPE_VIDEO) {
				return FFmpegMediaTypeName.AVMEDIA_TYPE_VIDEO;
			} else if (code==SymbolCodes.symbolCode_E_AVMEDIA_TYPE_AUDIO) {
				return FFmpegMediaTypeName.AVMEDIA_TYPE_AUDIO;
			} else if (code==SymbolCodes.symbolCode_E_AVMEDIA_TYPE_DATA) {
				return FFmpegMediaTypeName.AVMEDIA_TYPE_DATA;
			} else if (code==SymbolCodes.symbolCode_E_AVMEDIA_TYPE_SUBTITLE) {
				return FFmpegMediaTypeName.AVMEDIA_TYPE_SUBTITLE;
			} else if (code==SymbolCodes.symbolCode_E_AVMEDIA_TYPE_ATTACHMENT) {
				return FFmpegMediaTypeName.AVMEDIA_TYPE_ATTACHMENT;
			} else if (code==SymbolCodes.symbolCode_E_AVMEDIA_TYPE_NB) {
				return FFmpegMediaTypeName.AVMEDIA_TYPE_NB;
			} else {
				throw TermIsNotFFmpegMediaTypeName.instance;
			}
		} catch (TermIsNotASymbol e) {
			throw TermIsNotFFmpegMediaTypeName.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term term_AVMEDIA_TYPE_UNKNOWN= new PrologSymbol(SymbolCodes.symbolCode_E_AVMEDIA_TYPE_UNKNOWN);
	protected static Term term_AVMEDIA_TYPE_VIDEO= new PrologSymbol(SymbolCodes.symbolCode_E_AVMEDIA_TYPE_VIDEO);
	protected static Term term_AVMEDIA_TYPE_AUDIO= new PrologSymbol(SymbolCodes.symbolCode_E_AVMEDIA_TYPE_AUDIO);
	protected static Term term_AVMEDIA_TYPE_DATA= new PrologSymbol(SymbolCodes.symbolCode_E_AVMEDIA_TYPE_DATA);
	protected static Term term_AVMEDIA_TYPE_SUBTITLE= new PrologSymbol(SymbolCodes.symbolCode_E_AVMEDIA_TYPE_SUBTITLE);
	protected static Term term_AVMEDIA_TYPE_ATTACHMENT= new PrologSymbol(SymbolCodes.symbolCode_E_AVMEDIA_TYPE_ATTACHMENT);
	protected static Term term_AVMEDIA_TYPE_NB= new PrologSymbol(SymbolCodes.symbolCode_E_AVMEDIA_TYPE_NB);
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public int toInteger();
	abstract public Term toTerm();
}
