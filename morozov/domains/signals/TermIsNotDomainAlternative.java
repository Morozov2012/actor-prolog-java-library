// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.domains.signals;

import morozov.run.*;

public final class TermIsNotDomainAlternative extends LightweightException {
	//
	public static final TermIsNotDomainAlternative instance= new TermIsNotDomainAlternative();
	//
	private TermIsNotDomainAlternative() {
	}
}
