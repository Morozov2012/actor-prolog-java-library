// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.gui.signals;

import morozov.run.*;

public final class IsNotColorSymbolCode extends LightweightException {
	public static final IsNotColorSymbolCode instance= new IsNotColorSymbolCode();
	//
	private IsNotColorSymbolCode() {
	}
}
