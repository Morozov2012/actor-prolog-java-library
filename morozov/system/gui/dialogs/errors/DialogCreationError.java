// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs.errors;

public class DialogCreationError extends RuntimeException {
	public DialogCreationError(Throwable e) {
		super(e);
	}
}
