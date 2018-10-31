// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.modes.converters;

import target.*;

import morozov.run.*;
import morozov.system.modes.converters.errors.*;
import morozov.system.modes.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class VideoBufferOperatingModeConverters {
	//
	public static VideoBufferOperatingMode argumentToVideoBufferOperatingMode(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_RECORDING) {
				return VideoBufferOperatingMode.RECORDING;
			} else if (code==SymbolCodes.symbolCode_E_PLAYING) {
				return VideoBufferOperatingMode.PLAYING;
			} else if (code==SymbolCodes.symbolCode_E_READING) {
				return VideoBufferOperatingMode.READING;
			} else if (code==SymbolCodes.symbolCode_E_SPECULATIVE_READING) {
				return VideoBufferOperatingMode.SPECULATIVE_READING;
			} else if (code==SymbolCodes.symbolCode_E_LISTENING) {
				return VideoBufferOperatingMode.LISTENING;
			} else {
				throw new WrongArgumentIsNotVideoBufferOperatingMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotVideoBufferOperatingMode(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termRecording= new PrologSymbol(SymbolCodes.symbolCode_E_RECORDING);
	protected static Term termPlaying= new PrologSymbol(SymbolCodes.symbolCode_E_PLAYING);
	protected static Term termReading= new PrologSymbol(SymbolCodes.symbolCode_E_READING);
	protected static Term termSpeculativeReading= new PrologSymbol(SymbolCodes.symbolCode_E_SPECULATIVE_READING);
	protected static Term termListening= new PrologSymbol(SymbolCodes.symbolCode_E_LISTENING);
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(VideoBufferOperatingMode mode) {
		switch (mode) {
		case RECORDING:
			return termRecording;
		case PLAYING:
			return termPlaying;
		case SPECULATIVE_READING:
			return termSpeculativeReading;
		case READING:
			return termReading;
		case LISTENING:
			return termListening;
		};
		throw new UnknownVideoBufferOperatingMode(mode);
	}
}
