// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.gui.signals;

import morozov.run.*;

public final class UseDefaultColor extends LightweightException {
	public static final UseDefaultColor instance= new UseDefaultColor();
	//
	private UseDefaultColor() {
	}
}
