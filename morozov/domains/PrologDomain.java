// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.domains.signals.*;
import morozov.run.*;
import morozov.terms.*;

public class PrologDomain {
	//
	protected PrologDomainName name;
	protected DomainAlternative[] alternatives;
	//
	public PrologDomain(PrologDomainName n, DomainAlternative[] list) {
		name= n;
		alternatives= list;
	}
	public boolean coversTerm(Term t, ChoisePoint cp, boolean ignoreFreeVariables) {
		for (int i= 0; i < alternatives.length; i++) {
			// System.out.printf("PrologDomain:alternatives[%s]: %s = %s\n",i,alternatives[i],t);
			if (alternatives[i].coversTerm(t,cp,this,ignoreFreeVariables)) {
				return true;
			}
		};
		return false;
	}
	public boolean coversOptimizedSet(long[] keys, Term[] elements, Term tail, PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		return false;
	}
	public Term checkAndOptimizeTerm(Term t, ChoisePoint cp) throws DomainAlternativeDoesNotCoverTerm {
		long maximalPosition= -1;
		for (int i= 0; i < alternatives.length; i++) {
			try {
				// System.out.printf("PrologDomain::%s.checkAndOptimizeTerm:t=%s\n",alternatives[i],t);
				return alternatives[i].checkAndOptimizeTerm(t,cp,this);
			} catch (DomainAlternativeDoesNotCoverTerm e) {
				long p= e.getPosition();
				if (maximalPosition < p) {
					maximalPosition= p;
				};
				continue;
			} catch (Throwable e) {
				continue;
			}
		};
		long p= t.getPosition();
		if (maximalPosition < p) {
			maximalPosition= p;
		};
		throw new DomainAlternativeDoesNotCoverTerm(maximalPosition);
	}
	public Term checkTerm(Term t, ChoisePoint cp) throws DomainAlternativeDoesNotCoverTerm {
		long maximalPosition= -1;
		for (int i= 0; i < alternatives.length; i++) {
			try {
				return alternatives[i].checkTerm(t,cp,this);
			} catch (DomainAlternativeDoesNotCoverTerm e) {
				long p= e.getPosition();
				if (maximalPosition < p) {
					maximalPosition= p;
				};
				continue;
			} catch (Throwable e) {
				continue;
			}
		};
		long p= t.getPosition();
		if (maximalPosition < p) {
			maximalPosition= p;
		};
		throw new DomainAlternativeDoesNotCoverTerm(maximalPosition);
	}
	public Term checkOptimizedSet(long[] keys, Term[] elements, Term tail, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		long maximalPosition= -1;
		for (int i= 0; i < alternatives.length; i++) {
			try {
				return alternatives[i].checkOptimizedSet(keys,elements,tail,initialValue,cp,baseDomain);
			} catch (DomainAlternativeDoesNotCoverTerm e) {
				long p= e.getPosition();
				if (maximalPosition < p) {
					maximalPosition= p;
				};
				continue;
			} catch (Throwable e) {
				continue;
			}
		};
		long p= initialValue.getPosition();
		if (maximalPosition < p) {
			maximalPosition= p;
		};
		throw new DomainAlternativeDoesNotCoverTerm(maximalPosition);
	}
	public PrologDomain getPairDomain(long key) throws UnknownPairName {
		for (int i= 0; i < alternatives.length; i++) {
			try {
				PrologDomain pairDomain= alternatives[i].getPairDomain(key);
				return pairDomain;
			} catch (IsNotPairDomainAlternative e) {
				continue;
			}
		};
		throw UnknownPairName.instance;
	}
}
