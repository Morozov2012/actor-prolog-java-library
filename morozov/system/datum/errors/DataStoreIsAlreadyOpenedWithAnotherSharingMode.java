// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.datum.errors;

import morozov.system.datum.*;

public class DataStoreIsAlreadyOpenedWithAnotherSharingMode extends RuntimeException {
	//
	protected String fileName;
	protected DatabaseSharingMode sharingMode1;
	protected DatabaseSharingMode sharingMode2;
	//
	public DataStoreIsAlreadyOpenedWithAnotherSharingMode(String name, DatabaseSharingMode sharing1, DatabaseSharingMode sharing2) {
		fileName= name;
		sharingMode1= sharing1;
		sharingMode2= sharing2;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(" + fileName.toString() + ";" + sharingMode1.toString() + ";" + sharingMode2.toString() + ")";
	}
}
