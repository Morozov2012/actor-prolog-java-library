// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.signals;

import morozov.run.*;

public final class UseDefaultLocation extends LightweightException {
	//
	public static final UseDefaultLocation instance= new UseDefaultLocation();
	//
	private UseDefaultLocation() {
	}
}
