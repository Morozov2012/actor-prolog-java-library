// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.gui;

import target.*;

import morozov.run.*;
import morozov.system.gui.errors.*;
import morozov.system.gui.signals.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class ExtendedTitle {
	//
	private boolean useDefaultTitle= true;
	private String value= "";
	//
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	public ExtendedTitle() {
	}
	public ExtendedTitle(String v) {
		useDefaultTitle= false;
		value= v;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void useDefaultTitle() {
		useDefaultTitle= true;
	}
	//
	public boolean isDefault() {
		return useDefaultTitle;
	}
	//
	public void setValue(String v) {
		value= v;
		useDefaultTitle= false;
	}
	//
	public String getValue() throws UseDefaultTitle {
		if (useDefaultTitle) {
			throw UseDefaultTitle.instance;
		} else {
			return value;
		}
	}
	//
	public String getValueOrDefaultText(String defaultText) {
		if (useDefaultTitle) {
			return defaultText;
		} else {
			return value;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static ExtendedTitle termToExtendedTitleSafe(Term value, ChoisePoint iX) {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			return new ExtendedTitle();
		} else {
			try {
				return termToExtendedTitle(value,iX);
			} catch (RuntimeException e) {
				return new ExtendedTitle();
			}
		}
	}
	//
	public static Term termToExtendedTitleOrFail(Term value, ChoisePoint iX) throws Backtracking {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable()) {
			throw Backtracking.instance;
		} else if (value.thisIsUnknownValue()) {
			return termDefault;
		} else {
			try {
				return new PrologString(termToTitle(value,iX));
			} catch (TermIsSymbolDefault e) {
				return termDefault;
			} catch (RuntimeException e) {
				return termDefault;
			}
		}
	}
	//
	public static ExtendedTitle termToExtendedTitle(Term value, ChoisePoint iX) {
		try {
			return new ExtendedTitle(termToTitle(value,iX));
		} catch (TermIsSymbolDefault e) {
			return new ExtendedTitle();
		}
	}
	//
	public static String termToTitleSafe(Term value, ChoisePoint iX) throws TermIsSymbolDefault {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			throw TermIsSymbolDefault.instance;
		} else {
			try {
				return termToTitle(value,iX);
			} catch (RuntimeException e) {
				throw TermIsSymbolDefault.instance;
			}
		}
	}
	//
	public static String termToTitle(Term value, ChoisePoint iX) throws TermIsSymbolDefault {
		try {
			return value.getStringValue(iX);
		} catch (TermIsNotAString e1) {
			try {
				long code= value.getSymbolValue(iX);
				if (code==SymbolCodes.symbolCode_E_default) {
					throw TermIsSymbolDefault.instance;
				} else {
					throw new WrongArgumentIsNotATitle(value);
				}
			} catch (TermIsNotASymbol e2) {
				throw new WrongArgumentIsNotATitle(value);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		if (useDefaultTitle) {
			return termDefault;
		} else {
			return new PrologString(value);
		}
	}
	//
	public String toString() {
		return "(" +
			String.format("%B,",useDefaultTitle) +
			String.format("%s",value) + ")";
	}
}
