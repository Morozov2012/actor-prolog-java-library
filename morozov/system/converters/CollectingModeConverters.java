// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.converters;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class CollectingModeConverters {
	//
	public Term toTerm(CollectingMode mode) {
		switch (mode) {
		case SET:
			return termSet;
		case BAG:
			return termBag;
		default:
			throw new UnknownCollectingMode(mode);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static String stringSet= "set";
	protected static String stringBag= "bag";
	protected static Term termSet= new PrologSymbol(SymbolCodes.symbolCode_E_set);
	protected static Term termBag= new PrologSymbol(SymbolCodes.symbolCode_E_bag);
	//
	///////////////////////////////////////////////////////////////
	//
	public static CollectingMode argumentToCollectingMode(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_set) {
				return CollectingMode.SET;
			} else if (code==SymbolCodes.symbolCode_E_bag) {
				return CollectingMode.BAG;
			} else {
				throw new WrongArgumentIsNotCollectingMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotCollectingMode(value);
		}
	}
}
