// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.datum.errors;

public class DataStoreEntryError extends RuntimeException {
	public String entryName;
	public DataStoreEntryError(String name) {
		entryName= name;
	}
	public String toString() {
		return this.getClass().toString() + "(" + entryName.toString() + ")";
	}
}
