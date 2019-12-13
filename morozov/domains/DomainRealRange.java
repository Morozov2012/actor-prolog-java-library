// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.run.*;
import morozov.system.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashSet;

public class DomainRealRange extends DomainAlternative {
	//
	protected double leftBound;
	protected double rightBound;
	//
	private static final long serialVersionUID= 0x6C01A2926AAE3FC6L; // 7782680380811657158L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.domains","DomainRealRange");
	// }
	//
	public DomainRealRange(double left, double right) {
		leftBound= left;
		rightBound= right;
	}
	//
	@Override
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
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
	@Override
	public boolean isEqualTo(DomainAlternative a, HashSet<PrologDomainPair> stack) {
		return a.isEqualToRealRange(leftBound,rightBound);
	}
	@Override
	public boolean isEqualToRealRange(double value1, double value2) {
		return Arithmetic.realsAreEqual(leftBound,value1) && Arithmetic.realsAreEqual(rightBound,value2);
	}
	@Override
	public boolean isCoveredByReal() {
		return true;
	}
	@Override
	public boolean coversAlternative(DomainAlternative a, PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) {
		return a.isCoveredByRealRange(leftBound,rightBound);
	}
	@Override
	public boolean isCoveredByRealRange(double value1, double value2) {
		return (value1 <= leftBound) && (value2 >= rightBound);
	}
	//
	@Override
	public String toString(CharsetEncoder encoder) {
		return PrologDomainName.tagDomainAlternative_RealRange + "(" + FormatOutput.realToString(leftBound) + "," + FormatOutput.realToString(rightBound) + ")";
	}
}
