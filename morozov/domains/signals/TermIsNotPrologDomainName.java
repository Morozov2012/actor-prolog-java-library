// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.domains.signals;

import morozov.run.*;

public final class TermIsNotPrologDomainName extends LightweightException {
	//
	public static final TermIsNotPrologDomainName instance= new TermIsNotPrologDomainName();
	//
	private TermIsNotPrologDomainName() {
	}
}
