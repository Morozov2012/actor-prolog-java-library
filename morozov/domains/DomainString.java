// (c) 2010 IRE RAS Alexei A. Morozov
package morozov.domains;

import morozov.terms.*;
public class DomainString extends DomainAlternative {
	public DomainString() {
	}
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		// t= t.dereferenceValue(cp);
		t= t.dereferenceValue(cp);
		if (ignoreFreeVariables && t.thisIsFreeVariable()) {
			return true;
		} else {
			try {
				t.getStringValue(cp);
				return true;
			} catch (TermIsNotAString e) {
				return false;
			}
		}
	}
}
