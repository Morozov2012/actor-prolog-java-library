// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs.errors;

public class CannotAccessDialogControl extends RuntimeException {
	public CannotAccessDialogControl(Throwable e) {
		super(e);
	}
}
