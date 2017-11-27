// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg;

import target.*;

import morozov.run.*;
import morozov.system.ffmpeg.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum FFmpegOperatingMode {
	//
	PLAYING,
	READING,
	WRITING;
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegOperatingMode argumentToFFmpegOperatingMode(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_PLAYING) {
				return FFmpegOperatingMode.PLAYING;
			} else if (code==SymbolCodes.symbolCode_E_READING) {
				return FFmpegOperatingMode.READING;
			} else if (code==SymbolCodes.symbolCode_E_WRITING) {
				return FFmpegOperatingMode.WRITING;
			} else {
				throw new WrongArgumentIsNotFFmpegOperatingMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotFFmpegOperatingMode(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termPlaying= new PrologSymbol(SymbolCodes.symbolCode_E_PLAYING);
	protected static Term termReading= new PrologSymbol(SymbolCodes.symbolCode_E_READING);
	protected static Term termWriting= new PrologSymbol(SymbolCodes.symbolCode_E_WRITING);
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(FFmpegOperatingMode mode) {
		switch (mode) {
		case PLAYING:
			return termPlaying;
		case READING:
			return termReading;
		case WRITING:
			return termWriting;
		};
		throw new UnknownFFmpegOperatingMode(mode);
	}
}
