// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.signals;

import morozov.run.*;

public final class UseDefaultValue extends LightweightException {
	//
	public static final UseDefaultValue instance= new UseDefaultValue();
	//
	private UseDefaultValue() {
	}
}
