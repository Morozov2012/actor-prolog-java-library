// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms.signals;

import morozov.run.*;

public final class TermIsNotAReal extends LightweightException {
	public static final TermIsNotAReal instance= new TermIsNotAReal();
	//
	private TermIsNotAReal() {
	}
}
