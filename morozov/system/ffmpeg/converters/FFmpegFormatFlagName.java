// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avformat;

import target.*;

import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum FFmpegFormatFlagName {
	//
	AVFMT_NOFILE {
		@Override
		public int toInteger() {
			return avformat.AVFMT_NOFILE;
		}
		@Override
		public Term toTerm() {
			return term_AVFMT_NOFILE;
		}
	},
	AVFMT_NEEDNUMBER {
		@Override
		public int toInteger() {
			return avformat.AVFMT_NEEDNUMBER;
		}
		@Override
		public Term toTerm() {
			return term_AVFMT_NEEDNUMBER;
		}
	},
	AVFMT_SHOW_IDS {
		@Override
		public int toInteger() {
			return avformat.AVFMT_SHOW_IDS;
		}
		@Override
		public Term toTerm() {
			return term_AVFMT_SHOW_IDS;
		}
	},
	AVFMT_RAWPICTURE {
		@Override
		public int toInteger() {
			return avformat.AVFMT_RAWPICTURE;
		}
		@Override
		public Term toTerm() {
			return term_AVFMT_RAWPICTURE;
		}
	},
	AVFMT_GLOBALHEADER {
		@Override
		public int toInteger() {
			return avformat.AVFMT_GLOBALHEADER;
		}
		@Override
		public Term toTerm() {
			return term_AVFMT_GLOBALHEADER;
		}
	},
	AVFMT_NOTIMESTAMPS {
		@Override
		public int toInteger() {
			return avformat.AVFMT_NOTIMESTAMPS;
		}
		@Override
		public Term toTerm() {
			return term_AVFMT_NOTIMESTAMPS;
		}
	},
	AVFMT_GENERIC_INDEX {
		@Override
		public int toInteger() {
			return avformat.AVFMT_GENERIC_INDEX;
		}
		@Override
		public Term toTerm() {
			return term_AVFMT_GENERIC_INDEX;
		}
	},
	AVFMT_TS_DISCONT {
		@Override
		public int toInteger() {
			return avformat.AVFMT_TS_DISCONT;
		}
		@Override
		public Term toTerm() {
			return term_AVFMT_TS_DISCONT;
		}
	},
	AVFMT_VARIABLE_FPS {
		@Override
		public int toInteger() {
			return avformat.AVFMT_VARIABLE_FPS;
		}
		@Override
		public Term toTerm() {
			return term_AVFMT_VARIABLE_FPS;
		}
	},
	AVFMT_NODIMENSIONS {
		@Override
		public int toInteger() {
			return avformat.AVFMT_NODIMENSIONS;
		}
		@Override
		public Term toTerm() {
			return term_AVFMT_NODIMENSIONS;
		}
	},
	AVFMT_NOSTREAMS {
		@Override
		public int toInteger() {
			return avformat.AVFMT_NOSTREAMS;
		}
		@Override
		public Term toTerm() {
			return term_AVFMT_NOSTREAMS;
		}
	},
	AVFMT_NOBINSEARCH {
		@Override
		public int toInteger() {
			return avformat.AVFMT_NOBINSEARCH;
		}
		@Override
		public Term toTerm() {
			return term_AVFMT_NOBINSEARCH;
		}
	},
	AVFMT_NOGENSEARCH {
		@Override
		public int toInteger() {
			return avformat.AVFMT_NOGENSEARCH;
		}
		@Override
		public Term toTerm() {
			return term_AVFMT_NOGENSEARCH;
		}
	},
	AVFMT_NO_BYTE_SEEK {
		@Override
		public int toInteger() {
			return avformat.AVFMT_NO_BYTE_SEEK;
		}
		@Override
		public Term toTerm() {
			return term_AVFMT_NO_BYTE_SEEK;
		}
	},
	AVFMT_ALLOW_FLUSH {
		@Override
		public int toInteger() {
			return avformat.AVFMT_ALLOW_FLUSH;
		}
		@Override
		public Term toTerm() {
			return term_AVFMT_ALLOW_FLUSH;
		}
	},
	AVFMT_TS_NONSTRICT {
		@Override
		public int toInteger() {
			return avformat.AVFMT_TS_NONSTRICT;
		}
		@Override
		public Term toTerm() {
			return term_AVFMT_TS_NONSTRICT;
		}
	},
	AVFMT_TS_NEGATIVE {
		@Override
		public int toInteger() {
			return avformat.AVFMT_TS_NEGATIVE;
		}
		@Override
		public Term toTerm() {
			return term_AVFMT_TS_NEGATIVE;
		}
	},
	AVFMT_SEEK_TO_PTS {
		@Override
		public int toInteger() {
			return avformat.AVFMT_SEEK_TO_PTS;
		}
		@Override
		public Term toTerm() {
			return term_AVFMT_SEEK_TO_PTS;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegFormatFlagName argumentToFFmpegFormatFlagName(Term value, ChoisePoint iX) throws TermIsNotFFmpegFormatFlagName {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_AVFMT_NOFILE) {
				return FFmpegFormatFlagName.AVFMT_NOFILE;
			} else if (code==SymbolCodes.symbolCode_E_AVFMT_NEEDNUMBER) {
				return FFmpegFormatFlagName.AVFMT_NEEDNUMBER;
			} else if (code==SymbolCodes.symbolCode_E_AVFMT_SHOW_IDS) {
				return FFmpegFormatFlagName.AVFMT_SHOW_IDS;
			} else if (code==SymbolCodes.symbolCode_E_AVFMT_RAWPICTURE) {
				return FFmpegFormatFlagName.AVFMT_RAWPICTURE;
			} else if (code==SymbolCodes.symbolCode_E_AVFMT_GLOBALHEADER) {
				return FFmpegFormatFlagName.AVFMT_GLOBALHEADER;
			} else if (code==SymbolCodes.symbolCode_E_AVFMT_NOTIMESTAMPS) {
				return FFmpegFormatFlagName.AVFMT_NOTIMESTAMPS;
			} else if (code==SymbolCodes.symbolCode_E_AVFMT_GENERIC_INDEX) {
				return FFmpegFormatFlagName.AVFMT_GENERIC_INDEX;
			} else if (code==SymbolCodes.symbolCode_E_AVFMT_TS_DISCONT) {
				return FFmpegFormatFlagName.AVFMT_TS_DISCONT;
			} else if (code==SymbolCodes.symbolCode_E_AVFMT_VARIABLE_FPS) {
				return FFmpegFormatFlagName.AVFMT_VARIABLE_FPS;
			} else if (code==SymbolCodes.symbolCode_E_AVFMT_NODIMENSIONS) {
				return FFmpegFormatFlagName.AVFMT_NODIMENSIONS;
			} else if (code==SymbolCodes.symbolCode_E_AVFMT_NOSTREAMS) {
				return FFmpegFormatFlagName.AVFMT_NOSTREAMS;
			} else if (code==SymbolCodes.symbolCode_E_AVFMT_NOBINSEARCH) {
				return FFmpegFormatFlagName.AVFMT_NOBINSEARCH;
			} else if (code==SymbolCodes.symbolCode_E_AVFMT_NOGENSEARCH) {
				return FFmpegFormatFlagName.AVFMT_NOGENSEARCH;
			} else if (code==SymbolCodes.symbolCode_E_AVFMT_NO_BYTE_SEEK) {
				return FFmpegFormatFlagName.AVFMT_NO_BYTE_SEEK;
			} else if (code==SymbolCodes.symbolCode_E_AVFMT_ALLOW_FLUSH) {
				return FFmpegFormatFlagName.AVFMT_ALLOW_FLUSH;
			} else if (code==SymbolCodes.symbolCode_E_AVFMT_TS_NONSTRICT) {
				return FFmpegFormatFlagName.AVFMT_TS_NONSTRICT;
			} else if (code==SymbolCodes.symbolCode_E_AVFMT_TS_NEGATIVE) {
				return FFmpegFormatFlagName.AVFMT_TS_NEGATIVE;
			} else if (code==SymbolCodes.symbolCode_E_AVFMT_SEEK_TO_PTS) {
				return FFmpegFormatFlagName.AVFMT_SEEK_TO_PTS;
			} else {
				throw TermIsNotFFmpegFormatFlagName.instance;
			}
		} catch (TermIsNotASymbol e) {
			throw TermIsNotFFmpegFormatFlagName.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term term_AVFMT_NOFILE= new PrologSymbol(SymbolCodes.symbolCode_E_AVFMT_NOFILE);
	protected static Term term_AVFMT_NEEDNUMBER= new PrologSymbol(SymbolCodes.symbolCode_E_AVFMT_NEEDNUMBER);
	protected static Term term_AVFMT_SHOW_IDS= new PrologSymbol(SymbolCodes.symbolCode_E_AVFMT_SHOW_IDS);
	protected static Term term_AVFMT_RAWPICTURE= new PrologSymbol(SymbolCodes.symbolCode_E_AVFMT_RAWPICTURE);
	protected static Term term_AVFMT_GLOBALHEADER= new PrologSymbol(SymbolCodes.symbolCode_E_AVFMT_GLOBALHEADER);
	protected static Term term_AVFMT_NOTIMESTAMPS= new PrologSymbol(SymbolCodes.symbolCode_E_AVFMT_NOTIMESTAMPS);
	protected static Term term_AVFMT_GENERIC_INDEX= new PrologSymbol(SymbolCodes.symbolCode_E_AVFMT_GENERIC_INDEX);
	protected static Term term_AVFMT_TS_DISCONT= new PrologSymbol(SymbolCodes.symbolCode_E_AVFMT_TS_DISCONT);
	protected static Term term_AVFMT_VARIABLE_FPS= new PrologSymbol(SymbolCodes.symbolCode_E_AVFMT_VARIABLE_FPS);
	protected static Term term_AVFMT_NODIMENSIONS= new PrologSymbol(SymbolCodes.symbolCode_E_AVFMT_NODIMENSIONS);
	protected static Term term_AVFMT_NOSTREAMS= new PrologSymbol(SymbolCodes.symbolCode_E_AVFMT_NOSTREAMS);
	protected static Term term_AVFMT_NOBINSEARCH= new PrologSymbol(SymbolCodes.symbolCode_E_AVFMT_NOBINSEARCH);
	protected static Term term_AVFMT_NOGENSEARCH= new PrologSymbol(SymbolCodes.symbolCode_E_AVFMT_NOGENSEARCH);
	protected static Term term_AVFMT_NO_BYTE_SEEK= new PrologSymbol(SymbolCodes.symbolCode_E_AVFMT_NO_BYTE_SEEK);
	protected static Term term_AVFMT_ALLOW_FLUSH= new PrologSymbol(SymbolCodes.symbolCode_E_AVFMT_ALLOW_FLUSH);
	protected static Term term_AVFMT_TS_NONSTRICT= new PrologSymbol(SymbolCodes.symbolCode_E_AVFMT_TS_NONSTRICT);
	protected static Term term_AVFMT_TS_NEGATIVE= new PrologSymbol(SymbolCodes.symbolCode_E_AVFMT_TS_NEGATIVE);
	protected static Term term_AVFMT_SEEK_TO_PTS= new PrologSymbol(SymbolCodes.symbolCode_E_AVFMT_SEEK_TO_PTS);
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public int toInteger();
	abstract public Term toTerm();
}
