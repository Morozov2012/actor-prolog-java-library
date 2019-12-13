// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.converters.signals;

import morozov.run.*;

public final class TermIsNotADate extends LightweightException {
	//
	public static final TermIsNotADate instance= new TermIsNotADate();
	//
	private TermIsNotADate() {
	}
}
