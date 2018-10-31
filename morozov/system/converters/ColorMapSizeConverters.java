// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.converters;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.errors.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class ColorMapSizeConverters {
	//
	public static ColorMapSize argumentToColorMapSize(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_default) {
				return new ColorMapSize();
			} else {
				throw new WrongArgumentIsNotColorMapSize(value);
			}
		} catch (TermIsNotASymbol e1) {
			int size= GeneralConverters.argumentToSmallInteger(value,iX);
			return new ColorMapSize(size);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(ColorMapSize colorMapSize) {
		try {
			return new PrologInteger(colorMapSize.getValue());
		} catch (UseDefaultSize e) {
			return termDefault;
		}
	}
}
