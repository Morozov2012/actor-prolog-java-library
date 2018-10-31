// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.signals;

import morozov.run.*;

public final class UseDefaultSize extends LightweightException {
	//
	public static final UseDefaultSize instance= new UseDefaultSize();
	//
	private UseDefaultSize() {
	}
}
