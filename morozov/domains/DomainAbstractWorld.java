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
	// public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
	//	t= t.dereferenceValue(cp);
	//	if (ignoreFreeVariables && t.thisIsFreeVariable()) {
	//		return true;
	//	} else {
	//		if (t.thisIsOwnWorld()) {
	//			return true; // Never compare worlds with foreign worlds.
	//		} else {
	//			return false;
	//		}
	//	}
	// }
	//
	public boolean isEqualToForeignDomain(ExternalDomainInterface s) {
		return true; // Never compare world domains with foreign world domains.
	}
	//
	public boolean coversAlternative(DomainAlternative a, PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) {
		return false;
	}
}
