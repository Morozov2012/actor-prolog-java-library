// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.run.*;
import morozov.system.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashSet;
import java.math.BigInteger;

public class DomainIntegerRange extends DomainAlternative {
	protected BigInteger leftBound;
	protected BigInteger rightBound;
	//
	public DomainIntegerRange(BigInteger left, BigInteger right) {
		leftBound= left;
		rightBound= right;
	}
	public DomainIntegerRange(long left, long right) {
		leftBound= BigInteger.valueOf(left);
		rightBound= BigInteger.valueOf(right);
	}
	//
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
	//
	public boolean isEqualTo(DomainAlternative a, HashSet<PrologDomainPair> stack) {
		return a.isEqualToIntegerRange(leftBound,rightBound);
	}
	public boolean isEqualToIntegerRange(BigInteger value1, BigInteger value2) {
		return (leftBound.compareTo(value1)==0) && (rightBound.compareTo(value2)==0);
	}
	public boolean isCoveredByInteger() {
		return true;
	}
	public boolean coversAlternative(DomainAlternative a, PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) {
		return a.isCoveredByIntegerRange(leftBound,rightBound);
	}
	public boolean isCoveredByIntegerRange(BigInteger value1, BigInteger value2) {
		return (value1.compareTo(leftBound)<=0) && (value2.compareTo(rightBound)>=0);
	}
	//
	public String toString(CharsetEncoder encoder) {
		return PrologDomainName.tagDomainAlternative_IntegerRange + "(" + FormatOutput.integerToString(leftBound) + "," + FormatOutput.integerToString(rightBound) + ")";
	}
}
