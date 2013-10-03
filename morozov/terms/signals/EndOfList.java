// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms.signals;

import morozov.run.*;

public final class EndOfList extends LightweightException {
	public static final EndOfList instance= new EndOfList();
	//
	private EndOfList() {
	}
}
