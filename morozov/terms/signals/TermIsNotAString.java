// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms.signals;

import morozov.run.*;

public final class TermIsNotAString extends LightweightException {
	//
	public static final TermIsNotAString instance= new TermIsNotAString();
	//
	private TermIsNotAString() {
	}
}
