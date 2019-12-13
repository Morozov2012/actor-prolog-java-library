// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.converters.errors;

import morozov.system.*;

public class UnknownColorMapName extends RuntimeException {
	//
	protected ColorMapName name;
	//
	public UnknownColorMapName(ColorMapName n) {
		name= n;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(" + name.toString() + ")";
	}
}
