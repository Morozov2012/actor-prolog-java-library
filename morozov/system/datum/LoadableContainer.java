// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.datum;

import morozov.built_in.*;
import morozov.domains.errors.*;
import morozov.run.*;
import morozov.run.errors.*;
import morozov.syntax.errors.*;
import morozov.syntax.interfaces.*;
import morozov.syntax.scanner.errors.*;
import morozov.system.checker.signals.*;
import morozov.system.files.*;
import morozov.system.files.errors.*;
import morozov.system.datum.errors.*;
import morozov.system.datum.signals.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.channels.FileChannel;
import java.nio.file.NoSuchFileException;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayDeque;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.atomic.AtomicReference;

public abstract class LoadableContainer implements Serializable, Cloneable {
	//
	protected boolean isShared= false;
	//
	protected transient String recentErrorText;
	protected transient long recentErrorPosition= -1;
	protected transient Throwable recentErrorException;
	//
	protected static boolean useFairLock= true;
	//
	protected transient ReentrantLock lock= new ReentrantLock(useFairLock);
	//
	protected transient Condition databaseTableIsFree= lock.newCondition();
	protected transient Condition externalFileIsFree= lock.newCondition();
	//
	protected transient HashMap<ActiveWorld,ArrayDeque<DatabaseAccessMode>> readers= new HashMap<>();
	//
	protected transient AtomicReference<ActiveWorld> writer= new AtomicReference<>(null);
	//
	protected transient long externalFileNumber= -1;
	protected transient String sectionFileName_MainLock;
	protected transient String sectionFileName_MainData;
	protected transient String sectionFileName_BackupLock;
	protected transient String sectionFileName_BackupData;
	//
	public transient Path sectionPath_MainLock;
	public transient Path sectionPath_MainData;
	public transient Path sectionPath_BackupLock;
	public transient Path sectionPath_BackupData;
	public transient AtomicReference<FileChannel> sectionMainLockChannel= new AtomicReference<>();
	public transient AtomicReference<FileChannel> sectionBackupLockChannel= new AtomicReference<>();
	//
	protected transient HashSet<DataAbstraction> informedWorlds= new HashSet<>();
	protected transient HashSet<DataAbstraction> linkedWorlds= new HashSet<>();
	//
	protected static final String messageAnObsoleteFileIsErased= "An obsolete file is erased: %s\n";
	//
	private static final long serialVersionUID= 0x89C782A14974D48BL; // -8518696540914264949L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.datum","LoadableContainer");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public LoadableContainer() {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setNumberOfExternalFile(long n, Path subdirectoryPath) {
		externalFileNumber= n;
		sectionFileName_MainLock= String.format(OpenedDataStore.externalMainLockFormat,externalFileNumber);
		sectionFileName_MainData= String.format(OpenedDataStore.externalMainDataFormat,externalFileNumber);
		sectionFileName_BackupLock= String.format(OpenedDataStore.externalBackupLockFormat,externalFileNumber);
		sectionFileName_BackupData= String.format(OpenedDataStore.externalBackupDataFormat,externalFileNumber);
		sectionPath_MainLock= subdirectoryPath.resolve(sectionFileName_MainLock);
		sectionPath_MainData= subdirectoryPath.resolve(sectionFileName_MainData);
		sectionPath_BackupLock= subdirectoryPath.resolve(sectionFileName_BackupLock);
		sectionPath_BackupData= subdirectoryPath.resolve(sectionFileName_BackupData);
	}
	//
	public long getNumberOfExternalFile() {
		return externalFileNumber;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static boolean deleteObsoleteFiles(long externalFileNumber, Path subdirectoryPath, boolean reportActions) {
		String fn_MainLock= String.format(OpenedDataStore.externalMainLockFormat,externalFileNumber);
		String fn_MainData= String.format(OpenedDataStore.externalMainDataFormat,externalFileNumber);
		String fn_BackupLock= String.format(OpenedDataStore.externalBackupLockFormat,externalFileNumber);
		String fn_BackupData= String.format(OpenedDataStore.externalBackupDataFormat,externalFileNumber);
		Path sp_MainLock= subdirectoryPath.resolve(fn_MainLock);
		Path sp_MainData= subdirectoryPath.resolve(fn_MainData);
		Path sp_BackupLock= subdirectoryPath.resolve(fn_BackupLock);
		Path sp_BackupData= subdirectoryPath.resolve(fn_BackupData);
		boolean ok= true;
		try {
			boolean result= Files.deleteIfExists(sp_MainLock);
			if (reportActions && result) {
				System.err.printf(messageAnObsoleteFileIsErased,sp_MainLock);
			}
		} catch (IOException e) {
			ok= false;
		};
		try {
			boolean result= Files.deleteIfExists(sp_MainData);
			if (reportActions && result) {
				System.err.printf(messageAnObsoleteFileIsErased,sp_MainData);
			}
		} catch (IOException e) {
			ok= false;
		};
		try {
			boolean result= Files.deleteIfExists(sp_BackupLock);
			if (reportActions && result) {
				System.err.printf(messageAnObsoleteFileIsErased,sp_BackupLock);
			}
		} catch (IOException e) {
			ok= false;
		};
		try {
			boolean result= Files.deleteIfExists(sp_BackupData);
			if (reportActions && result) {
				System.err.printf(messageAnObsoleteFileIsErased,sp_BackupData);
			}
		} catch (IOException e) {
			ok= false;
		};
		return ok;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void loadContent(ExtendedFileName fileName, int timeout, CharacterSet requestedCharacterSet, StaticContext staticContext, ActiveWorld currentProcess, boolean checkPrivileges, ParserMasterInterface master, ChoisePoint iX) {
		claimModifyingAccess(currentProcess,checkPrivileges);
		recentErrorText= "";
		recentErrorPosition= -1;
		recentErrorException= null;
		try {
			String textBuffer= fileName.getTextData(timeout,requestedCharacterSet,staticContext);
			try {
				loadContent(textBuffer,currentProcess,checkPrivileges,master,iX);
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
	abstract protected void loadContent(String textBuffer, ActiveWorld currentProcess, boolean checkPrivileges, ParserMasterInterface master, ChoisePoint iX) throws SyntaxError, DatabaseRecordDoesNotBelongToDomain;
	//
	///////////////////////////////////////////////////////////////
	//
	public void recentLoadingError(PrologVariable a1, PrologVariable a2, PrologVariable a3, PrologVariable a4, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) throws Backtracking {
		claimReadingAccess(currentProcess,checkPrivileges);
		if (recentErrorException != null && recentErrorText != null) {
			a1.setBacktrackableValue(new PrologString(recentErrorText),iX);
			a2.setBacktrackableValue(new PrologInteger(recentErrorPosition),iX);
			a3.setBacktrackableValue(new PrologString(recentErrorException.toString()),iX);
			a4.setBacktrackableValue(new PrologString(recentErrorException.toString()),iX);
		} else {
			throw Backtracking.instance;
		}
	}
	public void recentLoadingError(PrologVariable a1, PrologVariable a2, PrologVariable a3, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) throws Backtracking {
		claimReadingAccess(currentProcess,checkPrivileges);
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
	public void linkWorld(DataAbstraction currentWorld) {
		if (currentWorld != null) {
			synchronized (linkedWorlds) {
				linkedWorlds.add(currentWorld);
			}
		}
	}
	//
	public void forgetWorld(DataAbstraction currentWorld) {
		if (currentWorld != null) {
			synchronized (linkedWorlds) {
				linkedWorlds.remove(currentWorld);
			}
		}
	}
	//
	protected void freeWriter(DataAbstraction currentWorld/*, ActiveWorld currentProcess*/, OpenedDataStore openedDataStore, boolean watchTable) {
		writer.set(null);
		if (currentWorld != null) {
			synchronized (informedWorlds) {
				informedWorlds.add(currentWorld);
			};
			if (watchTable) {
				synchronized (linkedWorlds) {
					linkedWorlds.add(currentWorld);
				}
			}
		};
		if (openedDataStore != null) {
			unlockSectionFile();
		};
		databaseTableIsFree.signal();
	}
	//
	protected void freeReader(DataAbstraction currentWorld, ActiveWorld currentProcess, OpenedDataStore openedDataStore, boolean watchTable) {
		readers.remove(currentProcess);
		if (currentWorld != null) {
			synchronized (informedWorlds) {
				informedWorlds.add(currentWorld);
			};
			if (watchTable) {
				synchronized (linkedWorlds) {
					linkedWorlds.add(currentWorld);
				}
			}
		};
		if (openedDataStore != null) {
			unlockSectionFile();
		};
		if (readers.isEmpty()) {
			databaseTableIsFree.signal();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void activateWatcher(Database database, ActiveWorld currentProcess, OpenedDataStore openedDataStore) {
		lock.lock();
		try {
			if (writer.get()==currentProcess) {
				if (writerAccessModesStackIsEmpty()) {
					if (openedDataStore != null) {
						openedDataStore.registerWatcher(database,this,sectionPath_MainData);
					}
				}
			} else {
				ArrayDeque<DatabaseAccessMode> list= readers.get(currentProcess);
				if (list != null) {
					if (list.isEmpty()) {
						if (openedDataStore != null) {
							openedDataStore.registerWatcher(database,this,sectionPath_MainData);
						}
					}
				} else {
					if (openedDataStore != null) {
						openedDataStore.registerWatcher(database,this,sectionPath_MainData);
					}
				}
			}
		} finally {
			lock.unlock();
		}
	}
	//
	abstract protected boolean writerAccessModesStackIsEmpty();
	//
	public void unregisterWatcher(Database database, ActiveWorld currentProcess, OpenedDataStore openedDataStore) {
		lock.lock();
		try {
			if (openedDataStore != null) {
				openedDataStore.unregisterWatcher(database,sectionPath_MainData);
			}
		} finally {
			lock.unlock();
		}
	}
	//
	public void reportExternalUpdate() {
		synchronized (informedWorlds) {
			informedWorlds.clear();
		}
	}
	//
	public boolean isUpdated(DataAbstraction currentWorld) {
		lock.lock();
		try {
			synchronized (informedWorlds) {
				if (informedWorlds.contains(currentWorld)) {
					return false;
				} else {
					return true;
				}
			}
		} finally {
			lock.unlock();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void claimReadingAccess(ActiveWorld currentProcess, boolean checkPrivileges) {
		if (isShared && checkPrivileges) {
			lock.lock();
			try {
				if (writer.get() != currentProcess && readers.get(currentProcess)==null) {
					throw new NotInsideReadingTransaction();
				}
			} finally {
				lock.unlock();
			}
		}
	}
	protected void claimModifyingAccess(ActiveWorld currentProcess, boolean checkPrivileges) {
		if (isShared && checkPrivileges) {
			lock.lock();
			try {
				if (writer.get()==currentProcess) {
					if (getCurrentDatabaseAccessModes()==DatabaseAccessMode.READING) {
						throw new NotInsideModifyingTransaction();
					};
					registerModifyingAccess();
				} else {
					throw new NotInsideModifyingTransaction();
				}
			} finally {
				lock.unlock();
			}
		}
	}
	//
	abstract protected DatabaseAccessMode getCurrentDatabaseAccessModes();
	abstract protected void registerModifyingAccess();
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean openMainLockChannel(DatabaseAccessMode accessMode) {
		synchronized (sectionMainLockChannel) {
			FileChannel c0= sectionMainLockChannel.get();
			if (c0==null || !c0.isOpen()) {
				try {
					if (accessMode==DatabaseAccessMode.MODIFYING) {
						FileChannel channel= FileChannel.open(sectionPath_MainLock,StandardOpenOption.CREATE,StandardOpenOption.READ,StandardOpenOption.WRITE);
						if (channel.isOpen()) {
							sectionMainLockChannel.set(channel);
						} else {
							throw new FileAccessError(sectionPath_MainLock.toString());
						}
					} else {
						try {
							FileChannel channel= FileChannel.open(sectionPath_MainLock,StandardOpenOption.CREATE,StandardOpenOption.READ);
							if (channel.isOpen()) {
								sectionMainLockChannel.set(channel);
							} else {
								throw new FileAccessError(sectionPath_MainLock.toString());
							}
						} catch (NoSuchFileException e) {
							FileChannel channel= FileChannel.open(sectionPath_MainLock,StandardOpenOption.CREATE,StandardOpenOption.READ,StandardOpenOption.WRITE);
							if (channel.isOpen()) {
								sectionMainLockChannel.set(channel);
							} else {
								throw new FileAccessError(sectionPath_MainLock.toString());
							}
						}
					}
				} catch (IOException e) {
					throw new FileInputOutputError(sectionPath_MainLock.toString(),e);
				};
				return true;
			} else {
				return false;
			}
		}
	}
	//
	public boolean openBackupLockChannel(DatabaseAccessMode accessMode) {
		synchronized (sectionBackupLockChannel) {
			FileChannel c0= sectionBackupLockChannel.get();
			if (c0==null || !c0.isOpen()) {
				try {
					if (accessMode==DatabaseAccessMode.MODIFYING) {
						FileChannel channel= FileChannel.open(sectionPath_BackupLock,StandardOpenOption.CREATE,StandardOpenOption.READ,StandardOpenOption.WRITE);
						if (channel.isOpen()) {
							sectionBackupLockChannel.set(channel);
						} else {
							throw new FileAccessError(sectionPath_BackupLock.toString());
						}
					} else {
						try {
							FileChannel channel= FileChannel.open(sectionPath_BackupLock,StandardOpenOption.CREATE,StandardOpenOption.READ);
							if (channel.isOpen()) {
								sectionBackupLockChannel.set(channel);
							} else {
								throw new FileAccessError(sectionPath_BackupLock.toString());
							}
						} catch (NoSuchFileException e) {
							FileChannel channel= FileChannel.open(sectionPath_BackupLock,StandardOpenOption.CREATE,StandardOpenOption.READ,StandardOpenOption.WRITE);
							if (channel.isOpen()) {
								sectionBackupLockChannel.set(channel);
							} else {
								throw new FileAccessError(sectionPath_BackupLock.toString());
							}
						}
					}
				} catch (IOException e) {
					throw new FileInputOutputError(sectionPath_BackupLock.toString(),e);
				};
				return true;
			} else {
				return false;
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void unlockSectionFile() {
		try {
			closeMainLockChannel();
			closeBackupLockChannel();
		} catch (Throwable e) {
		}
	}
	//
	public void closeMainLockChannel() {
		synchronized (sectionMainLockChannel) {
			if (sectionMainLockChannel.get() != null) {
				try {
					sectionMainLockChannel.get().close();
					sectionMainLockChannel.set(null);
				} catch (IOException e) {
					throw new FileInputOutputError(sectionPath_MainLock.toString(),e);
				}
			}
		}
	}
	//
	public void closeBackupLockChannel() {
		synchronized (sectionBackupLockChannel) {
			if (sectionBackupLockChannel.get() != null) {
				try {
					sectionBackupLockChannel.get().close();
					sectionBackupLockChannel.set(null);
				} catch (IOException e) {
					throw new FileInputOutputError(sectionPath_BackupLock.toString(),e);
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		recentErrorText= null;
		recentErrorPosition= -1;
		recentErrorException= null;
		lock= new ReentrantLock(useFairLock);
		databaseTableIsFree= lock.newCondition();
		externalFileIsFree= lock.newCondition();
		readers= new HashMap<>();
		writer= new AtomicReference<>(null);
		externalFileNumber= -1;
		sectionFileName_MainLock= null;
		sectionFileName_MainData= null;
		sectionFileName_BackupLock= null;
		sectionFileName_BackupData= null;
		sectionPath_MainLock= null;
		sectionPath_MainData= null;
		sectionPath_BackupLock= null;
		sectionPath_BackupData= null;
		sectionMainLockChannel= new AtomicReference<>();
		sectionBackupLockChannel= new AtomicReference<>();
		informedWorlds= new HashSet<>();
		linkedWorlds= new HashSet<>();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Object clone() {
		LoadableContainer o;
		try {
			o= (LoadableContainer)super.clone();
		} catch (CloneNotSupportedException e) {
			throw new CloningError();
		};
		o.recentErrorText= null;
		o.recentErrorPosition= -1;
		o.recentErrorException= null;
		o.lock= new ReentrantLock(useFairLock);
		o.databaseTableIsFree= o.lock.newCondition();
		o.externalFileIsFree= o.lock.newCondition();
		o.readers= new HashMap<>();
		o.writer= new AtomicReference<>(null);
		o.externalFileNumber= -1;
		o.sectionFileName_MainLock= null;
		o.sectionFileName_MainData= null;
		o.sectionFileName_BackupLock= null;
		o.sectionFileName_BackupData= null;
		o.sectionPath_MainLock= null;
		o.sectionPath_MainData= null;
		o.sectionPath_BackupLock= null;
		o.sectionPath_BackupData= null;
		o.sectionMainLockChannel= new AtomicReference<>();
		o.sectionBackupLockChannel= new AtomicReference<>();
		o.informedWorlds= new HashSet<>();
		o.linkedWorlds= new HashSet<>();
		return o;
	}
}
