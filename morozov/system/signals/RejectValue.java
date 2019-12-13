// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.signals;

import morozov.run.*;

public final class RejectValue extends LightweightException {
	//
	public static final RejectValue instance= new RejectValue();
	//
	private RejectValue() {
	}
}
