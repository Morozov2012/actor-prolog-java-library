// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.classes.*;
import morozov.run.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;

public final class PrologUnknownValue extends Term {
	public static final PrologUnknownValue instance= new PrologUnknownValue();
	//
	private PrologUnknownValue() {
	}
	//
	public void isUnknownValue(ChoisePoint cp) throws Backtracking {
	}
	public boolean thisIsUnknownValue() {
		return true;
	}
	//
	public long getInternalWorldClass(AbstractWorld currentClass, ChoisePoint cp) throws Backtracking, TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		throw TermIsDummyWorld.instance;
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
