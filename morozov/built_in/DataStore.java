// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.domains.*;
import morozov.domains.errors.*;
import morozov.domains.signals.*;
import morozov.run.*;
import morozov.syntax.*;
import morozov.syntax.errors.*;
import morozov.syntax.scanner.errors.*;
import morozov.system.*;
import morozov.system.checker.signals.*;
import morozov.system.converters.*;
import morozov.system.files.*;
import morozov.system.files.errors.*;
import morozov.system.datum.*;
import morozov.system.datum.errors.*;
import morozov.system.datum.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;
import morozov.worlds.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.charset.CharsetEncoder;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryIteratorException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.TimeUnit;
import java.math.BigInteger;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public abstract class DataStore extends DataAbstraction {
	//
	public DatabaseAccessMode accessMode= null;
	public DatabaseSharingMode sharingMode= null;
	public Boolean reuseTableNumbers= null;
	//
	public AtomicReference<OpenedDataStore> openedDataStore= new AtomicReference<>(null);
	//
	protected AtomicReference<HashMap<String,DatabaseTableContainer>> tableHash= new AtomicReference<>(new HashMap<String,DatabaseTableContainer>());
	//
	protected ArrayDeque<DataStoreAccessMode> accessModesStack= new ArrayDeque<>();
	//
	protected static HashSet<OpenedDataStore> openedDataStoreList= new HashSet<>();
	//
	protected HashSet<DatabaseTableContainer> lockedTables= new HashSet<>();
	//
	protected ReentrantReadWriteLock fairLock= new ReentrantReadWriteLock(true);
	//
	protected Lock readLock= fairLock.readLock();
	protected Lock writeLock= fairLock.writeLock();
	//
	protected static boolean useFairLock= true;
	protected ReentrantLock auxiliaryLock= new ReentrantLock(useFairLock);
	protected Condition externalFileIsFree= auxiliaryLock.newCondition();
	//
	protected String recentErrorText;
	protected long recentErrorPosition= -1;
	protected Throwable recentErrorException;
	//
	protected AtomicBoolean isInformed= new AtomicBoolean(false);
	protected AtomicBoolean isActive= new AtomicBoolean(false);
	//
	protected static final long mode_NoDatastoreDamage= 0;
	protected static final long mode_DatastoreFileIsReassembled= 1;
	protected static final long mode_DataStore_BAK_FileIsUsed= 2;
	protected static final long mode_DatastoreContainsDamagedTables= 3;
	protected static final long mode_DatastoreIndexIsDamaged= 4;
	protected static final long mode_DatastoreContentIsTotallyDamaged= 5;
	//
	protected static final String messageNoDamagesAreFoundInTheDataStore= "No damages are found in the data store: %s\n";
	protected static final String messageTheDataStoreIsUnpackedAnew= "The data store is unpacked anew: %s\n";
	protected static final String messageTheDataStoreIsReassembled= "The data store is reassembled: %s\n";
	protected static final String messageTheDataStoreLoadedFromThe_BAK_File= "The data store is loaded from the bak file: %s\n";
	protected static final String messageTheDataStoreContainsDamagedTables= "The data store contains damaged entries: %s\n";
	protected static final String messageTheDataStoreIndexIsDamaged= "The index of the data store is damaged: %s\n";
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term getBuiltInSlot_E_access_mode();
	abstract public Term getBuiltInSlot_E_sharing_mode();
	abstract public Term getBuiltInSlot_E_reuse_table_numbers();
	//
	///////////////////////////////////////////////////////////////
	//
	public DataStore() {
	}
	public DataStore(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set access_mode
	//
	public void setAccessMode1s(ChoisePoint iX, Term a1) {
		setAccessMode(DatabaseAccessMode.argumentToDatabaseAccessMode(a1,iX));
	}
	public void setAccessMode(DatabaseAccessMode value) {
		accessMode= value;
	}
	public void getAccessMode0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getAccessMode(iX).toTerm());
	}
	public void getAccessMode0fs(ChoisePoint iX) {
	}
	public DatabaseAccessMode getAccessMode(ChoisePoint iX) {
		if (accessMode != null) {
			return accessMode;
		} else {
			Term value= getBuiltInSlot_E_access_mode();
			return DatabaseAccessMode.argumentToDatabaseAccessMode(value,iX);
		}
	}
	//
	// get/set sharing_mode
	//
	public void setSharingMode1s(ChoisePoint iX, Term a1) {
		setSharingMode(DatabaseSharingMode.argumentToDatabaseSharingMode(a1,iX));
	}
	public void setSharingMode(DatabaseSharingMode value) {
		sharingMode= value;
	}
	public void getSharingMode0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getSharingMode(iX).toTerm());
	}
	public void getSharingMode0fs(ChoisePoint iX) {
	}
	public DatabaseSharingMode getSharingMode(ChoisePoint iX) {
		if (sharingMode != null) {
			return sharingMode;
		} else {
			Term value= getBuiltInSlot_E_sharing_mode();
			return DatabaseSharingMode.argumentToDatabaseSharingMode(value,iX);
		}
	}
	//
	// get/set reuse_table_numbers
	//
	public void setReuseTableNumbers1s(ChoisePoint iX, Term a1) {
		boolean reuseTN= YesNoConverters.termYesNo2Boolean(a1,iX);
		setReuseTableNumbers(reuseTN);
	}
	public void setReuseTableNumbers(boolean value) {
		reuseTableNumbers= value;
	}
	public void getReuseTableNumbers0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(YesNoConverters.boolean2TermYesNo(getReuseTableNumbers(iX)));
	}
	public void getReuseTableNumbers0fs(ChoisePoint iX) {
	}
	public boolean getReuseTableNumbers(ChoisePoint iX) {
		if (reuseTableNumbers != null) {
			return reuseTableNumbers;
		} else {
			Term value= getBuiltInSlot_E_reuse_table_numbers();
			return YesNoConverters.termYesNo2Boolean(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setWatchUpdates1s(ChoisePoint iX, Term a1) {
		super.setWatchUpdates1s(iX,a1);
		if (!getWatchUpdates(iX)) {
			OpenedDataStore dataStore= openedDataStore.get();
			if (dataStore != null) {
				dataStore.unregisterIndexWatcher(this);
			} else {
				isActive.set(false);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public DatabaseTable getDatabaseTable(String entryName) {
		int numberOfHolds= fairLock.getReadHoldCount();
		if (numberOfHolds > 0) {
			HashMap<String,DatabaseTableContainer> hash= tableHash.get();
			DatabaseTable table;
			synchronized (hash) {
				table= hash.get(entryName).getTable();
			};
			if (table != null) {
				return table;
			} else {
				throw new NotInsideTransaction();
			}
		} else {
			throw new NotInsideTransaction();
		}
	}
	//
	public DatabaseTableContainer getDatabaseTableContainer(String entryName, PrologDomain domain, DatabaseType type, boolean reuseKN, boolean isShared) {
		HashMap<String,DatabaseTableContainer> hash= tableHash.get();
		synchronized (hash) {
			DatabaseTableContainer tableContainer= hash.get(entryName);
			if (tableContainer == null) {
				tableContainer= new DatabaseTableContainer(type.createTable(domain,reuseKN),isShared);
				hash.put(entryName,tableContainer);
				sendInternalMessage();
			};
			return tableContainer;
		}
	}
	//
	public OpenedDataStore getOpenedDataStore() {
		return openedDataStore.get();
	}
	//
	public void beginDatabaseTableTransaction(String entryName, PrologDomain domain, DatabaseType type, boolean reuseKN, DatabaseAccessMode transactionMode, TimeInterval waitingPeriod, TimeInterval sleepPeriod, BigInteger maximalRetryNumber, ActiveWorld currentProcess, ChoisePoint iX) throws Backtracking {
		OpenedDataStore dataStore= openedDataStore.get();
		if (dataStore != null) {
			if (transactionMode==DatabaseAccessMode.MODIFYING && dataStore.getAccessMode()==DatabaseAccessMode.READING) {
				throw new DataStoreIsNotInModifyingAccessMode();
			}
		};
		readLock.lock();
		try {
			boolean checkDomain= true;
			HashMap<String,DatabaseTableContainer> hash= tableHash.get();
			DatabaseTableContainer tableContainer;
			synchronized (hash) {
				tableContainer= hash.get(entryName);
				if (tableContainer==null) {
					tableContainer= new DatabaseTableContainer(type.createTable(domain,reuseKN),true);
					hash.put(entryName,tableContainer);
					checkDomain= false;
					sendInternalMessage();
				}
			};
			if (tableContainer.beginTransaction(transactionMode,waitingPeriod,sleepPeriod,maximalRetryNumber,this,currentProcess,entryName,this,dataStore,reuseKN,iX)) {
				checkDomain= true;
			};
			if (checkDomain) {
				tableContainer.checkDomain(domain,iX);
			}
		} catch (Throwable e) {
			readLock.unlock();
			throw e;
		}
	}
	//
	public void endDatabaseTableTransaction(String entryName, Database database, ActiveWorld currentProcess, boolean watchTable) {
		int numberOfHolds= fairLock.getReadHoldCount();
		if (numberOfHolds > 0) {
			HashMap<String,DatabaseTableContainer> hash= tableHash.get();
			DatabaseTableContainer tableContainer;
			synchronized (hash) {
				tableContainer= hash.get(entryName);
			};
			if (tableContainer != null) {
				OpenedDataStore dataStore= openedDataStore.get();
				tableContainer.endTransaction(database,currentProcess,dataStore,true,watchTable);
				readLock.unlock();
			} else {
				throw new NotInsideTransaction();
			}
		} else {
			throw new NotInsideTransaction();
		}
	}
	//
	public void rollbackDatabaseTableCurrentTransaction(String entryName, ActiveWorld currentProcess, boolean watchTable) {
		int numberOfHolds= fairLock.getReadHoldCount();
		if (numberOfHolds > 0) {
			HashMap<String,DatabaseTableContainer> hash= tableHash.get();
			DatabaseTableContainer tableContainer;
			synchronized (hash) {
				tableContainer= hash.get(entryName);
			};
			if (tableContainer != null) {
				OpenedDataStore dataStore= openedDataStore.get();
				tableContainer.rollbackCurrentTransaction(this,currentProcess,dataStore,watchTable);
				readLock.unlock();
			} else {
				throw new NotInsideTransaction();
			}
		} else {
			throw new NotInsideTransaction();
		}
	}
	//
	public void rollbackDatabaseTableTransactionTree(String entryName, ActiveWorld currentProcess, boolean watchTable) {
		int numberOfHolds= fairLock.getReadHoldCount();
		if (numberOfHolds > 0) {
			HashMap<String,DatabaseTableContainer> hash= tableHash.get();
			DatabaseTableContainer tableContainer;
			synchronized (hash) {
				tableContainer= hash.get(entryName);
			};
			if (tableContainer != null) {
				OpenedDataStore dataStore= openedDataStore.get();
				tableContainer.rollbackTransactionTree(this,currentProcess,dataStore,watchTable);
				readLock.unlock();
			}
		}
	}
	//
	public void activateDatabaseTableWatcher(String entryName, PrologDomain domain, DatabaseType type, boolean reuseKN, Database database, ActiveWorld currentProcess) {
		readLock.lock();
		try {
			OpenedDataStore dataStore= openedDataStore.get();
			if (dataStore != null) {
				HashMap<String,DatabaseTableContainer> hash= tableHash.get();
				DatabaseTableContainer tableContainer;
				synchronized (hash) {
					tableContainer= hash.get(entryName);
					if (tableContainer==null) {
						tableContainer= new DatabaseTableContainer(type.createTable(domain,reuseKN),true);
						hash.put(entryName,tableContainer);
						sendInternalMessage();
					}
				};
				synchronized (dataStore) {
					auxiliaryLock.lock();
					try {
						dataStore.unsafelyRegisterEntry(entryName,tableContainer,reuseKN,externalFileIsFree);
					} finally {
						auxiliaryLock.unlock();
					}
				};
				tableContainer.activateWatcher(database,currentProcess,dataStore);
			} else {
				HashMap<String,DatabaseTableContainer> hash= tableHash.get();
				DatabaseTableContainer tableContainer;
				synchronized (hash) {
					tableContainer= hash.get(entryName);
					if (tableContainer==null) {
						tableContainer= new DatabaseTableContainer(type.createTable(domain,reuseKN),true);
						hash.put(entryName,tableContainer);
						sendInternalMessage();
					}
				};
				tableContainer.linkWorld(database);
			}
		} finally {
			readLock.unlock();
		}
	}
	//
	public void unregisterDatabaseTableWatcher(String entryName, Database database, ActiveWorld currentProcess) {
		readLock.lock();
		try {
			HashMap<String,DatabaseTableContainer> hash= tableHash.get();
			DatabaseTableContainer tableContainer;
			synchronized (hash) {
				tableContainer= hash.get(entryName);
			};
			if (tableContainer != null) {
				OpenedDataStore dataStore= openedDataStore.get();
				if (dataStore != null) {
					tableContainer.unregisterWatcher(database,currentProcess,dataStore);
				};
				tableContainer.forgetWorld(database);
			}
		} finally {
			readLock.unlock();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void unsafelyDownloadAllExternalFiles(OpenedDataStore dataStore) {
		if (dataStore != null) {
			synchronized (dataStore) {
				auxiliaryLock.lock();
				try {
					tableHash.set(dataStore.unsafelyAssembleDataStore(DataStoreErrorHandling.NONE,false,externalFileIsFree));
				} finally {
					auxiliaryLock.unlock();
				}
			}
		} else {
			throw new DataStoreIsNotOpen();
		}
	}
	//
	public void unsafelyDownloadExternalFile(String currentEntryName, DatabaseTableContainer actualTableContainer) {
		OpenedDataStore dataStore= openedDataStore.get();
		if (dataStore != null) {
			synchronized (dataStore) {
				auxiliaryLock.lock();
				try {
					dataStore.unsafelyReadSectionIfNecessary(currentEntryName,actualTableContainer,externalFileIsFree);
				} finally {
					auxiliaryLock.unlock();
				}
			}
		} else {
			throw new DataStoreIsNotOpen();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public class GetEntry2s extends Continuation {
		protected PrologVariable resultName;
		protected PrologVariable resultType;
		//
		public GetEntry2s(Continuation aC, PrologVariable a1, PrologVariable a2) {
			c0= aC;
			resultName= a1;
			resultType= a2;
		}
		//
		@Override
		public void execute(ChoisePoint iX) throws Backtracking {
			claimReadingAccess();
			Set<String> setOfKeys;
			String[] arrayOfKeys;
			Term[] arrayOfTypes;
			HashMap<String,DatabaseTableContainer> hash= tableHash.get();
			synchronized (hash) {
				setOfKeys= hash.keySet();
				arrayOfKeys= setOfKeys.toArray(new String[setOfKeys.size()]);
				Arrays.sort(arrayOfKeys);
				arrayOfTypes= new Term[arrayOfKeys.length];
				for (int n=0; n < arrayOfKeys.length; n++) {
					DatabaseTableContainer tableContainer= hash.get(arrayOfKeys[n]);
					arrayOfTypes[n]= tableContainer.getType();
				}
			};
			ChoisePoint newIx= new ChoisePoint(iX);
			for (int n=0; n < arrayOfKeys.length; n++) {
				resultName.setBacktrackableValue(new PrologString(arrayOfKeys[n]),newIx);
				resultType.setBacktrackableValue(arrayOfTypes[n],newIx);
				try {
					c0.execute(newIx);
				} catch (Backtracking b) {
					resultName.clear();
					resultType.clear();
					if (newIx.isEnabled()) {
						newIx.freeTrail();
						claimReadingAccess();
						continue;
					} else {
						throw Backtracking.instance;
					}
				};
				return;
			};
			resultName.clear();
			resultType.clear();
			throw Backtracking.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void deleteEntry1s(ChoisePoint iX, Term a1) {
		String oldEntryName= GeneralConverters.argumentToString(a1,iX);
		claimModifyingAccess();
		HashMap<String,DatabaseTableContainer> hash= tableHash.get();
		DatabaseTableContainer oldTableContainer;
		synchronized (hash) {
			oldTableContainer= hash.get(oldEntryName);
			if (oldTableContainer==null) {
				throw new DataStoreDoesNotContainEntry(oldEntryName);
			};
			hash.remove(oldEntryName);
			OpenedDataStore dataStore= openedDataStore.get();
			if (dataStore != null) {
				synchronized (dataStore) {
					dataStore.deleteEntry(oldEntryName);
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void copyEntry2s(ChoisePoint iX, Term a1, Term a2) {
		String oldEntryName= GeneralConverters.argumentToString(a1,iX);
		String newEntryName= GeneralConverters.argumentToString(a2,iX);
		TimeInterval waitingPeriod= getTransactionWaitingPeriod(iX);
		TimeInterval sleepPeriod= getTransactionSleepPeriod(iX);
		BigInteger maximalRetryNumber= getTransactionMaximalRetryNumber(iX);
		boolean reuseNumbers= getReuseTableNumbers(iX);
		claimModifyingAccess();
		HashMap<String,DatabaseTableContainer> hash= tableHash.get();
		synchronized (hash) {
			DatabaseTableContainer oldTableContainer= hash.get(oldEntryName);
			if (oldTableContainer==null) {
				throw new DataStoreDoesNotContainEntry(oldEntryName);
			};
			DatabaseTableContainer newTableContainer= hash.get(newEntryName);
			if (newTableContainer != null) {
				throw new DataStoreDoesAlreadyContainEntry(newEntryName);
			};
			DatabaseTableContainer copyOfTableContainer= (DatabaseTableContainer)oldTableContainer.clone();
			oldTableContainer.copyContent(copyOfTableContainer,currentProcess,iX);
			OpenedDataStore dataStore= openedDataStore.get();
			if (dataStore != null) {
				synchronized (dataStore) {
					auxiliaryLock.lock();
					try {
						dataStore.unsafelyRegisterEntry(newEntryName,copyOfTableContainer,reuseNumbers,externalFileIsFree);
					} finally {
						auxiliaryLock.unlock();
					}
				}
			};
			try {
				copyOfTableContainer.beginTransaction(accessModesStack.peek().getDatabaseAccessMode(),waitingPeriod,sleepPeriod,maximalRetryNumber,this,currentProcess,newEntryName,this,dataStore,reuseNumbers,iX);
				lockedTables.add(copyOfTableContainer);
				hash.put(newEntryName,copyOfTableContainer);
			} catch (Backtracking b) {
				lockedTables.remove(copyOfTableContainer);
				throw new CannotLockNewDataStoreEntry(newEntryName);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void renameEntry2s(ChoisePoint iX, Term a1, Term a2) {
		String oldEntryName= GeneralConverters.argumentToString(a1,iX);
		String newEntryName= GeneralConverters.argumentToString(a2,iX);
		claimModifyingAccess();
		HashMap<String,DatabaseTableContainer> hash= tableHash.get();
		synchronized (hash) {
			DatabaseTableContainer oldTableContainer= hash.get(oldEntryName);
			if (oldTableContainer==null) {
				throw new DataStoreDoesNotContainEntry(oldEntryName);
			};
			DatabaseTableContainer newTableContainer= hash.get(newEntryName);
			if (newTableContainer != null) {
				throw new DataStoreDoesAlreadyContainEntry(newEntryName);
			};
			hash.put(newEntryName,oldTableContainer);
			hash.remove(oldEntryName);
			OpenedDataStore dataStore= openedDataStore.get();
			if (dataStore != null) {
				synchronized (dataStore) {
					dataStore.renameEntry(oldEntryName,newEntryName);
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void save0s(ChoisePoint iX) {
		ExtendedFileName fileName= retrieveRealLocalFileName(iX);
		saveContent(fileName,iX);
	}
	public void save1s(ChoisePoint iX, Term a1) {
		ExtendedFileName fileName= retrieveRealLocalFileName(a1,iX);
		saveContent(fileName,iX);
	}
	//
	protected void saveContent(ExtendedFileName fileName, ChoisePoint iX) {
		claimModifyingAccess();
		HashMap<String,DatabaseTableContainer> hash= tableHash.get();
		synchronized (hash) {
			fileName.writeObject(hash);
			deleteUnpackedDataStore(fileName);
			openAndCloseDataStore(fileName,DataStoreUnpackMode.REQUESTED,DataStoreErrorHandling.NONE,false,iX);
		}
	}
	//
	public void load0s(ChoisePoint iX) {
		ExtendedFileName fileName= retrieveRealGlobalFileName(iX);
		loadContent(fileName,staticContext,iX);
	}
	public void load1s(ChoisePoint iX, Term a1) {
		ExtendedFileName fileName= retrieveRealGlobalFileName(a1,iX);
		loadContent(fileName,staticContext,iX);
	}
	//
	@SuppressWarnings("unchecked")
	protected void loadContent(ExtendedFileName fileName, StaticContext staticContext, ChoisePoint iX) {
		int timeout= getMaximalWaitingTimeInMilliseconds(iX);
		claimModifyingAccess();
		tableHash.set((HashMap<String,DatabaseTableContainer>)fileName.readObject(timeout,staticContext));
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void saveText1s(ChoisePoint iX, Term a1) {
		ExtendedFileName fileName= retrieveRealLocalFileName(a1,iX);
		saveTextContent(fileName,iX);
	}
	//
	protected void saveTextContent(ExtendedFileName fileName, ChoisePoint iX) {
		CharacterSet requestedCharacterSet= getCharacterSet(iX);
		claimModifyingAccess();
		HashMap<String,DatabaseTableContainer> hash= tableHash.get();
		synchronized (hash) {
			StringBuilder textBuffer= new StringBuilder();
			if (requestedCharacterSet.isDummy()) {
				saveToTextBuffer(hash,textBuffer,iX,null);
			} else {
				CharsetEncoder encoder= requestedCharacterSet.toCharSet().newEncoder();
				saveToTextBuffer(hash,textBuffer,iX,encoder);
			};
			try {
				fileName.writeTextFile(textBuffer.toString(),requestedCharacterSet);
			} catch (IOException e) {
				throw new FileInputOutputError(fileName.toString(),e);
			}
		}
	}
	//
	protected void saveToTextBuffer(HashMap<String,DatabaseTableContainer> hash, StringBuilder textBuffer, ChoisePoint iX, CharsetEncoder encoder) {
		Set<String> setOfKeys= hash.keySet();
		String[] arrayOfKeys= setOfKeys.toArray(new String[setOfKeys.size()]);
		Arrays.sort(arrayOfKeys);
		for (int n=0; n < arrayOfKeys.length; n++) {
			String tableName= FormatOutput.encodeString(arrayOfKeys[n],false,encoder);
			DatabaseTableContainer tableContainer= hash.get(arrayOfKeys[n]);
			tableContainer.saveToTextBuffer(tableName,textBuffer,iX,encoder);
		}
	}
	//
	public void loadText1s(ChoisePoint iX, Term a1) {
		ExtendedFileName fileName= retrieveRealGlobalFileName(a1,iX);
		loadTextContent(fileName,staticContext,iX);
	}
	//
	@SuppressWarnings("unchecked")
	protected void loadTextContent(ExtendedFileName fileName, StaticContext staticContext, ChoisePoint iX) {
		CharacterSet requestedCharacterSet= getCharacterSet(iX);
		int timeout= getMaximalWaitingTimeInMilliseconds(iX);
		claimModifyingAccess();
		recentErrorText= "";
		recentErrorPosition= -1;
		recentErrorException= null;
		try {
			String textBuffer= fileName.getTextData(timeout,requestedCharacterSet,staticContext);
			try {
				HashMap<String,DatabaseTableContainer> hash= new HashMap<>();
				loadContent(textBuffer,hash,iX);
				tableHash.set(hash);
			} catch (SyntaxError e) {
				recentErrorText= textBuffer.toString();
				recentErrorPosition= e.getPosition();
				recentErrorException= e;
				throw new ActorPrologParserError(e);
			} catch (DatabaseRecordDoesNotBelongToDomain e) {
				recentErrorText= e.getText();
				recentErrorPosition= e.getPosition();
				recentErrorException= e;
				throw new WrongTermDoesNotBelongToDomain(e.getItem());
			} catch (RuntimeException e) {
				if (recentErrorException==null) {
					recentErrorException= e;
				};
				throw e;
			}
		} catch (CannotRetrieveContent e) {
			throw new FileInputOutputError(fileName.toString(),e);
		}
	}
	//
	public void loadContent(String textBuffer, HashMap<String,DatabaseTableContainer> hash, ChoisePoint iX) throws SyntaxError, DatabaseRecordDoesNotBelongToDomain {
		GroundTermParser parser= new GroundTermParser(dummyParserMaster,true);
		Term[] terms= parser.stringToTerms(textBuffer,null);
		boolean optimizeSets= DefaultOptions.underdeterminedSetsOptimizationIsEnabled;
		String currentEntry= null;
		DatabaseType currentDatabaseType= DatabaseType.PLAIN;
		Boolean currentReuseKeyNumbers= true;
		PrologDomain currentTargetDomain= null;
		HashMap<String,PrologDomain> currentLocalDomainTable= new HashMap<>();
		DatabaseTable currentTable= null;
		DatabaseTableContainer currentContainer= null;
		for (int k=0; k < terms.length; k++) {
			Term item= terms[k];
			try {
				long functor= item.getStructureFunctor(iX);
				Term[] arguments= item.getStructureArguments(iX);
				if (arguments.length==1) {
					Term value= arguments[0];
					if (functor==SymbolCodes.symbolCode_E_entry) {
						if (currentContainer != null) {
							hash.put(currentEntry,currentContainer);
						};
						currentEntry= value.getStringValue(iX);
						currentDatabaseType= DatabaseType.PLAIN;
						currentReuseKeyNumbers= true;
						currentTargetDomain= null;
						currentLocalDomainTable.clear();
						currentTable= null;
						currentContainer= null;
					} else if (functor==SymbolCodes.symbolCode_E_database_type) {
						currentDatabaseType= DatabaseType.argumentToDatabaseType(value,iX);
					} else if (functor==SymbolCodes.symbolCode_E_reuse_key_numbers) {
						currentReuseKeyNumbers= YesNoConverters.termYesNo2Boolean(value,iX);
					} else if (functor==SymbolCodes.symbolCode_E_target_domain) {
						try {
							currentTargetDomain= PrologDomain.argumentToPrologDomain(value,iX);
						} catch (TermIsNotPrologDomain e) {
							throw new DatabaseRecordDoesNotBelongToDomain(item,textBuffer.toString(),item.getPosition());
						} catch (Throwable e) {
							throw new DatabaseRecordDoesNotBelongToDomain(item,textBuffer.toString(),item.getPosition(),e);
						}
					} else if (functor==SymbolCodes.symbolCode_E_record) {
						if (currentContainer == null) {
							if (currentTargetDomain == null) {
								currentTargetDomain= new PrologAnyDomain();
							};
							currentTargetDomain.acceptLocalDomainTable(currentLocalDomainTable);
							Set<String> keys= currentLocalDomainTable.keySet();
							Iterator<String> iterator= keys.iterator();
							while (iterator.hasNext()) {
								String key= iterator.next();
								PrologDomain localDomain= currentLocalDomainTable.get(key);
								localDomain.acceptLocalDomainTable(currentLocalDomainTable);
							};
							currentTable= currentDatabaseType.createTable(currentTargetDomain,currentReuseKeyNumbers,currentLocalDomainTable);
							currentContainer= new DatabaseTableContainer(currentTable,true);
							currentTable.acceptAttributes(currentEntry,currentContainer);
						};
						try {
							if (optimizeSets) {
								// It can be optimized:
								value= currentTargetDomain.checkAndOptimizeTerm(value,iX);
							} else {
								value= currentTargetDomain.checkTerm(value,iX);
							};
							currentTable.appendRecord(value,currentProcess,false,iX,false);
						} catch (DomainAlternativeDoesNotCoverTerm e) {
							throw new DatabaseRecordDoesNotBelongToDomain(value,textBuffer,e.getPosition());
						}
					} else {
						throw new DatabaseRecordDoesNotBelongToDomain(item,textBuffer.toString(),item.getPosition());
					}
				} else if (arguments.length==2) {
					if (functor==SymbolCodes.symbolCode_E_domain) {
						try {
							String domainName= arguments[0].getStringValue(iX);
							PrologDomain prologDomain= PrologDomain.argumentToPrologDomain(arguments[1],iX);
							currentLocalDomainTable.put(domainName,prologDomain);
						} catch (TermIsNotPrologDomain e) {
							throw new DatabaseRecordDoesNotBelongToDomain(item,textBuffer.toString(),item.getPosition());
						} catch (Throwable e) {
							throw new DatabaseRecordDoesNotBelongToDomain(item,textBuffer.toString(),item.getPosition(),e);
						}
					} else {
						throw new DatabaseRecordDoesNotBelongToDomain(item,textBuffer.toString(),item.getPosition());
					}
				} else {
					throw new DatabaseRecordDoesNotBelongToDomain(item,textBuffer.toString(),item.getPosition());
				}
			} catch (TermIsNotAStructure e) {
				throw new DatabaseRecordDoesNotBelongToDomain(item,textBuffer.toString(),item.getPosition());
			} catch (TermIsNotAString e) {
				throw new DatabaseRecordDoesNotBelongToDomain(item,textBuffer.toString(),item.getPosition());
			} catch (WrongArgumentIsNotDatabaseType e) {
				throw new DatabaseRecordDoesNotBelongToDomain(item,textBuffer.toString(),item.getPosition());
			}
		};
		if (currentContainer != null) {
			hash.put(currentEntry,currentContainer);
		};
		sendInternalMessage();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void recentLoadingError4s(ChoisePoint iX, PrologVariable a1, PrologVariable a2, PrologVariable a3, PrologVariable a4) throws Backtracking {
		claimReadingAccess();
		if (recentErrorException != null && recentErrorText != null) {
			a1.setBacktrackableValue(new PrologString(recentErrorText),iX);
			a2.setBacktrackableValue(new PrologInteger(recentErrorPosition),iX);
			a3.setBacktrackableValue(new PrologString(recentErrorException.toString()),iX);
			a4.setBacktrackableValue(new PrologString(recentErrorException.toString()),iX);
		} else {
			throw Backtracking.instance;
		}
	}
	public void recentLoadingError3s(ChoisePoint iX, PrologVariable a1, PrologVariable a2, PrologVariable a3) throws Backtracking {
		claimReadingAccess();
		if (recentErrorException != null && recentErrorText != null) {
			a1.setBacktrackableValue(new PrologString(recentErrorText),iX);
			a2.setBacktrackableValue(new PrologInteger(recentErrorPosition),iX);
			a3.setBacktrackableValue(new PrologString(recentErrorException.toString()),iX);
		} else {
			throw Backtracking.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void repair0ff(ChoisePoint iX, PrologVariable result) {
		ExtendedFileName fileName= retrieveRealGlobalFileName(iX);
		result.setNonBacktrackableValue(new PrologInteger(repairContent(fileName,false,staticContext,iX)));
	}
	public void repair0fs(ChoisePoint iX) {
		ExtendedFileName fileName= retrieveRealGlobalFileName(iX);
		repairContent(fileName,false,staticContext,iX);
	}
	//
	public void repair1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		boolean reportActions= YesNoConverters.termYesNo2Boolean(a1,iX);
		ExtendedFileName fileName= retrieveRealGlobalFileName(iX);
		result.setNonBacktrackableValue(new PrologInteger(repairContent(fileName,reportActions,staticContext,iX)));
	}
	public void repair1fs(ChoisePoint iX, Term a1) {
		boolean reportActions= YesNoConverters.termYesNo2Boolean(a1,iX);
		ExtendedFileName fileName= retrieveRealGlobalFileName(iX);
		repairContent(fileName,reportActions,staticContext,iX);
	}
	//
	public void repair2ff(ChoisePoint iX, PrologVariable result, Term a1, Term a2) {
		boolean reportActions= YesNoConverters.termYesNo2Boolean(a1,iX);
		ExtendedFileName fileName= retrieveRealGlobalFileName(a2,iX);
		result.setNonBacktrackableValue(new PrologInteger(repairContent(fileName,reportActions,staticContext,iX)));
	}
	public void repair2fs(ChoisePoint iX, Term a1, Term a2) {
		boolean reportActions= YesNoConverters.termYesNo2Boolean(a1,iX);
		ExtendedFileName fileName= retrieveRealGlobalFileName(a2,iX);
		repairContent(fileName,reportActions,staticContext,iX);
	}
	//
	@SuppressWarnings("unchecked")
	protected long repairContent(ExtendedFileName fileName, boolean reportActions, StaticContext staticContext, ChoisePoint iX) {
		int timeout= getMaximalWaitingTimeInMilliseconds(iX);
		claimModifyingAccess();
		try {
			tableHash.set((HashMap<String,DatabaseTableContainer>)fileName.readObject(timeout,staticContext));
			if (reportActions) {
				System.err.printf(messageNoDamagesAreFoundInTheDataStore,fileName);
			};
			openAndCloseDataStore(fileName,DataStoreUnpackMode.REQUESTED,DataStoreErrorHandling.NONE,reportActions,iX);
			if (reportActions) {
				System.err.printf(messageTheDataStoreIsUnpackedAnew,fileName);
			};
			return mode_NoDatastoreDamage;
		} catch (Throwable e1) {
			try {
				openAndCloseDataStore(fileName,DataStoreUnpackMode.DISABLED,DataStoreErrorHandling.NONE,reportActions,iX);
				tableHash.set((HashMap<String,DatabaseTableContainer>)fileName.readObject(timeout,staticContext));
				if (reportActions) {
					System.err.printf(messageTheDataStoreIsReassembled,fileName);
				};
				return mode_DatastoreFileIsReassembled;
			} catch (Throwable e2) {
				try {
					ExtendedFileName bakFile= fileName.modifyFileExtension(".bak",true,currentDirectory,staticContext);
					tableHash.set((HashMap<String,DatabaseTableContainer>)bakFile.readObject(timeout,staticContext));
					bakFile.copyFile(fileName);
					openAndCloseDataStore(fileName,DataStoreUnpackMode.REQUESTED,DataStoreErrorHandling.NONE,reportActions,iX);
					if (reportActions) {
						System.err.printf(messageTheDataStoreLoadedFromThe_BAK_File,fileName);
					};
					return mode_DataStore_BAK_FileIsUsed;
				} catch (Throwable e3) {
					try {
						openAndCloseDataStore(fileName,DataStoreUnpackMode.DISABLED,DataStoreErrorHandling.IGNORE_DAMAGED_TABLES,reportActions,iX);
						tableHash.set((HashMap<String,DatabaseTableContainer>)fileName.readObject(timeout,staticContext));
						if (reportActions) {
							System.err.printf(messageTheDataStoreContainsDamagedTables,fileName);
						};
						return mode_DatastoreContainsDamagedTables;
					} catch (Throwable e4) {
						try {
							if (reportActions) {
								System.err.printf(messageTheDataStoreIndexIsDamaged,fileName);
							};
							openAndCloseDataStore(fileName,DataStoreUnpackMode.DISABLED,DataStoreErrorHandling.REASSEMBLE_INDEX,reportActions,iX);
							tableHash.set((HashMap<String,DatabaseTableContainer>)fileName.readObject(timeout,staticContext));
							return mode_DatastoreIndexIsDamaged;
						} catch (Throwable e5) {
							return mode_DatastoreContentIsTotallyDamaged;
						}
					}
				}
			}
		}
	}
	//
	protected void openAndCloseDataStore(ExtendedFileName fileName, DataStoreUnpackMode unpackDataStore, DataStoreErrorHandling ingoreErrors, boolean reportActions, ChoisePoint iX) {
		OpenedDataStore auxiliaryDataStore= null;
		try {
			auxiliaryDataStore= openNewDataStore(fileName,DatabaseAccessMode.MODIFYING,getSharingMode(iX),unpackDataStore,iX);
			synchronized (auxiliaryDataStore) {
				auxiliaryLock.lock();
				try {
					if (unpackDataStore==DataStoreUnpackMode.DISABLED) {
						if (auxiliaryDataStore.isUnlocked()) {
							auxiliaryDataStore.lockDataStore(DatabaseAccessMode.MODIFYING,ingoreErrors,reportActions,externalFileIsFree);
						};
						try {
							auxiliaryDataStore.unsafelyPackDataStore(ingoreErrors,reportActions,externalFileIsFree);
						} finally {
							auxiliaryDataStore.unlockDataStoreFile();
						}
					};
					synchronized (openedDataStoreList) {
						openedDataStoreList.remove(auxiliaryDataStore);
					};
					auxiliaryDataStore.destroy();
					// auxiliaryDataStore= null;
				} finally {
					auxiliaryLock.unlock();
				}
			}
		} finally {
			if (auxiliaryDataStore != null) {
				synchronized (openedDataStoreList) {
					openedDataStoreList.remove(auxiliaryDataStore);
				};
				auxiliaryDataStore.destroy();
				// auxiliaryDataStore= null;
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void open0s(ChoisePoint iX) {
		ExtendedFileName fileName= retrieveRealLocalFileName(iX);
		openDataStore(fileName,getAccessMode(iX),getSharingMode(iX),DataStoreUnpackMode.ENABLED,iX);
	}
	public void open2s(ChoisePoint iX, Term a1, Term a2) {
		DatabaseAccessMode access= DatabaseAccessMode.argumentToDatabaseAccessMode(a1,iX);
		DatabaseSharingMode sharing= DatabaseSharingMode.argumentToDatabaseSharingMode(a2,iX);
		ExtendedFileName fileName= retrieveRealLocalFileName(iX);
		openDataStore(fileName,access,sharing,DataStoreUnpackMode.ENABLED,iX);
	}
	public void open3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		ExtendedFileName fileName= retrieveRealLocalFileName(a1,iX);
		DatabaseAccessMode access= DatabaseAccessMode.argumentToDatabaseAccessMode(a2,iX);
		DatabaseSharingMode sharing= DatabaseSharingMode.argumentToDatabaseSharingMode(a3,iX);
		openDataStore(fileName,access,sharing,DataStoreUnpackMode.ENABLED,iX);
	}
	//
	protected void openDataStore(ExtendedFileName fileName, DatabaseAccessMode access, DatabaseSharingMode sharing, DataStoreUnpackMode unpackDataStore, ChoisePoint iX) {
		String subdirectoryName= fileName.discardFileExtension();
		Path subdirectoryPath= fileSystem.getPath(subdirectoryName);
		subdirectoryPath= subdirectoryPath.toAbsolutePath();
		OpenedDataStore dataStore= openedDataStore.get();
		if (dataStore==null) {
			openedDataStore.set(openNewDataStore(fileName,access,sharing,unpackDataStore,iX));
		} else {
			synchronized (dataStore) {
				auxiliaryLock.lock();
				try {
					dataStore.initiate(unpackDataStore,externalFileIsFree);
					if (dataStore.isSameFile(subdirectoryPath)) {
						dataStore.checkAccessMode(access);
						dataStore.checkSharingMode(sharing);
					} else {
						throw new DataStoreIsAlreadyOpenedWithAnotherFileName(subdirectoryPath.toString(),dataStore.getSubdirectoryPath().toString());
					}
				} finally {
					auxiliaryLock.unlock();
				}
			}
		}
	}
	//
	protected OpenedDataStore openNewDataStore(ExtendedFileName fileName, DatabaseAccessMode access, DatabaseSharingMode sharing, DataStoreUnpackMode unpackDataStore, ChoisePoint iX) {
		Path dataStorePath= fileName.getPathOfLocalResource();
		String subdirectoryName= fileName.discardFileExtension();
		Path subdirectoryPath= fileSystem.getPath(subdirectoryName);
		dataStorePath= dataStorePath.toAbsolutePath();
		subdirectoryPath= subdirectoryPath.toAbsolutePath();
		OpenedDataStore newDataStore= null;
		try {
			synchronized (openedDataStoreList) {
				Iterator<OpenedDataStore> iterator= openedDataStoreList.iterator();
				while (iterator.hasNext()) {
					OpenedDataStore currentStore= iterator.next();
					if (currentStore.isSameFile(subdirectoryPath)) {
						throw new DataStoreIsAlreadyOpened(subdirectoryPath.toString(),currentStore.getSubdirectoryPath().toString());
					}
				};
				TimeInterval waitingPeriod= getTransactionWaitingPeriod(iX);
				TimeInterval sleepPeriod= getTransactionSleepPeriod(iX);
				BigInteger maximalRetryNumber= getTransactionMaximalRetryNumber(iX);
				newDataStore= new OpenedDataStore(fileName,dataStorePath,subdirectoryPath,currentProcess,access,sharing,waitingPeriod,sleepPeriod,maximalRetryNumber);
				openedDataStoreList.add(newDataStore);
			};
			synchronized (newDataStore) {
				auxiliaryLock.lock();
				try {
					newDataStore.initiate(unpackDataStore,externalFileIsFree);
					if (!newDataStore.isValid()) {
						throw new CannotAccessSharedDataStore(fileName.toString());
					}
				} finally {
					auxiliaryLock.unlock();
				}
			}
		} catch (Throwable e) {
			if (newDataStore != null) {
				synchronized (openedDataStoreList) {
					openedDataStoreList.remove(newDataStore);
				};
				newDataStore.destroy();
			};
			throw e;
		};
		return newDataStore;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void close0s(ChoisePoint iX) {
		OpenedDataStore dataStore= openedDataStore.get();
		if (dataStore != null) {
			synchronized (dataStore) {
				endAllTransactionsAndPackDataStore(iX);
				dataStore.destroy();
				synchronized (openedDataStoreList) {
					openedDataStoreList.remove(dataStore);
				};
				openedDataStore.set(null);
				tableHash.set(new HashMap<String,DatabaseTableContainer>());
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@SuppressWarnings("unchecked")
	public void beginTransaction1s(ChoisePoint iX, Term a1) throws Backtracking {
		DatabaseAccessMode transactionMode= DatabaseAccessMode.argumentToDatabaseAccessMode(a1,iX);
		boolean reuseNumbers= getReuseTableNumbers(iX);
		TimeInterval waitingPeriod= getTransactionWaitingPeriod(iX);
		TimeInterval sleepPeriod= getTransactionSleepPeriod(iX);
		BigInteger maximalRetryNumber= getTransactionMaximalRetryNumber(iX);
		long nanosTimeout= waitingPeriod.toNanosecondsLong();
		OpenedDataStore dataStore= openedDataStore.get();
		if (dataStore != null) {
			if (transactionMode==DatabaseAccessMode.MODIFYING && dataStore.getAccessMode()==DatabaseAccessMode.READING) {
				throw new DataStoreIsNotInModifyingAccessMode();
			}
		};
		try {
			if (!writeLock.tryLock(nanosTimeout,TimeUnit.NANOSECONDS)) {
				throw Backtracking.instance;
			}
		} catch (InterruptedException e) {
			throw Backtracking.instance;
		};
		Integer previousLengthOfAccessModesStack= null;
		try {
			if (accessModesStack.isEmpty()) {
				HashMap<String,DatabaseTableContainer> hash= tableHash.get();
				synchronized (hash) {
					int totalNumberOfEntries= hash.size();
					HashMap<String,DatabaseTableContainer> tableHashCopy= new HashMap<>();
					BigInteger numberOfUnsuccessfulAttempts= BigInteger.ZERO;
					while (true) {
						Set<String> setOfKeysOfTableHashCopy= tableHashCopy.keySet();
						Iterator<String> iteratorOfSetOfKeysOfTableHashCopy= setOfKeysOfTableHashCopy.iterator();
						while (iteratorOfSetOfKeysOfTableHashCopy.hasNext()) {
							String currentKey= iteratorOfSetOfKeysOfTableHashCopy.next();
							DatabaseTableContainer tableContainer= tableHashCopy.get(currentKey);
							if (tableContainer != null) {
								try {
									tableContainer.beginTransaction(transactionMode,waitingPeriod,sleepPeriod,BigInteger.ZERO/*maximalRetryNumber*/,this,currentProcess,currentKey,this,dataStore,reuseNumbers,iX);
									lockedTables.add(tableContainer);
									iteratorOfSetOfKeysOfTableHashCopy.remove();
								} catch (Backtracking b) {
									numberOfUnsuccessfulAttempts= numberOfUnsuccessfulAttempts.add(BigInteger.ONE);
									if (numberOfUnsuccessfulAttempts.compareTo(maximalRetryNumber.multiply(BigInteger.valueOf(totalNumberOfEntries))) > 0) {
										unlockAllLockedTables(dataStore);
										throw Backtracking.instance;
									}
								}
							}
						};
						boolean continueLoop= false;
						Set<String> setOfKeysOfUpdatedTable= hash.keySet();
						Iterator<String> iteratorOfSetOfKeysOfUpdatedTable= setOfKeysOfUpdatedTable.iterator();
						while (iteratorOfSetOfKeysOfUpdatedTable.hasNext()) {
							String currentKey= iteratorOfSetOfKeysOfUpdatedTable.next();
							DatabaseTableContainer tableContainer= hash.get(currentKey);
							if (!lockedTables.contains(tableContainer)) {
								if (tableHashCopy.get(currentKey)==null) {
									tableHashCopy.put(currentKey,tableContainer);
									totalNumberOfEntries= totalNumberOfEntries + 1;
								};
								continueLoop= true;
							}
						};
						if (!continueLoop) {
							break;
						};
					};
					if (dataStore != null) {
						synchronized (dataStore) {
							auxiliaryLock.lock();
							try {
								dataStore.lockDataStore(transactionMode,DataStoreErrorHandling.NONE,false,externalFileIsFree);
								unsafelyDownloadAllExternalFiles(dataStore);
							} finally {
								auxiliaryLock.unlock();
							}
						}
					};
					previousLengthOfAccessModesStack= accessModesStack.size();
					hash= tableHash.get();
					accessModesStack.push(new DataStoreAccessMode(transactionMode,hash));
				}
			} else {
				transactionMode.checkAuthority(accessModesStack.peek().getDatabaseAccessMode());
				previousLengthOfAccessModesStack= accessModesStack.size();
				HashMap<String,DatabaseTableContainer> hash= tableHash.get();
				accessModesStack.push(new DataStoreAccessMode(transactionMode,hash));
			};
			if (transactionMode==DatabaseAccessMode.MODIFYING) {
				HashMap<String,DatabaseTableContainer> hash= tableHash.get();
				HashMap<String,DatabaseTableContainer> copyOfTable= (HashMap<String,DatabaseTableContainer>)hash.clone();
				tableHash.set(copyOfTable);
			};
		} catch (Throwable e) {
			cancelTransaction(previousLengthOfAccessModesStack);
			throw e;
		}
	}
	//
	protected void cancelTransaction(Integer previousLengthOfAccessModesStack) {
		if (previousLengthOfAccessModesStack != null) {
			while (accessModesStack.size() > previousLengthOfAccessModesStack) {
				if (!accessModesStack.isEmpty()) {
					DataStoreAccessMode previousValue= accessModesStack.pop();
					tableHash.set(previousValue.getPreviousIndex());
				} else {
					break;
				}
			}
		};
		if (accessModesStack.isEmpty()) {
			OpenedDataStore dataStore= openedDataStore.get();
			if (dataStore != null) {
				synchronized (dataStore) {
					dataStore.unlockDataStoreFile();
				}
			};
			unlockAllLockedTables(dataStore);
		};
		writeLock.unlock();
	}
	//
	public void endTransaction0s(ChoisePoint iX) {
		if (accessModesStack.isEmpty()) {
			throw new NotInsideTransaction();
		} else {
			DataStoreAccessMode access= accessModesStack.pop();
			if (accessModesStack.isEmpty()) {
				isInformed.set(true);
				OpenedDataStore dataStore= openedDataStore.get();
				if (dataStore != null) {
					synchronized (dataStore) {
						auxiliaryLock.lock();
						try {
							dataStore.unsafelyWriteDataStoreIndex();
							if (access.getDatabaseAccessMode()==DatabaseAccessMode.MODIFYING) {
								dataStore.unsafelyPackDataStore(externalFileIsFree);
							};
							dataStore.unlockDataStoreFile();
						} finally {
							auxiliaryLock.unlock();
						};
						dataStore.resetExternalUpdate();
					}
				};
				unlockAllLockedTables(dataStore);
				boolean indexIsModified= access.getIndexIsModified();
				if (indexIsModified) {
					sendInternalMessage();
				}
			} else {
				accessModesStack.peek().setIndexIsModified(access);
			};
			writeLock.unlock();
		}
	}
	//
	protected void sendInternalMessage() {
		isInformed.set(false);
		if (isActive.get()) {
			long domainSignature= entry_s_Update_0();
			AsyncCall call= new AsyncCall(domainSignature,this,true,false,noArguments,true);
			transmitAsyncCall(call,null);
		}
	}
	//
	protected void endAllTransactionsAndPackDataStore(ChoisePoint iX) {
		accessModesStack.clear();
		isInformed.set(true);
		OpenedDataStore dataStore= openedDataStore.get();
		if (dataStore != null) {
			synchronized (dataStore) {
				auxiliaryLock.lock();
				try {
					if (dataStore.getAccessMode()==DatabaseAccessMode.MODIFYING) {
						if (dataStore.isUnlocked()) {
							dataStore.lockDataStore(DatabaseAccessMode.MODIFYING,DataStoreErrorHandling.NONE,false,externalFileIsFree);
						};
						try {
							dataStore.unsafelyPackDataStore(externalFileIsFree);
						} finally {
							dataStore.unlockDataStoreFile();
						}
					}
				} finally {
					auxiliaryLock.unlock();
				};
				dataStore.resetExternalUpdate();
			}
		};
		unlockAllLockedTables(dataStore);
		try {
			writeLock.unlock();
		} catch (Throwable e) {
		}
	}
	protected void unlockAllLockedTables(OpenedDataStore dataStore) {
		Iterator<DatabaseTableContainer> lockedTablesIterator= lockedTables.iterator();
		while (lockedTablesIterator.hasNext()) {
			DatabaseTableContainer tableContainer= lockedTablesIterator.next();
			if (tableContainer != null) {
				tableContainer.rollbackCurrentTransaction(null,currentProcess,dataStore,false);
			};
			lockedTablesIterator.remove();
		}
	}
	//
	public void rollbackTransaction0s(ChoisePoint iX) {
		if (accessModesStack.isEmpty()) {
			throw new NotInsideTransaction();
		} else {
			DataStoreAccessMode access= accessModesStack.pop();
			OpenedDataStore dataStore= openedDataStore.get();
			if (access.getDatabaseAccessMode()==DatabaseAccessMode.MODIFYING) {
				HashMap<String,DatabaseTableContainer> previousValue= access.getPreviousIndex();
				tableHash.set(previousValue);
				if (dataStore != null) {
					synchronized (dataStore) {
						auxiliaryLock.lock();
						try {
							synchronized (previousValue) {
								ExtendedFileName fileName= dataStore.getFileName();
								fileName.writeObject(previousValue);
								deleteUnpackedDataStore(fileName);
								dataStore.unsafelyUnpackAndMarkDataStore(DataStoreUnpackMode.REQUESTED,externalFileIsFree);
							}
						} finally {
							auxiliaryLock.unlock();
						}
					}
				}
			};
			if (accessModesStack.isEmpty()) {
				isInformed.set(true);
				if (dataStore != null) {
					synchronized (dataStore) {
						dataStore.unlockDataStoreFile();
						dataStore.resetExternalUpdate();
					}
				};
				unlockAllLockedTables(dataStore);
			};
			writeLock.unlock();
		}
	}
	//
	public void rollbackTransactionTree0s(ChoisePoint iX) {
		OpenedDataStore dataStore= openedDataStore.get();
		if (!accessModesStack.isEmpty()) {
			DataStoreAccessMode access= accessModesStack.getLast();
			HashMap<String,DatabaseTableContainer> previousValue= access.getPreviousIndex();
			tableHash.set(previousValue);
			if (dataStore != null) {
				synchronized (dataStore) {
					auxiliaryLock.lock();
					try {
						synchronized (previousValue) {
							ExtendedFileName fileName= dataStore.getFileName();
							fileName.writeObject(previousValue);
							deleteUnpackedDataStore(fileName);
							dataStore.unsafelyUnpackAndMarkDataStore(DataStoreUnpackMode.REQUESTED,externalFileIsFree);
						}
					} finally {
						auxiliaryLock.unlock();
					}
				}
			};
			accessModesStack.clear();
		};
		isInformed.set(true);
		if (dataStore != null) {
			synchronized (dataStore) {
				dataStore.unlockDataStoreFile();
				dataStore.resetExternalUpdate();
			}
		};
		unlockAllLockedTables(dataStore);
		writeLock.unlock();
	}
	//
	public void activate0s(ChoisePoint iX) {
		setWatchUpdates(true);
		OpenedDataStore dataStore= openedDataStore.get();
		if (dataStore != null) {
			dataStore.registerIndexWatcher(this);
		} else {
			isActive.set(true);
		}
	}
	//
	public void isUpdated0s(ChoisePoint iX) throws Backtracking {
		if (!isInformed.get()) {
			return;
		};
		OpenedDataStore dataStore= openedDataStore.get();
		if (dataStore != null) {
			if (!dataStore.isUpdated()) {
				throw Backtracking.instance;
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void claimReadingAccess() {
		if (accessModesStack.isEmpty()) {
			throw new NotInsideTransaction();
		}
	}
	protected void claimModifyingAccess() {
		if (accessModesStack.isEmpty()) {
			throw new NotInsideTransaction();
		} else {
			DataStoreAccessMode mode= accessModesStack.peek();
			if (mode.getDatabaseAccessMode()==DatabaseAccessMode.READING) {
				throw new NotInsideModifyingTransaction();
			};
			mode.registerModifyingAccess();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void delete0s(ChoisePoint iX) {
		ExtendedFileName fileName= retrieveRealLocalFileName(iX);
		deleteDataStore(fileName);
	}
	@Override
	public void delete1s(ChoisePoint iX, Term a1) {
		ExtendedFileName fileName= retrieveRealLocalFileName(a1,iX);
		deleteDataStore(fileName);
	}
	//
	protected void deleteDataStore(ExtendedFileName fileName) {
		fileName.delete();
		deleteUnpackedDataStore(fileName);
	}
	protected void deleteUnpackedDataStore(ExtendedFileName fileName) {
		Path subdirectoryPath= fileName.createSubdirectoryPath();
		if (Files.exists(subdirectoryPath)) {
			try {
				DirectoryStream<Path> stream= Files.newDirectoryStream(subdirectoryPath);
				try {
					for (Path targetPath: stream) {
						try {
							Files.deleteIfExists(targetPath);
						} catch (DirectoryNotEmptyException e) {
						} catch (IOException e) {
						}
					}
				} finally {
					try {
						stream.close();
					} catch (Throwable e) {
					}
				}
			} catch (DirectoryIteratorException e) {
				// I/O error encounted during the iteration, the cause is an IOException
				// throw e.getCause();
				throw new FileInputOutputError(subdirectoryPath.toString(),e);
			} catch (IOException e) {
				throw new FileInputOutputError(subdirectoryPath.toString(),e);
			}
		};
		try {
			Files.deleteIfExists(subdirectoryPath);
		} catch (IOException e) {
		}
	}
	//
	@Override
	public void rename1s(ChoisePoint iX, Term destination) {
		ExtendedFileName fileName2= retrieveRealLocalFileName(destination,iX);
		ExtendedFileName fileName1= retrieveRealLocalFileName(iX);
		renameDataStore(fileName1,fileName2);
	}
	@Override
	public void rename2s(ChoisePoint iX, Term source, Term destination) {
		ExtendedFileName fileName1= retrieveRealLocalFileName(source,iX);
		ExtendedFileName fileName2= retrieveRealLocalFileName(destination,iX);
		renameDataStore(fileName1,fileName2);
	}
	//
	protected void renameDataStore(ExtendedFileName fileName1, ExtendedFileName fileName2) {
		Path path1= fileName1.getPathOfLocalResource();
		Path path2= fileName2.getPathOfLocalResource();
		if (Files.exists(path2)) {
			try {
				if (Files.isSameFile(path1,path2)) {
					return;
				}
			} catch (Throwable e) {
			};
			throw new FileAlreadyExists(path2.toString());
		} else {
			fileName1.renameFile(fileName2);
			deleteUnpackedDataStore(fileName2);
			renameUnpackedDataStore(fileName1,fileName2);
		}
	}
	protected void renameUnpackedDataStore(ExtendedFileName fileName1, ExtendedFileName fileName2) {
		Path path1= fileName1.createSubdirectoryPath();
		Path path2= fileName2.createSubdirectoryPath();
		if (Files.exists(path1)) {
			try {
				Files.move(path1,path2); // StandardCopyOption.ATOMIC_MOVE
			} catch (IOException e) {
				throw new FileInputOutputError(path1.toString(),path2.toString(),e);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void collectGarbage0s(ChoisePoint iX) {
		claimModifyingAccess();
		OpenedDataStore dataStore= openedDataStore.get();
		if (dataStore != null) {
			synchronized (dataStore) {
				auxiliaryLock.lock();
				try {
					dataStore.collectGarbage(externalFileIsFree);
				} finally {
					auxiliaryLock.unlock();
				}
			}
		};
		System.runFinalization();
		System.gc();
	}
	//
	public void flush0s(ChoisePoint iX) {
		claimModifyingAccess();
		OpenedDataStore dataStore= openedDataStore.get();
		if (dataStore != null) {
			synchronized (dataStore) {
				auxiliaryLock.lock();
				try {
					dataStore.unsafelyPackDataStore(externalFileIsFree);
				} finally {
					auxiliaryLock.unlock();
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void update0s(ChoisePoint iX) {
	}
	//
	public class Update0s extends Continuation {
		public Update0s(Continuation aC) {
			c0= aC;
		}
		//
		@Override
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
}
