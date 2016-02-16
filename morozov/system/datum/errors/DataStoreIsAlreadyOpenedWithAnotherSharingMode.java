// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.datum.errors;

import morozov.system.datum.*;

public class DataStoreIsAlreadyOpenedWithAnotherSharingMode extends RuntimeException {
	public String fileName;
	public DatabaseSharingMode sharingMode1;
	public DatabaseSharingMode sharingMode2;
	public DataStoreIsAlreadyOpenedWithAnotherSharingMode(String name, DatabaseSharingMode sharing1, DatabaseSharingMode sharing2) {
		fileName= name;
		sharingMode1= sharing1;
		sharingMode2= sharing2;
	}
	public String toString() {
		return this.getClass().toString() + "(" + fileName.toString() + ";" + sharingMode1.toString() + ";" + sharingMode2.toString() + ")";
	}
}
