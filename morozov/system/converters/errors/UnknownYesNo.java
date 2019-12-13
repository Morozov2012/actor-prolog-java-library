// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.converters.errors;

import morozov.system.*;

public class UnknownYesNo extends RuntimeException {
	//
	protected YesNo value;
	//
	public UnknownYesNo(YesNo v) {
		value= v;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(" + value.toString() + ")";
	}
}
