// (c) 2010 IRE RAS Alexei A. Morozov
package morozov.domains;

import morozov.terms.*;
public class DomainSymbolConstant extends DomainAlternative {
	protected long constantCode;
	public DomainSymbolConstant(long code) {
		constantCode= code;
	}
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		// t= t.dereferenceValue(cp);
		t= t.dereferenceValue(cp);
		if (ignoreFreeVariables && t.thisIsFreeVariable()) {
			return true;
		} else {
			try {
				if (constantCode == t.getSymbolValue(cp)) {
					return true;
				} else {
					return false;
				}
			} catch (TermIsNotASymbol e) {
				return false;
			}
		}
	}
}
