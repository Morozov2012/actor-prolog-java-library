// (c) 2009 IRE RAS Alexei A. Morozov

package morozov.system.gui.reports.signals;

import morozov.run.*;

public final class TermIsSymbolNoLimit extends LightweightException {
	public static final TermIsSymbolNoLimit instance= new TermIsSymbolNoLimit();
	//
	private TermIsSymbolNoLimit() {
	}
}
