// (c) 2010 IRE RAS Alexei A. Morozov
package morozov.domains;

import morozov.terms.*;
public class DomainInteger extends DomainAlternative {
	public DomainInteger() {
	}
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		// t= t.dereferenceValue(cp);
		t= t.dereferenceValue(cp);
		if (ignoreFreeVariables && t.thisIsFreeVariable()) {
			return true;
		} else {
			try {
				t.getIntegerValue(cp);
				return true;
			} catch (TermIsNotAnInteger e) {
				return false;
			}
		}
	}
}
