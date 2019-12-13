// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.terms.signals;

import morozov.run.*;

public final class SetElementIsProhibited extends LightweightException {
	//
	public static final SetElementIsProhibited instance= new SetElementIsProhibited();
	//
	private SetElementIsProhibited() {
	}
}
