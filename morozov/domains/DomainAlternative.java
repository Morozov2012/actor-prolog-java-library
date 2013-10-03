// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.domains.signals.*;
import morozov.run.*;
import morozov.terms.*;

import java.util.HashMap;
import java.util.Set;

public abstract class DomainAlternative {
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		return false;
	}
	public boolean coversOptimizedSet(long[] keys, Term[] elements, Term tail, PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		return false;
	}
	public Term checkAndOptimizeTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		if (coversTerm(t,cp,baseDomain,false)) {
			return t.dereferenceValue(cp);
		} else {
			throw new DomainAlternativeDoesNotCoverTerm(t.getPosition());
		}
	}
	public Term checkTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		if (coversTerm(t,cp,baseDomain,false)) {
			return t.dereferenceValue(cp);
		} else {
			throw new DomainAlternativeDoesNotCoverTerm(t.getPosition());
		}
	}
	public Term checkOptimizedSet(long[] keys, Term[] elements, Term tail, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		throw new DomainAlternativeDoesNotCoverTerm(initialValue.getPosition());
	}
	protected static Term checkAndCollectSetElements(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		// initiateDomainItemIfNecessary();
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
		Term setEnd= t.exploreSetPositiveElements(setPositiveMap,cp);
		setEnd= setEnd.dereferenceValue(cp);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			Set<Long> keys= setPositiveMap.keySet();
			Long[] functors= new Long[keys.size()];
			keys.toArray(functors);
			// Term[] arguments= new Term[functors.length];
			DomainAlternative[] alternatives= baseDomain.alternatives;
			Term result= PrologEmptySet.instance;
			for (int n=functors.length-1; n > 0; n--) {
				long currentKey= functors[n];
				Term currentElement= setPositiveMap.get(currentKey);
				boolean elementIsFound= false;
				for (int i=0; i < alternatives.length; i++) {
					DomainAlternative alternative= alternatives[i];
					try {
						PrologDomain currentDomain= alternative.getPairDomain(currentKey);
						currentElement= currentDomain.checkTerm(currentElement,cp);
						elementIsFound= true;
						break;
					} catch (IsNotPairDomainAlternative e) {
					}
				};
				if (!elementIsFound) {
					throw new DomainAlternativeDoesNotCoverTerm(currentElement.getPosition());
				};
				result= new PrologSet(currentKey,currentElement,result);
			};
			return result;
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
		throw IsNotPairDomainAlternative.instance;
	}
}
