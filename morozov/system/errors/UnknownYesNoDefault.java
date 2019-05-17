// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.errors;

import morozov.system.*;

public class UnknownYesNoDefault extends RuntimeException {
	protected YesNoDefault value;
	public UnknownYesNoDefault(YesNoDefault v) {
		value= v;
	}
	public String toString() {
		return this.getClass().toString() + "(" + value.toString() + ")";
	}
}
