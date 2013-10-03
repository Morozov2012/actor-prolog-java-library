// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.signals;

import morozov.run.*;

public final class UseDefaultSize extends LightweightException {
	public static final UseDefaultSize instance= new UseDefaultSize();
	//
	private UseDefaultSize() {
	}
}
