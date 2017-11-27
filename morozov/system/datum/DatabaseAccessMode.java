// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.datum;

import target.*;

import morozov.run.*;
import morozov.system.datum.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum DatabaseAccessMode {
	//
	READING {
		public void checkAuthority(DatabaseAccessMode mode) {
		}
		public Term toTerm() {
			return termReading;
		}
	},
	MODIFYING {
		public void checkAuthority(DatabaseAccessMode mode) {
			if (mode==DatabaseAccessMode.READING) {
				throw new ModifyingTransactionCannotBeInsideReadingTransaction();
			}
		}
		public Term toTerm() {
			return termModifying;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public void checkAuthority(DatabaseAccessMode mode);
	abstract public Term toTerm();
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termReading= new PrologSymbol(SymbolCodes.symbolCode_E_reading);
	protected static Term termModifying= new PrologSymbol(SymbolCodes.symbolCode_E_modifying);
	//
	///////////////////////////////////////////////////////////////
	//
	public static DatabaseAccessMode argumentToDatabaseAccessMode(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_reading) {
				return DatabaseAccessMode.READING;
			} else if (code==SymbolCodes.symbolCode_E_modifying) {
				return DatabaseAccessMode.MODIFYING;
			} else {
				throw new WrongArgumentIsNotDatabaseAccessMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotDatabaseAccessMode(value);
		}
	}
}
