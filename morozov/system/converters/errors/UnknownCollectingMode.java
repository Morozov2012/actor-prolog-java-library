// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.converters.errors;

import morozov.system.*;

public class UnknownCollectingMode extends RuntimeException {
	//
	protected CollectingMode value;
	//
	public UnknownCollectingMode(CollectingMode v) {
		value= v;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(" + value.toString() + ")";
	}
}
