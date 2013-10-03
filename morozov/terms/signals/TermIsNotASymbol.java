// (c) 2009 IRE RAS Alexei A. Morozov

package morozov.terms.signals;

import morozov.run.*;

public final class TermIsNotASymbol extends LightweightException {
	public static final TermIsNotASymbol instance= new TermIsNotASymbol();
	//
	private TermIsNotASymbol() {
	}
}
