// (c) 2010 IRE RAS Alexei A. Morozov
package morozov.domains;

import morozov.terms.*;
public class DomainUnknownValue extends DomainAlternative {
	public DomainUnknownValue() {
	}
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		t= t.dereferenceValue(cp);
		if (ignoreFreeVariables && t.thisIsFreeVariable()) {
			return true;
		} else {
			if (t.thisIsUnknownValue()) {
				return true;
			} else {
				return false;
			}
		}
	}
}
