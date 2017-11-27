// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.converters;

import edu.ufl.digitalworlds.j4k.J4KSDK;

import target.*;

import morozov.run.*;
import morozov.system.kinect.frames.*;
import morozov.system.kinect.frames.tools.*;
import morozov.system.kinect.modes.*;
import morozov.system.kinect.modes.converters.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class KinectFrameTypeConverters {
	//
	public static KinectFrameType argumentToKinectFrameType(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_DEPTH_MAPS) {
				return KinectFrameType.DEPTH_MAPS;
			} else if (code==SymbolCodes.symbolCode_E_COLORED_DEPTH_MAPS) {
				return KinectFrameType.COLORED_DEPTH_MAPS;
			} else if (code==SymbolCodes.symbolCode_E_INFRARED) {
				return KinectFrameType.INFRARED;
			} else if (code==SymbolCodes.symbolCode_E_LONG_EXPOSURE_INFRARED) {
				return KinectFrameType.LONG_EXPOSURE_INFRARED;
			} else if (code==SymbolCodes.symbolCode_E_MAPPED_COLOR) {
				return KinectFrameType.MAPPED_COLOR;
			} else if (code==SymbolCodes.symbolCode_E_POINT_CLOUDS) {
				return KinectFrameType.POINT_CLOUDS;
			} else if (code==SymbolCodes.symbolCode_E_COLOR) {
				return KinectFrameType.COLOR;
			} else if (code==SymbolCodes.symbolCode_E_DEVICE_TUNING) {
				return KinectFrameType.DEVICE_TUNING;
			} else if (code==SymbolCodes.symbolCode_E_NONE) {
				return KinectFrameType.NONE;
			} else {
				throw new WrongArgumentIsNotKinectFrameType(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotKinectFrameType(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termDepthMaps= new PrologSymbol(SymbolCodes.symbolCode_E_DEPTH_MAPS);
	protected static Term termColoredDepthMaps= new PrologSymbol(SymbolCodes.symbolCode_E_COLORED_DEPTH_MAPS);
	protected static Term termInfrared= new PrologSymbol(SymbolCodes.symbolCode_E_INFRARED);
	protected static Term termLongExposureInfrared= new PrologSymbol(SymbolCodes.symbolCode_E_LONG_EXPOSURE_INFRARED);
	protected static Term termMappedColor= new PrologSymbol(SymbolCodes.symbolCode_E_MAPPED_COLOR);
	protected static Term termPointClouds= new PrologSymbol(SymbolCodes.symbolCode_E_POINT_CLOUDS);
	protected static Term termColor= new PrologSymbol(SymbolCodes.symbolCode_E_COLOR);
	protected static Term termDeviceTuning= new PrologSymbol(SymbolCodes.symbolCode_E_DEVICE_TUNING);
	protected static Term termNone= new PrologSymbol(SymbolCodes.symbolCode_E_NONE);
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(KinectFrameType type) {
		switch (type) {
		case DEPTH_MAPS:
			return termDepthMaps;
		case COLORED_DEPTH_MAPS:
			return termColoredDepthMaps;
		case INFRARED:
			return termInfrared;
		case LONG_EXPOSURE_INFRARED:
			return termLongExposureInfrared;
		case MAPPED_COLOR:
			return termMappedColor;
		case POINT_CLOUDS:
			return termPointClouds;
		case COLOR:
			return termColor;
		case DEVICE_TUNING:
			return termDeviceTuning;
		case NONE:
			return termNone;
		};
		return termNone;
	}
}
