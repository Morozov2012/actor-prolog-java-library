// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.run.*;
import morozov.system.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashSet;
import java.math.BigInteger;

public class DomainIntegerConstant extends DomainAlternative {
	//
	private static final long serialVersionUID= 0x47D56AD69FF31EA6L; // 5176160816771309222L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.domains","DomainIntegerConstant");
	// }
	//
	protected BigInteger constantValue;
	//
	public DomainIntegerConstant(BigInteger value) {
		constantValue= value;
	}
	public DomainIntegerConstant(long value) {
		constantValue= BigInteger.valueOf(value);
	}
	//
	@Override
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		t= t.dereferenceValue(cp);
		if (ignoreFreeVariables && t.thisIsFreeVariable()) {
			return true;
		} else {
			try {
				if (constantValue.equals(t.getIntegerValue(cp))) {
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
	@Override
	public boolean isEqualTo(DomainAlternative a, HashSet<PrologDomainPair> stack) {
		return a.isEqualToIntegerConstant(constantValue);
	}
	@Override
	public boolean isEqualToIntegerConstant(BigInteger value) {
		return constantValue.compareTo(value)==0;
	}
	@Override
	public boolean coversAlternative(DomainAlternative a, PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) {
		return false;
	}
	@Override
	public boolean isCoveredByInteger() {
		return true;
	}
	@Override
	public boolean isCoveredByIntegerRange(BigInteger value1, BigInteger value2) {
		return (value1.compareTo(constantValue)<=0) && (value2.compareTo(constantValue)>=0);
	}
	//
	@Override
	public String toString(CharsetEncoder encoder) {
		return PrologDomainName.tagDomainAlternative_IntegerConstant + "(" + FormatOutput.integerToString(constantValue) + ")";
	}
}
