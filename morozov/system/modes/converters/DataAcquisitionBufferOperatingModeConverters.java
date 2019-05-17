// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.modes.converters;

import target.*;

import morozov.run.*;
import morozov.system.modes.converters.errors.*;
import morozov.system.modes.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class DataAcquisitionBufferOperatingModeConverters {
	//
	public static DataAcquisitionBufferOperatingMode argumentToDataAcquisitionBufferOperatingMode(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_RECORDING) {
				return DataAcquisitionBufferOperatingMode.RECORDING;
			} else if (code==SymbolCodes.symbolCode_E_PLAYING) {
				return DataAcquisitionBufferOperatingMode.PLAYING;
			} else if (code==SymbolCodes.symbolCode_E_READING) {
				return DataAcquisitionBufferOperatingMode.READING;
			} else if (code==SymbolCodes.symbolCode_E_SPECULATIVE_READING) {
				return DataAcquisitionBufferOperatingMode.SPECULATIVE_READING;
			} else if (code==SymbolCodes.symbolCode_E_LISTENING) {
				return DataAcquisitionBufferOperatingMode.LISTENING;
			// } else if (code==SymbolCodes.symbolCode_E_DISPLAYING) {
			//	return DataAcquisitionBufferOperatingMode.DISPLAYING;
			} else {
				throw new WrongArgumentIsNotDataAcquisitionBufferOperatingMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotDataAcquisitionBufferOperatingMode(value);
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
	// protected static Term termDisplaying= new PrologSymbol(SymbolCodes.symbolCode_E_DISPLAYING);
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(DataAcquisitionBufferOperatingMode mode) {
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
		// case DISPLAYING:
		//	return termDisplaying;
		};
		throw new UnknownDataAcquisitionBufferOperatingMode(mode);
	}
}
