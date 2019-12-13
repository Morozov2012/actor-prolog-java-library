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
	//
	public static final PrologEmptySet instance= new PrologEmptySet();
	//
	private static final long serialVersionUID= 0x921289D7BC45F8BCL; // -7921117234921604932L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.terms","PrologEmptySet");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	private PrologEmptySet() {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean equals(Object o2) {
		if (o2 instanceof Term) {
			return ((Term)o2).isEqualToEmptySet();
		} else {
			return false;
		}
	}
	@Override
	public int compare(Object o2) {
		if (o2 instanceof Term) {
			return -((Term)o2).compareWithEmptySet();
		} else {
			return 1;
		}
	}
	@Override
	public boolean isEqualToEmptySet() {
		return true;
	}
	@Override
	public int compareWithEmptySet() {
		return 0;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void isEmptySet(ChoisePoint cp) throws Backtracking {
	}
	@Override
	public boolean thisIsEmptySet() {
		return true;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void inheritSetElements(PrologOptimizedSet set, ChoisePoint cp) throws Backtracking {
		set.setNoValueElements();
	}
	@Override
	public void checkIfTermIsASet(ChoisePoint cp) throws TermIsNotASet {
	}
	@Override
	public Term excludeNamedElements(long[] aNames, ChoisePoint cp) throws Backtracking {
		return this;
	}
	@Override
	public void hasNoMoreElements(long[] aNames, ChoisePoint cp) throws Backtracking {
	}
	@Override
	public void prohibitNamedElement(long aName, ChoisePoint cp) throws Backtracking {
	}
	@Override
	public void verifySet(long[] aNames, ChoisePoint cp) throws Backtracking {
	}
	@Override
	public long getNextPairName(ChoisePoint cp) throws EndOfSet, TermIsNotASet {
		throw EndOfSet.instance;
	}
	@Override
	public Term getNextPairValue(ChoisePoint cp) throws EndOfSet, TermIsNotASet, SetElementIsProhibited {
		throw EndOfSet.instance;
	}
	@Override
	public Term getNextSetTail(ChoisePoint cp) throws EndOfSet, TermIsNotASet {
		throw EndOfSet.instance;
	}
	@Override
	public Term exploreSetPositiveElements(HashMap<Long,Term> positiveMap, ChoisePoint cp) {
		return this;
	}
	@Override
	public Term exploreSet(HashMap<Long,Term> positiveMap, HashSet<Long> negativeMap, ChoisePoint cp) {
		return this;
	}
	@Override
	public Term exploreSet(ChoisePoint cp) {
		return this;
	}
	@Override
	public void appendNamedElement(long aName, Term aValue, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	@Override
	public void appendNamedElementProhibition(long aName, ChoisePoint cp) throws Backtracking {
	}
	@Override
	public void unifyWithProhibitedElement(long aName, Term aTail, Term aSet, ChoisePoint cp) throws Backtracking {
		aTail.isEmptySet(cp);
	}
	@Override
	public void unifyWithOptimizedSet(PrologOptimizedSet set, ChoisePoint cp) throws Backtracking {
		set.isEmptySet(cp);
	}
	@Override
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.isEmptySet(cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected void unify_with_set(
			Term aTail,
			ChoisePoint cp,
			HashMap<Long,Term> leftSetPositiveMap,
			HashSet<Long> leftSetNegativeMap,
			HashMap<Long,Term> rightSetPositiveMap,
			HashSet<Long> rightSetNegativeMap
			) throws Backtracking {
		Term rightSetEnd= aTail.exploreSet(rightSetPositiveMap,rightSetNegativeMap,cp);
		if (!rightSetPositiveMap.isEmpty()) {
			throw Backtracking.instance;
		};
		rightSetEnd= rightSetEnd.exploreSet(cp);
		rightSetEnd.isEmptySet(cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean isCoveredBySetDomain(long functor, PrologDomain headDomain, PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		return true;
	}
	@Override
	public boolean isCoveredByEmptySetDomain(PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		return true;
	}
	@Override
	public Term checkSetTerm(long functor, PrologDomain headDomain, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		return this;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, boolean encodeWorlds, CharsetEncoder encoder) {
		return "{}";
	}
}
