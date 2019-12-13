// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avcodec;

import target.*;

import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum FFmpegStandardComplianceName {
	//
	FF_COMPLIANCE_VERY_STRICT {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_COMPLIANCE_VERY_STRICT;
		}
		@Override
		public Term toTerm() {
			return term_FF_COMPLIANCE_VERY_STRICT;
		}
	},
	FF_COMPLIANCE_STRICT {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_COMPLIANCE_STRICT;
		}
		@Override
		public Term toTerm() {
			return term_FF_COMPLIANCE_STRICT;
		}
	},
	FF_COMPLIANCE_NORMAL {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_COMPLIANCE_NORMAL;
		}
		@Override
		public Term toTerm() {
			return term_FF_COMPLIANCE_NORMAL;
		}
	},
	FF_COMPLIANCE_UNOFFICIAL {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_COMPLIANCE_UNOFFICIAL;
		}
		@Override
		public Term toTerm() {
			return term_FF_COMPLIANCE_UNOFFICIAL;
		}
	},
	FF_COMPLIANCE_EXPERIMENTAL {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_COMPLIANCE_EXPERIMENTAL;
		}
		@Override
		public Term toTerm() {
			return term_FF_COMPLIANCE_EXPERIMENTAL;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegStandardComplianceName argumentToFFmpegStandardComplianceName(Term value, ChoisePoint iX) throws TermIsNotFFmpegStandardComplianceName {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_FF_COMPLIANCE_VERY_STRICT) {
				return FFmpegStandardComplianceName.FF_COMPLIANCE_VERY_STRICT;
			} else if (code==SymbolCodes.symbolCode_E_FF_COMPLIANCE_STRICT) {
				return FFmpegStandardComplianceName.FF_COMPLIANCE_STRICT;
			} else if (code==SymbolCodes.symbolCode_E_FF_COMPLIANCE_NORMAL) {
				return FFmpegStandardComplianceName.FF_COMPLIANCE_NORMAL;
			} else if (code==SymbolCodes.symbolCode_E_FF_COMPLIANCE_UNOFFICIAL) {
				return FFmpegStandardComplianceName.FF_COMPLIANCE_UNOFFICIAL;
			} else if (code==SymbolCodes.symbolCode_E_FF_COMPLIANCE_EXPERIMENTAL) {
				return FFmpegStandardComplianceName.FF_COMPLIANCE_EXPERIMENTAL;
			} else {
				throw TermIsNotFFmpegStandardComplianceName.instance;
			}
		} catch (TermIsNotASymbol e) {
			throw TermIsNotFFmpegStandardComplianceName.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term term_FF_COMPLIANCE_VERY_STRICT= new PrologSymbol(SymbolCodes.symbolCode_E_FF_COMPLIANCE_VERY_STRICT);
	protected static Term term_FF_COMPLIANCE_STRICT= new PrologSymbol(SymbolCodes.symbolCode_E_FF_COMPLIANCE_STRICT);
	protected static Term term_FF_COMPLIANCE_NORMAL= new PrologSymbol(SymbolCodes.symbolCode_E_FF_COMPLIANCE_NORMAL);
	protected static Term term_FF_COMPLIANCE_UNOFFICIAL= new PrologSymbol(SymbolCodes.symbolCode_E_FF_COMPLIANCE_UNOFFICIAL);
	protected static Term term_FF_COMPLIANCE_EXPERIMENTAL= new PrologSymbol(SymbolCodes.symbolCode_E_FF_COMPLIANCE_EXPERIMENTAL);
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public int toInteger();
	abstract public Term toTerm();
}
