// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.terms.signals;

import morozov.run.*;

public final class TermIsNotSetElement extends LightweightException {
	public static final TermIsNotSetElement instance= new TermIsNotSetElement();
	//
	private TermIsNotSetElement() {
	}
}
