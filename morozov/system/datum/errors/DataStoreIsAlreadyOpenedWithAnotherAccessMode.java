// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.datum.errors;

import morozov.system.datum.*;

public class DataStoreIsAlreadyOpenedWithAnotherAccessMode extends RuntimeException {
	//
	protected String fileName;
	protected DatabaseAccessMode accessMode1;
	protected DatabaseAccessMode accessMode2;
	//
	public DataStoreIsAlreadyOpenedWithAnotherAccessMode(String name, DatabaseAccessMode access1, DatabaseAccessMode access2) {
		fileName= name;
		accessMode1= access1;
		accessMode2= access2;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(" + fileName.toString() + ";" + accessMode1.toString() + ";" + accessMode2.toString() + ")";
	}
}
