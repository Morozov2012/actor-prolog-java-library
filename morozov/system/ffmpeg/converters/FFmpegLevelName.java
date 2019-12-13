// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avcodec;

import target.*;

import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum FFmpegLevelName {
	//
	FF_LEVEL_UNKNOWN {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_LEVEL_UNKNOWN;
		}
		@Override
		public Term toTerm() {
			return term_FF_LEVEL_UNKNOWN;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegLevelName argumentToFFmpegLevelName(Term value, ChoisePoint iX) throws TermIsNotFFmpegLevelName {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_FF_LEVEL_UNKNOWN) {
				return FFmpegLevelName.FF_LEVEL_UNKNOWN;
			} else {
				throw TermIsNotFFmpegLevelName.instance;
			}
		} catch (TermIsNotASymbol e) {
			throw TermIsNotFFmpegLevelName.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term term_FF_LEVEL_UNKNOWN= new PrologSymbol(SymbolCodes.symbolCode_E_FF_LEVEL_UNKNOWN);
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public int toInteger();
	abstract public Term toTerm();
}
