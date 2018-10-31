// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.gui;

import target.*;

import morozov.run.*;
import morozov.system.gui.errors.*;
import morozov.system.gui.signals.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.math.BigInteger;

public class ExtendedFontSize {
	//
	private boolean useDefaultFontSize= true;
	private int value= 18;
	//
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	public ExtendedFontSize() {
	}
	public ExtendedFontSize(int v) {
		useDefaultFontSize= false;
		value= v;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void useDefaultFontSize() {
		useDefaultFontSize= true;
	}
	//
	public boolean isDefault() {
		return useDefaultFontSize;
	}
	//
	public void setFontSize(int v) {
		value= v;
		useDefaultFontSize= false;
	}
	//
	public int getValue() throws UseDefaultFontSize {
		if (useDefaultFontSize) {
			throw UseDefaultFontSize.instance;
		} else {
			return value;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static ExtendedFontSize argumentToExtendedFontSizeSafe(Term value, ChoisePoint iX) {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			return new ExtendedFontSize();
		} else {
			try {
				return argumentToExtendedFontSize(value,iX);
			} catch (RuntimeException e) {
				return new ExtendedFontSize();
			}
		}
	}
	//
	public static Term argumentToExtendedFontSizeOrFail(Term value, ChoisePoint iX) throws Backtracking {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable()) {
			throw Backtracking.instance;
		} if(value.thisIsUnknownValue()) {
			return termDefault;
		} else {
			try {
				return new PrologInteger(argumentToFontSize(value,iX));
			} catch (TermIsSymbolDefault e) {
				return termDefault;
			} catch (RuntimeException e) {
				return termDefault;
			}
		}
	}
	//
	public static ExtendedFontSize argumentToExtendedFontSize(Term value, ChoisePoint iX) {
		try {
			return new ExtendedFontSize(argumentToFontSize(value,iX));
		} catch (TermIsSymbolDefault e) {
			return new ExtendedFontSize();
		}
	}
	//
	public static int argumentToFontSizeSafe(Term value, ChoisePoint iX) throws TermIsSymbolDefault {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			throw TermIsSymbolDefault.instance;
		} else {
			try {
				return argumentToFontSize(value,iX);
			} catch (RuntimeException e) {
				throw TermIsSymbolDefault.instance;
			}
		}
	}
	//
	public static int argumentToFontSize(Term value, ChoisePoint iX) throws TermIsSymbolDefault {
		try {
			return value.getIntegerValue(iX).intValue();
		} catch (TermIsNotAnInteger e1) {
			try {
				return PrologInteger.toInteger(value.getRealValue(iX));
			} catch (TermIsNotAReal e2) {
				try {
					long code= value.getSymbolValue(iX);
					if (code==SymbolCodes.symbolCode_E_default) {
						throw TermIsSymbolDefault.instance;
					} else {
						throw new WrongArgumentIsNotFontSize(value);
					}
				} catch (TermIsNotASymbol e3) {
					throw new WrongArgumentIsNotFontSize(value);
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term standardizeFontSizeValue(Term value, ChoisePoint iX) throws RejectValue {
		value= value.dereferenceValue(iX);
		if (value.thisIsUnknownValue()) {
			return termDefault;
		} else {
			try {
				BigInteger number= value.getIntegerValue(iX);
				return new PrologInteger(number);
			} catch (TermIsNotAnInteger e1) {
				try {
					double number= value.getRealValue(iX);
					// BigInteger bigInteger= GeneralConverters.doubleToBigInteger(number);
					// return new PrologReal(number);
					return new PrologInteger((long)StrictMath.round(number));
				} catch (TermIsNotAReal e2) {
					try {
						long code= value.getSymbolValue(iX);
						if (code==SymbolCodes.symbolCode_E_default) {
							return new PrologSymbol(code);
						} else {
							throw RejectValue.instance;
						}
					} catch (TermIsNotASymbol e3) {
						throw RejectValue.instance;
					}
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		if (useDefaultFontSize) {
			return termDefault;
		} else {
			return new PrologInteger(value);
		}
	}
	//
	public String toString() {
		return "(" +
			String.format("%B,",useDefaultFontSize) +
			String.format("%s",value) + ")";
	}
}
