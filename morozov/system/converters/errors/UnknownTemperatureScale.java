// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.converters.errors;

import morozov.system.*;

public class UnknownTemperatureScale extends RuntimeException {
	//
	protected TemperatureScale value;
	//
	public UnknownTemperatureScale(TemperatureScale v) {
		value= v;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(" + value.toString() + ")";
	}
}
