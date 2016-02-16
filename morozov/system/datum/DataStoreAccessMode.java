// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.datum;

import java.util.HashMap;

public class DataStoreAccessMode {
	//
	protected DatabaseAccessMode mode;
	protected HashMap<String,DatabaseTableContainer> index;
	protected boolean indexIsModified;
	//
	public DataStoreAccessMode(DatabaseAccessMode m, HashMap<String,DatabaseTableContainer> i) {
		mode= m;
		index= i;
	}
	//
	public void registerModifyingAccess() {
		indexIsModified= true;
	}
	//
	public void setIndexIsModified(DataStoreAccessMode previousMode) {
		indexIsModified= previousMode.indexIsModified;
	}
	//
	public DatabaseAccessMode getDatabaseAccessMode() {
		return mode;
	}
	//
	public HashMap<String,DatabaseTableContainer> getPreviousIndex() {
		return index;
	}
	//
	public boolean getIndexIsModified() {
		return indexIsModified;
	}
}
