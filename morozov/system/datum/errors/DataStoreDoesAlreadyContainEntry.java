// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.datum.errors;

public class DataStoreDoesAlreadyContainEntry extends DataStoreEntryError {
	public DataStoreDoesAlreadyContainEntry(String name) {
		super(name);
	}
}
