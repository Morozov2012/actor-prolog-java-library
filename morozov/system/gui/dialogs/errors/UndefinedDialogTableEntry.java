// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs.errors;

public class UndefinedDialogTableEntry extends RuntimeException {
	public String dialogName;
	public UndefinedDialogTableEntry(String name) {
		dialogName= name;
	}
}
