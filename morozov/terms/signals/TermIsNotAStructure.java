// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.terms.signals;

import morozov.run.*;

public final class TermIsNotAStructure extends LightweightException {
	public static final TermIsNotAStructure instance= new TermIsNotAStructure();
	//
	private TermIsNotAStructure() {
	}
}
