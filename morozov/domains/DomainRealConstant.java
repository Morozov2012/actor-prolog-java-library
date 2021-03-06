// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.run.*;
import morozov.system.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashSet;

public class DomainRealConstant extends DomainAlternative {
	//
	protected double constantValue;
	//
	private static final long serialVersionUID= 0x9D125B93A85C64A2L; // -7128534570431126366L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.domains","DomainRealConstant");
	// }
	//
	public DomainRealConstant(double value) {
		constantValue= value;
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
				if (Arithmetic.realsAreEqual(constantValue,value)) {
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
		return a.isEqualToRealConstant(constantValue);
	}
	@Override
	public boolean isEqualToRealConstant(double value) {
		return Arithmetic.realsAreEqual(constantValue,value);
	}
	@Override
	public boolean coversAlternative(DomainAlternative a, PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) {
		return false;
	}
	@Override
	public boolean isCoveredByReal() {
		return true;
	}
	@Override
	public boolean isCoveredByRealRange(double value1, double value2) {
		return (value1 <= constantValue) && (value2 >= constantValue);
	}
	//
	@Override
	public String toString(CharsetEncoder encoder) {
		return PrologDomainName.tagDomainAlternative_RealConstant + "(" + FormatOutput.realToString(constantValue) + ")";
	}
}
