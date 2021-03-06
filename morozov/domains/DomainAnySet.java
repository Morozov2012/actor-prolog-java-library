// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.domains.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

public class DomainAnySet extends DomainAlternative {
	//
	private static final long serialVersionUID= 0x90439D5141FE90D9L; // -8051418736483200807L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.domains","DomainAnySet");
	// }
	//
	public DomainAnySet() {
	}
	//
	@Override
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		t= t.dereferenceValue(cp);
		if (ignoreFreeVariables && t.thisIsFreeVariable()) {
			return true;
		} else {
			try {
				t.checkIfTermIsASet(cp);
				return true;
			} catch (TermIsNotASet e) {
				return false;
			}
		}
	}
	@Override
	public boolean coversOptimizedSet(long[] keys, Term[] elements, Term tail, PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		return true;
	}
	@Override
	public Term checkAndOptimizeTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		return checkCollectAndOptimizeSetElements(t,cp,baseDomain);
	}
	@Override
	public Term checkTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		return checkAndCollectSetElements(t,cp,baseDomain);
	}
	@Override
	public Term checkOptimizedSet(long[] keys, Term[] elements, Term tail, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		return checkAndCollectSetElements(initialValue,cp,baseDomain);
	}
	protected PrologOptimizedSet checkCollectAndOptimizeSetElements(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		HashMap<Long,Term> setPositiveMap= new HashMap<>();
		Term setEnd= t.exploreSetPositiveElements(setPositiveMap,cp);
		setEnd= setEnd.dereferenceValue(cp);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			Set<Long> keys= setPositiveMap.keySet();
			long[] functors= new long[keys.size()];
			Iterator<Long> iterator= keys.iterator();
			int k= 0;
			while (iterator.hasNext()) {
				functors[k++]= iterator.next();
			}
			Term[] arguments= new Term[functors.length];
			for (int n=0; n < functors.length; n++) {
				long currentKey= functors[n];
				if (setPositiveMap.containsKey(currentKey)) {
					Term currentElement= setPositiveMap.get(currentKey);
					arguments[n]= currentElement;
					setPositiveMap.remove(currentKey);
				} else {
					arguments[n]= null;
				}
			};
			if (setPositiveMap.isEmpty()) {
				return new PrologOptimizedSet(arguments,functors);
			} else {
				throw new DomainAlternativeDoesNotCoverTerm(t.getPosition());
			}
		} else {
			long position1= t.getPosition();
			long position2= setEnd.getPosition();
			if (position1 < position2) {
				position1= position2;
			};
			throw new DomainAlternativeDoesNotCoverTerm(position1);
		}
	}
	@Override
	public PrologDomain getPairDomain(long key) throws TermIsNotPairDomainAlternative {
		return new PrologAnyDomain();
	}
	//
	@Override
	public boolean isEqualTo(DomainAlternative a, HashSet<PrologDomainPair> stack) {
		return a.isEqualToAnySet();
	}
	@Override
	public boolean isEqualToAnySet() {
		return true;
	}
	@Override
	public boolean coversAlternative(DomainAlternative a, PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) {
		return a.isCoveredBySetAny();
	}
	@Override
	public boolean isCoveredBySetAny() {
		return true;
	}
	//
	@Override
	public String toString(CharsetEncoder encoder) {
		return PrologDomainName.tagDomainAlternative_AnySet;
	}
}
