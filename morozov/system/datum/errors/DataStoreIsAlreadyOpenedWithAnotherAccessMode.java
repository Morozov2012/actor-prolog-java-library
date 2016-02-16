// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.datum.errors;

import morozov.system.datum.*;

public class DataStoreIsAlreadyOpenedWithAnotherAccessMode extends RuntimeException {
	public String fileName;
	public DatabaseAccessMode accessMode1;
	public DatabaseAccessMode accessMode2;
	public DataStoreIsAlreadyOpenedWithAnotherAccessMode(String name, DatabaseAccessMode access1, DatabaseAccessMode access2) {
		fileName= name;
		accessMode1= access1;
		accessMode2= access2;
	}
	public String toString() {
		return this.getClass().toString() + "(" + fileName.toString() + ";" + accessMode1.toString() + ";" + accessMode2.toString() + ")";
	}
}
