// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d.signals;

import morozov.run.*;

public final class NoObjectSelected extends LightweightException {
	public static final NoObjectSelected instance= new NoObjectSelected();
	//
	private NoObjectSelected() {
	}
}
