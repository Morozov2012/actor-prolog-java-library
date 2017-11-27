// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.gui;

import target.*;

import morozov.run.*;
import morozov.system.gui.errors.*;
import morozov.system.gui.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.math.BigInteger;

public class ExtendedSize {
	//
	private boolean useDefaultSize= true;
	//
	private boolean useDoubleValue= false;
	private BigInteger integerValue;
	private double doubleValue= 0;
	//
	protected static String stringDefault= "default";
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	public ExtendedSize() {
	}
	public ExtendedSize(int v) {
		useDefaultSize= false;
		useDoubleValue= false;
		integerValue= BigInteger.valueOf(v);
	}
	public ExtendedSize(BigInteger v) {
		useDefaultSize= false;
		useDoubleValue= false;
		integerValue= v;
	}
	public ExtendedSize(double v) {
		useDefaultSize= false;
		useDoubleValue= true;
		doubleValue= v;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void useDefaultSize() {
		useDefaultSize= true;
	}
	//
	public boolean isDefault() {
		return useDefaultSize;
	}
	//
	public void setIntegerValue(BigInteger v) {
		useDoubleValue= false;
		integerValue= v;
		doubleValue= 0;
		useDefaultSize= false;
	}
	public void setDoubleValue(double v) {
		useDoubleValue= true;
		integerValue= null;
		doubleValue= v;
		useDefaultSize= false;
	}
	//
	public int getIntegerValue() throws UseDefaultSize {
		if (useDefaultSize) {
			throw UseDefaultSize.instance;
		} else {
			if (useDoubleValue) {
				return PrologInteger.toInteger(doubleValue);
			} else {
				return PrologInteger.toInteger(integerValue);
			}
		}
	}
	public double getDoubleValue() throws UseDefaultSize {
		if (useDefaultSize) {
			throw UseDefaultSize.instance;
		} else {
			if (useDoubleValue) {
				return doubleValue;
			} else {
				return integerValue.doubleValue();
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static ExtendedSize argumentToExtendedSizeSafe(Term value, ChoisePoint iX) {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			return new ExtendedSize();
		} else {
			try {
				return argumentToExtendedSize(value,iX);
			} catch (RuntimeException e) {
				return new ExtendedSize();
			}
		}
	}
	//
	public static ExtendedSize argumentToExtendedSize(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_default) {
				return new ExtendedSize();
			} else {
				throw new WrongArgumentIsNotASize(value);
			}
		} catch (TermIsNotASymbol e1) {
			try {
				return new ExtendedSize(value.getIntegerValue(iX));
			} catch (TermIsNotAnInteger e2) {
				try {
					return new ExtendedSize(value.getRealValue(iX));
				} catch (TermIsNotAReal e3) {
					throw new WrongArgumentIsNotASize(value);
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		if (useDefaultSize) {
			return termDefault;
		} else {
			if (useDoubleValue) {
				return new PrologReal(doubleValue);
			} else {
				return new PrologInteger(integerValue);
			}
		}
	}
	//
	public String toString() {
		if (useDefaultSize) {
			return stringDefault;
		} else {
			if (useDoubleValue) {
				return Double.toString(doubleValue);
			} else {
				return integerValue.toString();
			}
		}
	}
}
