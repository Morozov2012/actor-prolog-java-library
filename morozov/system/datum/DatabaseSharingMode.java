// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.datum;

import target.*;

import morozov.run.*;
import morozov.system.datum.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum DatabaseSharingMode {
	//
	EXCLUSIVE_ACCESS {
		public Term toTerm() {
			return termExclusiveAccess;
		}
	},
	EXCLUSIVE_WRITING {
		public Term toTerm() {
			return termExclusiveWriting;
		}
	},
	SHARED_ACCESS {
		public Term toTerm() {
			return termSharedAccess;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termExclusiveAccess= new PrologSymbol(SymbolCodes.symbolCode_E_exclusive_access);
	protected static Term termExclusiveWriting= new PrologSymbol(SymbolCodes.symbolCode_E_exclusive_writing);
	protected static Term termSharedAccess= new PrologSymbol(SymbolCodes.symbolCode_E_shared_access);
	//
	///////////////////////////////////////////////////////////////
	//
	public static DatabaseSharingMode termToDatabaseSharingMode(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_exclusive_access) {
				return DatabaseSharingMode.EXCLUSIVE_ACCESS;
			} else if (code==SymbolCodes.symbolCode_E_exclusive_writing) {
				return DatabaseSharingMode.EXCLUSIVE_WRITING;
			} else if (code==SymbolCodes.symbolCode_E_shared_access) {
				return DatabaseSharingMode.SHARED_ACCESS;
			} else {
				throw new WrongArgumentIsNotDatabaseSharingMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotDatabaseSharingMode(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term toTerm();
}
