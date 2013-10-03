// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs.signals;

import morozov.run.*;

public final class TermIsNotFrontCell extends LightweightException {
	public static final TermIsNotFrontCell instance= new TermIsNotFrontCell();
	//
	private TermIsNotFrontCell() {
	}
}
