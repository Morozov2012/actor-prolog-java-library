// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system;

import target.*;

import morozov.run.*;
import morozov.system.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum CollectingMode {
	//
	SET {
		public boolean toBoolean() {
			return true;
		}
		public Term toTerm() {
			return termSet;
		}
	},
	BAG {
		public boolean toBoolean() {
			return false;
		}
		public Term toTerm() {
			return termBag;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public boolean toBoolean();
	abstract public Term toTerm();
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
