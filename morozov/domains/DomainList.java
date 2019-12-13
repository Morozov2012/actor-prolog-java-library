// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.domains.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.util.HashSet;

public class DomainList extends MonoArgumentDomainItem {
	//
	private static final long serialVersionUID= 0x65FE1DE00A041BCAL; // 7349344489993149386L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.domains","DomainList");
	// }
	//
	public DomainList(String entry) {
		super(entry);
	}
	//
	@Override
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		Term nextHead;
		Term currentTail= t;
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
	@Override
	public Term checkAndOptimizeTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
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
	@Override
	public Term checkTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
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
	@Override
	public boolean isEqualTo(DomainAlternative a, HashSet<PrologDomainPair> stack) {
		return a.isEqualToList(domainItem,stack);
	}
	@Override
	public boolean isEqualToList(PrologDomain domain, HashSet<PrologDomainPair> stack) {
		try {
			domainItem.isEqualTo(domain,stack);
			return true;
		} catch (PrologDomainsAreNotEqual e) {
			return false;
		}
	}
	@Override
	public boolean coversAlternative(DomainAlternative a, PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) {
		return false;
	}
	//
	@Override
	protected String getMonoArgumentDomainTag() {
		return PrologDomainName.tagDomainAlternative_DomainList;
	}
}
