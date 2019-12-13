// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms.signals;

import morozov.run.*;

public final class TermIsUnboundVariable extends LightweightException {
	//
	public static final TermIsUnboundVariable instance= new TermIsUnboundVariable();
	//
	private TermIsUnboundVariable() {
	}
}
