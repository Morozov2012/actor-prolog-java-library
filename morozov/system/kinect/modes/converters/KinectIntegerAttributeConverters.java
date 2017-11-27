// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.converters;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.kinect.modes.*;
import morozov.system.kinect.modes.converters.errors.*;
import morozov.system.kinect.modes.interfaces.*;
import morozov.system.kinect.modes.interfaces.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class KinectIntegerAttributeConverters {
	//
	public static KinectIntegerAttribute argumentToKinectIntegerAttribute(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_default) {
				return new KinectIntegerAttribute();
			} else {
				throw new WrongArgumentIsNotKinectIntegerAttribute(value);
			}
		} catch (TermIsNotASymbol e) {
			long number= Converters.argumentToLongInteger(value,iX);
			return new KinectIntegerAttribute(number);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(KinectIntegerAttributeInterface attribute) {
		try {
			return new PrologInteger(attribute.getValue());
		} catch (UseDefaultValue e) {
			return termDefault;
		}
	}
}
