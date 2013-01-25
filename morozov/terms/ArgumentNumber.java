// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import java.nio.charset.CharsetEncoder;

public class ArgumentNumber extends Term {
	private long value;
	public ArgumentNumber(long v) {
		value= v;
	}
	public int hashCode() {
		return (int)value;
	}
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		throw new WrongTermIsArgumentNumber(t);
	}
	// Internal operations
	public boolean thisIsArgumentNumber() {
		return true;
	}
	public long getNumber() {
		return value;
	}
	public ArgumentNumber add(long deltaN) {
		return new ArgumentNumber(value+deltaN);
	}
	public ArgumentNumber subtract(long deltaN) {
		return new ArgumentNumber(value-deltaN);
	}
	public void isArityEqualTo(long v, ChoisePoint cp) throws Backtracking {
		if (value != v)
			throw new Backtracking();
	}
	public void isArityMoreOrEqualTo(long v, ChoisePoint cp) throws Backtracking {
		if (value < v)
			throw new Backtracking();
	}
	// public void verifyListOfRestValues(long deltaN, Term list, ChoisePoint cp) {
	//	System.out.printf("long deltaN=%s, counter=%s; list=%s\n",deltaN,value,list);
	// }
	public void verifyListOfRestValues(long deltaN, Term list, ChoisePoint cp) {
		long counter= value + deltaN;
		// System.out.printf("counter=%d, (deltaN=%d + value=%d) list=%s\n",counter,deltaN,value,list);
		Term tail= list;
		while (counter > 0) {
			// System.out.printf("counter=%s; tail=%s\n",counter,tail);
			tail= tail.getOutputTail(cp);
			counter= counter - 1;
		};
		tail.isOutputEmptyList(cp);
	}
	// Converting Term to String
	// public String toString() {
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, CharsetEncoder encoder) {
		return String.format("%d",value);
	}
}
