// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs.errors;

public class UndefinedDialogTableEntry extends RuntimeException {
	//
	protected String entryName;
	//
	public UndefinedDialogTableEntry(String name) {
		entryName= name;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(" + entryName + ")";
	}
}
