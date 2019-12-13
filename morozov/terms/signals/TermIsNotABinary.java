// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.terms.signals;

import morozov.run.*;

public final class TermIsNotABinary extends LightweightException {
	//
	public static final TermIsNotABinary instance= new TermIsNotABinary();
	//
	private TermIsNotABinary() {
	}
}
