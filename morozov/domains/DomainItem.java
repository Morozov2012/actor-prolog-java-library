// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.domains.signals.*;
import morozov.run.*;
import morozov.terms.*;

import java.util.HashSet;

public class DomainItem extends MonoArgumentDomainItem {
	//
	private static final long serialVersionUID= 0xC1688AADFC0C46E3L; // -4510202546948913437L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.domains","DomainItem");
	// }
	//
	public DomainItem(String entry) {
		super(entry);
	}
	//
	@Override
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		return t.isCoveredByDomain(domainItem,cp,ignoreFreeVariables);
	}
	@Override
	public Term checkAndOptimizeTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		return domainItem.checkAndOptimizeTerm(t,cp);
	}
	@Override
	public Term checkTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		return domainItem.checkTerm(t,cp);
	}
	//
	@Override
	public boolean isEqualTo(DomainAlternative a, HashSet<PrologDomainPair> stack) {
		return a.isEqualToItem(domainItem,stack);
	}
	@Override
	public boolean isEqualToItem(PrologDomain domain, HashSet<PrologDomainPair> stack) {
		try {
			domainItem.isEqualTo(domain,stack);
			return true;
		} catch (PrologDomainsAreNotEqual e) {
			return false;
		}
	}
	@Override
	public boolean coversAlternative(DomainAlternative a, PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) {
		try {
			domainItem.coversAlternative(a,ownerDomain,stack);
			return true;
		} catch (PrologDomainsAreNotEqual e) {
			return false;
		}
	}
	//
	@Override
	protected String getMonoArgumentDomainTag() {
		return PrologDomainName.tagDomainAlternative_DomainItem;
	}
}
