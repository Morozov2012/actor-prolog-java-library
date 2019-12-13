// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashSet;

public class DomainString extends DomainAlternative {
	//
	private static final long serialVersionUID= 0xFDD3D68AC7A6752BL; // -156545670484429525L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.domains","DomainString");
	// }
	//
	public DomainString() {
	}
	//
	@Override
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
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
	//
	@Override
	public boolean isEqualTo(DomainAlternative a, HashSet<PrologDomainPair> stack) {
		return a.isEqualToString();
	}
	@Override
	public boolean isEqualToString() {
		return true;
	}
	@Override
	public boolean coversAlternative(DomainAlternative a, PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) {
		return a.isCoveredByString();
	}
	@Override
	public boolean isCoveredByString() {
		return true;
	}
	//
	@Override
	public String toString(CharsetEncoder encoder) {
		return PrologDomainName.tagDomainAlternative_String;
	}
}
