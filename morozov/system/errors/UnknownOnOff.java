// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.errors;

import morozov.system.*;

public class UnknownOnOff extends RuntimeException {
	protected OnOff value;
	public UnknownOnOff(OnOff v) {
		value= v;
	}
	public String toString() {
		return this.getClass().toString() + "(" + value.toString() + ")";
	}
}
