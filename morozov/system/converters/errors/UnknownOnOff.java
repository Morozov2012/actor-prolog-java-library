// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.converters.errors;

import morozov.system.*;

public class UnknownOnOff extends RuntimeException {
	//
	protected OnOff value;
	//
	public UnknownOnOff(OnOff v) {
		value= v;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(" + value.toString() + ")";
	}
}
