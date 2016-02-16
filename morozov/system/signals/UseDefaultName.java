// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.signals;

import morozov.run.*;

public final class UseDefaultName extends LightweightException {
	public static final UseDefaultName instance= new UseDefaultName();
	//
	private UseDefaultName() {
	}
}
