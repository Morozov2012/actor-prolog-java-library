// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.terms.*;

public class DomainItem extends MonoArgumentDomainItem {
	public DomainItem(String entry) {
		super(entry);
	}
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		initiateDomainItemIfNecessary();
		// t= t.dereferenceValue(cp);
		return t.isCoveredByDomain(domainItem,cp,ignoreFreeVariables);
	}
	public Term checkAndOptimizeTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		initiateDomainItemIfNecessary();
		// t= t.dereferenceValue(cp);
		return domainItem.checkAndOptimizeTerm(t,cp);
	}
	public Term checkTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		initiateDomainItemIfNecessary();
		// t= t.dereferenceValue(cp);
		return domainItem.checkTerm(t,cp);
	}
}
