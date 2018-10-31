// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system;

import target.*;

import morozov.run.*;
import morozov.system.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum TemperatureScale {
	//
	CELSIUS {
		public boolean isCelsius() {
			return true;
		}
		public Term toTerm() {
			return termCelsius;
		}
	},
	FAHRENHEIT {
		public boolean isCelsius() {
			return false;
		}
		public Term toTerm() {
			return termFahrenheit;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public boolean isCelsius();
	abstract public Term toTerm();
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termCelsius= new PrologSymbol(SymbolCodes.symbolCode_E_CELSIUS);
	protected static Term termFahrenheit= new PrologSymbol(SymbolCodes.symbolCode_E_FAHRENHEIT);
	//
	///////////////////////////////////////////////////////////////
	//
	public static TemperatureScale argumentToTemperatureScale(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_CELSIUS) {
				return TemperatureScale.CELSIUS;
			} else if (code==SymbolCodes.symbolCode_E_FAHRENHEIT) {
				return TemperatureScale.FAHRENHEIT;
			} else {
				throw new WrongArgumentIsNotTemperatureScale(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotTemperatureScale(value);
		}
	}
}
