// (c) 2009 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.run.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;

public final class PrologNoValue extends Term {
	//
	public static final PrologNoValue instance= new PrologNoValue();
	public static final String namePrologNoValue= "~";
	//
	private static final long serialVersionUID= 0x24584BCE49AC1715L; // 2618926532687304469L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.terms","PrologNoValue");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	private PrologNoValue() {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean equals(Object o2) {
		if (o2 instanceof Term) {
			return ((Term)o2).isEqualToNoValue();
		} else {
			return false;
		}
	}
	@Override
	public int compare(Object o2) {
		if (o2 instanceof Term) {
			return -((Term)o2).compareWithNoValue();
		} else {
			return 1;
		}
	}
	@Override
	public boolean isEqualToNoValue() {
		return true;
	}
	@Override
	public int compareWithNoValue() {
		return 0;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void isNoValue(ChoisePoint cp) throws Backtracking {
	}
	@Override
	public boolean thisIsNoValue() {
		return true;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.isNoValue(cp);
	}
	@Override
	public Term retrieveSetElementValue(ChoisePoint cp) throws Backtracking, TermIsNotSetElement {
		throw Backtracking.instance;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, boolean encodeWorlds, CharsetEncoder encoder) {
		return namePrologNoValue;
	}
}
