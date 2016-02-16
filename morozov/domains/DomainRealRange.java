// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.run.*;
import morozov.system.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashSet;

public class DomainRealRange extends DomainAlternative {
	protected double leftBound;
	protected double rightBound;
	//
	public DomainRealRange(double left, double right) {
		leftBound= left;
		rightBound= right;
	}
	//
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		// t= t.dereferenceValue(cp);
		t= t.dereferenceValue(cp);
		if (ignoreFreeVariables && t.thisIsFreeVariable()) {
			return true;
		} else {
			try {
				double value= t.getRealValue(cp);
				if (leftBound <= value && value <= rightBound) {
					return true;
				} else {
					return false;
				}
			} catch (TermIsNotAReal e) {
				return false;
			}
		}
	}
	//
	public boolean isEqualTo(DomainAlternative a, HashSet<PrologDomainPair> stack) {
		return a.isEqualToRealRange(leftBound,rightBound);
	}
	public boolean isEqualToRealRange(double value1, double value2) {
		return Arithmetic.realsAreEqual(leftBound,value1) && Arithmetic.realsAreEqual(rightBound,value2);
	}
	public boolean isCoveredByReal() {
		return true;
	}
	public boolean coversAlternative(DomainAlternative a, PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) {
		return a.isCoveredByRealRange(leftBound,rightBound);
	}
	public boolean isCoveredByRealRange(double value1, double value2) {
		return (value1 <= leftBound) && (value2 >= rightBound);
	}
	//
	public String toString(CharsetEncoder encoder) {
		return PrologDomainName.tagDomainAlternative_RealRange + "(" + FormatOutput.realToString(leftBound) + "," + FormatOutput.realToString(rightBound) + ")";
	}
}
