// (c) 2010 IRE RAS Alexei A. Morozov
package morozov.domains;

import morozov.terms.*;
public class DomainRealRange extends DomainAlternative {
	protected double leftBound;
	protected double rightBound;
	public DomainRealRange(double left, double right) {
		leftBound= left;
		rightBound= right;
	}
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
}
