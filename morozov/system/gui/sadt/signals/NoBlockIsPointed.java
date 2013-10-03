// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.sadt.signals;

import morozov.run.*;

public final class NoBlockIsPointed extends LightweightException {
	public static final NoBlockIsPointed instance= new NoBlockIsPointed();
	//
	private NoBlockIsPointed() {
	}
}
