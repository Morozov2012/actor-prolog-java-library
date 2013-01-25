// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.terms.*;

import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

public class DomainOptimizedSet extends MultiArgumentDomainItem {
	protected long[] functors;
	public DomainOptimizedSet(long[] names, String[] entries) {
		super(entries);
		functors= names;
	}
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		initiateDomainItemsIfNecessary();
		// t= t.dereferenceValue(cp);
		t= t.dereferenceValue(cp);
		if (ignoreFreeVariables && t.thisIsFreeVariable()) {
			return true;
		} else {
			HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
			Term setEnd= t.exploreSetPositiveElements(setPositiveMap,cp);
			setEnd= setEnd.dereferenceValue(cp);
			if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
				Set<Long> keys= setPositiveMap.keySet();
				Long[] keyArray= new Long[keys.size()];
				keys.toArray(keyArray);
				for (int n=0; n < keyArray.length; n++) {
					long currentKey= keyArray[n];
					// Term value= setPositiveMap.get(currentKey).retrieveSetElementValue(cp);
					Term value= setPositiveMap.get(currentKey);
					// value= value.dereferenceValue(cp);
					value= value.dereferenceValue(cp);
					if (ignoreFreeVariables && value.thisIsFreeVariable()) {
						// return true;
						continue;
					} else {
						boolean elementIsFound= false;
						for (int k=0; k < functors.length; k++) {
							if (functors[k] == currentKey) {
								if (!value.isCoveredByDomain(domainItems[k],cp,ignoreFreeVariables)) {
									return false;
								};
								elementIsFound= true;
								break;
							}
						};
						if (!elementIsFound) {
							return false;
						}
					}
				};
				return true;
			} else {
				return false;
			}
		}
	}
	public boolean coversOptimizedSet(long[] keys, Term[] elements, Term tail, PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		initiateDomainItemsIfNecessary();
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
					for (int k=0; k < functors.length; k++) {
						if (functors[k] == currentKey) {
							if (!value.isCoveredByDomain(domainItems[k],cp,ignoreFreeVariables)) {
								return false;
							};
							elementIsFound= true;
							break;
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
		initiateDomainItemsIfNecessary();
		Term[] arguments= checkCollectAndOptimizeSetElements(t,cp,baseDomain,true);
		return new PrologOptimizedSet(arguments,functors);
	}
	public Term checkTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		initiateDomainItemsIfNecessary();
		// System.out.printf("DimainOptimizedSet::checkTerm:t=%s\n",t);
		Term[] arguments= checkCollectAndOptimizeSetElements(t,cp,baseDomain,false);
		Term set= new PrologEmptySet();
		for (int n=arguments.length-1; n >= 0; n--) {
			Term argument= arguments[n];
			if (argument != null) {
				set= new PrologSet(functors[n],arguments[n],set);
			} else {
				set= new ProhibitedSetElement(functors[n],set);
			}
		};
		return set;
	}
	public Term checkOptimizedSet(long[] keys, Term[] elements, Term tail, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		initiateDomainItemsIfNecessary();
		Term[] arguments= checkCollectAndOptimizeSetElements(initialValue,cp,baseDomain,true);
		return new PrologOptimizedSet(arguments,functors);
	}
	protected Term[] checkCollectAndOptimizeSetElements(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean optimizeTerms) throws DomainAlternativeDoesNotCoverTerm {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
		Term setEnd= t.exploreSetPositiveElements(setPositiveMap,cp);
		setEnd= setEnd.dereferenceValue(cp);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			Term[] arguments= new Term[functors.length];
			for (int n=0; n < functors.length; n++) {
				long currentKey= functors[n];
				if (setPositiveMap.containsKey(currentKey)) {
					Term currentElement= setPositiveMap.get(currentKey);
					if (optimizeTerms) {
						arguments[n]= domainItems[n].checkAndOptimizeTerm(currentElement,cp);
					} else {
						arguments[n]= domainItems[n].checkTerm(currentElement,cp);
					};
					setPositiveMap.remove(currentKey);
				} else {
					arguments[n]= null; // new PrologNoValue();
				}
			};
			if (setPositiveMap.isEmpty()) {
				return arguments;
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
		initiateDomainItemsIfNecessary();
		int elementNumber= -1;
		for (int n=0; n < functors.length; n++) {
			if (key==functors[n]) {
				elementNumber= n;
				break;
			}
		};
		if (elementNumber >= 0) {
			return domainItems[elementNumber];
		} else {
			throw new IsNotPairDomainAlternative();
		}
	}
}
