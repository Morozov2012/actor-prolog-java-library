// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.run.*;
import morozov.terms.errors.*;

import java.nio.charset.CharsetEncoder;

public class ArgumentNumber extends Term {
	//
	private long value;
	//
	private static final long serialVersionUID= 0x702E26F20F3506F9L; // 8083441202255693561L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.terms","ArgumentNumber");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public ArgumentNumber(long v) {
		value= v;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		throw new WrongArgumentIsArgumentNumber(t);
	}
	//
	///////////////////////////////////////////////////////////////
	//
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
			throw Backtracking.instance;
	}
	public void isArityMoreOrEqualTo(long v, ChoisePoint cp) throws Backtracking {
		if (value < v)
			throw Backtracking.instance;
	}
	public void verifyListOfRestValues(long deltaN, Term list, ChoisePoint cp) {
		long counter= value + deltaN;
		Term tail= list;
		while (counter > 0) {
			tail= tail.getOutputTail(cp);
			counter= counter - 1;
		};
		tail.isOutputEmptyList(cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, boolean encodeWorlds, CharsetEncoder encoder) {
		return String.format("%d",value);
	}
}
