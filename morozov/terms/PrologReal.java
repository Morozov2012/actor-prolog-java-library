// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.system.*;

import java.nio.charset.CharsetEncoder;

public class PrologReal extends Term {
	private double value;
	public PrologReal(double v) {
		value= v;
	}
	public int hashCode() {
		return (int)StrictMath.round(value);
	}
	public void isReal(double v, ChoisePoint cp) throws Backtracking {
		if (Arithmetic.realsAreEqual(v,value)) {
			return;
		} else {
			throw new Backtracking();
		}
	}
	public double getRealValue(ChoisePoint cp) throws TermIsNotAReal {
		return value;
	}
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.isReal(value,cp);
	}
	// Converting Term to String
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, CharsetEncoder encoder) {
		return FormatOutput.realToString(value);
	}
	// public String toString() {
	//	return FormatOutput.realToString(value);
	// }
}
