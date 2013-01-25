// (c) 2009 IRE RAS Alexei A. Morozov

package morozov.terms;

import java.nio.charset.CharsetEncoder;

public class PrologNoValue extends Term {
	public static final String namePrologNoValue= "~";
	public void isNoValue(ChoisePoint cp) throws Backtracking {
	}
	public boolean thisIsNoValue() {
		return true;
	}
	public Term retrieveSetElementValue(ChoisePoint cp) throws Backtracking, TermIsNotSetElement {
		throw new Backtracking();
	}
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.isNoValue(cp);
	}
	// Converting Term to String
	// public String toString() {
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, CharsetEncoder encoder) {
		return namePrologNoValue;
	}
}
