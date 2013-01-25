// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.terms.*;

public class DomainSet extends MonoArgumentDomainItem {
	protected long functor;
	public DomainSet(long name, String entry) {
		super(entry);
		functor= name;
	}
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		initiateDomainItemIfNecessary();
		// t= t.dereferenceValue(cp);
		t= t.dereferenceValue(cp);
		if (ignoreFreeVariables && t.thisIsFreeVariable()) {
			return true;
		} else {
			return t.isCoveredBySetDomain(functor,domainItem,baseDomain,cp,ignoreFreeVariables);
		}
	}
	public boolean coversOptimizedSet(long[] keys, Term[] elements, Term tail, PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		initiateDomainItemIfNecessary();
		for (int n=0; n < keys.length; n++) {
			long currentKey= keys[n];
			try {
				Term value= elements[n].retrieveSetElementValue(cp);
				// value= value.dereferenceValue(cp);
				value= value.dereferenceValue(cp);
				if (ignoreFreeVariables && value.thisIsFreeVariable()) {
					// return true;
					continue;
				} else {
					boolean elementIsFound= false;
					DomainAlternative[] alternatives= baseDomain.alternatives;
					for (int i=0; i < alternatives.length; i++) {
						// System.out.printf("PrologDomain:alternatives[%s]: %s = %s\n",i,alternatives[i],t);
						DomainAlternative alternative= alternatives[i];
						try {
							PrologDomain currentDomain= alternative.getPairDomain(currentKey);
							if (!value.isCoveredByDomain(currentDomain,cp,ignoreFreeVariables)) {
								return false;
							} else {
								elementIsFound= true;
								break;
							}
						} catch (IsNotPairDomainAlternative e) {
						}
					};
					if (!elementIsFound) {
						return false;
					}
				}
			} catch (Backtracking b) {
			} catch (TermIsNotSetElement b) {
				return false;
			}
		};
		return tail.isCoveredByDomain(baseDomain,cp,ignoreFreeVariables);
	}
	public Term checkAndOptimizeTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		throw new DomainAlternativeDoesNotCoverTerm(t.getPosition());
	}
	public Term checkTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		initiateDomainItemIfNecessary();
		return t.checkSetTerm(functor,domainItem,t,cp,baseDomain);
	}
	public Term checkOptimizedSet(long[] keys, Term[] elements, Term tail, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		initiateDomainItemIfNecessary();
		return checkAndCollectSetElements(initialValue,cp,baseDomain);
	}
	public PrologDomain getPairDomain(long key) throws IsNotPairDomainAlternative {
		if (key==functor) {
			initiateDomainItemIfNecessary();
			return domainItem;
		} else {
			throw new IsNotPairDomainAlternative();
		}
	}
}
