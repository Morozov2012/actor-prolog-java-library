// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class DomainStringConstant extends DomainAlternative {
	protected String constantText;
	public DomainStringConstant(String text) {
		constantText= text;
	}
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		// t= t.dereferenceValue(cp);
		t= t.dereferenceValue(cp);
		if (ignoreFreeVariables && t.thisIsFreeVariable()) {
			return true;
		} else {
			try {
				if (constantText.equals(t.getStringValue(cp))) {
					return true;
				} else {
					return false;
				}
			} catch (TermIsNotAString e) {
				return false;
			}
		}
	}
}
