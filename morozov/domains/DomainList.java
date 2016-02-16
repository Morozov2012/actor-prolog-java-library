// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.domains.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.util.HashSet;

public class DomainList extends MonoArgumentDomainItem {
	public DomainList(String entry) {
		super(entry);
	}
	//
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		// initiateDomainItemIfNecessary();
		Term nextHead;
		Term currentTail= t; // t.dereferenceValue(cp);
		try {
			while (true) {
				currentTail= currentTail.dereferenceValue(cp);
				if (ignoreFreeVariables && currentTail.thisIsFreeVariable()) {
					return true;
				} else {
					nextHead= currentTail.getNextListHead(cp);
					if (!nextHead.isCoveredByDomain(domainItem,cp,ignoreFreeVariables)) {
						return false;
					};
					currentTail= currentTail.getNextListTail(cp);
				}
			}
		} catch (EndOfList e) {
			return true;
		} catch (TermIsNotAList e) {
			return false;
		}
	}
	public Term checkAndOptimizeTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		// initiateDomainItemIfNecessary();
		// t= t.dereferenceValue(cp);
		try {
			return new PrologList(
				domainItem.checkAndOptimizeTerm(t.getNextListHead(cp),cp),
				baseDomain.checkAndOptimizeTerm(t.getNextListTail(cp),cp));
		} catch (EndOfList e) {
			return PrologEmptyList.instance;
		} catch (TermIsNotAList e) {
			throw new DomainAlternativeDoesNotCoverTerm(t.getPosition());
		}
	}
	public Term checkTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		// initiateDomainItemIfNecessary();
		// t= t.dereferenceValue(cp);
		try {
			return new PrologList(
				domainItem.checkTerm(t.getNextListHead(cp),cp),
				baseDomain.checkTerm(t.getNextListTail(cp),cp));
		} catch (EndOfList e) {
			return PrologEmptyList.instance;
		} catch (TermIsNotAList e) {
			throw new DomainAlternativeDoesNotCoverTerm(t.getPosition());
		}
	}
	//
	public boolean isEqualTo(DomainAlternative a, HashSet<PrologDomainPair> stack) {
		// initiateDomainItemIfNecessary();
		return a.isEqualToList(domainItem,stack);
	}
	public boolean isEqualToList(PrologDomain domain, HashSet<PrologDomainPair> stack) {
		// initiateDomainItemIfNecessary();
		try {
			domainItem.isEqualTo(domain,stack);
			return true;
		} catch (PrologDomainsAreNotEqual e) {
			return false;
		}
	}
	public boolean coversAlternative(DomainAlternative a, PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) {
		return false;
	}
	//
	protected String getMonoArgumentDomainTag() {
		return PrologDomainName.tagDomainAlternative_DomainList;
	}
}
