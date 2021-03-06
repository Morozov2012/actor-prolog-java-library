// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.worlds.remote.*;

import java.util.HashSet;

public abstract class DomainAbstractWorld extends DomainAlternative {
	//
	private static final long serialVersionUID= 0xDB2884F17AC5DB1CL; // -2654725807653135588L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.domains","DomainAbstractWorld");
	// }
	//
	public DomainAbstractWorld() {
	}
	//
	@Override
	public boolean isEqualToForeignDomain(ExternalDomainInterface s) {
		return true; // Never compare world domains with foreign world domains.
	}
	//
	@Override
	public boolean coversAlternative(DomainAlternative a, PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) {
		return false;
	}
}
