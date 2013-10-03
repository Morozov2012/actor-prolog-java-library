// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.domains.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class DomainEmptySet extends DomainAlternative {
	public DomainEmptySet() {
	}
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		// t= t.dereferenceValue(cp);
		t= t.dereferenceValue(cp);
		if (ignoreFreeVariables && t.thisIsFreeVariable()) {
			return true;
		} else {
			if (t.thisIsEmptySet()) {
				return true;
			} else {
				return t.isCoveredByEmptySetDomain(baseDomain,cp,ignoreFreeVariables);
			}
		}
	}
	public boolean coversOptimizedSet(long[] keys, Term[] elements, Term tail, PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		// initiateDomainItemsIfNecessary();
		if (keys.length == 0) {
			return true;
		} else {
			for (int n=0; n < keys.length; n++) {
				long currentKey= keys[n];
				try {
					Term value= elements[n].retrieveSetElementValue(cp);
					return false;
				} catch (Backtracking b) {
				} catch (TermIsNotSetElement b) {
					return false;
				}
			};
			return tail.isCoveredByEmptySetDomain(baseDomain,cp,ignoreFreeVariables);
		}
	}
	public Term checkAndOptimizeTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		if (coversTerm(t,cp,baseDomain,false)) {
			return PrologEmptySet.instance;
		} else {
			throw new DomainAlternativeDoesNotCoverTerm(t.getPosition());
		}
	}
	public Term checkTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		if (coversTerm(t,cp,baseDomain,false)) {
			return PrologEmptySet.instance;
		} else {
			throw new DomainAlternativeDoesNotCoverTerm(t.getPosition());
		}
	}
	public Term checkOptimizedSet(long[] keys, Term[] elements, Term tail, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		if (keys.length == 0) {
			return PrologEmptySet.instance;
		} else {
			if (coversTerm(initialValue,cp,baseDomain,false)) {
				return PrologEmptySet.instance;
			} else {
				throw new DomainAlternativeDoesNotCoverTerm(initialValue.getPosition());
			}
		}
	}
}
