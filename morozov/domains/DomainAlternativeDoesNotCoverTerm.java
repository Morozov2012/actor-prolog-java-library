// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.terms.*;

public class DomainAlternativeDoesNotCoverTerm extends LightweightException {
	protected long position;
	public DomainAlternativeDoesNotCoverTerm(long p) {
		// System.out.printf("DomainAlternativeDoesNotCoverTerm::new:position=%d\n",p);
		position= p;
	}
	// public DomainAlternativeDoesNotCoverTerm() {
	//	position= -1;
	// }
	public long getPosition() {
		return position;
	}
}
