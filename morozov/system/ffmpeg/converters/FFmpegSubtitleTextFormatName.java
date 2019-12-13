// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avcodec;

import target.*;

import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum FFmpegSubtitleTextFormatName {
	//
	FF_SUB_TEXT_FMT_ASS {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_SUB_TEXT_FMT_ASS;
		}
		@Override
		public Term toTerm() {
			return term_FF_SUB_TEXT_FMT_ASS;
		}
	},
	FF_SUB_TEXT_FMT_ASS_WITH_TIMINGS {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_SUB_TEXT_FMT_ASS_WITH_TIMINGS;
		}
		@Override
		public Term toTerm() {
			return term_FF_SUB_TEXT_FMT_ASS_WITH_TIMINGS;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegSubtitleTextFormatName argumentToFFmpegSubtitleTextFormatName(Term value, ChoisePoint iX) throws TermIsNotFFmpegSubtitleTextFormatName {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_FF_SUB_TEXT_FMT_ASS) {
				return FFmpegSubtitleTextFormatName.FF_SUB_TEXT_FMT_ASS;
			} else if (code==SymbolCodes.symbolCode_E_FF_SUB_TEXT_FMT_ASS_WITH_TIMINGS) {
				return FFmpegSubtitleTextFormatName.FF_SUB_TEXT_FMT_ASS_WITH_TIMINGS;
			} else {
				throw TermIsNotFFmpegSubtitleTextFormatName.instance;
			}
		} catch (TermIsNotASymbol e) {
			throw TermIsNotFFmpegSubtitleTextFormatName.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term term_FF_SUB_TEXT_FMT_ASS= new PrologSymbol(SymbolCodes.symbolCode_E_FF_SUB_TEXT_FMT_ASS);
	protected static Term term_FF_SUB_TEXT_FMT_ASS_WITH_TIMINGS= new PrologSymbol(SymbolCodes.symbolCode_E_FF_SUB_TEXT_FMT_ASS_WITH_TIMINGS);
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public int toInteger();
	abstract public Term toTerm();
}
