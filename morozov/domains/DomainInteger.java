// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashSet;

public class DomainInteger extends DomainAlternative {
	//
	private static final long serialVersionUID= 0x2E934A169D369C14L; // 3356107608308489236L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.domains","DomainInteger");
	// }
	//
	public DomainInteger() {
	}
	//
	@Override
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
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
	//
	@Override
	public boolean isEqualTo(DomainAlternative a, HashSet<PrologDomainPair> stack) {
		return a.isEqualToInteger();
	}
	@Override
	public boolean isEqualToInteger() {
		return true;
	}
	@Override
	public boolean coversAlternative(DomainAlternative a, PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) {
		return a.isCoveredByInteger();
	}
	@Override
	public boolean isCoveredByInteger() {
		return true;
	}
	//
	@Override
	public String toString(CharsetEncoder encoder) {
		return PrologDomainName.tagDomainAlternative_Integer;
	}
}
