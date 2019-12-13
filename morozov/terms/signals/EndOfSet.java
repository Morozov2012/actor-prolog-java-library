// (c) 2009 IRE RAS Alexei A. Morozov

package morozov.terms.signals;

import morozov.run.*;

public final class EndOfSet extends LightweightException {
	//
	public static final EndOfSet instance= new EndOfSet();
	//
	private EndOfSet() {
	}
}
