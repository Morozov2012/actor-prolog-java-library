// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.domains.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashSet;

public class DomainEmptySet extends DomainAlternative {
	//
	private static final long serialVersionUID= 0x6560042A07C67840L; // 7304843174160529472L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.domains","DomainEmptySet");
	// }
	//
	public DomainEmptySet() {
	}
	//
	@Override
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
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
	@Override
	public boolean coversOptimizedSet(long[] keys, Term[] elements, Term tail, PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
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
	@Override
	public Term checkAndOptimizeTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		if (coversTerm(t,cp,baseDomain,false)) {
			return PrologEmptySet.instance;
		} else {
			throw new DomainAlternativeDoesNotCoverTerm(t.getPosition());
		}
	}
	@Override
	public Term checkTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		if (coversTerm(t,cp,baseDomain,false)) {
			return PrologEmptySet.instance;
		} else {
			throw new DomainAlternativeDoesNotCoverTerm(t.getPosition());
		}
	}
	@Override
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
	//
	@Override
	public boolean isEqualTo(DomainAlternative a, HashSet<PrologDomainPair> stack) {
		return a.isEqualToEmptySet();
	}
	@Override
	public boolean isEqualToEmptySet() {
		return true;
	}
	@Override
	public boolean coversAlternative(DomainAlternative a, PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) {
		return false;
	}
	@Override
	public boolean isCoveredBySetAny() {
		return true;
	}
	@Override
	public boolean isCoveredByOptimizedSet(long[] names, PrologDomain[] domains, HashSet<PrologDomainPair> stack) {
		return true;
	}
	public boolean isCoveredBySet(PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) {
		return true;
	}
	//
	@Override
	public String toString(CharsetEncoder encoder) {
		return PrologDomainName.tagDomainAlternative_EmptySet;
	}
}
