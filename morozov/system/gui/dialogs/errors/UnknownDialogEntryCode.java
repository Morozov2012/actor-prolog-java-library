// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs.errors;

public class UnknownDialogEntryCode extends RuntimeException {
	public long entryCode;
	public UnknownDialogEntryCode(long code) {
		entryCode= code;
	}
	public String toString() {
		return this.getClass().toString() + "(" + Long.toString(entryCode) + ")";
	}
}
