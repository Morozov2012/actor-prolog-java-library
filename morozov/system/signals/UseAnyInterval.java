// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.signals;

import morozov.run.*;

public final class UseAnyInterval extends LightweightException {
	public static final UseAnyInterval instance= new UseAnyInterval();
	//
	private UseAnyInterval() {
	}
}
