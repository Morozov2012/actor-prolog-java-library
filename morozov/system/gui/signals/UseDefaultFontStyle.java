// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.gui.signals;

import morozov.run.*;

public final class UseDefaultFontStyle extends LightweightException {
	public static final UseDefaultFontStyle instance= new UseDefaultFontStyle();
	//
	private UseDefaultFontStyle() {
	}
}
