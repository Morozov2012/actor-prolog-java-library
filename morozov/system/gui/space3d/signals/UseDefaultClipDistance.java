// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d.signals;

import morozov.run.*;

public final class UseDefaultClipDistance extends LightweightException {
	public static final UseDefaultClipDistance instance= new UseDefaultClipDistance();
	//
	private UseDefaultClipDistance() {
	}
}
