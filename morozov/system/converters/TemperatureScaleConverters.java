// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.converters;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class TemperatureScaleConverters {
	//
	public static Term toTerm(TemperatureScale scale) {
		switch (scale) {
		case CELSIUS:
			return termCelsius;
		case FAHRENHEIT:
			return termFahrenheit;
		default:
			throw new UnknownTemperatureScale(scale);
		}
	}
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
