// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms.signals;

import morozov.run.*;

public final class TermIsNotAnInteger extends LightweightException {
	public static final TermIsNotAnInteger instance= new TermIsNotAnInteger();
	//
	private TermIsNotAnInteger() {
	}
}
