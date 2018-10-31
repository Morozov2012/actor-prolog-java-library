// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs.errors;

public class UnknownDialogSlotOrActionName extends RuntimeException {
	public String name;
	public UnknownDialogSlotOrActionName(String n) {
		name= n;
	}
	public String toString() {
		return this.getClass().toString() + "(" + name.toString() + ")";
	}
}
