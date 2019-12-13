// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains.signals;

import morozov.run.*;

public final class DomainAlternativeDoesNotCoverTerm extends LightweightException {
	//
	protected long position;
	//
	public DomainAlternativeDoesNotCoverTerm(long p) {
		position= p;
	}
	//
	public long getPosition() {
		return position;
	}
}
