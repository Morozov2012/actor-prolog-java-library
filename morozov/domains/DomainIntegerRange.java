// (c) 2010 IRE RAS Alexei A. Morozov
package morozov.domains;

import morozov.terms.*;
import java.math.BigInteger;public class DomainIntegerRange extends DomainAlternative {
	protected BigInteger leftBound;
	protected BigInteger rightBound;
	public DomainIntegerRange(long left, long right) {
		leftBound= BigInteger.valueOf(left);
		rightBound= BigInteger.valueOf(right);
	}
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		// t= t.dereferenceValue(cp);
		t= t.dereferenceValue(cp);
		if (ignoreFreeVariables && t.thisIsFreeVariable()) {
			return true;
		} else {
			try {
				BigInteger value= t.getIntegerValue(cp);
				// if (leftBound <= value && value <= rightBound) {
				if (leftBound.compareTo(value) <= 0 && value.compareTo(rightBound) <= 0) {
					return true;
				} else {
					return false;
				}
			} catch (TermIsNotAnInteger e) {
				return false;
			}
		}
	}
}
