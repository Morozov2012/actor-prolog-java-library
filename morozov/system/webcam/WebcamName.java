// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.webcam;

import target.*;

import morozov.run.*;
import morozov.system.signals.*;
import morozov.system.webcam.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class WebcamName {
	//
	protected boolean isDefault= true;
	protected String name= "";
	//
	///////////////////////////////////////////////////////////////
	//
	public WebcamName() {
	}
	public WebcamName(String text) {
		name= text;
		isDefault= false;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public String getTextName() throws UseDefaultName {
		if (isDefault) {
			throw UseDefaultName.instance;
		} else {
			return name;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static String stringDefault= "default";
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	///////////////////////////////////////////////////////////////
	//
	public static WebcamName argumentToWebcamName(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_default) {
				return new WebcamName();
			} else {
				throw new WrongArgumentIsNotWebcamName(value);
			}
		} catch (TermIsNotASymbol e1) {
			try {
				String text= value.getStringValue(iX);
				return new WebcamName(text);
			} catch (TermIsNotAString e2) {
				throw new WrongArgumentIsNotWebcamName(value);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		if (isDefault) {
			return termDefault;
		} else {
			return new PrologString(name);
		}
	}
	//
	@Override
	public String toString() {
		if (isDefault) {
			return stringDefault;
		} else {
			return name;
		}
	}
}
