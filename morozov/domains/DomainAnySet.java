// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.terms.*;

import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

public class DomainAnySet extends DomainAlternative {
	public DomainAnySet() {
	}
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		// t= t.dereferenceValue(cp);
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
	public boolean coversOptimizedSet(long[] keys, Term[] elements, Term tail, PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		return true;
	}
	public Term checkAndOptimizeTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		return checkCollectAndOptimizeSetElements(t,cp,baseDomain);
	}
	public Term checkTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		// initiateDomainItemIfNecessary();
		return checkAndCollectSetElements(t,cp,baseDomain);
	}
	public Term checkOptimizedSet(long[] keys, Term[] elements, Term tail, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		return checkAndCollectSetElements(initialValue,cp,baseDomain);
	}
	protected PrologOptimizedSet checkCollectAndOptimizeSetElements(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		// initiateDomainItemsIfNecessary();
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
		Term setEnd= t.exploreSetPositiveElements(setPositiveMap,cp);
		setEnd= setEnd.dereferenceValue(cp);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			Set<Long> keys= setPositiveMap.keySet();
			long[] functors= new long[keys.size()];
			// keys.toArray(functors);
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
					arguments[n]= null; // new PrologNoValue();
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
	public PrologDomain getPairDomain(long key) throws IsNotPairDomainAlternative {
		return new PrologAnyDomain();
	}
}
