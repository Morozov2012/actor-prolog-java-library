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

public class IntegerAttribute implements Serializable {
	//
	protected boolean useDefaultValue;
	protected long value;
	//
	private static final long serialVersionUID= 1;
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system","IntegerAttribute");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public IntegerAttribute(long n) {
		useDefaultValue= false;
		value= n;
	}
	public IntegerAttribute() {
		useDefaultValue= true;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean getUseDefaultValue() {
		return useDefaultValue;
	}
	public long getValue() throws UseDefaultValue {
		return value;
	}
	public long getValue(long defaultValue) {
		if (useDefaultValue) {
			return defaultValue;
		} else {
			return value;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static IntegerAttribute argumentToIntegerAttribute(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_default) {
				return new IntegerAttribute();
			} else {
				throw new WrongArgumentIsNotIntegerAttribute(value);
			}
		} catch (TermIsNotASymbol e) {
			long number= GeneralConverters.argumentToLongInteger(value,iX);
			return new IntegerAttribute(number);
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
			return new PrologInteger(getValue());
		} catch (UseDefaultValue e) {
			return termDefault;
		}
	}
}
