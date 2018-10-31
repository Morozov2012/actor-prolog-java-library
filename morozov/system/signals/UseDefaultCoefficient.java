// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.signals;

import morozov.run.*;

public final class UseDefaultCoefficient extends LightweightException {
	//
	public static final UseDefaultCoefficient instance= new UseDefaultCoefficient();
	//
	private UseDefaultCoefficient() {
	}
}
