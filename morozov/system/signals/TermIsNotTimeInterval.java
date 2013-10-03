// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.signals;

import morozov.run.*;

public final class TermIsNotTimeInterval extends LightweightException {
	public static final TermIsNotTimeInterval instance= new TermIsNotTimeInterval();
	//
	private TermIsNotTimeInterval() {
	}
}
