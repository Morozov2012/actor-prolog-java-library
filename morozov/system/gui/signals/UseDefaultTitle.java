// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.gui.signals;

import morozov.run.*;

public final class UseDefaultTitle extends LightweightException {
	public static final UseDefaultTitle instance= new UseDefaultTitle();
	//
	private UseDefaultTitle() {
	}
}
