// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avcodec;

import target.*;

import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum FFmpegMacroblockDecisionModeName {
	//
	FF_MB_DECISION_SIMPLE {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_MB_DECISION_SIMPLE;
		}
		@Override
		public Term toTerm() {
			return term_FF_MB_DECISION_SIMPLE;
		}
	},
	FF_MB_DECISION_BITS {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_MB_DECISION_BITS;
		}
		@Override
		public Term toTerm() {
			return term_FF_MB_DECISION_BITS;
		}
	},
	FF_MB_DECISION_RD {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_MB_DECISION_RD;
		}
		@Override
		public Term toTerm() {
			return term_FF_MB_DECISION_RD;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegMacroblockDecisionModeName argumentToFFmpegMacroblockDecisionModeName(Term value, ChoisePoint iX) throws TermIsNotFFmpegMacroblockDecisionModeName {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_FF_MB_DECISION_SIMPLE) {
				return FFmpegMacroblockDecisionModeName.FF_MB_DECISION_SIMPLE;
			} else if (code==SymbolCodes.symbolCode_E_FF_MB_DECISION_BITS) {
				return FFmpegMacroblockDecisionModeName.FF_MB_DECISION_BITS;
			} else if (code==SymbolCodes.symbolCode_E_FF_MB_DECISION_RD) {
				return FFmpegMacroblockDecisionModeName.FF_MB_DECISION_RD;
			} else {
				throw TermIsNotFFmpegMacroblockDecisionModeName.instance;
			}
		} catch (TermIsNotASymbol e) {
			throw TermIsNotFFmpegMacroblockDecisionModeName.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term term_FF_MB_DECISION_SIMPLE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_MB_DECISION_SIMPLE);
	protected static Term term_FF_MB_DECISION_BITS= new PrologSymbol(SymbolCodes.symbolCode_E_FF_MB_DECISION_BITS);
	protected static Term term_FF_MB_DECISION_RD= new PrologSymbol(SymbolCodes.symbolCode_E_FF_MB_DECISION_RD);
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public int toInteger();
	abstract public Term toTerm();
}
