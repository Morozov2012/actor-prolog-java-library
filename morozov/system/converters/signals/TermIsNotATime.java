// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.converters.signals;

import morozov.run.*;

public final class TermIsNotATime extends LightweightException {
	//
	public static final TermIsNotATime instance= new TermIsNotATime();
	//
	private TermIsNotATime() {
	}
}
