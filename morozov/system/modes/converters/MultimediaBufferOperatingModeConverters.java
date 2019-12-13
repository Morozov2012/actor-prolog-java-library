// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.modes.converters;

import target.*;

import morozov.run.*;
import morozov.system.modes.converters.errors.*;
import morozov.system.modes.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class MultimediaBufferOperatingModeConverters {
	//
	public static MultimediaBufferOperatingMode argumentToMultimediaBufferOperatingMode(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_RECORDING) {
				return MultimediaBufferOperatingMode.RECORDING;
			} else if (code==SymbolCodes.symbolCode_E_PLAYING) {
				return MultimediaBufferOperatingMode.PLAYING;
			} else if (code==SymbolCodes.symbolCode_E_READING) {
				return MultimediaBufferOperatingMode.READING;
			} else if (code==SymbolCodes.symbolCode_E_SPECULATIVE_READING) {
				return MultimediaBufferOperatingMode.SPECULATIVE_READING;
			} else {
				throw new WrongArgumentIsNotMultimediaBufferOperatingMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotMultimediaBufferOperatingMode(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termRecording= new PrologSymbol(SymbolCodes.symbolCode_E_RECORDING);
	protected static Term termPlaying= new PrologSymbol(SymbolCodes.symbolCode_E_PLAYING);
	protected static Term termReading= new PrologSymbol(SymbolCodes.symbolCode_E_READING);
	protected static Term termSpeculativeReading= new PrologSymbol(SymbolCodes.symbolCode_E_SPECULATIVE_READING);
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(MultimediaBufferOperatingMode mode) {
		switch (mode) {
		case RECORDING:
			return termRecording;
		case PLAYING:
			return termPlaying;
		case SPECULATIVE_READING:
			return termSpeculativeReading;
		case READING:
			return termReading;
		};
		throw new UnknownMultimediaBufferOperatingMode(mode);
	}
}
