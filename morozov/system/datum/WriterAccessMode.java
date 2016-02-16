// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.datum;

public class WriterAccessMode {
	//
	protected DatabaseAccessMode mode;
	protected DatabaseTable table;
	protected boolean tableIsModified;
	//
	public WriterAccessMode(DatabaseAccessMode m, DatabaseTable t) {
		mode= m;
		table= t;
	}
	//
	public void registerModifyingAccess() {
		tableIsModified= true;
	}
	//
	public void setTableIsModified(WriterAccessMode previousMode) {
		tableIsModified= previousMode.tableIsModified;
	}
	//
	public DatabaseAccessMode getDatabaseAccessMode() {
		return mode;
	}
	//
	public DatabaseTable getPreviousTable() {
		return table;
	}
	//
	public boolean getTableIsModified() {
		return tableIsModified;
	}
}
