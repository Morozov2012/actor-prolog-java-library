// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs.errors;

public class UnknownDialogEntryName extends RuntimeException {
	public String entryName;
	public UnknownDialogEntryName(String name) {
		entryName= name;
	}
	public String toString() {
		return this.getClass().toString() + "(" + entryName + ")";
	}
}
