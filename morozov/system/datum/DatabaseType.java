// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.datum;

import target.*;

import morozov.domains.*;
import morozov.run.*;
import morozov.system.datum.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.util.HashMap;

public enum DatabaseType {
	//
	PLAIN {
		@Override
		public DatabaseTable createTable(PrologDomain domain, boolean reuseKeyNumbers) {
			return new DatabaseTable(domain,reuseKeyNumbers);
		}
		@Override
		public DatabaseTable createTable(PrologDomain domain, boolean reuseKeyNumbers, HashMap<String,PrologDomain> currentLocalDomainTable) {
			return new DatabaseTable(domain,reuseKeyNumbers,currentLocalDomainTable);
		}
	},
	HASH_SET {
		@Override
		public DatabaseTable createTable(PrologDomain domain, boolean reuseKeyNumbers) {
			return new HashSetTable(domain,reuseKeyNumbers);
		}
		@Override
		public DatabaseTable createTable(PrologDomain domain, boolean reuseKeyNumbers, HashMap<String,PrologDomain> currentLocalDomainTable) {
			return new HashSetTable(domain,reuseKeyNumbers,currentLocalDomainTable);
		}
	},
	HASH_MAP {
		@Override
		public DatabaseTable createTable(PrologDomain domain, boolean reuseKeyNumbers) {
			return new HashMapTable(domain,reuseKeyNumbers);
		}
		@Override
		public DatabaseTable createTable(PrologDomain domain, boolean reuseKeyNumbers, HashMap<String,PrologDomain> currentLocalDomainTable) {
			return new HashMapTable(domain,reuseKeyNumbers,currentLocalDomainTable);
		}
	},
	LINKED_HASH_MAP {
		@Override
		public DatabaseTable createTable(PrologDomain domain, boolean reuseKeyNumbers) {
			return new LinkedHashMapTable(domain,reuseKeyNumbers);
		}
		@Override
		public DatabaseTable createTable(PrologDomain domain, boolean reuseKeyNumbers, HashMap<String,PrologDomain> currentLocalDomainTable) {
			return new LinkedHashMapTable(domain,reuseKeyNumbers,currentLocalDomainTable);
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public DatabaseTable createTable(PrologDomain domain, boolean reuseKeyNumbers);
	abstract public DatabaseTable createTable(PrologDomain domain, boolean reuseKeyNumbers, HashMap<String,PrologDomain> currentLocalDomainTable);
	//
	///////////////////////////////////////////////////////////////
	//
	public static DatabaseType argumentToDatabaseType(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_Database) {
				return DatabaseType.PLAIN;
			} else if (code==SymbolCodes.symbolCode_E_HashSet) {
				return DatabaseType.HASH_SET;
			} else if (code==SymbolCodes.symbolCode_E_HashMap) {
				return DatabaseType.HASH_MAP;
			} else if (code==SymbolCodes.symbolCode_E_LinkedHashMap) {
				return DatabaseType.LINKED_HASH_MAP;
			} else {
				throw new WrongArgumentIsNotDatabaseType(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotDatabaseType(value);
		}
	}
}
