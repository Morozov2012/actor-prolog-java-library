// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d.signals;

import morozov.run.*;

public final class UseDefaultFieldOfView extends LightweightException {
	public static final UseDefaultFieldOfView instance= new UseDefaultFieldOfView();
	//
	private UseDefaultFieldOfView() {
	}
}
