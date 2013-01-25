// (c) 2010 IRE RAS Alexei A. Morozov
package morozov.domains;

import morozov.terms.*;

import java.math.BigInteger;

public class DomainIntegerConstant extends DomainAlternative {
	protected BigInteger constantValue;
	public DomainIntegerConstant(long value) {
		constantValue= BigInteger.valueOf(value);
	}
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		// t= t.dereferenceValue(cp);
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
}
