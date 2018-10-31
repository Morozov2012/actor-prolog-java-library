// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.domains.signals.*;
import morozov.run.*;
import morozov.terms.*;

import java.nio.charset.CharsetEncoder;

public class PrologAnyDomain extends PrologDomain {
	//
	public PrologAnyDomain() {
		super(new AnonymousDomainName(), new DomainAlternative[0]);
	}
	//
	public boolean coversTerm(Term t, ChoisePoint cp, boolean ignoreFreeVariables) {
		return true;
	}
	public boolean coversOptimizedSet(long[] keys, Term[] elements, Term tail, PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		return true;
	}
	public Term checkAndOptimizeTerm(Term t, ChoisePoint cp) throws DomainAlternativeDoesNotCoverTerm {
		return t.dereferenceValue(cp);
	}
	public Term checkTerm(Term t, ChoisePoint cp) throws DomainAlternativeDoesNotCoverTerm {
		return t.dereferenceValue(cp);
	}
	public Term checkOptimizedSet(long[] keys, Term[] elements, Term tail, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		return initialValue.dereferenceValue(cp);
	}
	public PrologDomain getPairDomain(long key) {
		return this;
	}
	//
	public String toString(CharsetEncoder encoder) {
		return PrologDomainName.tagDomainItem_AnyDomain;
	}
}
