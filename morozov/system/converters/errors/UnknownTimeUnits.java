// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.converters.errors;

import morozov.system.*;

public class UnknownTimeUnits extends RuntimeException {
	//
	protected TimeUnits value;
	//
	public UnknownTimeUnits(TimeUnits v) {
		value= v;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(" + value.toString() + ")";
	}
}
