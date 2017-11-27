// (c) 2009 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.run.*;
import morozov.terms.errors.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Iterator;

public abstract class UnderdeterminedSet extends Term {
	//
	public static final long keyNameCode= 0;
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean equals(Object o2) {
		if (o2 instanceof Term) {
			return ((Term)o2).isEqualToSet(this);
		} else {
			return false;
		}
	}
	public int compare(Object o2) {
		if (o2 instanceof Term) {
			return -((Term)o2).compareWithSet(this);
		} else {
			return 1;
		}
	}
	public boolean isEqualToSet(UnderdeterminedSet o2) {
		return compareWithSet(o2)==0;
	}
	public int compareWithSet(UnderdeterminedSet o2) {
		HashMap<Long,Term> leftSetPositiveMap= new HashMap<Long,Term>();
		HashSet<Long> leftSetNegativeMap= new HashSet<Long>();
		ChoisePoint cp= null;
		Term leftSetEnd= exploreSet(leftSetPositiveMap,leftSetNegativeMap,cp);
		leftSetEnd= leftSetEnd.dereferenceValue(cp);
		if (!leftSetEnd.thisIsEmptySet() && !leftSetEnd.thisIsUnknownValue()) {
			throw new WrongArgumentIsNotBoundVariable(this);
		};
		HashMap<Long,Term> rightSetPositiveMap= new HashMap<Long,Term>();
		HashSet<Long> rightSetNegativeMap= new HashSet<Long>();
		Term rightSetEnd= o2.exploreSet(rightSetPositiveMap,rightSetNegativeMap,cp);
		rightSetEnd= rightSetEnd.dereferenceValue(cp);
		if (!rightSetEnd.thisIsEmptySet() && !rightSetEnd.thisIsUnknownValue()) {
			throw new WrongArgumentIsNotBoundVariable(o2);
		};
		TreeSet<Long> nameList= new TreeSet<Long>();
		TermComparator.insertNamesIntoTree(nameList,leftSetPositiveMap);
		TermComparator.insertNamesIntoTree(nameList,leftSetNegativeMap);
		TermComparator.insertNamesIntoTree(nameList,rightSetPositiveMap);
		TermComparator.insertNamesIntoTree(nameList,rightSetNegativeMap);
		Iterator<Long> iterator= nameList.iterator();
		while(iterator.hasNext()) {
			Long name= iterator.next();
			Term leftValue= leftSetPositiveMap.get(name);
			Term rightValue= rightSetPositiveMap.get(name);
			if (leftValue!=null && rightValue!=null) {
				int result= leftValue.compare(rightValue);
				if (result==0) {
					continue;
				} else {
					return result;
				}
			} else if (leftValue==null && rightValue==null) {
				continue;
			} else if (leftValue==null && rightValue!=null) {
				return -1;
			} else if (leftValue!=null && rightValue==null) {
				return 1;
			}
		};
		return 0;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean thisIsUnderdeterminedSet() {
		return true;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract protected void unify_with_set(
			Term aTail,
			ChoisePoint cp,
			HashMap<Long,Term> leftSetPositiveMap,
			HashSet<Long> leftSetNegativeMap,
			HashMap<Long,Term> rightSetPositiveMap,
			HashSet<Long> rightSetNegativeMap
			) throws Backtracking;
}
