// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avutil;

import target.*;

import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum FFmpegChannelLayoutName {
	//
	AV_CH_LAYOUT_MONO {
		public long toLong() {
			return avutil.AV_CH_LAYOUT_MONO;
		}
		public Term toTerm() {
			return term_AV_CH_LAYOUT_MONO;
		}
	},
	AV_CH_LAYOUT_STEREO {
		public long toLong() {
			return avutil.AV_CH_LAYOUT_STEREO;
		}
		public Term toTerm() {
			return term_AV_CH_LAYOUT_STEREO;
		}
	},
	AV_CH_LAYOUT_2POINT1 {
		public long toLong() {
			return avutil.AV_CH_LAYOUT_2POINT1;
		}
		public Term toTerm() {
			return term_AV_CH_LAYOUT_2POINT1;
		}
	},
	AV_CH_LAYOUT_2_1 {
		public long toLong() {
			return avutil.AV_CH_LAYOUT_2_1;
		}
		public Term toTerm() {
			return term_AV_CH_LAYOUT_2_1;
		}
	},
	AV_CH_LAYOUT_SURROUND {
		public long toLong() {
			return avutil.AV_CH_LAYOUT_SURROUND;
		}
		public Term toTerm() {
			return term_AV_CH_LAYOUT_SURROUND;
		}
	},
	AV_CH_LAYOUT_3POINT1 {
		public long toLong() {
			return avutil.AV_CH_LAYOUT_3POINT1;
		}
		public Term toTerm() {
			return term_AV_CH_LAYOUT_3POINT1;
		}
	},
	AV_CH_LAYOUT_4POINT0 {
		public long toLong() {
			return avutil.AV_CH_LAYOUT_4POINT0;
		}
		public Term toTerm() {
			return term_AV_CH_LAYOUT_4POINT0;
		}
	},
	AV_CH_LAYOUT_4POINT1 {
		public long toLong() {
			return avutil.AV_CH_LAYOUT_4POINT1;
		}
		public Term toTerm() {
			return term_AV_CH_LAYOUT_4POINT1;
		}
	},
	AV_CH_LAYOUT_2_2 {
		public long toLong() {
			return avutil.AV_CH_LAYOUT_2_2;
		}
		public Term toTerm() {
			return term_AV_CH_LAYOUT_2_2;
		}
	},
	AV_CH_LAYOUT_QUAD {
		public long toLong() {
			return avutil.AV_CH_LAYOUT_QUAD;
		}
		public Term toTerm() {
			return term_AV_CH_LAYOUT_QUAD;
		}
	},
	AV_CH_LAYOUT_5POINT0 {
		public long toLong() {
			return avutil.AV_CH_LAYOUT_5POINT0;
		}
		public Term toTerm() {
			return term_AV_CH_LAYOUT_5POINT0;
		}
	},
	AV_CH_LAYOUT_5POINT1 {
		public long toLong() {
			return avutil.AV_CH_LAYOUT_5POINT1;
		}
		public Term toTerm() {
			return term_AV_CH_LAYOUT_5POINT1;
		}
	},
	AV_CH_LAYOUT_5POINT0_BACK {
		public long toLong() {
			return avutil.AV_CH_LAYOUT_5POINT0_BACK;
		}
		public Term toTerm() {
			return term_AV_CH_LAYOUT_5POINT0_BACK;
		}
	},
	AV_CH_LAYOUT_5POINT1_BACK {
		public long toLong() {
			return avutil.AV_CH_LAYOUT_5POINT1_BACK;
		}
		public Term toTerm() {
			return term_AV_CH_LAYOUT_5POINT1_BACK;
		}
	},
	AV_CH_LAYOUT_6POINT0 {
		public long toLong() {
			return avutil.AV_CH_LAYOUT_6POINT0;
		}
		public Term toTerm() {
			return term_AV_CH_LAYOUT_6POINT0;
		}
	},
	AV_CH_LAYOUT_6POINT0_FRONT {
		public long toLong() {
			return avutil.AV_CH_LAYOUT_6POINT0_FRONT;
		}
		public Term toTerm() {
			return term_AV_CH_LAYOUT_6POINT0_FRONT;
		}
	},
	AV_CH_LAYOUT_HEXAGONAL {
		public long toLong() {
			return avutil.AV_CH_LAYOUT_HEXAGONAL;
		}
		public Term toTerm() {
			return term_AV_CH_LAYOUT_HEXAGONAL;
		}
	},
	AV_CH_LAYOUT_6POINT1 {
		public long toLong() {
			return avutil.AV_CH_LAYOUT_6POINT1;
		}
		public Term toTerm() {
			return term_AV_CH_LAYOUT_6POINT1;
		}
	},
	AV_CH_LAYOUT_6POINT1_BACK {
		public long toLong() {
			return avutil.AV_CH_LAYOUT_6POINT1_BACK;
		}
		public Term toTerm() {
			return term_AV_CH_LAYOUT_6POINT1_BACK;
		}
	},
	AV_CH_LAYOUT_6POINT1_FRONT {
		public long toLong() {
			return avutil.AV_CH_LAYOUT_6POINT1_FRONT;
		}
		public Term toTerm() {
			return term_AV_CH_LAYOUT_6POINT1_FRONT;
		}
	},
	AV_CH_LAYOUT_7POINT0 {
		public long toLong() {
			return avutil.AV_CH_LAYOUT_7POINT0;
		}
		public Term toTerm() {
			return term_AV_CH_LAYOUT_7POINT0;
		}
	},
	AV_CH_LAYOUT_7POINT0_FRONT {
		public long toLong() {
			return avutil.AV_CH_LAYOUT_7POINT0_FRONT;
		}
		public Term toTerm() {
			return term_AV_CH_LAYOUT_7POINT0_FRONT;
		}
	},
	AV_CH_LAYOUT_7POINT1 {
		public long toLong() {
			return avutil.AV_CH_LAYOUT_7POINT1;
		}
		public Term toTerm() {
			return term_AV_CH_LAYOUT_7POINT1;
		}
	},
	AV_CH_LAYOUT_7POINT1_WIDE {
		public long toLong() {
			return avutil.AV_CH_LAYOUT_7POINT1_WIDE;
		}
		public Term toTerm() {
			return term_AV_CH_LAYOUT_7POINT1_WIDE;
		}
	},
	AV_CH_LAYOUT_7POINT1_WIDE_BACK {
		public long toLong() {
			return avutil.AV_CH_LAYOUT_7POINT1_WIDE_BACK;
		}
		public Term toTerm() {
			return term_AV_CH_LAYOUT_7POINT1_WIDE_BACK;
		}
	},
	AV_CH_LAYOUT_OCTAGONAL {
		public long toLong() {
			return avutil.AV_CH_LAYOUT_OCTAGONAL;
		}
		public Term toTerm() {
			return term_AV_CH_LAYOUT_OCTAGONAL;
		}
	},
	AV_CH_LAYOUT_HEXADECAGONAL {
		public long toLong() {
			return avutil.AV_CH_LAYOUT_HEXADECAGONAL;
		}
		public Term toTerm() {
			return term_AV_CH_LAYOUT_HEXADECAGONAL;
		}
	},
	AV_CH_LAYOUT_STEREO_DOWNMIX {
		public long toLong() {
			return avutil.AV_CH_LAYOUT_STEREO_DOWNMIX;
		}
		public Term toTerm() {
			return term_AV_CH_LAYOUT_STEREO_DOWNMIX;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegChannelLayoutName argumentToFFmpegChannelLayoutName(Term value, ChoisePoint iX) throws TermIsNotFFmpegChannelLayoutName {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_AV_CH_LAYOUT_MONO) {
				return FFmpegChannelLayoutName.AV_CH_LAYOUT_MONO;
			} else if (code==SymbolCodes.symbolCode_E_AV_CH_LAYOUT_STEREO) {
				return FFmpegChannelLayoutName.AV_CH_LAYOUT_STEREO;
			} else if (code==SymbolCodes.symbolCode_E_AV_CH_LAYOUT_2POINT1) {
				return FFmpegChannelLayoutName.AV_CH_LAYOUT_2POINT1;
			} else if (code==SymbolCodes.symbolCode_E_AV_CH_LAYOUT_2_1) {
				return FFmpegChannelLayoutName.AV_CH_LAYOUT_2_1;
			} else if (code==SymbolCodes.symbolCode_E_AV_CH_LAYOUT_SURROUND) {
				return FFmpegChannelLayoutName.AV_CH_LAYOUT_SURROUND;
			} else if (code==SymbolCodes.symbolCode_E_AV_CH_LAYOUT_3POINT1) {
				return FFmpegChannelLayoutName.AV_CH_LAYOUT_3POINT1;
			} else if (code==SymbolCodes.symbolCode_E_AV_CH_LAYOUT_4POINT0) {
				return FFmpegChannelLayoutName.AV_CH_LAYOUT_4POINT0;
			} else if (code==SymbolCodes.symbolCode_E_AV_CH_LAYOUT_4POINT1) {
				return FFmpegChannelLayoutName.AV_CH_LAYOUT_4POINT1;
			} else if (code==SymbolCodes.symbolCode_E_AV_CH_LAYOUT_2_2) {
				return FFmpegChannelLayoutName.AV_CH_LAYOUT_2_2;
			} else if (code==SymbolCodes.symbolCode_E_AV_CH_LAYOUT_QUAD) {
				return FFmpegChannelLayoutName.AV_CH_LAYOUT_QUAD;
			} else if (code==SymbolCodes.symbolCode_E_AV_CH_LAYOUT_5POINT0) {
				return FFmpegChannelLayoutName.AV_CH_LAYOUT_5POINT0;
			} else if (code==SymbolCodes.symbolCode_E_AV_CH_LAYOUT_5POINT1) {
				return FFmpegChannelLayoutName.AV_CH_LAYOUT_5POINT1;
			} else if (code==SymbolCodes.symbolCode_E_AV_CH_LAYOUT_5POINT0_BACK) {
				return FFmpegChannelLayoutName.AV_CH_LAYOUT_5POINT0_BACK;
			} else if (code==SymbolCodes.symbolCode_E_AV_CH_LAYOUT_5POINT1_BACK) {
				return FFmpegChannelLayoutName.AV_CH_LAYOUT_5POINT1_BACK;
			} else if (code==SymbolCodes.symbolCode_E_AV_CH_LAYOUT_6POINT0) {
				return FFmpegChannelLayoutName.AV_CH_LAYOUT_6POINT0;
			} else if (code==SymbolCodes.symbolCode_E_AV_CH_LAYOUT_6POINT0_FRONT) {
				return FFmpegChannelLayoutName.AV_CH_LAYOUT_6POINT0_FRONT;
			} else if (code==SymbolCodes.symbolCode_E_AV_CH_LAYOUT_HEXAGONAL) {
				return FFmpegChannelLayoutName.AV_CH_LAYOUT_HEXAGONAL;
			} else if (code==SymbolCodes.symbolCode_E_AV_CH_LAYOUT_6POINT1) {
				return FFmpegChannelLayoutName.AV_CH_LAYOUT_6POINT1;
			} else if (code==SymbolCodes.symbolCode_E_AV_CH_LAYOUT_6POINT1_BACK) {
				return FFmpegChannelLayoutName.AV_CH_LAYOUT_6POINT1_BACK;
			} else if (code==SymbolCodes.symbolCode_E_AV_CH_LAYOUT_6POINT1_FRONT) {
				return FFmpegChannelLayoutName.AV_CH_LAYOUT_6POINT1_FRONT;
			} else if (code==SymbolCodes.symbolCode_E_AV_CH_LAYOUT_7POINT0) {
				return FFmpegChannelLayoutName.AV_CH_LAYOUT_7POINT0;
			} else if (code==SymbolCodes.symbolCode_E_AV_CH_LAYOUT_7POINT0_FRONT) {
				return FFmpegChannelLayoutName.AV_CH_LAYOUT_7POINT0_FRONT;
			} else if (code==SymbolCodes.symbolCode_E_AV_CH_LAYOUT_7POINT1) {
				return FFmpegChannelLayoutName.AV_CH_LAYOUT_7POINT1;
			} else if (code==SymbolCodes.symbolCode_E_AV_CH_LAYOUT_7POINT1_WIDE) {
				return FFmpegChannelLayoutName.AV_CH_LAYOUT_7POINT1_WIDE;
			} else if (code==SymbolCodes.symbolCode_E_AV_CH_LAYOUT_7POINT1_WIDE_BACK) {
				return FFmpegChannelLayoutName.AV_CH_LAYOUT_7POINT1_WIDE_BACK;
			} else if (code==SymbolCodes.symbolCode_E_AV_CH_LAYOUT_OCTAGONAL) {
				return FFmpegChannelLayoutName.AV_CH_LAYOUT_OCTAGONAL;
			} else if (code==SymbolCodes.symbolCode_E_AV_CH_LAYOUT_HEXADECAGONAL) {
				return FFmpegChannelLayoutName.AV_CH_LAYOUT_HEXADECAGONAL;
			} else if (code==SymbolCodes.symbolCode_E_AV_CH_LAYOUT_STEREO_DOWNMIX) {
				return FFmpegChannelLayoutName.AV_CH_LAYOUT_STEREO_DOWNMIX;
			} else {
				throw TermIsNotFFmpegChannelLayoutName.instance;
			}
		} catch (TermIsNotASymbol e) {
			throw TermIsNotFFmpegChannelLayoutName.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term term_AV_CH_LAYOUT_MONO= new PrologSymbol(SymbolCodes.symbolCode_E_AV_CH_LAYOUT_MONO);
	protected static Term term_AV_CH_LAYOUT_STEREO= new PrologSymbol(SymbolCodes.symbolCode_E_AV_CH_LAYOUT_STEREO);
	protected static Term term_AV_CH_LAYOUT_2POINT1= new PrologSymbol(SymbolCodes.symbolCode_E_AV_CH_LAYOUT_2POINT1);
	protected static Term term_AV_CH_LAYOUT_2_1= new PrologSymbol(SymbolCodes.symbolCode_E_AV_CH_LAYOUT_2_1);
	protected static Term term_AV_CH_LAYOUT_SURROUND= new PrologSymbol(SymbolCodes.symbolCode_E_AV_CH_LAYOUT_SURROUND);
	protected static Term term_AV_CH_LAYOUT_3POINT1= new PrologSymbol(SymbolCodes.symbolCode_E_AV_CH_LAYOUT_3POINT1);
	protected static Term term_AV_CH_LAYOUT_4POINT0= new PrologSymbol(SymbolCodes.symbolCode_E_AV_CH_LAYOUT_4POINT0);
	protected static Term term_AV_CH_LAYOUT_4POINT1= new PrologSymbol(SymbolCodes.symbolCode_E_AV_CH_LAYOUT_4POINT1);
	protected static Term term_AV_CH_LAYOUT_2_2= new PrologSymbol(SymbolCodes.symbolCode_E_AV_CH_LAYOUT_2_2);
	protected static Term term_AV_CH_LAYOUT_QUAD= new PrologSymbol(SymbolCodes.symbolCode_E_AV_CH_LAYOUT_QUAD);
	protected static Term term_AV_CH_LAYOUT_5POINT0= new PrologSymbol(SymbolCodes.symbolCode_E_AV_CH_LAYOUT_5POINT0);
	protected static Term term_AV_CH_LAYOUT_5POINT1= new PrologSymbol(SymbolCodes.symbolCode_E_AV_CH_LAYOUT_5POINT1);
	protected static Term term_AV_CH_LAYOUT_5POINT0_BACK= new PrologSymbol(SymbolCodes.symbolCode_E_AV_CH_LAYOUT_5POINT0_BACK);
	protected static Term term_AV_CH_LAYOUT_5POINT1_BACK= new PrologSymbol(SymbolCodes.symbolCode_E_AV_CH_LAYOUT_5POINT1_BACK);
	protected static Term term_AV_CH_LAYOUT_6POINT0= new PrologSymbol(SymbolCodes.symbolCode_E_AV_CH_LAYOUT_6POINT0);
	protected static Term term_AV_CH_LAYOUT_6POINT0_FRONT= new PrologSymbol(SymbolCodes.symbolCode_E_AV_CH_LAYOUT_6POINT0_FRONT);
	protected static Term term_AV_CH_LAYOUT_HEXAGONAL= new PrologSymbol(SymbolCodes.symbolCode_E_AV_CH_LAYOUT_HEXAGONAL);
	protected static Term term_AV_CH_LAYOUT_6POINT1= new PrologSymbol(SymbolCodes.symbolCode_E_AV_CH_LAYOUT_6POINT1);
	protected static Term term_AV_CH_LAYOUT_6POINT1_BACK= new PrologSymbol(SymbolCodes.symbolCode_E_AV_CH_LAYOUT_6POINT1_BACK);
	protected static Term term_AV_CH_LAYOUT_6POINT1_FRONT= new PrologSymbol(SymbolCodes.symbolCode_E_AV_CH_LAYOUT_6POINT1_FRONT);
	protected static Term term_AV_CH_LAYOUT_7POINT0= new PrologSymbol(SymbolCodes.symbolCode_E_AV_CH_LAYOUT_7POINT0);
	protected static Term term_AV_CH_LAYOUT_7POINT0_FRONT= new PrologSymbol(SymbolCodes.symbolCode_E_AV_CH_LAYOUT_7POINT0_FRONT);
	protected static Term term_AV_CH_LAYOUT_7POINT1= new PrologSymbol(SymbolCodes.symbolCode_E_AV_CH_LAYOUT_7POINT1);
	protected static Term term_AV_CH_LAYOUT_7POINT1_WIDE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_CH_LAYOUT_7POINT1_WIDE);
	protected static Term term_AV_CH_LAYOUT_7POINT1_WIDE_BACK= new PrologSymbol(SymbolCodes.symbolCode_E_AV_CH_LAYOUT_7POINT1_WIDE_BACK);
	protected static Term term_AV_CH_LAYOUT_OCTAGONAL= new PrologSymbol(SymbolCodes.symbolCode_E_AV_CH_LAYOUT_OCTAGONAL);
	protected static Term term_AV_CH_LAYOUT_HEXADECAGONAL= new PrologSymbol(SymbolCodes.symbolCode_E_AV_CH_LAYOUT_HEXADECAGONAL);
	protected static Term term_AV_CH_LAYOUT_STEREO_DOWNMIX= new PrologSymbol(SymbolCodes.symbolCode_E_AV_CH_LAYOUT_STEREO_DOWNMIX);
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public long toLong();
	abstract public Term toTerm();
}
