// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.datum;

import target.*;

import morozov.built_in.*;
import morozov.domains.*;
import morozov.run.*;
import morozov.syntax.interfaces.*;
import morozov.syntax.scanner.errors.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.datum.errors.*;
import morozov.system.datum.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;
import morozov.worlds.*;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayDeque;
import java.lang.InterruptedException;
import java.math.BigInteger;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class DatabaseTableContainer extends LoadableContainer {
	//
	protected AtomicReference<DatabaseTable> table= new AtomicReference<>();
	//
	protected transient ArrayDeque<WriterAccessMode> writerAccessModesStack= new ArrayDeque<>();
	//
	protected transient AtomicLong numberOfWaitingWriters= new AtomicLong(0);
	//
	private static final long serialVersionUID= 0xE055595E1B41FF26L; // -2281819375474180314L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.datum","DatabaseTableContainer");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public DatabaseTableContainer(DatabaseTable t, boolean shared) {
		table.set(t);
		isShared= shared;
		t.setContainer(this);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setTable(DatabaseTable t) {
		table.set(t);
	}
	//
	public DatabaseTable getTable() {
		return table.get();
	}
	//
	public Term getType() {
		return table.get().getType();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void checkDomain(PrologDomain domain, ChoisePoint iX) {
		DatabaseTable t= table.get();
		if (t != null) {
			t.checkDomain(domain,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static DatabaseTable argumentToDatabaseTable(Term value, Database database, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_standalone) {
				return database.retrieveStandaloneDatabaseTable(iX);
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
							DatabaseTable databaseTable= dataStore.getDatabaseTable(entry);
							return databaseTable;
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
	public static DatabaseTableContainer argumentToDatabaseTableContainer(Term value, Database database, PrologDomain domain, DatabaseType type, boolean reuseKN, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_standalone) {
				return database.retrieveStandaloneDatabaseTableContainer(iX);
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
							DatabaseTableContainer databaseTableContainer= dataStore.getDatabaseTableContainer(entry,domain,type,reuseKN,true);
							return databaseTableContainer;
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
	public static void convertTermToDatabaseTableAndBeginTransaction(Term value, PrologDomain domain, DatabaseType type, boolean reuseKN, Database database, DatabaseAccessMode accessMode, TimeInterval waitingPeriod, TimeInterval sleepPeriod, BigInteger maxRetryNumber, ActiveWorld currentProcess, ChoisePoint iX) throws Backtracking {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_standalone) {
				database.retrieveStandaloneDatabaseTableContainer(iX).beginTransaction(accessMode,waitingPeriod,sleepPeriod,maxRetryNumber,database,currentProcess,null,null,null,false,iX);
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
							dataStore.beginDatabaseTableTransaction(entry,domain,type,reuseKN,accessMode,waitingPeriod,sleepPeriod,maxRetryNumber,currentProcess,iX);
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
	public static void convertTermToDatabaseTableAndEndTransaction(Term value, PrologDomain domain, DatabaseType type, Database database, ActiveWorld currentProcess, boolean watchTable, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_standalone) {
				database.retrieveStandaloneDatabaseTableContainer(iX).endTransaction(database,currentProcess,null,false,watchTable);
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
							dataStore.endDatabaseTableTransaction(entry,database,currentProcess,watchTable);
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
	public static void convertTermToDatabaseTableAndRollbackCurrentTransaction(Term value, PrologDomain domain, DatabaseType type, Database database, ActiveWorld currentProcess, boolean watchTable, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_standalone) {
				database.retrieveStandaloneDatabaseTableContainer(iX).rollbackCurrentTransaction(database,currentProcess,null,watchTable);
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
							dataStore.rollbackDatabaseTableCurrentTransaction(entry,currentProcess,watchTable);
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
	public static void convertTermToDatabaseTableAndRollbackTransactionTree(Term value, PrologDomain domain, DatabaseType type, Database database, ActiveWorld currentProcess, boolean watchTable, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_standalone) {
				database.retrieveStandaloneDatabaseTableContainer(iX).rollbackTransactionTree(database,currentProcess,null,watchTable);
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
							dataStore.rollbackDatabaseTableTransactionTree(entry,currentProcess,watchTable);
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
	public static void convertTermToDatabaseTableAndActivateWatcher(Term value, PrologDomain domain, DatabaseType type, boolean reuseKeyNumbers, Database database, ActiveWorld currentProcess, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_standalone) {
				database.retrieveStandaloneDatabaseTableContainer(iX).activateWatcher(database,currentProcess,null);
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
							dataStore.activateDatabaseTableWatcher(entry,domain,type,reuseKeyNumbers,database,currentProcess);
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
	public static void convertTermToDatabaseTableAndUnregisterWatcher(Term value, Database database, ActiveWorld currentProcess, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_standalone) {
				database.retrieveStandaloneDatabaseTableContainer(iX).unregisterWatcher(database,currentProcess,null);
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
							dataStore.unregisterDatabaseTableWatcher(entry,database,currentProcess);
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
	@Override
	protected void loadContent(String textBuffer, ActiveWorld currentProcess, boolean checkPrivileges, ParserMasterInterface master, ChoisePoint iX) throws SyntaxError, DatabaseRecordDoesNotBelongToDomain {
		table.get().loadContent(textBuffer,currentProcess,checkPrivileges,master,iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean beginTransaction(DatabaseAccessMode accessMode, TimeInterval waitingPeriod, TimeInterval sleepPeriod, BigInteger maxRetryNumber, DataAbstraction currentWorld, ActiveWorld currentProcess, String entryName, DataStore dataStore, OpenedDataStore openedDataStore, boolean reuseNumbers, ChoisePoint iX) throws Backtracking {
		boolean domainIsToBeChecked= false;
		lock.lock();
		Integer previousLengthOfWriterAccessModesStack= null;
		Integer previousLengthOfReaderAccessModesStack= null;
		try {
			long nanosTimeout= waitingPeriod.toNanosecondsLong();
			while (true) {
				if (readers.isEmpty() && writer.get()==null) {
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// Если мы оказались здесь, значит, таблица
// никем не захвачена, и можно её захватить.
if (openedDataStore==null || sectionMainLockChannel.get() != null) {
	// Если мы здесь, значит, таблица
	// не расположена во внешнем файле или
	// расположена, но файл этот уже захвачен.
	if (accessMode==DatabaseAccessMode.READING) {
		previousLengthOfReaderAccessModesStack= 0;
		ArrayDeque<DatabaseAccessMode> list= new ArrayDeque<>();
		list.add(accessMode);
		readers.put(currentProcess,list);
	} else { // accessMode==DatabaseAccessMode.MODIFYING
		writerAccessModesStack.clear();
		previousLengthOfWriterAccessModesStack= 0;
		writer.set(currentProcess);
		pushWriterAccessModeStack(accessMode,currentProcess,iX);
	};
	break;
} else {
	// Если мы здесь, значит, таблица расположена во внешнем
	// файле и требуется его захватить.
	if (accessMode==DatabaseAccessMode.READING) {
		openedDataStore.lockSectionFile(entryName,this,true,reuseNumbers,externalFileIsFree);
		dataStore.unsafelyDownloadExternalFile(entryName,this);
		domainIsToBeChecked= true;
	} else { // accessMode==DatabaseAccessMode.MODIFYING
		openedDataStore.lockSectionFile(entryName,this,false,reuseNumbers,externalFileIsFree);
		dataStore.unsafelyDownloadExternalFile(entryName,this);
		domainIsToBeChecked= true;
	};
	continue;
}
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
				} else {
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// Если мы здесь, значит, таблица захвачена другим
// процессом и надо подождать, пока таблица не будет освобождена.
if (writer.get() != currentProcess) {
	// Если мы здесь, значит:
	// 1) либо таблица захвачена по записи другим процессом,
	// 2) либо таблица по записи не захвачена вовсе (writer.get()==null).
	// При этом вполне возможно, что таблица захвачена кем-то по чтению.
	if (openedDataStore==null || sectionMainLockChannel.get() != null) {
		// Если мы здесь, значит, таблица
		// не расположена во внешнем файле или
		// расположена, но файл этот уже захвачен.
		if (accessMode==DatabaseAccessMode.READING) {
			if (writer.get() != null || numberOfWaitingWriters.get() > 0) {
				// Если мы здесь, значит, таблица захвачена
				// по записи другим процессом.
				nanosTimeout= databaseTableIsFree.awaitNanos(nanosTimeout);
				if (nanosTimeout <= 0) {
					throw Backtracking.instance;
				};
				continue;
			} else {
				// Если мы здесь, значит, таблица НЕ захвачена
				// по записи другим процессом. Но, вполне может быть,
				// захвачена по чтению.
				ArrayDeque<DatabaseAccessMode> list= readers.get(currentProcess);
				if (list==null) {
					list= new ArrayDeque<>();
					readers.put(currentProcess,list);
				} else if (!list.isEmpty()) {
					accessMode.checkAuthority(list.peek());
				};
				previousLengthOfReaderAccessModesStack= list.size();
				list.add(accessMode);
				break;
			}
		} else { // accessMode==DatabaseAccessMode.MODIFYING
			if (readers.get(currentProcess) != null) {
				throw new ModifyingTransactionCannotBeInsideReadingTransaction();
			} else {
				if ( !(readers.isEmpty() && writer.get()==null) ) {
					numberOfWaitingWriters.incrementAndGet();
					try {
						nanosTimeout= databaseTableIsFree.awaitNanos(nanosTimeout);
					} finally {
						numberOfWaitingWriters.decrementAndGet();
					};
					if (nanosTimeout <= 0) {
						throw Backtracking.instance;
					};
					continue;
				} else {
					writerAccessModesStack.clear();
					previousLengthOfWriterAccessModesStack= 0;
					writer.set(currentProcess);
					pushWriterAccessModeStack(accessMode,currentProcess,iX);
					break;
				}
			}
		}
	} else {
		// Если мы здесь, значит, таблица расположена во внешнем
		// файле и требуется его захватить.
		if (accessMode==DatabaseAccessMode.READING) {
			openedDataStore.lockSectionFile(entryName,this,true,reuseNumbers,externalFileIsFree);
			dataStore.unsafelyDownloadExternalFile(entryName,this);
			domainIsToBeChecked= true;
		} else { // accessMode==DatabaseAccessMode.MODIFYING
			if (readers.get(currentProcess) != null) {
				throw new ModifyingTransactionCannotBeInsideReadingTransaction();
			} else {
				openedDataStore.lockSectionFile(entryName,this,false,reuseNumbers,externalFileIsFree);
				dataStore.unsafelyDownloadExternalFile(entryName,this);
				domainIsToBeChecked= true;
			}
		};
		continue;
	}
} else {
	// Если мы здесь, значит:
	// 1) таблица НЕ захвачена по записи другим процессом,
	// 2) таблица ВСЁ-ТАКИ захвачена кем-то по записи.
	// Это означает, что таблица УЖЕ захвачена по записи этим же процессом.
	if (!writerAccessModesStack.isEmpty()) {
		accessMode.checkAuthority(writerAccessModesStack.peek().getDatabaseAccessMode());
	};
	previousLengthOfWriterAccessModesStack= writerAccessModesStack.size();
	pushWriterAccessModeStack(accessMode,currentProcess,iX);
	break;
}
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
				}
			}
		} catch (InterruptedException e) {
			cancelCurrentTransaction(currentWorld,currentProcess,openedDataStore,false,previousLengthOfWriterAccessModesStack,previousLengthOfReaderAccessModesStack);
			throw Backtracking.instance;
		} catch (Throwable e) {
			cancelCurrentTransaction(currentWorld,currentProcess,openedDataStore,false,previousLengthOfWriterAccessModesStack,previousLengthOfReaderAccessModesStack);
			throw Backtracking.instance;
		} finally {
			lock.unlock();
		};
		return domainIsToBeChecked;
	}
	//
	protected void pushWriterAccessModeStack(DatabaseAccessMode mode, ActiveWorld currentProcess, ChoisePoint iX) {
		DatabaseTable oldTable= table.get();
		DatabaseTable copyOfTable= (DatabaseTable)oldTable.clone();
		oldTable.copyContent(copyOfTable,currentProcess,iX);
		writerAccessModesStack.push(new WriterAccessMode(mode,oldTable));
		table.set(copyOfTable);
	}
	//
	public void endTransaction(Database database, ActiveWorld currentProcess, OpenedDataStore openedDataStore, boolean updateSectionFile, boolean watchTable) {
		lock.lock();
		try {
			terminateTransaction(database,currentProcess,openedDataStore,updateSectionFile,watchTable);
		} finally {
			lock.unlock();
		}
	}
	//
	protected void terminateTransaction(Database database, ActiveWorld currentProcess, OpenedDataStore openedDataStore, boolean updateSectionFile, boolean watchTable) {
		if (writer.get()==currentProcess) {
			if (!writerAccessModesStack.isEmpty()) {
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
WriterAccessMode currentAccessMode= writerAccessModesStack.pop();
if (writerAccessModesStack.isEmpty()) {
	writer.set(null);
	boolean tableIsModified= currentAccessMode.getTableIsModified();
	synchronized (informedWorlds) {
		if (tableIsModified) {
			informedWorlds.clear();
		};
		if (database != null) {
			informedWorlds.add(database);
		}
	};
	if (watchTable && database != null) {
		synchronized (linkedWorlds) {
			linkedWorlds.add(database);
		}
	};
	if (tableIsModified) {
		sendInternalMessages(database);
	};
	if (openedDataStore != null) {
		if (watchTable) {
			openedDataStore.registerWatcher(database,this,sectionPath_MainData);
		};
		if (tableIsModified && updateSectionFile) {
			openedDataStore.unsafelyWriteSection(this,externalFileIsFree);
		};
		openedDataStore.unlockSectionFile(this);
	};
	databaseTableIsFree.signal();
} else {
	writerAccessModesStack.peek().setTableIsModified(currentAccessMode);
}
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
			} else {
				throw new NotInsideTransaction();
			}
		} else {
			ArrayDeque<DatabaseAccessMode> list= readers.get(currentProcess);
			if (list != null) {
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
if (!list.isEmpty()) {
	list.pop();
	if (list.isEmpty()) {
		readers.remove(currentProcess);
		if (database != null) {
			synchronized (informedWorlds) {
				informedWorlds.add(database);
			};
			if (watchTable) {
				synchronized (linkedWorlds) {
					linkedWorlds.add(database);
				}
			}
		};
		if (openedDataStore != null) {
			if (watchTable) {
				openedDataStore.registerWatcher(database,this,sectionPath_MainData);
			};
			openedDataStore.unlockSectionFile(this);
		};
		if (readers.isEmpty()) {
			databaseTableIsFree.signal();
		}
	}
} else {
	throw new NotInsideTransaction();
}
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
			} else {
				throw new NotInsideTransaction();
			}
		}
	}
	//
	protected void sendInternalMessages(DataAbstraction sender) {
		synchronized (linkedWorlds) {
			Iterator<DataAbstraction> targetWorldsIterator= linkedWorlds.iterator();
			while (targetWorldsIterator.hasNext()) {
				DataAbstraction targetWorld= targetWorldsIterator.next();
				if (targetWorld != sender) {
					long domainSignature= targetWorld.entry_s_Update_0();
					AsyncCall call= new AsyncCall(domainSignature,targetWorld,true,false,new Term[0],true);
					targetWorld.transmitAsyncCall(call,null);
				}
			}
		}
	}
	//
	public void rollbackCurrentTransaction(DataAbstraction currentWorld, ActiveWorld currentProcess, OpenedDataStore openedDataStore, boolean watchTable) {
		lock.lock();
		try {
			cancelOneTransaction(currentWorld,currentProcess,openedDataStore,watchTable);
		} finally {
			lock.unlock();
		}
	}
	//
	public void cancelCurrentTransaction(DataAbstraction currentWorld, ActiveWorld currentProcess, OpenedDataStore openedDataStore, boolean watchTable, Integer previousLengthOfWriterAccessModesStack, Integer previousLengthOfReaderAccessModesStack) {
		if (writer.get()==currentProcess) {
			if (previousLengthOfWriterAccessModesStack != null) {
				while (writerAccessModesStack.size() > previousLengthOfWriterAccessModesStack) {
					if (!writerAccessModesStack.isEmpty()) {
						WriterAccessMode currentAccessMode= writerAccessModesStack.pop();
						table.set(currentAccessMode.getPreviousTable());
						if (writerAccessModesStack.isEmpty()) {
							freeWriter(currentWorld,openedDataStore,watchTable);
							break;
						}
					} else {
						freeWriter(currentWorld,openedDataStore,watchTable);
						break;
					}
				}
			}
		} else {
			ArrayDeque<DatabaseAccessMode> list= readers.get(currentProcess);
			if (list != null) {
				if (previousLengthOfReaderAccessModesStack != null) {
					while (list.size() > previousLengthOfReaderAccessModesStack) {
						if (!list.isEmpty()) {
							list.pop();
							if (list.isEmpty()) {
								freeReader(currentWorld,currentProcess,openedDataStore,watchTable);
								break;
							}
						} else {
							freeReader(currentWorld,currentProcess,openedDataStore,watchTable);
							break;
						}
					}
				}
			}
		}
	}
	//
	public void cancelOneTransaction(DataAbstraction currentWorld, ActiveWorld currentProcess, OpenedDataStore openedDataStore, boolean watchTable) {
		if (writer.get()==currentProcess) {
			if (!writerAccessModesStack.isEmpty()) {
				WriterAccessMode currentAccessMode= writerAccessModesStack.pop();
				table.set(currentAccessMode.getPreviousTable());
				if (writerAccessModesStack.isEmpty()) {
					freeWriter(currentWorld,openedDataStore,watchTable);
				}
			} else {
				freeWriter(currentWorld,openedDataStore,watchTable);
			}
		} else {
			ArrayDeque<DatabaseAccessMode> list= readers.get(currentProcess);
			if (list != null) {
				if (!list.isEmpty()) {
					list.pop();
					if (list.isEmpty()) {
						freeReader(currentWorld,currentProcess,openedDataStore,watchTable);
					}
				} else {
					freeReader(currentWorld,currentProcess,openedDataStore,watchTable);
				}
			}
		}
	}
	//
	public void rollbackTransactionTree(DataAbstraction currentWorld, ActiveWorld currentProcess, OpenedDataStore openedDataStore, boolean watchTable) {
		lock.lock();
		try {
			cancelTransactionTree(currentWorld,currentProcess,openedDataStore,watchTable);
		} finally {
			lock.unlock();
		}
	}
	//
	public void cancelTransactionTree(DataAbstraction currentWorld, ActiveWorld currentProcess, OpenedDataStore openedDataStore, boolean watchTable) {
		if (writer.get()==currentProcess) {
			if (!writerAccessModesStack.isEmpty()) {
				WriterAccessMode firstAccessMode= writerAccessModesStack.getLast();
				table.set(firstAccessMode.getPreviousTable());
				writerAccessModesStack.clear();
				freeWriter(currentWorld,openedDataStore,watchTable);
			}
		} else {
			ArrayDeque<DatabaseAccessMode> list= readers.get(currentProcess);
			if (list != null) {
				if (!list.isEmpty()) {
					list.clear();
					freeReader(currentWorld,currentProcess,openedDataStore,watchTable);
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void saveToTextBuffer(String tableName, StringBuilder textBuffer, ChoisePoint iX, CharsetEncoder encoder) {
		String textEntry= SymbolNames.retrieveSymbolName(SymbolCodes.symbolCode_E_entry).toRawString(encoder);
		Term type= getType();
		textBuffer.append(textEntry);
		textBuffer.append("(\"");
		textBuffer.append(tableName);
		textBuffer.append("\");\n");
		String textDatabaseType= SymbolNames.retrieveSymbolName(SymbolCodes.symbolCode_E_database_type).toRawString(encoder);
		textBuffer.append(textDatabaseType);
		textBuffer.append("(");
		textBuffer.append(type.toString());
		textBuffer.append(");\n");
		table.get().saveToTextBuffer(textBuffer,iX,encoder);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		table.get().setContainer(this);
		writerAccessModesStack= new ArrayDeque<>();
		numberOfWaitingWriters= new AtomicLong(0);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Object clone() {
		DatabaseTableContainer o= (DatabaseTableContainer)super.clone();
		o.table= new AtomicReference<>();
		DatabaseTable newTable= (DatabaseTable)table.get().clone();
		o.table.set(newTable);
		newTable.setContainer(o);
		o.writerAccessModesStack= new ArrayDeque<>();
		o.numberOfWaitingWriters= new AtomicLong(0);
		return o;
	}
	public void copyContent(DatabaseTableContainer copyOfTableContainer, ActiveWorld currentProcess, ChoisePoint iX) {
		if (table != null) {
			table.get().copyContent(copyOfTableContainer.table.get(),currentProcess,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected boolean writerAccessModesStackIsEmpty() {
		return writerAccessModesStack.isEmpty();
	}
	@Override
	protected DatabaseAccessMode getCurrentDatabaseAccessModes() {
		return writerAccessModesStack.peek().getDatabaseAccessMode();
	}
	@Override
	protected void registerModifyingAccess() {
		writerAccessModesStack.peek().registerModifyingAccess();
	}
}
