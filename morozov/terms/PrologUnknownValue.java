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
	private static final long serialVersionUID= 0x8E4A8C0EDAC1629AL; // -8193582576619658598L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.terms","PrologUnknownValue");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	private PrologUnknownValue() {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean equals(Object o2) {
		if (o2 instanceof Term) {
			return ((Term)o2).isEqualToUnknownValue();
		} else {
			return false;
		}
	}
	@Override
	public int compare(Object o2) {
		if (o2 instanceof Term) {
			return -((Term)o2).compareWithUnknownValue();
		} else {
			return 1;
		}
	}
	@Override
	public boolean isEqualToUnknownValue() {
		return true;
	}
	@Override
	public int compareWithUnknownValue() {
		return 0;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void isUnknownValue(ChoisePoint cp) throws Backtracking {
	}
	@Override
	public boolean thisIsUnknownValue() {
		return true;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public AbstractInternalWorld getInternalWorld(ChoisePoint cp) throws Backtracking, TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		throw TermIsDummyWorld.instance;
	}
	//
	@Override
	public GlobalWorldIdentifier getGlobalWorldIdentifier(ChoisePoint cp) throws TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		throw TermIsDummyWorld.instance;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.isUnknownValue(cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, boolean encodeWorlds, CharsetEncoder encoder) {
		return "#";
	}
}
