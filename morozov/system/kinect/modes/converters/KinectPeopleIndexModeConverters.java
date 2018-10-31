// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.converters;

import target.*;

import morozov.run.*;
import morozov.system.kinect.modes.*;
import morozov.system.kinect.modes.converters.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class KinectPeopleIndexModeConverters {
	//
	public static KinectPeopleIndexMode argumentToKinectPeopleIndexMode(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_TINCTURE_PEOPLE) {
				return KinectPeopleIndexMode.TINCTURE_PEOPLE;
			} else if (code==SymbolCodes.symbolCode_E_PAINT_PEOPLE) {
				return KinectPeopleIndexMode.PAINT_PEOPLE;
			} else if (code==SymbolCodes.symbolCode_E_EXTRACT_PEOPLE) {
				return KinectPeopleIndexMode.EXTRACT_PEOPLE;
			} else if (code==SymbolCodes.symbolCode_E_ADAPTIVELY_EXTRACT_PEOPLE) {
				return KinectPeopleIndexMode.ADAPTIVELY_EXTRACT_PEOPLE;
			} else if (code==SymbolCodes.symbolCode_E_PROJECT_PEOPLE) {
				return KinectPeopleIndexMode.PROJECT_PEOPLE;
			} else if (code==SymbolCodes.symbolCode_E_NONE) {
				return KinectPeopleIndexMode.NONE;
			} else {
				throw new WrongArgumentIsNotKinectPeopleIndexMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotKinectPeopleIndexMode(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termTincture= new PrologSymbol(SymbolCodes.symbolCode_E_TINCTURE_PEOPLE);
	protected static Term termPaint= new PrologSymbol(SymbolCodes.symbolCode_E_PAINT_PEOPLE);
	protected static Term termExtract= new PrologSymbol(SymbolCodes.symbolCode_E_EXTRACT_PEOPLE);
	protected static Term termAdaptivelyExtract= new PrologSymbol(SymbolCodes.symbolCode_E_ADAPTIVELY_EXTRACT_PEOPLE);
	protected static Term termProject= new PrologSymbol(SymbolCodes.symbolCode_E_PROJECT_PEOPLE);
	protected static Term termNone= new PrologSymbol(SymbolCodes.symbolCode_E_NONE);
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(KinectPeopleIndexMode mode) {
		switch (mode) {
		case TINCTURE_PEOPLE:
			return termTincture;
		case PAINT_PEOPLE:
			return termPaint;
		case EXTRACT_PEOPLE:
			return termExtract;
		case ADAPTIVELY_EXTRACT_PEOPLE:
			return termAdaptivelyExtract;
		case PROJECT_PEOPLE:
			return termProject;
		case NONE:
			return termNone;
		};
		return termNone;
	}
}
