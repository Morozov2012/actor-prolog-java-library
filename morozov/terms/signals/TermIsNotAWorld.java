// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms.signals;

import morozov.run.*;

public final class TermIsNotAWorld extends LightweightException {
	public static final TermIsNotAWorld instance= new TermIsNotAWorld();
	//
	private TermIsNotAWorld() {
	}
}
