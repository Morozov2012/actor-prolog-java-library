// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.domains.signals;

import morozov.run.*;

public final class TermIsNotPrologDomain extends LightweightException {
	//
	public static final TermIsNotPrologDomain instance= new TermIsNotPrologDomain();
	//
	private TermIsNotPrologDomain() {
	}
}
