// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.datum.errors;

public class DataStoreEntryError extends RuntimeException {
	//
	protected String entryName;
	//
	public DataStoreEntryError(String name) {
		entryName= name;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(" + entryName.toString() + ")";
	}
}
