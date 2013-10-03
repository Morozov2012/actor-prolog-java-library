// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms.signals;

import morozov.run.*;

public final class TermIsNotAList extends LightweightException {
	public static final TermIsNotAList instance= new TermIsNotAList();
	//
	private TermIsNotAList() {
	}
}
