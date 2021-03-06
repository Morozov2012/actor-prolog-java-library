// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.datum.errors;

public class DataStoreIsAlreadyOpened extends RuntimeException {
	//
	protected String fileName1;
	protected String fileName2;
	//
	public DataStoreIsAlreadyOpened(String name1, String name2) {
		fileName1= name1;
		fileName2= name2;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(" + fileName1.toString() + ";" + fileName2.toString() + ")";
	}
}
