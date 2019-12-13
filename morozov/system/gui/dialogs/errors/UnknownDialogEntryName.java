// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs.errors;

public class UnknownDialogEntryName extends RuntimeException {
	//
	protected String entryName;
	//
	public UnknownDialogEntryName(String name) {
		entryName= name;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(" + entryName + ")";
	}
}
