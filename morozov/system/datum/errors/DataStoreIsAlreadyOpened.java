// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.datum.errors;

public class DataStoreIsAlreadyOpened extends RuntimeException {
	public String fileName1;
	public String fileName2;
	public DataStoreIsAlreadyOpened(String name1, String name2) {
		fileName1= name1;
		fileName2= name2;
	}
	public String toString() {
		return this.getClass().toString() + "(" + fileName1.toString() + ";" + fileName2.toString() + ")";
	}
}
