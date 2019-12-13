// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avcodec;

import target.*;

import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum FFmpegThreadTypeName {
	//
	FF_THREAD_FRAME {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_THREAD_FRAME;
		}
		@Override
		public Term toTerm() {
			return term_FF_THREAD_FRAME;
		}
	},
	FF_THREAD_SLICE {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_THREAD_SLICE;
		}
		@Override
		public Term toTerm() {
			return term_FF_THREAD_SLICE;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegThreadTypeName argumentToFFmpegThreadTypeName(Term value, ChoisePoint iX) throws TermIsNotFFmpegThreadTypeName {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_FF_THREAD_FRAME) {
				return FFmpegThreadTypeName.FF_THREAD_FRAME;
			} else if (code==SymbolCodes.symbolCode_E_FF_THREAD_SLICE) {
				return FFmpegThreadTypeName.FF_THREAD_SLICE;
			} else {
				throw TermIsNotFFmpegThreadTypeName.instance;
			}
		} catch (TermIsNotASymbol e) {
			throw TermIsNotFFmpegThreadTypeName.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term term_FF_THREAD_FRAME= new PrologSymbol(SymbolCodes.symbolCode_E_FF_THREAD_FRAME);
	protected static Term term_FF_THREAD_SLICE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_THREAD_SLICE);
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public int toInteger();
	abstract public Term toTerm();
}
