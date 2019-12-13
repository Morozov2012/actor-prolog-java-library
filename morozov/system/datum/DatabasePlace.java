// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.datum;

import target.*;

import morozov.built_in.*;
import morozov.domains.*;
import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.datum.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;
import morozov.worlds.*;

import java.math.BigInteger;

public class DatabasePlace {
	//
	protected DatabaseTableContainer databaseTableContainer;
	protected boolean isShared= false;
	protected DataStore dataStore;
	protected String entry;
	//
	public DatabasePlace(Database database, ChoisePoint iX) {
		isShared= false;
		databaseTableContainer= database.retrieveStandaloneDatabaseTableContainer(iX);
	}
	public DatabasePlace(DataStore store, String tableName, PrologDomain domain, DatabaseType type, boolean reuseKN) {
		isShared= true;
		dataStore= store;
		entry= tableName;
		databaseTableContainer= store.getDatabaseTableContainer(entry,domain,type,reuseKN,isShared);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public DatabaseTableContainer getDatabaseTableContainer() {
		return databaseTableContainer;
	}
	public boolean isShared() {
		return isShared;
	}
	public DataStore getDataStore() {
		return dataStore;
	}
	public String getEntry() {
		return entry;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static DatabasePlace argumentToDatabasePlace(Term value, Database database, PrologDomain domain, DatabaseType type, boolean reuseKN, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_standalone) {
				return new DatabasePlace(database,iX);
			} else {
				throw new WrongArgumentIsNotDatabasePlace(value);
			}
		} catch (TermIsNotASymbol e1) {
			try {
				long functor= value.getStructureFunctor(iX);
				if (functor==SymbolCodes.symbolCode_E_shared) {
					Term[] arguments= value.getStructureArguments(iX);
					if (arguments.length == 2) {
						Term store= arguments[0];
						store= store.dereferenceValue(iX);
						if (store instanceof DataStore) {
							DataStore dataStore= (DataStore)store;
							String entry= GeneralConverters.argumentToString(arguments[1],iX);
							return new DatabasePlace(dataStore,entry,domain,type,reuseKN);
						} else {
							throw new WrongArgumentIsNotDataStore(value);
						}
					} else {
						throw new WrongArgumentIsNotDatabasePlace(value);
					}
				} else {
					throw new WrongArgumentIsNotDatabasePlace(value);
				}
			} catch (TermIsNotAStructure e2) {
				throw new WrongArgumentIsNotDatabasePlace(value);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void beginTransaction(DatabaseAccessMode accessMode, TimeInterval waitingPeriod, TimeInterval sleepPeriod, BigInteger maxRetryNumber, DataAbstraction currentWorld, ActiveWorld currentProcess, ChoisePoint iX) throws Backtracking {
		databaseTableContainer.beginTransaction(accessMode,waitingPeriod,sleepPeriod,maxRetryNumber,currentWorld,currentProcess,entry,dataStore,dataStore.getOpenedDataStore(),dataStore.getReuseTableNumbers(iX),iX);
	}
	//
	public void endTransaction(Database database, ActiveWorld currentProcess, boolean watchTable) {
		databaseTableContainer.endTransaction(database,currentProcess,dataStore.getOpenedDataStore(),true,watchTable);
	}
	//
	public void activateWatcher(Database database, ActiveWorld currentProcess) {
		databaseTableContainer.activateWatcher(database,currentProcess,dataStore.getOpenedDataStore());
	}
	//
	public void unregisterWatcher(Database database, ActiveWorld currentProcess) {
		databaseTableContainer.unregisterWatcher(database,currentProcess,dataStore.getOpenedDataStore());
	}
	//
	public void rollbackCurrentTransaction(DataAbstraction currentWorld, ActiveWorld currentProcess, boolean watchTable) {
		databaseTableContainer.rollbackCurrentTransaction(currentWorld,currentProcess,dataStore.getOpenedDataStore(),watchTable);
	}
	//
	public void rollbackTransactionTree(DataAbstraction currentWorld, ActiveWorld currentProcess, boolean watchTable) {
		databaseTableContainer.rollbackTransactionTree(currentWorld,currentProcess,dataStore.getOpenedDataStore(),watchTable);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		if (isShared) {
			Term[] arguments= new Term[2];
			arguments[0]= dataStore;
			arguments[1]= new PrologString(entry);
			return new PrologStructure(SymbolCodes.symbolCode_E_shared,arguments);
		} else {
			return new PrologSymbol(SymbolCodes.symbolCode_E_standalone);
		}
	}
}
