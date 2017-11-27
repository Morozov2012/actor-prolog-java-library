// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs.errors;

public class UndefinedDialogTableEntry extends RuntimeException {
	public String entryName;
	public UndefinedDialogTableEntry(String name) {
		entryName= name;
	}
	public String toString() {
		return this.getClass().toString() + "(" + entryName + ")";
	}
}
