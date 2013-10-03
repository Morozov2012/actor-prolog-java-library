// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs.signals;

import morozov.run.*;

public final class TermIsSymbolAuto extends LightweightException {
	public static final TermIsSymbolAuto instance= new TermIsSymbolAuto();
	//
	private TermIsSymbolAuto() {
	}
}
