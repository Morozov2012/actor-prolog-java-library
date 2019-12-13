// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.domains.signals;

import morozov.run.*;

public final class UnknownPairName extends LightweightException {
	//
	public static final UnknownPairName instance= new UnknownPairName();
	//
	private UnknownPairName() {
	}
}
