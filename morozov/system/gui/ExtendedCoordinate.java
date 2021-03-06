// (c) 2008-2010 IRE RAS Alexei A. Morozov

package morozov.system.gui;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.gui.errors.*;
import morozov.system.gui.signals.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.math.BigInteger;

public class ExtendedCoordinate {
	//
	protected boolean locateFigureInDefaultPosition= true;
	protected boolean locateFigureInTheCentre= false;
	//
	protected boolean useDoubleValue= false;
	protected BigInteger integerValue;
	protected double doubleValue= 0;
	//
	protected static String stringDefault= "default";
	protected static String stringCentered= "centered";
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	protected static Term termCentered= new PrologSymbol(SymbolCodes.symbolCode_E_centered);
	//
	public ExtendedCoordinate() {
	}
	public ExtendedCoordinate(BigInteger v) {
		locateFigureInDefaultPosition= false;
		locateFigureInTheCentre= false;
		useDoubleValue= false;
		integerValue= v;
	}
	public ExtendedCoordinate(double v) {
		locateFigureInDefaultPosition= false;
		locateFigureInTheCentre= false;
		useDoubleValue= true;
		doubleValue= v;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setUseDefaultLocation(boolean mode) {
		locateFigureInDefaultPosition= mode;
	}
	public void setUseDefaultLocation() {
		setUseDefaultLocation(true);
	}
	//
	public boolean isDefault() {
		return locateFigureInDefaultPosition;
	}
	//
	public void centreFigure() {
		locateFigureInTheCentre= true;
		locateFigureInDefaultPosition= false;
	}
	//
	public boolean isCentered() throws UseDefaultLocation {
		if (locateFigureInDefaultPosition) {
			throw UseDefaultLocation.instance;
		} else {
			return locateFigureInTheCentre;
		}
	}
	//
	public void setIntegerValue(BigInteger v) {
		useDoubleValue= false;
		doubleValue= 0;
		integerValue= v;
		locateFigureInDefaultPosition= false;
		locateFigureInTheCentre= false;
	}
	public void setDoubleValue(double v) {
		useDoubleValue= true;
		doubleValue= v;
		integerValue= null;
		locateFigureInDefaultPosition= false;
		locateFigureInTheCentre= false;
	}
	//
	public int getIntegerValue() throws UseDefaultLocation, CentreFigure {
		if (locateFigureInDefaultPosition) {
			throw UseDefaultLocation.instance;
		} else if (locateFigureInTheCentre) {
			throw CentreFigure.instance;
		} else {
			if (useDoubleValue) {
				return Arithmetic.toInteger(doubleValue);
			} else {
				return Arithmetic.toInteger(integerValue);
			}
		}
	}
	public double getDoubleValue() throws UseDefaultLocation, CentreFigure {
		if (locateFigureInDefaultPosition) {
			throw UseDefaultLocation.instance;
		} else if (locateFigureInTheCentre) {
			throw CentreFigure.instance;
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
	public static ExtendedCoordinate argumentToExtendedCoordinateSafe(Term value, ChoisePoint iX) {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			ExtendedCoordinate coordinate= new ExtendedCoordinate();
			coordinate.setUseDefaultLocation();
			return coordinate;
		} else {
			try {
				return argumentToExtendedCoordinate(value,iX);
			} catch (RuntimeException e) {
				ExtendedCoordinate coordinate= new ExtendedCoordinate();
				coordinate.setUseDefaultLocation();
				return coordinate;
			}
		}
	}
	//
	public static Term argumentToExtendedCoordinateOrFail(Term value, ChoisePoint iX) throws Backtracking {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable()) {
			throw Backtracking.instance;
		} else if (value.thisIsUnknownValue()) {
			return termDefault;
		} else {
			try {
				return argumentToExtendedCoordinate(value,iX).toTerm();
			} catch (RuntimeException e) {
				return termDefault;
			}
		}
	}
	//
	public static ExtendedCoordinate argumentToExtendedCoordinate(Term value, ChoisePoint iX) {
		ExtendedCoordinate coordinate= new ExtendedCoordinate();
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_default) {
				coordinate.setUseDefaultLocation();
				return coordinate;
			} else if (code==SymbolCodes.symbolCode_E_centered) {
				coordinate.centreFigure();
				return coordinate;
			} else {
				throw new WrongArgumentIsNotACoordinate(value);
			}
		} catch (TermIsNotASymbol e1) {
			try {
				coordinate.setIntegerValue(value.getIntegerValue(iX));
				return coordinate;
			} catch (TermIsNotAnInteger e2) {
				try {
					coordinate.setDoubleValue(value.getRealValue(iX));
					return coordinate;
				} catch (TermIsNotAReal e3) {
					throw new WrongArgumentIsNotACoordinate(value);
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term standardizeCoordinateValue(Term value, ChoisePoint iX) throws RejectValue {
		value= value.dereferenceValue(iX);
		if (value.thisIsUnknownValue()) {
			return termDefault;
		} else {
			try {
				long code= value.getSymbolValue(iX);
				if (code==SymbolCodes.symbolCode_E_default) {
					return new PrologSymbol(code);
				} else if (code==SymbolCodes.symbolCode_E_centered) {
					return new PrologSymbol(code);
				} else {
					throw RejectValue.instance;
				}
			} catch (TermIsNotASymbol e1) {
				try {
					double number= value.getIntegerValue(iX).doubleValue();
					return new PrologReal(number);
				} catch (TermIsNotAnInteger e2) {
					try {
						double number= value.getRealValue(iX);
						return new PrologReal(number);
					} catch (TermIsNotAReal e3) {
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
		if (locateFigureInDefaultPosition) {
			return termDefault;
		} else if (locateFigureInTheCentre) {
			return termCentered;
		} else {
			if (useDoubleValue) {
				return new PrologReal(doubleValue);
			} else {
				return new PrologInteger(integerValue);
			}
		}
	}
	//
	@Override
	public String toString() {
		if (locateFigureInDefaultPosition) {
			return stringDefault;
		} else if (locateFigureInTheCentre) {
			return stringCentered;
		} else {
			if (useDoubleValue) {
				return Double.toString(doubleValue);
			} else {
				return integerValue.toString();
			}
		}
	}
}
