// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.domains.signals;

import morozov.run.*;

public final class PrologDomainsAreNotEqual extends LightweightException {
	//
	public static final PrologDomainsAreNotEqual instance= new PrologDomainsAreNotEqual();
	//
	private PrologDomainsAreNotEqual() {
	}
}
