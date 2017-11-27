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

public class KinectTextAttributeConverters {
	//
	public static KinectTextAttribute argumentToKinectTextAttribute(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_default) {
				return new KinectTextAttribute();
			} else {
				throw new WrongArgumentIsNotKinectTextAttribute(value);
			}
		} catch (TermIsNotASymbol e) {
			String text= Converters.argumentToString(value,iX);
			return new KinectTextAttribute(text);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(KinectTextAttributeInterface attribute) {
		try {
			return new PrologString(attribute.getValue());
		} catch (UseDefaultValue e) {
			return termDefault;
		}
	}
}
