// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.gui.reports;

import target.*;

import morozov.run.*;
import morozov.system.gui.reports.errors.*;
import morozov.system.gui.reports.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class ExtendedMaxLineNumber {
	//
	private boolean noLimit= true;
	private boolean windowHeight= false;
	private int value= 0;
	//
	protected static Term noLimitTerm= new PrologSymbol(SymbolCodes.symbolCode_E_no_limit);
	protected static Term windowHeightTerm= new PrologSymbol(SymbolCodes.symbolCode_E_window_height);
	//
	public ExtendedMaxLineNumber() {
	}
	public ExtendedMaxLineNumber(boolean nL, boolean wH) {
		noLimit= nL;
		windowHeight= wH;
	}
	public ExtendedMaxLineNumber(int v) {
		noLimit= false;
		windowHeight= false;
		value= v;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void useNoLimit() {
		noLimit= true;
	}
	//
	public boolean noLimit() {
		return noLimit;
	}
	//
	public void useWindowHeight() {
		windowHeight= true;
		noLimit= false;
	}
	//
	public boolean windowHeight() throws UseNoLimit {
		if (noLimit) {
			throw UseNoLimit.instance;
		} else {
			return windowHeight;
		}
	}
	//
	public void setValue(int v) {
		value= v;
		noLimit= false;
		windowHeight= false;
	}
	//
	public int getValue() throws UseNoLimit, UseWindowHeight {
		if (noLimit) {
			throw UseNoLimit.instance;
		} else if (windowHeight) {
			throw UseWindowHeight.instance;
		} else {
			return value;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static ExtendedMaxLineNumber termToExtendedMaxLineNumberSafe(Term value, ChoisePoint iX) {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			return new ExtendedMaxLineNumber();
		} else {
			try {
				return termToExtendedMaxLineNumber(value,iX);
			} catch (RuntimeException e) {
				return new ExtendedMaxLineNumber();
			}
		}
	}
	//
	public static ExtendedMaxLineNumber termToExtendedMaxLineNumber(Term value, ChoisePoint iX) {
		try {
			return new ExtendedMaxLineNumber(termToMaxLineNumber(value,iX));
		} catch (TermIsSymbolNoLimit e) {
			return new ExtendedMaxLineNumber();
		} catch (TermIsSymbolWindowHeight e) {
			return new ExtendedMaxLineNumber(false,true);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	// Auxiliary operations
	///////////////////////////////////////////////////////////////
	//
	protected static int termToMaxLineNumber(Term value, ChoisePoint iX) throws TermIsSymbolNoLimit, TermIsSymbolWindowHeight {
		try {
			return value.getSmallIntegerValue(iX);
		} catch (TermIsNotAnInteger e1) {
			try {
				long code= value.getSymbolValue(iX);
				if (code==SymbolCodes.symbolCode_E_no_limit) {
					throw TermIsSymbolNoLimit.instance;
				} else if (code==SymbolCodes.symbolCode_E_window_height) {
					throw TermIsSymbolWindowHeight.instance;
				} else {
					throw new WrongArgumentIsNotMaxLineNumber(value);
				}
			} catch (TermIsNotASymbol e2) {
				throw new WrongArgumentIsNotMaxLineNumber(value);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		if (noLimit) {
			return noLimitTerm;
		} else if (windowHeight) {
			return windowHeightTerm;
		} else {
			return new PrologInteger(value);
		}
	}
	//
	public String toString() {
		return "(" +
			String.format("%B,",noLimit) +
			String.format("%B,",windowHeight) +
			String.format("%s",value) + ")";
	}
}
