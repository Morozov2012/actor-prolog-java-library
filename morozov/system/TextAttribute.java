// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system;

import target.*;

import morozov.run.*;
import morozov.system.converters.*;
import morozov.system.errors.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.io.Serializable;

public class TextAttribute implements Serializable {
	//
	protected boolean useDefaultValue;
	protected String value;
	//
	private static final long serialVersionUID= 1;
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system","TextAttribute");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public TextAttribute(String s) {
		useDefaultValue= false;
		value= s;
	}
	public TextAttribute() {
		useDefaultValue= true;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean getUseDefaultValue() {
		return useDefaultValue;
	}
	public String getValue() throws UseDefaultValue {
		return value;
	}
	public String getValue(String defaultValue) {
		if (useDefaultValue) {
			return defaultValue;
		} else {
			return value;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static TextAttribute argumentToTextAttribute(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_default) {
				return new TextAttribute();
			} else {
				throw new WrongArgumentIsNotTextAttribute(value);
			}
		} catch (TermIsNotASymbol e) {
			String text= GeneralConverters.argumentToString(value,iX);
			return new TextAttribute(text);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		try {
			return new PrologString(getValue());
		} catch (UseDefaultValue e) {
			return termDefault;
		}
	}
}
