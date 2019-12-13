// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.domains.signals.*;
import morozov.run.*;
import morozov.terms.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashSet;

public class DomainAny extends DomainAlternative {
	//
	private static final long serialVersionUID= 0x1B2B5D10F9BD8A0EL; // 1957760790513420814L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.domains","DomainAny");
	// }
	//
	public DomainAny() {
	}
	//
	@Override
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		return true;
	}
	@Override
	public boolean coversOptimizedSet(long[] keys, Term[] elements, Term tail, PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		return true;
	}
	@Override
	public Term checkAndOptimizeTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		return t.dereferenceValue(cp);
	}
	@Override
	public Term checkTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		return t.dereferenceValue(cp);
	}
	@Override
	public Term checkOptimizedSet(long[] keys, Term[] elements, Term tail, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		return initialValue.dereferenceValue(cp);
	}
	@Override
	public PrologDomain getPairDomain(long key) throws TermIsNotPairDomainAlternative {
		return new PrologAnyDomain();
	}
	//
	@Override
	public boolean isEqualTo(DomainAlternative a, HashSet<PrologDomainPair> stack) {
		return a.isEqualToAny();
	}
	@Override
	public boolean isEqualToAny() {
		return true;
	}
	@Override
	public boolean coversAlternative(DomainAlternative a, PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) {
		return true;
	}
	//
	@Override
	public String toString(CharsetEncoder encoder) {
		return PrologDomainName.tagDomainAlternative_Any;
	}
}
