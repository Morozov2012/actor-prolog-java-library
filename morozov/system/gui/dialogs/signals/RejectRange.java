// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs.signals;

import morozov.run.*;

public final class RejectRange extends LightweightException {
	public static final RejectRange instance= new RejectRange();
	//
	private RejectRange() {
	}
}
