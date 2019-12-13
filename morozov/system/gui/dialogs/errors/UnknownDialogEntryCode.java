// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs.errors;

public class UnknownDialogEntryCode extends RuntimeException {
	//
	protected long entryCode;
	//
	public UnknownDialogEntryCode(long code) {
		entryCode= code;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(" + Long.toString(entryCode) + ")";
	}
}
