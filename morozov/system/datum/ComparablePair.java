// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.datum;

import morozov.terms.*;

public class ComparablePair implements Comparable<ComparablePair> {
	//
	protected Term key;
	protected Term value;
	protected static TermComparator comparator= new TermComparator(true);
	//
	public ComparablePair(Term k, Term v) {
		key= k;
		value= v;
	}
	//
	@Override
	public int compareTo(ComparablePair o) {
		return comparator.compare(key,o.key);
	}
}
