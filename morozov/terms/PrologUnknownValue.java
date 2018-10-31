// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.run.*;
import morozov.terms.signals.*;
import morozov.worlds.*;

import java.nio.charset.CharsetEncoder;

public final class PrologUnknownValue extends Term {
	//
	public static final PrologUnknownValue instance= new PrologUnknownValue();
	//
	private PrologUnknownValue() {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean equals(Object o2) {
		if (o2 instanceof Term) {
			return ((Term)o2).isEqualToUnknownValue();
		} else {
			return false;
		}
	}
	public int compare(Object o2) {
		if (o2 instanceof Term) {
			return -((Term)o2).compareWithUnknownValue();
		} else {
			return 1;
		}
	}
	public boolean isEqualToUnknownValue() {
		return true;
	}
	public int compareWithUnknownValue() {
		return 0;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void isUnknownValue(ChoisePoint cp) throws Backtracking {
	}
	public boolean thisIsUnknownValue() {
		return true;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public AbstractInternalWorld getInternalWorld(ChoisePoint cp) throws Backtracking, TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		throw TermIsDummyWorld.instance;
	}
	//
	public GlobalWorldIdentifier getGlobalWorldIdentifier(ChoisePoint cp) throws TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		throw TermIsDummyWorld.instance;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.isUnknownValue(cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, boolean encodeWorlds, CharsetEncoder encoder) {
		return "#";
	}
}
