// (c) 2009 IRE RAS Alexei A. Morozov

package morozov.system.signals;

import morozov.run.*;

public final class TermIsSymbolDefault extends LightweightException {
	//
	public static final TermIsSymbolDefault instance= new TermIsSymbolDefault();
	//
	private TermIsSymbolDefault() {
	}
}
