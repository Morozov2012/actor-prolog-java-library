// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.signals;

import morozov.run.*;

public final class UseDefaultInterval extends LightweightException {
	public static final UseDefaultInterval instance= new UseDefaultInterval();
	//
	private UseDefaultInterval() {
	}
}
