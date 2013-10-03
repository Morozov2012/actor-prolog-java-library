// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.terms.signals;

import morozov.run.*;

public final class TermIsDummyWorld extends LightweightException {
	public static final TermIsDummyWorld instance= new TermIsDummyWorld();
	//
	private TermIsDummyWorld() {
	}
}
