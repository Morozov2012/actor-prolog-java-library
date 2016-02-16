// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.gui.reports.signals;

import morozov.run.*;

public final class UseNoLimit extends LightweightException {
	public static final UseNoLimit instance= new UseNoLimit();
	//
	private UseNoLimit() {
	}
}
