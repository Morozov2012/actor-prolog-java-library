// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.domains.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class DomainList extends MonoArgumentDomainItem {
	public DomainList(String entry) {
		super(entry);
	}
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		initiateDomainItemIfNecessary();
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
		initiateDomainItemIfNecessary();
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
		initiateDomainItemIfNecessary();
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
}
