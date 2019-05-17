// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.datum;

import morozov.system.datum.errors.*;
import morozov.system.datum.signals.*;

import java.nio.file.Path;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class SectionIndex implements Serializable {
	//
	public HashMap<String,Long> table;
	public long counter= OpenedDataStore.maximalSystemSectionNumber;
	public HashSet<Long> freeNumbers= new HashSet<>();
	//
	private static final long serialVersionUID= 0x6EC7DC9B6AF642C7L; // 7982591424610058951L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.datum","SectionIndex");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public SectionIndex(HashMap<String,Long> index, long c) {
		table= index;
		counter= c;
	}
	public SectionIndex(HashMap<String,Long> index, long c, HashSet<Long> fN) {
		table= index;
		counter= c;
		freeNumbers= fN;
	}
	//
	public long tryToGetSectionNumber(String tableName) throws SectionNumberDoesNotExist {
		Long sectionNumber= table.get(tableName);
		if (sectionNumber != null) {
			return sectionNumber;
		} else {
			throw SectionNumberDoesNotExist.instance;
		}
	}
	public long getSectionNumber(String tableName, boolean reuseNumbers, Path subdirectoryPath) {
		Long sectionNumber= table.get(tableName);
		if (sectionNumber != null) {
			return sectionNumber;
		} else {
			boolean numberIsFound= false;
			if (reuseNumbers && !freeNumbers.isEmpty()) {
				Iterator<Long> i= freeNumbers.iterator();
				while (i.hasNext()) {
					long number= i.next();
					if (DatabaseTableContainer.deleteObsoleteFiles(number,subdirectoryPath,false)) {
						sectionNumber= number;
						numberIsFound= true;
						i.remove();
						break;
					}
				}
			};
			if (!numberIsFound) {
				counter= counter + 1;
				if (counter >= Long.MAX_VALUE) {
					throw new TooManyTablesInTheDataStore();
				};
				sectionNumber= counter;
				DatabaseTableContainer.deleteObsoleteFiles(sectionNumber,subdirectoryPath,false);
			};
			table.put(tableName,sectionNumber);
			return sectionNumber;
		}
	}
	public void deleteEntry(String tableName) {
		Long sectionNumber= table.get(tableName);
		if (sectionNumber != null) {
			freeNumbers.add(sectionNumber);
		};
		table.remove(tableName);
	}
	public void renameEntry(String oldEntryName, String newEntryName) {
		Long sectionNumber= table.get(oldEntryName);
		table.put(newEntryName,sectionNumber);
		table.remove(oldEntryName);
	}
	public void collectGarbage(Path subdirectoryPath, boolean reportActions) {
		if (!freeNumbers.isEmpty()) {
			Iterator<Long> i= freeNumbers.iterator();
			while (i.hasNext()) {
				long number= i.next();
				DatabaseTableContainer.deleteObsoleteFiles(number,subdirectoryPath,reportActions);
			}
		}
	}
}
