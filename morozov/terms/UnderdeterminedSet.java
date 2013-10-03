// (c) 2009 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.run.*;

import java.util.HashMap;
import java.util.HashSet;

public abstract class UnderdeterminedSet extends Term {
	// protected long name;
	// protected Term tail;
	public static final long keyNameCode= 0;
	//
	public boolean thisIsUnderdeterminedSet() {
		return true;
	}
	abstract protected void unify_with_set(
			Term aTail,
			ChoisePoint cp,
			HashMap<Long,Term> leftSetPositiveMap,
			HashSet<Long> leftSetNegativeMap,
			HashMap<Long,Term> rightSetPositiveMap,
			HashSet<Long> rightSetNegativeMap
			) throws Backtracking;
}
