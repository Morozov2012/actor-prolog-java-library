// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.domains.signals.*;
import morozov.run.*;
import morozov.terms.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashSet;

public class DomainAny extends DomainAlternative {
	//
	public DomainAny() {
	}
	//
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		return true;
	}
	public boolean coversOptimizedSet(long[] keys, Term[] elements, Term tail, PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		return true;
	}
	public Term checkAndOptimizeTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		return t.dereferenceValue(cp);
	}
	public Term checkTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		return t.dereferenceValue(cp);
	}
	public Term checkOptimizedSet(long[] keys, Term[] elements, Term tail, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		return initialValue.dereferenceValue(cp);
	}
	public PrologDomain getPairDomain(long key) throws TermIsNotPairDomainAlternative {
		return new PrologAnyDomain();
	}
	//
	public boolean isEqualTo(DomainAlternative a, HashSet<PrologDomainPair> stack) {
		return a.isEqualToAny();
	}
	public boolean isEqualToAny() {
		return true;
	}
	public boolean coversAlternative(DomainAlternative a, PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) {
		return true;
	}
	//
	public String toString(CharsetEncoder encoder) {
		return PrologDomainName.tagDomainAlternative_Any;
	}
}
