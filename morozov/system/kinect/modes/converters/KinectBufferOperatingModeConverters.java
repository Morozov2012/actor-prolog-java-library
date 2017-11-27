// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.converters;

import target.*;

import morozov.run.*;
import morozov.system.kinect.modes.converters.errors.*;
import morozov.system.kinect.modes.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class KinectBufferOperatingModeConverters {
	//
	public static KinectBufferOperatingMode argumentToKinectBufferOperatingMode(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_RECORDING) {
				return KinectBufferOperatingMode.RECORDING;
			} else if (code==SymbolCodes.symbolCode_E_PLAYING) {
				return KinectBufferOperatingMode.PLAYING;
			} else if (code==SymbolCodes.symbolCode_E_READING) {
				return KinectBufferOperatingMode.READING;
			} else if (code==SymbolCodes.symbolCode_E_LISTENING) {
				return KinectBufferOperatingMode.LISTENING;
			} else {
				throw new WrongArgumentIsNotKinectBufferOperatingMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotKinectBufferOperatingMode(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termRecording= new PrologSymbol(SymbolCodes.symbolCode_E_RECORDING);
	protected static Term termPlaying= new PrologSymbol(SymbolCodes.symbolCode_E_PLAYING);
	protected static Term termReading= new PrologSymbol(SymbolCodes.symbolCode_E_READING);
	protected static Term termListening= new PrologSymbol(SymbolCodes.symbolCode_E_LISTENING);
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(KinectBufferOperatingMode mode) {
		switch (mode) {
		case RECORDING:
			return termRecording;
		case PLAYING:
			return termPlaying;
		case READING:
			return termReading;
		case LISTENING:
			return termListening;
		};
		throw new UnknownKinectBufferOperatingMode(mode);
	}
}
