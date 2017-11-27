// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.run.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class UnderdeterminedSetWithTail extends UnderdeterminedSet {
	//
	protected Term tail;
	//
	///////////////////////////////////////////////////////////////
	//
	public int hashCode() {
		return tail.hashCode();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void unify_with_set(
			Term aTail,
			ChoisePoint cp,
			HashMap<Long,Term> leftSetPositiveMap,
			HashSet<Long> leftSetNegativeMap,
			HashMap<Long,Term> rightSetPositiveMap,
			HashSet<Long> rightSetNegativeMap
			) throws Backtracking {
		Term leftSetEnd= tail.exploreSet(leftSetPositiveMap,leftSetNegativeMap,cp);
		Term rightSetEnd= aTail.exploreSet(rightSetPositiveMap,rightSetNegativeMap,cp);
		Set<Long> leftSetPositiveKeys= leftSetPositiveMap.keySet();
		Iterator<Long> leftSetPositiveIterator= leftSetPositiveKeys.iterator();
		while (leftSetPositiveIterator.hasNext()) {
			long leftSetPositiveKey= leftSetPositiveIterator.next();
			if (rightSetPositiveMap.containsKey(leftSetPositiveKey)) {
				Term leftSetPositiveValue= leftSetPositiveMap.get(leftSetPositiveKey);
				Term rightSetPositiveValue= rightSetPositiveMap.get(leftSetPositiveKey);
				leftSetPositiveValue.unifyWith(rightSetPositiveValue,cp);
				rightSetPositiveMap.remove(leftSetPositiveKey);
			} else {
				if (rightSetNegativeMap.contains(leftSetPositiveKey)) {
					throw Backtracking.instance;
				} else {
					Term leftSetPositiveValue= leftSetPositiveMap.get(leftSetPositiveKey);
					aTail.appendNamedElement(leftSetPositiveKey,leftSetPositiveValue,cp);
				}
			};
		leftSetPositiveIterator.remove();
		};
		Iterator<Long> leftSetNegativeIterator= leftSetNegativeMap.iterator();
		while (leftSetNegativeIterator.hasNext()) {
			long leftSetNegativeKey= leftSetNegativeIterator.next();
			if (rightSetPositiveMap.containsKey(leftSetNegativeKey)) {
				throw Backtracking.instance;
			} else {
				if (rightSetNegativeMap.contains(leftSetNegativeKey)) {
					rightSetNegativeMap.remove(leftSetNegativeKey);
				} else {
					aTail.appendNamedElementProhibition(leftSetNegativeKey,cp);
				}
			};
			leftSetNegativeIterator.remove();
		};
		Set<Long> rightSetPositiveKeys= rightSetPositiveMap.keySet();
		Iterator<Long> rightSetPositiveIterator= rightSetPositiveKeys.iterator();
		while (rightSetPositiveIterator.hasNext()) {
			long rightSetPositiveKey= rightSetPositiveIterator.next();
			Term rightSetPositiveValue= rightSetPositiveMap.get(rightSetPositiveKey);
			tail.appendNamedElement(rightSetPositiveKey,rightSetPositiveValue,cp);
			// rightSetPositiveIterator.remove();
		};
		Iterator<Long> rightSetNegativeIterator= rightSetNegativeMap.iterator();
		while (rightSetNegativeIterator.hasNext()) {
			long rightSetNegativeKey= rightSetNegativeIterator.next();
			tail.appendNamedElementProhibition(rightSetNegativeKey,cp);
			// rightSetNegativeIterator.remove();
		};
		leftSetEnd= leftSetEnd.exploreSet(cp);
		rightSetEnd= rightSetEnd.exploreSet(cp);
		if (leftSetEnd.thisIsFreeVariable() && rightSetEnd.thisIsFreeVariable()) {
			PrologVariable newTail= new PrologVariable();
			leftSetEnd.setBacktrackableValue(newTail,cp);
			rightSetEnd.setBacktrackableValue(newTail,cp);
		} else {
			leftSetEnd.isEmptySet(cp);
			rightSetEnd.isEmptySet(cp);
		}
	}
}
