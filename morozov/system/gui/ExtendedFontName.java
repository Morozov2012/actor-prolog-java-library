// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.gui;

import target.*;

import morozov.run.*;
import morozov.system.gui.errors.*;
import morozov.system.gui.signals.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.awt.Font;

public class ExtendedFontName {
	//
	private boolean useDefaultFontName= true;
	private String value= Font.MONOSPACED;
	//
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	public ExtendedFontName() {
	}
	public ExtendedFontName(String v) {
		useDefaultFontName= false;
		value= v;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void useDefaultFontName() {
		useDefaultFontName= true;
	}
	//
	public boolean isDefault() {
		return useDefaultFontName;
	}
	//
	public void setFontName(String v) {
		value= v;
		useDefaultFontName= false;
	}
	//
	public String getValue() throws UseDefaultFontName {
		if (useDefaultFontName) {
			throw UseDefaultFontName.instance;
		} else {
			return value;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static ExtendedFontName argumentToExtendedFontNameSafe(Term value, ChoisePoint iX) {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			return new ExtendedFontName();
		} else {
			try {
				return argumentToExtendedFontName(value,iX);
			} catch (RuntimeException e) {
				return new ExtendedFontName();
			}
		}
	}
	//
	public static Term argumentToExtendedFontNameOrFail(Term value, ChoisePoint iX) throws Backtracking {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable()) {
			throw Backtracking.instance;
		} else if (value.thisIsUnknownValue()) {
			return termDefault;
		} else {
			try {
				return fontNameToTerm(argumentToFontName(value,iX));
			} catch (TermIsSymbolDefault e) {
				return termDefault;
			} catch (RuntimeException e) {
				return termDefault;
			}
		}
	}
	//
	public static ExtendedFontName argumentToExtendedFontName(Term value, ChoisePoint iX) {
		try {
			return new ExtendedFontName(argumentToFontName(value,iX));
		} catch (TermIsSymbolDefault e) {
			return new ExtendedFontName();
		}
	}
	//
	public static String argumentToFontNameSafe(Term value, ChoisePoint iX) throws TermIsSymbolDefault {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			throw TermIsSymbolDefault.instance;
		} else {
			try {
				return argumentToFontName(value,iX);
			} catch (RuntimeException e) {
				throw TermIsSymbolDefault.instance;
			}
		}
	}
	//
	public static String argumentToFontName(Term value, ChoisePoint iX) throws TermIsSymbolDefault {
		try {
			long code= value.getSymbolValue(iX);
			try {
				return symbolCodeToFontName(code);
			} catch (IsNotFontNameSymbolCode e1) {
				throw new WrongArgumentIsNotFontName(value);
			}
		} catch (TermIsNotASymbol e1) {
			try {
				return value.getStringValue(iX);
			} catch (TermIsNotAString e2) {
				throw new WrongArgumentIsNotFontName(value);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term standardizeFontNameValue(Term value, ChoisePoint iX) throws RejectValue {
		value= value.dereferenceValue(iX);
		if (value.thisIsUnknownValue()) {
			return termDefault;
		} else {
			try {
				long code= value.getSymbolValue(iX);
				try {
					symbolCodeToFontName(code);
					return new PrologSymbol(code);
				} catch (TermIsSymbolDefault e1) {
					return termDefault;
				} catch (IsNotFontNameSymbolCode e1) {
					throw RejectValue.instance;
				}
			} catch (TermIsNotASymbol e1) {
				try {
					String fontName= value.getStringValue(iX);
					return new PrologString(fontName);
				} catch (TermIsNotAString e4) {
					throw RejectValue.instance;
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	// Auxiliary operations
	///////////////////////////////////////////////////////////////
	//
	protected static String symbolCodeToFontName(long code) throws TermIsSymbolDefault, IsNotFontNameSymbolCode {
		if (code==SymbolCodes.symbolCode_E_default) {
			throw TermIsSymbolDefault.instance;
		} else if (code==SymbolCodes.symbolCode_E_system) {
			return Font.DIALOG;
		} else if (code==SymbolCodes.symbolCode_E_fixed) {
			return Font.MONOSPACED;
		} else if (code==SymbolCodes.symbolCode_E_times) {
			return Font.SERIF;
		} else if (code==SymbolCodes.symbolCode_E_helvetica) {
			return Font.SANS_SERIF;
		} else {
			throw IsNotFontNameSymbolCode.instance;
		}
	}
	//
	protected static Term fontNameToTerm(String value) {
		int code= 0;
		boolean isStandardFontName= true;
		if (value.equals(Font.DIALOG)) {
			code= SymbolCodes.symbolCode_E_system;
		} else if (value.equals(Font.MONOSPACED)) {
			code= SymbolCodes.symbolCode_E_fixed;
		} else if (value.equals(Font.SERIF)) {
			code= SymbolCodes.symbolCode_E_times;
		} else if (value.equals(Font.SANS_SERIF)) {
			code= SymbolCodes.symbolCode_E_helvetica;
		} else {
			isStandardFontName= false;
		};
		if (isStandardFontName) {
			return new PrologSymbol(code);
		} else {
			return new PrologString(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		if (useDefaultFontName) {
			return termDefault;
		} else {
			return fontNameToTerm(value);
		}
	}
	//
	public String toString() {
		return "(" +
			String.format("%B,",useDefaultFontName) +
			String.format("%s",value) + ")";
	}
}
