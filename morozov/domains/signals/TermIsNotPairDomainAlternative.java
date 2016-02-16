// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains.signals;

import morozov.run.*;

public final class TermIsNotPairDomainAlternative extends LightweightException {
	public static final TermIsNotPairDomainAlternative instance= new TermIsNotPairDomainAlternative();
	//
	private TermIsNotPairDomainAlternative() {
	}
}
