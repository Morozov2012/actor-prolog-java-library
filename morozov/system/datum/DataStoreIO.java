// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.datum;

import morozov.system.*;
import morozov.system.files.*;
import morozov.system.files.errors.*;
import morozov.system.datum.errors.*;
import morozov.system.datum.signals.*;
import morozov.worlds.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.EOFException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.concurrent.locks.Condition;
import java.math.BigInteger;
import java.lang.NumberFormatException;

public class DataStoreIO {
	//
	protected ExtendedFileName fileName;
	protected String dataStoreName;
	protected Path dataStorePath;
	//
	protected Path subdirectoryPath;
	//
	protected ActiveWorld currentProcess;
	protected DatabaseAccessMode accessMode;
	protected DatabaseSharingMode sharingMode;
	protected TimeInterval waitingPeriod;
	protected TimeInterval sleepPeriod;
	protected BigInteger maxRetryNumber;
	//
	protected Path path_ExclusiveAccess;
	protected Path path_WriteAccess;
	protected Path path_ModeMain;
	protected Path path_ModeBackup1;
	protected Path path_ModeBackup2;
	protected Path path_IndexMain;
	protected Path path_IndexBackup1;
	protected Path path_IndexBackup2;
	protected FileChannel channel_ExclusiveAccess;
	protected FileChannel channel_WriteAccess;
	protected FileChannel channel_ModeMain;
	protected FileLock lock_ExclusiveAccess;
	protected FileLock lock_WriteAccess;
	protected FileLock lock_Mode;
	//
	protected SectionIndex lockedSectionIndex;
	//
	protected static final int sectionNumber_Mode= 0;
	protected static final int sectionNumber_ExclusiveAccess= 1;
	protected static final int sectionNumber_WriteAccess= 2;
	//
	public static final int maximalSystemSectionNumber= 2;
	public static final String externalMainLockFormat= "%08d.lck";
	public static final String externalMainDataFormat= "%08d.bit";
	public static final String externalBackupLockFormat= "%08d.flg";
	public static final String externalBackupDataFormat= "%08d.bck";
	public static final String externalMainDataMask= "*.bit";
	public static final String externalBackupDataMask= "*.bck";
	//
	protected static final String fileName_IndexMain= String.format(externalMainDataFormat,sectionNumber_Mode);
	protected static final String fileName_IndexBackup1= String.format(externalMainDataFormat,sectionNumber_ExclusiveAccess);
	protected static final String fileName_IndexBackup2= String.format(externalMainDataFormat,sectionNumber_WriteAccess);
	protected static final String fileName_ModeMain= String.format(externalMainLockFormat,sectionNumber_Mode);
	protected static final String fileName_ModeBackup1= String.format(externalBackupLockFormat,sectionNumber_Mode);
	protected static final String fileName_ModeBackup2= String.format(externalBackupDataFormat,sectionNumber_Mode);
	protected static final String fileName_ExclusiveAccess= String.format(externalMainLockFormat,sectionNumber_ExclusiveAccess);
	protected static final String fileName_WriteAccess= String.format(externalMainLockFormat,sectionNumber_WriteAccess);
	//
	protected static final String messageRecentEntryIsFoundInFileWithTime= "A recent version of the %s entry is found in the file: %s [%s]\n";
	protected static final String messageRecentEntryIsFoundInFileWithoutTime= "A recent version of the %s entry is found in the file: %s\n";
	protected static final String messageOlderEntryIsFoundInFileWithTime= "An older version of the %s entry is found in the file: %s [%s]\n";
	protected static final String messageEntryIsRestoredInSection= "The %s entry is restored in section %s.\n";
	protected static final String messageDataStoreIndexIsRestored= "The index of the data store is reconstructed.\n";
	//
	///////////////////////////////////////////////////////////////
	//
	public DataStoreIO(ExtendedFileName storeName, Path storePath, Path subdirectory, ActiveWorld process, DatabaseAccessMode access, DatabaseSharingMode sharing, TimeInterval waiting, TimeInterval sleep, BigInteger maxRetry) {
		fileName= storeName;
		dataStoreName= fileName.toString();
		dataStorePath= storePath;
		subdirectoryPath= subdirectory;
		currentProcess= process;
		accessMode= access;
		sharingMode= sharing;
		waitingPeriod= waiting;
		sleepPeriod= sleep;
		maxRetryNumber= maxRetry;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean isSameFile(Path p2) {
		try {
			return Files.isSameFile(subdirectoryPath,p2);
		} catch (IOException e) {
			return false;
		}
	}
	public ExtendedFileName getFileName() {
		return fileName;
	}
	public Path getSubdirectoryPath() {
		return subdirectoryPath;
	}
	public DatabaseAccessMode getAccessMode() {
		return accessMode;
	}
	public void checkAccessMode(DatabaseAccessMode access) {
		if (accessMode != access) {
			throw new DataStoreIsAlreadyOpenedWithAnotherAccessMode(dataStoreName,accessMode,access);
		}
	}
	public void checkSharingMode(DatabaseSharingMode sharing) {
		if (sharingMode != sharing) {
			throw new DataStoreIsAlreadyOpenedWithAnotherSharingMode(dataStoreName,sharingMode,sharing);
		}
	}
	public boolean isUnlocked() {
		return (lock_Mode==null);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected boolean dataStoreIsUnpacked(boolean shared, Condition externalFileIsFree) {
		int counter= 0;
		if (dataStoreIsUnpacked(channel_ModeMain,shared,path_ModeMain,externalFileIsFree)) {
			counter= counter + 1;
		};
		try {
			FileChannel fileChannel= FileChannel.open(path_ModeBackup1,StandardOpenOption.CREATE,StandardOpenOption.READ,StandardOpenOption.WRITE);
			try {
				if (dataStoreIsUnpacked(fileChannel,shared,path_ModeBackup1,externalFileIsFree)) {
					counter= counter + 1;
				}
			} finally {
				fileChannel.close();
			}
		} catch (Throwable e) {
		};
		try {
			FileChannel fileChannel= FileChannel.open(path_ModeBackup2,StandardOpenOption.CREATE,StandardOpenOption.READ,StandardOpenOption.WRITE);
			try {
				if (dataStoreIsUnpacked(fileChannel,shared,path_ModeBackup2,externalFileIsFree)) {
					counter= counter + 1;
				}
			} finally {
				fileChannel.close();
			}
		} catch (Throwable e) {
		};
		if (counter <= 0) {
			return false;
		} else if (counter >= 3) {
			return true;
		} else {
			try {
				markUnpackedDataStore(channel_ModeMain,path_ModeMain,externalFileIsFree);
			} catch (Throwable e) {
			};
			updateModeBackups(externalFileIsFree);
			return true;
		}
	}
	protected boolean dataStoreIsUnpacked(FileChannel modeChannel, boolean shared, Path modePath, Condition externalFileIsFree) {
		FileLock lock= tryToLock(modeChannel,shared,sleepPeriod,maxRetryNumber,externalFileIsFree,modePath);
		try {
			ByteBuffer destination= ByteBuffer.allocate(2);
			modeChannel.position(0);
			int nBytes= modeChannel.read(destination);
			if (nBytes >= 2 && destination.getShort(0)==1) {
				return true;
			}
		} catch (IOException e) {
			throw new FileInputOutputError(modePath.toString(),e);
		} finally {
			try {
				lock.release();
			} catch (Throwable e) {
			}
		};
		return false;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void updateModeBackups(Condition externalFileIsFree) {
		try {
			FileChannel fileChannel= FileChannel.open(path_ModeBackup1,StandardOpenOption.CREATE,StandardOpenOption.READ,StandardOpenOption.WRITE);
			try {
				markUnpackedDataStore(fileChannel,path_ModeBackup1,externalFileIsFree);
			} finally {
				fileChannel.close();
			}
		} catch (Throwable e) {
		};
		try {
			FileChannel fileChannel= FileChannel.open(path_ModeBackup2,StandardOpenOption.CREATE,StandardOpenOption.READ,StandardOpenOption.WRITE);
			try {
				markUnpackedDataStore(fileChannel,path_ModeBackup2,externalFileIsFree);
			} finally {
				fileChannel.close();
			}
		} catch (Throwable e) {
		}
	}
	protected void markUnpackedDataStore(FileChannel modeChannel, Path modePath, Condition externalFileIsFree) {
		FileLock lock= tryToLock(modeChannel,false,sleepPeriod,maxRetryNumber,externalFileIsFree,modePath);
		try {
			ByteBuffer destination= ByteBuffer.allocate(2);
			destination.putShort(0,(short)1);
			modeChannel.position(0);
			modeChannel.write(destination);
		} catch (IOException e) {
			throw new FileInputOutputError(modePath.toString(),e);
		} finally {
			try {
				lock.release();
			} catch (Throwable e) {
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected SectionIndex unsafelyReadDataStoreIndex() {
		if (Files.exists(path_IndexMain)) {
			try {
				return unsafelyReadDataStoreIndex(path_IndexMain);
			} catch (Throwable e) {
			}
		};
		if (Files.exists(path_IndexBackup1)) {
			try {
				return unsafelyReadDataStoreIndex(path_IndexBackup1);
			} catch (Throwable e) {
			}
		};
		if (Files.exists(path_IndexBackup2)) {
			return unsafelyReadDataStoreIndex(path_IndexBackup2);
		} else {
			throw new FileIsNotFound(path_IndexBackup2.toString());
		}
	}
	protected SectionIndex unsafelyReadDataStoreIndex(Path indexPath) {
		try {
			InputStream inputStream= Files.newInputStream(indexPath);
			try {
				ObjectInputStream objectInputStream= new DataStoreInputStream(new BufferedInputStream(inputStream)/*,false*/);
				try {
					return (SectionIndex)objectInputStream.readObject();
				} catch (ClassNotFoundException e) {
					throw new FileInputOutputError(indexPath.toString(),e);
				} finally {
					objectInputStream.close();
				}
			} finally {
				inputStream.close();
			}
		} catch (IOException e) {
			throw new FileInputOutputError(indexPath.toString(),e);
		}
	}
	//
	public void unsafelyWriteDataStoreIndex() {
		unsafelyWriteDataStoreIndex(lockedSectionIndex);
	}
	//
	protected void unsafelyWriteDataStoreIndex(SectionIndex index) {
		unsafelyWriteDataStoreIndex(index,path_IndexMain);
		unsafelyWriteDataStoreIndex(index,path_IndexBackup1);
		unsafelyWriteDataStoreIndex(index,path_IndexBackup2);
	}
	protected void unsafelyWriteDataStoreIndex(SectionIndex index, Path indexPath) {
		try {
			OutputStream outputStream= Files.newOutputStream(indexPath);
			try {
				ObjectOutputStream objectOutputStream= new DataStoreOutputStream(new BufferedOutputStream(outputStream));
				try {
					objectOutputStream.writeObject(index);
				} finally {
					objectOutputStream.close();
				}
			} finally {
				outputStream.close();
			}
		} catch (IOException e) {
			throw new FileInputOutputError(indexPath.toString(),e);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected SectionIndex unsafelyReassembleDataStoreIndex(boolean reportActions, Condition externalFileIsFree) {
		if (Files.exists(subdirectoryPath)) {
			HashMap<String,FileTime> timeMap= new HashMap<>();
			HashMap<String,DatabaseTableContainer> tableMap= new HashMap<>();
			HashMap<String,Long> numberMap= new HashMap<>();
			try {
				DirectoryStream<Path> stream= Files.newDirectoryStream(subdirectoryPath,externalMainDataMask);
				try {
					for (Path targetPath: stream) {
						String shortName= targetPath.getName(targetPath.getNameCount()-1).toString();
						int p1= shortName.lastIndexOf('.');
						if (p1 >= 0) {
							shortName= shortName.substring(0,p1);
							try {
								int sectionNumber= Integer.parseInt(shortName);
								if (sectionNumber <= maximalSystemSectionNumber) {
									continue;
								};
								String sectionLockFileName= String.format(externalMainLockFormat,sectionNumber);
								String sectionFileName= String.format(externalMainDataFormat,sectionNumber);
								String entryName= unsafelyReadSectionByNumber(sectionNumber,sectionLockFileName,sectionFileName,timeMap,tableMap,numberMap,reportActions,externalFileIsFree);
							} catch (NumberFormatException e) {
							} catch (Throwable e) {
							}
						}
					}
				} catch (DirectoryIteratorException e) {
					// I/O error encounted during the iteration, the cause is an IOException
					// throw e.getCause();
					// throw new FileInputOutputError(subdirectoryPath.toString(),e);
				} finally {
					try {
						stream.close();
					} catch (Throwable e) {
					}
				}
			} catch (IOException e) {
			};
			try {
				DirectoryStream<Path> stream= Files.newDirectoryStream(subdirectoryPath,externalBackupDataMask);
				try {
					for (Path targetPath: stream) {
						String shortName= targetPath.getName(targetPath.getNameCount()-1).toString();
						int p1= shortName.lastIndexOf('.');
						if (p1 >= 0) {
							shortName= shortName.substring(0,p1);
							try {
								int sectionNumber= Integer.parseInt(shortName);
								if (sectionNumber <= maximalSystemSectionNumber) {
									continue;
								};
								String sectionLockFileName= String.format(externalBackupLockFormat,sectionNumber);
								String sectionFileName= String.format(externalBackupDataFormat,sectionNumber);
								String entryName= unsafelyReadSectionByNumber(sectionNumber,sectionLockFileName,sectionFileName,timeMap,tableMap,numberMap,reportActions,externalFileIsFree);
							} catch (NumberFormatException e) {
							} catch (Throwable e) {
							}
						}
					}
				} catch (DirectoryIteratorException e) {
					// I/O error encounted during the iteration, the cause is an IOException
					// throw e.getCause();
					throw new FileInputOutputError(subdirectoryPath.toString(),e);
				} finally {
					try {
						stream.close();
					} catch (Throwable e) {
					}
				}
			} catch (IOException e) {
					throw new FileInputOutputError(subdirectoryPath.toString(),e);
			};
			Set<String> entryNameCollection= numberMap.keySet();
			Iterator<String> entryNameCollectionIterator= entryNameCollection.iterator();
			HashMap<Long,String> usedNumbers= new HashMap<>();
			long maximalSectionNumber= maximalSystemSectionNumber;
			while (entryNameCollectionIterator.hasNext()) {
				String entryName= entryNameCollectionIterator.next();
				Long number= numberMap.get(entryName);
				if (maximalSectionNumber < number) {
					maximalSectionNumber= number;
				};
				usedNumbers.put(number,entryName);
			};
			HashMap<String,Long> tableIndex= new HashMap<>();
			HashSet<Long> freeNumbers= new HashSet<>();
			for (long n=maximalSystemSectionNumber+1; n<=maximalSectionNumber; n++) {
				if (usedNumbers.containsKey(n)) {
					String entryName= usedNumbers.get(n);
					tableIndex.put(entryName,n);
				} else {
					freeNumbers.add(n);
				}
			};
			SectionIndex index= new SectionIndex(tableIndex,maximalSectionNumber,freeNumbers);
			for (long n=maximalSystemSectionNumber+1; n<=maximalSectionNumber; n++) {
				if (usedNumbers.containsKey(n)) {
					String entryName= usedNumbers.get(n);
					DatabaseTableContainer tableContainer= tableMap.get(entryName);
					safelyWriteSection(n,tableContainer,externalFileIsFree);
					if (reportActions) {
						System.err.printf(messageEntryIsRestoredInSection,entryName,n);
					}
				}
			};
			unsafelyWriteDataStoreIndex(index);
			if (reportActions) {
				System.err.printf(messageDataStoreIndexIsRestored);
			};
			index.collectGarbage(subdirectoryPath,true);
			return index;
		} else {
			throw new DirectoryDoesNotExist(subdirectoryPath.toString());
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void collectGarbage(Condition externalFileIsFree) {
		if (lock_Mode != null) {
			lockedSectionIndex.collectGarbage(subdirectoryPath,false);
		} else {
			FileLock lock= tryToLock(channel_ModeMain,true,sleepPeriod,maxRetryNumber,externalFileIsFree,path_ModeMain);
			try {
				SectionIndex index= unsafelyReadDataStoreIndex();
				index.collectGarbage(subdirectoryPath,false);
			} finally {
				try {
					lock.release();
				} catch (IOException e) {
					throw new FileInputOutputError(path_ModeMain.toString(),e);
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void safelyRegisterEntry(String currentEntryName, DatabaseTableContainer currentTableContainer, boolean reuseNumbers, Condition externalFileIsFree) {
		FileLock lock= tryToLock(channel_ModeMain,true,sleepPeriod,maxRetryNumber,externalFileIsFree,path_ModeMain);
		try {
			try {
				unsafelyQuicklyRegisterEntry(currentEntryName,currentTableContainer);
				return;
			} catch (SectionNumberDoesNotExist e) {
			}
		} finally {
			try {
				lock.release();
			} catch (IOException e) {
				throw new FileInputOutputError(path_ModeMain.toString(),e);
			}
		};
		lock= tryToLock(channel_ModeMain,false,sleepPeriod,maxRetryNumber,externalFileIsFree,path_ModeMain);
		try {
			unsafelyRegisterEntry(currentEntryName,currentTableContainer,reuseNumbers,externalFileIsFree);
		} finally {
			try {
				lock.release();
			} catch (IOException e) {
				throw new FileInputOutputError(path_ModeMain.toString(),e);
			}
		}
	}
	//
	protected void unsafelyQuicklyRegisterEntry(String currentEntryName, DatabaseTableContainer currentTableContainer) throws SectionNumberDoesNotExist {
		SectionIndex index;
		if (lock_Mode != null) {
			index= lockedSectionIndex;
		} else {
			index= unsafelyReadDataStoreIndex();
		};
		long sectionNumber= index.tryToGetSectionNumber(currentEntryName);
		currentTableContainer.setNumberOfExternalFile(sectionNumber,subdirectoryPath);
	}
	//
	public void unsafelyRegisterEntry(String currentEntryName, DatabaseTableContainer currentTableContainer, boolean reuseNumbers, Condition externalFileIsFree) {
		SectionIndex index;
		if (lock_Mode != null) {
			index= lockedSectionIndex;
		} else {
			index= unsafelyReadDataStoreIndex();
		};
		try {
			long sectionNumber= index.tryToGetSectionNumber(currentEntryName);
			currentTableContainer.setNumberOfExternalFile(sectionNumber,subdirectoryPath);
		} catch (SectionNumberDoesNotExist e1) {
			long sectionNumber= index.getSectionNumber(currentEntryName,reuseNumbers,subdirectoryPath);
			safelyWriteSection(sectionNumber,currentTableContainer,externalFileIsFree);
		};
		unsafelyWriteDataStoreIndex(index);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void lockDataStore(DatabaseAccessMode transactionMode, DataStoreErrorHandling ignoreErrors, boolean reportActions, Condition externalFileIsFree) {
		if (transactionMode==DatabaseAccessMode.MODIFYING) {
			lock_Mode= tryToLock(channel_ModeMain,false,sleepPeriod,maxRetryNumber,externalFileIsFree,path_ModeMain);
		} else {
			lock_Mode= tryToLock(channel_ModeMain,true,sleepPeriod,maxRetryNumber,externalFileIsFree,path_ModeMain);
		};
		if (ignoreErrors==DataStoreErrorHandling.REASSEMBLE_INDEX) {
			try {
				lockedSectionIndex= unsafelyReadDataStoreIndex();
			} catch (Throwable e) {
				lockedSectionIndex= unsafelyReassembleDataStoreIndex(reportActions,externalFileIsFree);
			}
		} else {
			lockedSectionIndex= unsafelyReadDataStoreIndex();
		}
	}
	//
	public void unlockDataStoreFile() {
		lockedSectionIndex= null;
		try {
			if (lock_Mode != null) {
				lock_Mode.release();
				lock_Mode= null;
			}
		} catch (Throwable e) {
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void deleteEntry(String entryName) {
		if (lockedSectionIndex != null) {
			lockedSectionIndex.deleteEntry(entryName);
		}
	}
	//
	public void renameEntry(String oldEntryName, String newEntryName) {
		if (lockedSectionIndex != null) {
			lockedSectionIndex.renameEntry(oldEntryName,newEntryName);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void lockSectionFile(String currentEntryName, DatabaseTableContainer currentTableContainer, boolean shared, boolean reuseNumbers, Condition externalFileIsFree) {
		if (currentTableContainer.getNumberOfExternalFile() <= 0) {
			safelyRegisterEntry(currentEntryName,currentTableContainer,reuseNumbers,externalFileIsFree);
		};
		tryToLockMainLockChannel(currentTableContainer,shared,sleepPeriod,maxRetryNumber,externalFileIsFree);
	}
	//
	public void unlockSectionFile(DatabaseTableContainer currentTableContainer) {
		currentTableContainer.closeMainLockChannel();
		currentTableContainer.closeBackupLockChannel();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected String unsafelyReadSectionByNumber(int sectionNumber, String sectionLockFileName, String sectionFileName, HashMap<String,FileTime> timeMap, HashMap<String,DatabaseTableContainer> tableMap, HashMap<String,Long> numberMap, boolean reportActions, Condition externalFileIsFree) {
		Path sectionLockFilePath= subdirectoryPath.resolve(sectionLockFileName);
		try {
			FileChannel sectionChannel= FileChannel.open(sectionLockFilePath,StandardOpenOption.CREATE,StandardOpenOption.READ,StandardOpenOption.WRITE);
			try {
				tryToLock(sectionChannel,true,sleepPeriod,maxRetryNumber,externalFileIsFree,sectionLockFilePath);
				Path sectionPath= subdirectoryPath.resolve(sectionFileName);
				DatabaseTable currentTable= unsafelyReadSection(sectionPath);
				DatabaseTableContainer currentTableContainer= new DatabaseTableContainer(currentTable,true);
				String entryName= currentTable.getCurrentEntryName();
				FileTime time2= null;
				try {
					BasicFileAttributes ba= Files.readAttributes(sectionPath,BasicFileAttributes.class);
					time2= ba.lastModifiedTime();
				} catch (UnsupportedOperationException e) {
				} catch (IOException e) {
				};
				if (time2 != null) {
					FileTime time1= timeMap.get(entryName);
					if (time1 != null) {
						if (time1.compareTo(time2) < 0) {
							timeMap.put(entryName,time2);
							tableMap.put(entryName,currentTableContainer);
							numberMap.put(entryName,new Long(sectionNumber));
							if (reportActions) {
								System.err.printf(messageRecentEntryIsFoundInFileWithTime,entryName,sectionPath,time2);
							}
						} else {
							if (reportActions) {
								System.err.printf(messageOlderEntryIsFoundInFileWithTime,entryName,sectionPath,time2);
							}
						}
					} else {
						timeMap.put(entryName,time2);
						tableMap.put(entryName,currentTableContainer);
						numberMap.put(entryName,new Long(sectionNumber));
						if (reportActions) {
							System.err.printf(messageRecentEntryIsFoundInFileWithTime,entryName,sectionPath,time2);
						}
					}
				} else {
					FileTime time1= timeMap.get(entryName);
					if (time1 == null) {
						tableMap.put(entryName,currentTableContainer);
						numberMap.put(entryName,new Long(sectionNumber));
						if (reportActions) {
							System.err.printf(messageRecentEntryIsFoundInFileWithoutTime,entryName,sectionPath);
						}
					}
				};
				return entryName;
			} finally {
				try {
					sectionChannel.close();
				} catch (Throwable e) {
				}
			}
		} catch (IOException e) {
			throw new FileInputOutputError(sectionLockFilePath.toString(),e);
		}
	}
	//
	public void unsafelyReadSectionIfNecessary(String entryName, DatabaseTableContainer actualTableContainer, Condition externalFileIsFree) {
		if (actualTableContainer.getNumberOfExternalFile() <= 0) {
			throw new NotInsideTransaction();
		};
		try {
			if (Files.exists(actualTableContainer.sectionPath_MainData)) {
				unsafelyReadSectionIfNecessary(entryName,actualTableContainer.sectionPath_MainData,actualTableContainer,false);
				return;
			}
		} catch (Throwable e) {
		};
		try {
			tryToLockBackupLockChannel(actualTableContainer,true,sleepPeriod,maxRetryNumber,externalFileIsFree);
			unsafelyReadSectionIfNecessary(entryName,actualTableContainer.sectionPath_BackupData,actualTableContainer,true);
		} catch (EOFException e) {
			throw new FileInputOutputError(actualTableContainer.sectionPath_BackupData.toString(),e);
		} finally {
			try {
				actualTableContainer.closeBackupLockChannel();
			} catch (Throwable e) {
			}
		}
	}
	public void unsafelyReadSectionIfNecessary(String entryName, Path dataFilePath, DatabaseTableContainer actualTableContainer, boolean ignoreEndOfFile) throws EOFException {
		try {
			InputStream inputStream= Files.newInputStream(dataFilePath);
			try {
				ObjectInputStream objectInputStream= new DataStoreInputStream(new BufferedInputStream(inputStream)/*,true*/);
				try {
					DatabaseTable newTable= (DatabaseTable)objectInputStream.readObject();
					newTable.acceptAttributes(entryName,actualTableContainer);
					actualTableContainer.setTable(newTable);
				} catch (ClassNotFoundException e) {
					throw new FileInputOutputError(dataFilePath.toString(),e);
				} finally {
					objectInputStream.close();
				}
			} catch (EOFException e) {
				if (ignoreEndOfFile) {
				} else {
					throw e;
				}
			} finally {
				inputStream.close();
			}
		} catch (IOException e) {
			throw new FileInputOutputError(dataFilePath.toString(),e);
		}
	}
	//
	protected static DatabaseTable unsafelyReadSection(Path dataFilePath) {
		try {
			InputStream inputStream= Files.newInputStream(dataFilePath);
			try {
				ObjectInputStream objectInputStream= new DataStoreInputStream(new BufferedInputStream(inputStream)/*,true*/);
				try {
					return (DatabaseTable)objectInputStream.readObject();
				} catch (ClassNotFoundException e) {
					throw new FileInputOutputError(dataFilePath.toString(),e);
				} finally {
					objectInputStream.close();
				}
			} finally {
				inputStream.close();
			}
		} catch (IOException e) {
			throw new FileInputOutputError(dataFilePath.toString(),e);
		}
	}
	//
	public void safelyWriteSection(long sectionNumber, DatabaseTableContainer currentTableContainer, Condition externalFileIsFree) {
		currentTableContainer.setNumberOfExternalFile(sectionNumber,subdirectoryPath);
		try {
			tryToLockMainLockChannel(currentTableContainer,false,sleepPeriod,maxRetryNumber,externalFileIsFree);
			unsafelyWriteSection(currentTableContainer,externalFileIsFree);
		} finally {
			try {
				currentTableContainer.closeMainLockChannel();
			} catch (Throwable e) {
			}
		}
	}
	//
	public void unsafelyWriteSection(DatabaseTableContainer currentTableContainer, Condition externalFileIsFree) {
		unsafelyWriteSection(currentTableContainer.sectionPath_MainData,currentTableContainer);
		tryToLockBackupLockChannel(currentTableContainer,false,sleepPeriod,maxRetryNumber,externalFileIsFree);
		try {
			unsafelyWriteSection(currentTableContainer.sectionPath_BackupData,currentTableContainer);
		} finally {
			try {
				currentTableContainer.closeBackupLockChannel();
			} catch (Throwable e) {
			}
		}
	}
	public static void unsafelyWriteSection(Path dataFilePath, DatabaseTableContainer currentTableContainer) {
		try {
			OutputStream outputStream= Files.newOutputStream(dataFilePath);
			try {
				ObjectOutputStream objectOutputStream= new DataStoreOutputStream(new BufferedOutputStream(outputStream));
				try {
					objectOutputStream.writeObject(currentTableContainer.getTable());
				} finally {
					objectOutputStream.close();
				}
			} finally {
				outputStream.close();
			}
		} catch (IOException e) {
			throw new FileInputOutputError(dataFilePath.toString(),e);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@SuppressWarnings("unchecked")
	protected HashMap<String,DatabaseTableContainer> unsafelyReadDataStore() {
		try {
			InputStream inputStream= Files.newInputStream(dataStorePath);
			try {
				ObjectInputStream objectInputStream= new DataStoreInputStream(new BufferedInputStream(inputStream)/*,true*/);
				try {
					return (HashMap<String,DatabaseTableContainer>)objectInputStream.readObject();
				} catch (ClassNotFoundException e) {
					throw new FileInputOutputError(dataStoreName,e);
				} finally {
					objectInputStream.close();
				}
			} finally {
				inputStream.close();
			}
		} catch (IOException e) {
			throw new FileInputOutputError(dataStoreName,e);
		}
	}
	//
	protected void unsafelyWriteDataStore(HashMap<String,DatabaseTableContainer> tableHash) {
		try {
			OutputStream outputStream= Files.newOutputStream(dataStorePath);
			try {
				ObjectOutputStream objectOutputStream= new DataStoreOutputStream(new BufferedOutputStream(outputStream));
				try {
					objectOutputStream.writeObject(tableHash);
				} finally {
					objectOutputStream.close();
				}
			} finally {
				outputStream.close();
			}
		} catch (IOException e) {
			throw new FileInputOutputError(dataStoreName,e);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	// Auxiliary functions                                       //
	///////////////////////////////////////////////////////////////
	//
	protected FileLock tryToLock(FileChannel channel, boolean isShared, TimeInterval sleepPeriod, BigInteger retryNumber, Condition externalFileIsFree, Path path) {
		long nanosTimeout= sleepPeriod.toNanosecondsLong();
		try {
			while (true) {
				try {
					FileLock lock= channel.tryLock(0L,Long.MAX_VALUE,isShared);
					if (lock != null) {
						return lock;
					}
				} catch (OverlappingFileLockException e) {
				} catch (IOException e) {
				};
				if (retryNumber.compareTo(BigInteger.ZERO) <= 0) {
					break;
				} else {
					retryNumber= retryNumber.subtract(BigInteger.ONE);
				};
				externalFileIsFree.awaitNanos(nanosTimeout);
			}
		} catch (InterruptedException e) {
		};
		throw new CannotAccessSharedDataStore(path.toString());
	}
	//
	protected FileLock tryToLockMainLockChannel(DatabaseTableContainer currentTableContainer, boolean isShared, TimeInterval sleepPeriod, BigInteger retryNumber, Condition externalFileIsFree) {
		long nanosTimeout= sleepPeriod.toNanosecondsLong();
		try {
			while (true) {
				if (currentTableContainer.openMainLockChannel(accessMode)) {
					try {
						FileChannel channel= currentTableContainer.sectionMainLockChannel.get();
						FileLock lock= channel.tryLock(0L,Long.MAX_VALUE,isShared);
						if (lock != null) {
							return lock;
						}
					} catch (OverlappingFileLockException e) {
					} catch (IOException e) {
					};
					currentTableContainer.closeMainLockChannel();
				};
				if (retryNumber.compareTo(BigInteger.ZERO) <= 0) {
					break;
				} else {
					retryNumber= retryNumber.subtract(BigInteger.ONE);
				};
				externalFileIsFree.awaitNanos(nanosTimeout);
			}
		} catch (InterruptedException e) {
		};
		throw new CannotAccessSharedDataStore(currentTableContainer.sectionPath_MainLock.toString());
	}
	//
	protected FileLock tryToLockBackupLockChannel(DatabaseTableContainer currentTableContainer, boolean isShared, TimeInterval sleepPeriod, BigInteger retryNumber, Condition externalFileIsFree) {
		long nanosTimeout= sleepPeriod.toNanosecondsLong();
		try {
			while (true) {
				if (currentTableContainer.openBackupLockChannel(accessMode)) {
					try {
						FileChannel channel= currentTableContainer.sectionBackupLockChannel.get();
						FileLock lock= channel.tryLock(0L,Long.MAX_VALUE,isShared);
						if (lock != null) {
							return lock;
						}
					} catch (OverlappingFileLockException e) {
					} catch (IOException e) {
					};
					currentTableContainer.closeBackupLockChannel();
				};
				if (retryNumber.compareTo(BigInteger.ZERO) <= 0) {
					break;
				} else {
					retryNumber= retryNumber.subtract(BigInteger.ONE);
				};
				externalFileIsFree.awaitNanos(nanosTimeout);
			}
		} catch (InterruptedException e) {
		} catch (Throwable e) {
			throw e;
		};
		throw new CannotAccessSharedDataStore(currentTableContainer.sectionPath_BackupLock.toString());
	}
}
