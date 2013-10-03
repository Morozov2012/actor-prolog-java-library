// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.run.*;
import morozov.system.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class DomainRealConstant extends DomainAlternative {
	protected double constantValue;
	public DomainRealConstant(double value) {
		constantValue= value;
	}
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		// t= t.dereferenceValue(cp);
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
}
