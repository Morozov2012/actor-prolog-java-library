// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.gui.signals;

import morozov.run.*;

public final class UseDefaultFontSize extends LightweightException {
	public static final UseDefaultFontSize instance= new UseDefaultFontSize();
	//
	private UseDefaultFontSize() {
	}
}
