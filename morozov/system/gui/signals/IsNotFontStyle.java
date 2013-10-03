// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.gui.signals;

import morozov.run.*;

public final class IsNotFontStyle extends LightweightException {
	public static final IsNotFontStyle instance= new IsNotFontStyle();
	//
	private IsNotFontStyle() {
	}
}
