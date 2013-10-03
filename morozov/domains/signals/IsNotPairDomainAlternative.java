// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains.signals;

import morozov.run.*;

public final class IsNotPairDomainAlternative extends LightweightException {
	public static final IsNotPairDomainAlternative instance= new IsNotPairDomainAlternative();
	//
	private IsNotPairDomainAlternative() {
	}
}
