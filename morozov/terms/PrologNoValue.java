// (c) 2009 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.run.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;

public final class PrologNoValue extends Term {
	public static final PrologNoValue instance= new PrologNoValue();
	//
	private PrologNoValue() {
	}
	//
	public static final String namePrologNoValue= "~";
	public void isNoValue(ChoisePoint cp) throws Backtracking {
	}
	public boolean thisIsNoValue() {
		return true;
	}
	public Term retrieveSetElementValue(ChoisePoint cp) throws Backtracking, TermIsNotSetElement {
		throw Backtracking.instance;
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
