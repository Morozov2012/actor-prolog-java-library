// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashSet;

public class DomainSymbol extends DomainAlternative {
	//
	private static final long serialVersionUID= 0x77533AC0F4A8AABEL; // 8598280713993235134L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.domains","DomainSymbol");
	// }
	//
	public DomainSymbol() {
	}
	//
	@Override
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		t= t.dereferenceValue(cp);
		if (ignoreFreeVariables && t.thisIsFreeVariable()) {
			return true;
		} else {
			try {
				t.getSymbolValue(cp);
				return true;
			} catch (TermIsNotASymbol e) {
				return false;
			}
		}
	}
	//
	@Override
	public boolean isEqualTo(DomainAlternative a, HashSet<PrologDomainPair> stack) {
		return a.isEqualToSymbol();
	}
	@Override
	public boolean isEqualToSymbol() {
		return true;
	}
	@Override
	public boolean coversAlternative(DomainAlternative a, PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) {
		return a.isCoveredBySymbol();
	}
	@Override
	public boolean isCoveredBySymbol() {
		return true;
	}
	//
	@Override
	public String toString(CharsetEncoder encoder) {
		return PrologDomainName.tagDomainAlternative_Symbol;
	}
}
