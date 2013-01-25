// (c) 2010 IRE RAS Alexei A. Morozov
package morozov.domains;

import morozov.terms.*;
public class DomainReal extends DomainAlternative {
	public DomainReal() {
	}
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		// t= t.dereferenceValue(cp);
		t= t.dereferenceValue(cp);
		if (ignoreFreeVariables && t.thisIsFreeVariable()) {
			return true;
		} else {
			try {
				t.getRealValue(cp);
				return true;
			} catch (TermIsNotAReal e) {
				return false;
			}
		}
	}
}
