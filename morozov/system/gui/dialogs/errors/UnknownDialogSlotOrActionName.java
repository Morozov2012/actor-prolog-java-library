// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs.errors;

public class UnknownDialogSlotOrActionName extends RuntimeException {
	//
	protected String name;
	//
	public UnknownDialogSlotOrActionName(String n) {
		name= n;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(" + name.toString() + ")";
	}
}
