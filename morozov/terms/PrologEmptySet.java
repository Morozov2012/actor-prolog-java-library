// (c) 2009 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.domains.*;
import morozov.domains.signals.*;
import morozov.run.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;

import java.util.HashMap;
import java.util.HashSet;

public final class PrologEmptySet extends UnderdeterminedSet {
	public static final PrologEmptySet instance= new PrologEmptySet();
	//
	private PrologEmptySet() {
	}
	//
	public void isEmptySet(ChoisePoint cp) throws Backtracking {
	}
	public boolean thisIsEmptySet() {
		return true;
	}
	public void inheritSetElements(PrologOptimizedSet set, ChoisePoint cp) throws Backtracking {
		set.setNoValueElements();
	}
	// public Term checkSetAndGetNamedElement(long aName, ChoisePoint cp) throws Backtracking, TermIsNotASet {
	//	throw Backtracking.instance;
	// }
	public void checkIfTermIsASet(ChoisePoint cp) throws TermIsNotASet {
	}
	public Term excludeNamedElements(long[] aNames, ChoisePoint cp) throws Backtracking {
		return this;
	}
	public void hasNoMoreElements(long[] aNames, ChoisePoint cp) throws Backtracking {
	}
	public void prohibitNamedElement(long aName, ChoisePoint cp) throws Backtracking {
	}
	public void verifySet(long[] aNames, ChoisePoint cp) throws Backtracking {
	}
	public long getNextPairName(ChoisePoint cp) throws EndOfSet, TermIsNotASet {
		throw EndOfSet.instance;
	}
	public Term getNextPairValue(ChoisePoint cp) throws EndOfSet, TermIsNotASet, SetElementIsProhibited {
		throw EndOfSet.instance;
	}
	public Term getNextSetTail(ChoisePoint cp) throws EndOfSet, TermIsNotASet {
		throw EndOfSet.instance;
	}
	public Term exploreSetPositiveElements(HashMap<Long,Term> positiveMap, ChoisePoint cp) {
		return this;
	}
	public Term exploreSet(HashMap<Long,Term> positiveMap, HashSet<Long> negativeMap, ChoisePoint cp) {
		return this;
	}
	public Term exploreSet(ChoisePoint cp) {
		return this;
	}
	public void appendNamedElement(long aName, Term aValue, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public void appendNamedElementProhibition(long aName, ChoisePoint cp) throws Backtracking {
	}
	public void unifyWithProhibitedElement(long aName, Term aTail, Term aSet, ChoisePoint cp) throws Backtracking {
		aTail.isEmptySet(cp);
	}
	public void unifyWithOptimizedSet(PrologOptimizedSet set, ChoisePoint cp) throws Backtracking {
		set.isEmptySet(cp);
	}
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.isEmptySet(cp);
	}
	//
	protected void unify_with_set(
			Term aTail,
			ChoisePoint cp,
			HashMap<Long,Term> leftSetPositiveMap,
			HashSet<Long> leftSetNegativeMap,
			HashMap<Long,Term> rightSetPositiveMap,
			HashSet<Long> rightSetNegativeMap
			) throws Backtracking {
		// Term leftSetEnd= tail.exploreSet(leftSetPositiveMap,leftSetNegativeMap,cp);
		Term rightSetEnd= aTail.exploreSet(rightSetPositiveMap,rightSetNegativeMap,cp);
		// Set<Long> leftSetPositiveKeys= leftSetPositiveMap.keySet();
		// Iterator<Long> leftSetPositiveIterator= leftSetPositiveKeys.iterator();
		// while (leftSetPositiveIterator.hasNext()) {
		//	long leftSetPositiveKey= leftSetPositiveIterator.next();
		//	if (rightSetPositiveMap.containsKey(leftSetPositiveKey)) {
		//		Term leftSetPositiveValue= leftSetPositiveMap.get(leftSetPositiveKey);
		//		Term rightSetPositiveValue= rightSetPositiveMap.get(leftSetPositiveKey);
		//		leftSetPositiveValue.unifyWith(rightSetPositiveValue,cp);
		//		rightSetPositiveMap.remove(leftSetPositiveKey);
		//	} else {
		//		if (rightSetNegativeMap.contains(leftSetPositiveKey)) {
		//			throw Backtracking.instance;
		//		} else {
		//			Term leftSetPositiveValue= leftSetPositiveMap.get(leftSetPositiveKey);
		//			aTail.appendNamedElement(leftSetPositiveKey,leftSetPositiveValue,cp);
		//		}
		//	};
		// leftSetPositiveIterator.remove();
		// };
		// Iterator<Long> leftSetNegativeIterator= leftSetNegativeMap.iterator();
		// while (leftSetNegativeIterator.hasNext()) {
		//	long leftSetNegativeKey= leftSetNegativeIterator.next();
		//	if (rightSetPositiveMap.containsKey(leftSetNegativeKey)) {
		//		throw Backtracking.instance;
		//	} else {
		//		if (rightSetNegativeMap.contains(leftSetNegativeKey)) {
		//			rightSetNegativeMap.remove(leftSetNegativeKey);
		//		} else {
		//			aTail.appendNamedElementProhibition(leftSetNegativeKey,cp);
		//		}
		//	};
		//	leftSetNegativeIterator.remove();
		// };
		if (!rightSetPositiveMap.isEmpty()) {
			throw Backtracking.instance;
		};
		// Set<Long> rightSetPositiveKeys= rightSetPositiveMap.keySet();
		// Iterator<Long> rightSetPositiveIterator= rightSetPositiveKeys.iterator();
		// while (rightSetPositiveIterator.hasNext()) {
		//	long rightSetPositiveKey= rightSetPositiveIterator.next();
		//	Term rightSetPositiveValue= rightSetPositiveMap.get(rightSetPositiveKey);
		//	tail.appendNamedElement(rightSetPositiveKey,rightSetPositiveValue,cp);
		//	// rightSetPositiveIterator.remove();
		// };
		// Iterator<Long> rightSetNegativeIterator= rightSetNegativeMap.iterator();
		// while (rightSetNegativeIterator.hasNext()) {
		//	long rightSetNegativeKey= rightSetNegativeIterator.next();
		//	tail.appendNamedElementProhibition(rightSetNegativeKey,cp);
		//	// rightSetNegativeIterator.remove();
		// };
		// leftSetEnd= leftSetEnd.exploreSet(cp);
		rightSetEnd= rightSetEnd.exploreSet(cp);
		// if (leftSetEnd.thisIsFreeVariable() && rightSetEnd.thisIsFreeVariable()) {
		//	PrologVariable newTail= new PrologVariable();
		//	leftSetEnd.setValue(newTail);
		//	rightSetEnd.setValue(newTail);
		// } else {
		//	leftSetEnd.isEmptySet(cp);
		rightSetEnd.isEmptySet(cp);
		// }
	}
	// Domain check
	public boolean isCoveredBySetDomain(long functor, PrologDomain headDomain, PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		return true;
	}
	public boolean isCoveredByEmptySetDomain(PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		return true;
	}
	public Term checkSetTerm(long functor, PrologDomain headDomain, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		return this;
	}
	// Converting Term to String
	// public String toString() {
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, CharsetEncoder encoder) {
		return "{}";
	}
}
