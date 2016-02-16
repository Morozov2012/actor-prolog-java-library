// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.datum;

import morozov.built_in.*;
import morozov.system.*;
import morozov.system.files.*;
import morozov.system.files.errors.*;
import morozov.worlds.*;

import java.nio.file.FileSystems;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.nio.file.NoSuchFileException;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.math.BigInteger;

public class OpenedDataStore extends DataStoreIO {
	//
	public boolean isValid= false;
	protected AtomicReference<UpdatesWatchHolder> watcher= new AtomicReference<UpdatesWatchHolder>(null);
	//
	protected AtomicBoolean isUpdated= new AtomicBoolean(false);
	//
	protected static final FileSystem fileSystem= FileSystems.getDefault();
	//
	protected static final String messageTheEntryIsDamagedInSection= "The %s entry is damaged (section %s).\n";
	//
	///////////////////////////////////////////////////////////////
	//
	public OpenedDataStore(ExtendedFileName storeName, Path storePath, Path subdirectory, ActiveWorld process, DatabaseAccessMode access, DatabaseSharingMode sharing, TimeInterval waiting, TimeInterval sleep, BigInteger maxRetry) {
		super(storeName,storePath,subdirectory,process,access,sharing,waiting,sleep,maxRetry);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void initiate(DataStoreUnpackMode unpackDataStore, Condition externalFileIsFree) {
		if (isValid) {
			return;
		} else {
			try {
				if (!Files.exists(subdirectoryPath)) {
					SimpleFileName.createDirectories(subdirectoryPath,false);
				};
				path_ExclusiveAccess= subdirectoryPath.resolve(fileName_ExclusiveAccess);
				path_WriteAccess= subdirectoryPath.resolve(fileName_WriteAccess);
				path_ModeMain= subdirectoryPath.resolve(fileName_ModeMain);
				path_ModeBackup1= subdirectoryPath.resolve(fileName_ModeBackup1);
				path_ModeBackup2= subdirectoryPath.resolve(fileName_ModeBackup2);
				path_IndexMain= subdirectoryPath.resolve(fileName_IndexMain);
				path_IndexBackup1= subdirectoryPath.resolve(fileName_IndexBackup1);
				path_IndexBackup2= subdirectoryPath.resolve(fileName_IndexBackup2);
				if (sharingMode==DatabaseSharingMode.EXCLUSIVE_ACCESS) {
					channel_ExclusiveAccess= FileChannel.open(path_ExclusiveAccess,StandardOpenOption.CREATE,StandardOpenOption.READ,StandardOpenOption.WRITE);
					lock_ExclusiveAccess= tryToLock(channel_ExclusiveAccess,false,sleepPeriod,maxRetryNumber,externalFileIsFree,path_ExclusiveAccess);
				} else {
					try {
						channel_ExclusiveAccess= FileChannel.open(path_ExclusiveAccess,StandardOpenOption.CREATE,StandardOpenOption.READ);
					} catch (NoSuchFileException e) {
						channel_ExclusiveAccess= FileChannel.open(path_ExclusiveAccess,StandardOpenOption.CREATE,StandardOpenOption.READ,StandardOpenOption.WRITE);
					};
					lock_ExclusiveAccess= tryToLock(channel_ExclusiveAccess,true,sleepPeriod,maxRetryNumber,externalFileIsFree,path_ExclusiveAccess);
					if (sharingMode==DatabaseSharingMode.EXCLUSIVE_WRITING) {
						channel_WriteAccess= FileChannel.open(path_WriteAccess,StandardOpenOption.CREATE,StandardOpenOption.READ,StandardOpenOption.WRITE);
						lock_WriteAccess= tryToLock(channel_WriteAccess,false,sleepPeriod,maxRetryNumber,externalFileIsFree,path_WriteAccess);
					} else {
						if (accessMode==DatabaseAccessMode.MODIFYING) {
							try {
								channel_WriteAccess= FileChannel.open(path_WriteAccess,StandardOpenOption.CREATE,StandardOpenOption.READ);
							} catch (NoSuchFileException e) {
								channel_WriteAccess= FileChannel.open(path_WriteAccess,StandardOpenOption.CREATE,StandardOpenOption.READ,StandardOpenOption.WRITE);
							};
							lock_WriteAccess= tryToLock(channel_WriteAccess,true,sleepPeriod,maxRetryNumber,externalFileIsFree,path_WriteAccess);
						}
					}
				};
				if (accessMode==DatabaseAccessMode.MODIFYING) {
					channel_ModeMain= FileChannel.open(path_ModeMain,StandardOpenOption.CREATE,StandardOpenOption.READ,StandardOpenOption.WRITE);
				} else {
					try {
						channel_ModeMain= FileChannel.open(path_ModeMain,StandardOpenOption.CREATE,StandardOpenOption.READ);
					} catch (NoSuchFileException e) {
						channel_ModeMain= FileChannel.open(path_ModeMain,StandardOpenOption.CREATE,StandardOpenOption.READ,StandardOpenOption.WRITE);
					}
				};
				if (unpackDataStore == DataStoreUnpackMode.ENABLED) {
					boolean dataStoreIsUnpacked= dataStoreIsUnpacked(true,externalFileIsFree);
					if (!dataStoreIsUnpacked) {
						unpackAndMarkDataStore(unpackDataStore,externalFileIsFree);
					}
				} else if (unpackDataStore == DataStoreUnpackMode.REQUESTED) {
					unpackAndMarkDataStore(unpackDataStore,externalFileIsFree);
				}
			} catch (IOException e1) {
				destroy();
				throw new FileInputOutputError(dataStoreName,e1);
			} catch (Throwable e1) {
				destroy();
				throw e1;
			};
			isValid= true;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void destroy() {
		try {
			if (lock_ExclusiveAccess != null) {
				if (lock_ExclusiveAccess.isValid()) {
					lock_ExclusiveAccess.release();
				};
			}
		} catch (Throwable e) {
		} finally {
			lock_ExclusiveAccess= null;
		};
		try {
			if (lock_WriteAccess != null) {
				if (lock_WriteAccess.isValid()) {
					lock_WriteAccess.release();
				};
			}
		} catch (Throwable e) {
		} finally {
			lock_WriteAccess= null;
		};
		try {
			if (channel_ExclusiveAccess != null) {
				channel_ExclusiveAccess.close();
			}
		} catch (Throwable e) {
		} finally {
			channel_ExclusiveAccess= null;
		};
		try {
			if (channel_WriteAccess != null) {
				channel_WriteAccess.close();
			}
		} catch (Throwable e) {
		} finally {
			channel_WriteAccess= null;
		};
		try {
			lock_Mode.release();
			lock_Mode= null;
		} catch (Throwable e) {
		};
		try {
			if (channel_ModeMain != null) {
				channel_ModeMain.close();
			}
		} catch (Throwable e) {
		} finally {
			channel_ModeMain= null;
		};
		isValid= false;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void unpackAndMarkDataStore(DataStoreUnpackMode unpackDataStore, Condition externalFileIsFree) {
		lock_Mode= tryToLock(channel_ModeMain,false,sleepPeriod,maxRetryNumber,externalFileIsFree,path_ModeMain);
		try {
			unsafelyUnpackAndMarkDataStore(unpackDataStore,externalFileIsFree);
		} finally {
			try {
				lock_Mode.release();
				lock_Mode= null;
			} catch (Throwable e) {
			}
		}
	}
	//
	public void unsafelyUnpackAndMarkDataStore(DataStoreUnpackMode unpackDataStore, Condition externalFileIsFree) {
		try {
			ByteBuffer destination= ByteBuffer.allocate(2);
			if (unpackDataStore == DataStoreUnpackMode.ENABLED) {
				channel_ModeMain.position(0);
				int nBytes= channel_ModeMain.read(destination);
				if (nBytes >= 2 && destination.getShort(0)==1) {
					return;
				}
			};
			unsafelyUnpackDataStore(externalFileIsFree);
			destination.putShort(0,(short)1);
			channel_ModeMain.position(0);
			channel_ModeMain.write(destination);
			updateModeBackups(externalFileIsFree);
		} catch (IOException e) {
			throw new FileInputOutputError(path_ModeMain.toString(),e);
		}
	}
	//
	protected void unsafelyUnpackDataStore(Condition externalFileIsFree) {
		HashMap<String,DatabaseTableContainer> tableHash;
		if (Files.exists(dataStorePath)) {
			tableHash= unsafelyReadDataStore();
		} else {
			tableHash= new HashMap<String,DatabaseTableContainer>();
			unsafelyWriteDataStore(tableHash);
		};
		HashMap<String,Long> tableIndex= new HashMap<>();
		Set<String> keys= tableHash.keySet();
		Iterator<String> iterator1= keys.iterator();
		long counter= maximalSystemSectionNumber;
		while (iterator1.hasNext()) {
			counter= counter + 1;
			String key= iterator1.next();
			DatabaseTableContainer currentTableContainer= tableHash.get(key);
			currentTableContainer.setNumberOfExternalFile(counter,subdirectoryPath);
			unsafelyWriteSection(currentTableContainer,externalFileIsFree);
			tableIndex.put(key,counter);
		};
		SectionIndex sectionIndex= new SectionIndex(tableIndex,counter);
		unsafelyWriteDataStoreIndex(sectionIndex);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void safelyPackDataStore(Condition externalFileIsFree) {
		lock_Mode= tryToLock(channel_ModeMain,false,sleepPeriod,maxRetryNumber,externalFileIsFree,path_ModeMain);
		try {
			unsafelyPackDataStore(externalFileIsFree);
		} finally {
			try {
				lock_Mode.release();
				lock_Mode= null;
			} catch (Throwable e) {
			}
		}
	}
	//
	public void unsafelyPackDataStore(Condition externalFileIsFree) {
		unsafelyPackDataStore(DataStoreErrorHandling.NONE,false,externalFileIsFree);
	}
	public void unsafelyPackDataStore(DataStoreErrorHandling ignoreErrors, boolean reportActions, Condition externalFileIsFree) {
		ByteBuffer destination= ByteBuffer.allocate(2);
		try {
			channel_ModeMain.position(0);
			int nBytes= channel_ModeMain.read(destination);
			if (nBytes >= 2 && destination.getShort(0)==1) {
				unsafelyAssembleAndWriteDataStore(ignoreErrors,reportActions,externalFileIsFree);
			}
		} catch (IOException e) {
			throw new FileInputOutputError(path_ModeMain.toString(),e);

		}
	}
	//
	protected void unsafelyAssembleAndWriteDataStore(DataStoreErrorHandling ignoreErrors, boolean reportActions, Condition externalFileIsFree) {
		HashMap<String,DatabaseTableContainer> tableHash= unsafelyAssembleDataStore(ignoreErrors,reportActions,externalFileIsFree);
		fileName.create_BAK_File();
		unsafelyWriteDataStore(tableHash);
	}
	public HashMap<String,DatabaseTableContainer> unsafelyAssembleDataStore(DataStoreErrorHandling ignoreErrors, boolean reportActions, Condition externalFileIsFree) {
		SectionIndex index;
		if (lock_Mode != null) {
			index= lockedSectionIndex;
		} else {
			index= unsafelyReadDataStoreIndex();
		};
		HashMap<String,Long> tableIndex= index.table;
		HashMap<String,DatabaseTableContainer> tableHash= new HashMap<>();
		Set<String> keys= tableIndex.keySet();
		Iterator<String> iterator1= keys.iterator();
		while (iterator1.hasNext()) {
			String key= iterator1.next();
			Long sectionNumber= tableIndex.get(key);
			String sectionLockFileName= String.format(externalBackupLockFormat,sectionNumber);
			Path sectionLockFilePath= subdirectoryPath.resolve(sectionLockFileName);
			try {
				FileChannel sectionChannel= FileChannel.open(sectionLockFilePath,StandardOpenOption.CREATE,StandardOpenOption.READ,StandardOpenOption.WRITE);
				try {
					tryToLock(sectionChannel,true,sleepPeriod,maxRetryNumber,externalFileIsFree,sectionLockFilePath);
					String sectionFileName= String.format(OpenedDataStore.externalBackupDataFormat,sectionNumber);
					Path sectionPath= subdirectoryPath.resolve(sectionFileName);
					DatabaseTable currentTable= unsafelyReadSection(sectionPath);
					DatabaseTableContainer currentContainer= new DatabaseTableContainer(currentTable,true);
					tableHash.put(key,currentContainer);
				} catch (Throwable e) {
					if (ignoreErrors==DataStoreErrorHandling.NONE) {
						throw e;
					} else {
						if (reportActions) {
							System.err.printf(messageTheEntryIsDamagedInSection,key,sectionNumber);
						}
					}
				} finally {
					try {
						sectionChannel.close();
					} catch (Throwable e) {
					}
				}
			} catch (IOException e) {
				throw new FileInputOutputError(sectionLockFilePath.toString(),e);
			}
		};
		return tableHash;
	}
	//
	public void registerWatcher(Database database, LoadableContainer container, Path sectionPath_MainData) {
		if (database != null) {
			UpdatesWatchHolder w= getWatchHolder();
			w.register(database,container,sectionPath_MainData);
		}
	}
	public void registerIndexWatcher(DataStore dataStore) {
		if (dataStore != null) {
			UpdatesWatchHolder w= getWatchHolder();
			w.register(dataStore,null,path_IndexMain);
		}
	}
	//
	protected UpdatesWatchHolder getWatchHolder() {
		synchronized (watcher) {
			UpdatesWatchHolder w= watcher.get();
			if (w==null) {
				w= new UpdatesWatchHolder(this,subdirectoryPath);
				watcher.set(w);
				w.start();
			};
			return w;
		}
	}
	//
	public void unregisterWatcher(Database database, Path sectionPath_MainData) {
		if (database != null) {
			UpdatesWatchHolder w= watcher.get();
			if (w != null) {
				w.unregister(database,sectionPath_MainData);
			}
		}
	}
	//
	public void unregisterIndexWatcher(DataStore dataStore) {
		if (dataStore != null) {
			UpdatesWatchHolder w= watcher.get();
			if (w != null) {
				w.unregister(dataStore,path_IndexMain);
			}
		}
	}
	//
	public void reportExternalUpdate() {
		isUpdated.set(true);
	}
	//
	public void resetExternalUpdate() {
		isUpdated.set(false);
	}
	//
	public boolean isUpdated() {
		return isUpdated.get();
	}
}
