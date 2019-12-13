// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.signals;

import morozov.run.*;

public final class TermIsNotProcessPriority extends LightweightException {
	//
	public static final TermIsNotProcessPriority instance= new TermIsNotProcessPriority();
	//
	private TermIsNotProcessPriority() {
	}
}
