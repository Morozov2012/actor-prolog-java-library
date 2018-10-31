// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.signals;

import morozov.run.*;

public final class IsNotColorName extends LightweightException {
	public static final IsNotColorName instance= new IsNotColorName();
	//
	private IsNotColorName() {
	}
}
