// (c) 2009 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.run.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;

public final class PrologNoValue extends Term {
	public static final PrologNoValue instance= new PrologNoValue();
	public static final String namePrologNoValue= "~";
	//
	private PrologNoValue() {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean equals(Object o2) {
		if (o2 instanceof Term) {
			return ((Term)o2).isEqualToNoValue();
		} else {
			return false;
		}
	}
	public int compare(Object o2) {
		if (o2 instanceof Term) {
			return -((Term)o2).compareWithNoValue();
		} else {
			return 1;
		}
	}
	public boolean isEqualToNoValue() {
		return true;
	}
	public int compareWithNoValue() {
		return 0;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void isNoValue(ChoisePoint cp) throws Backtracking {
	}
	public boolean thisIsNoValue() {
		return true;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.isNoValue(cp);
	}
	public Term retrieveSetElementValue(ChoisePoint cp) throws Backtracking, TermIsNotSetElement {
		throw Backtracking.instance;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, boolean encodeWorlds, CharsetEncoder encoder) {
		return namePrologNoValue;
	}
}
