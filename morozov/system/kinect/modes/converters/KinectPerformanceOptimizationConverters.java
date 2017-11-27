// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.converters;

import target.*;

import morozov.run.*;
import morozov.system.kinect.modes.*;
import morozov.system.kinect.modes.converters.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class KinectPerformanceOptimizationConverters {
	//
	public static KinectPerformanceOptimization argumentToKinectPerformanceOptimization(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_DATA_FLOW) {
				return KinectPerformanceOptimization.DATA_FLOW;
			} else if (code==SymbolCodes.symbolCode_E_OPERATION_SPEED) {
				return KinectPerformanceOptimization.OPERATION_SPEED;
			} else if (code==SymbolCodes.symbolCode_E_default) {
				return KinectPerformanceOptimization.DEFAULT;
			} else {
				throw new WrongArgumentIsNotKinectPerformanceOptimization(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotKinectPerformanceOptimization(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termDataFlow= new PrologSymbol(SymbolCodes.symbolCode_E_DATA_FLOW);
	protected static Term termOperationSpeed= new PrologSymbol(SymbolCodes.symbolCode_E_OPERATION_SPEED);
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(KinectPerformanceOptimization mode) {
		switch (mode) {
		case DATA_FLOW:
			return termDataFlow;
		case OPERATION_SPEED:
			return termOperationSpeed;
		case DEFAULT:
			return termDefault;
		};
		return termDefault;
	}
}
