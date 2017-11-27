// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.gui.reports;

import target.*;

import morozov.run.*;
import morozov.system.gui.reports.errors.*;
import morozov.system.gui.reports.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class ExtendedMaximalLineNumber {
	//
	private boolean noLimit= true;
	private boolean windowHeight= false;
	private int value= 0;
	//
	protected static Term noLimitTerm= new PrologSymbol(SymbolCodes.symbolCode_E_no_limit);
	protected static Term windowHeightTerm= new PrologSymbol(SymbolCodes.symbolCode_E_window_height);
	//
	public ExtendedMaximalLineNumber() {
	}
	public ExtendedMaximalLineNumber(boolean nL, boolean wH) {
		noLimit= nL;
		windowHeight= wH;
	}
	public ExtendedMaximalLineNumber(int v) {
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
	public static ExtendedMaximalLineNumber argumentToExtendedMaximalLineNumberSafe(Term value, ChoisePoint iX) {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			return new ExtendedMaximalLineNumber();
		} else {
			try {
				return argumentToExtendedMaximalLineNumber(value,iX);
			} catch (RuntimeException e) {
				return new ExtendedMaximalLineNumber();
			}
		}
	}
	//
	public static ExtendedMaximalLineNumber argumentToExtendedMaximalLineNumber(Term value, ChoisePoint iX) {
		try {
			return new ExtendedMaximalLineNumber(argumentToMaxLineNumber(value,iX));
		} catch (TermIsSymbolNoLimit e) {
			return new ExtendedMaximalLineNumber();
		} catch (TermIsSymbolWindowHeight e) {
			return new ExtendedMaximalLineNumber(false,true);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	// Auxiliary operations
	///////////////////////////////////////////////////////////////
	//
	protected static int argumentToMaxLineNumber(Term value, ChoisePoint iX) throws TermIsSymbolNoLimit, TermIsSymbolWindowHeight {
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
					throw new WrongArgumentIsNotMaximalLineNumber(value);
				}
			} catch (TermIsNotASymbol e2) {
				throw new WrongArgumentIsNotMaximalLineNumber(value);
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
