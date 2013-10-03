// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.records;

import target.*;

import morozov.run.*;
import morozov.system.records.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class DatabaseUtils {
	public static long termToSortingKey(Term targetKey, ChoisePoint iX) {
		try {
			long code= targetKey.getSymbolValue(iX);
			return - code;
		} catch (TermIsNotASymbol e1) {
				try {
					return targetKey.getLongIntegerValue(iX);
			} catch (TermIsNotAnInteger e2) {
				try {
					String text= targetKey.getStringValue(iX);
					long code= SymbolNames.insertSymbolName(text);
					return - code;
				} catch (TermIsNotAString e3) {
					throw new WrongArgumentIsNotSortingKey();
				}
			}
		}
	}
	public static Term extractMapKey(Term pair, ChoisePoint iX) {
		try {
			long functor= pair.getStructureFunctor(iX);
			// System.out.printf("DatabaseUtils::functor::%s\n",functor);
			if (functor==SymbolCodes.symbolCode_E_map) {
				Term[] arguments= pair.getStructureArguments(iX);
				if (arguments.length == 2) {
					// System.out.printf("DatabaseUtils::arguments.length::%s\n",arguments.length);
					return arguments[0];
				} else {
					throw new WrongArgumentIsNotMapStructure();
				}
			} else {
				throw new WrongArgumentIsNotMapStructure();
			}
		} catch (TermIsNotAStructure e) {
			throw new WrongArgumentIsNotMapStructure();
		}
	}
	public static Term extractSecondArgument(Term pair, ChoisePoint iX) {
		try {
			Term[] arguments= pair.getStructureArguments(iX);
			return arguments[1];
		} catch (TermIsNotAStructure e) {
			throw new WrongArgumentIsNotMapStructure();
		}
	}
}
