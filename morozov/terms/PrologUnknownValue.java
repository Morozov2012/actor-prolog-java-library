// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.classes.*;

import java.nio.charset.CharsetEncoder;

public class PrologUnknownValue extends Term {
	public void isUnknownValue(ChoisePoint cp) throws Backtracking {
	}
	public boolean thisIsUnknownValue() {
		return true;
	}
	//
	public long getInternalWorldClass(AbstractWorld currentClass, ChoisePoint cp) throws Backtracking, TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		throw new TermIsDummyWorld();
	}
	//
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.isUnknownValue(cp);
	}
	// Converting Term to String
	// public String toString() {
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, CharsetEncoder encoder) {
		return "#";
	}
}
