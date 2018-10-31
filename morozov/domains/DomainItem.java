// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.domains.signals.*;
import morozov.run.*;
import morozov.terms.*;

import java.util.HashSet;

public class DomainItem extends MonoArgumentDomainItem {
	//
	public DomainItem(String entry) {
		super(entry);
	}
	//
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		// initiateDomainItemIfNecessary();
		// t= t.dereferenceValue(cp);
		return t.isCoveredByDomain(domainItem,cp,ignoreFreeVariables);
	}
	public Term checkAndOptimizeTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		// initiateDomainItemIfNecessary();
		// t= t.dereferenceValue(cp);
		return domainItem.checkAndOptimizeTerm(t,cp);
	}
	public Term checkTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		// initiateDomainItemIfNecessary();
		// t= t.dereferenceValue(cp);
		return domainItem.checkTerm(t,cp);
	}
	//
	public boolean isEqualTo(DomainAlternative a, HashSet<PrologDomainPair> stack) {
		// initiateDomainItemIfNecessary();
		return a.isEqualToItem(domainItem,stack);
	}
	public boolean isEqualToItem(PrologDomain domain, HashSet<PrologDomainPair> stack) {
		// initiateDomainItemIfNecessary();
		try {
			domainItem.isEqualTo(domain,stack);
			return true;
		} catch (PrologDomainsAreNotEqual e) {
			return false;
		}
	}
	public boolean coversAlternative(DomainAlternative a, PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) {
		// initiateDomainItemIfNecessary();
		try {
			domainItem.coversAlternative(a,ownerDomain,stack);
			return true;
		} catch (PrologDomainsAreNotEqual e) {
			return false;
		}
	}
	//
	protected String getMonoArgumentDomainTag() {
		return PrologDomainName.tagDomainAlternative_DomainItem;
	}
}
