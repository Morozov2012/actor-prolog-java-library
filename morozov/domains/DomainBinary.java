// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashSet;

public class DomainBinary extends DomainAlternative {
	//
	public DomainBinary() {
	}
	//
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		// t= t.dereferenceValue(cp);
		t= t.dereferenceValue(cp);
		if (ignoreFreeVariables && t.thisIsFreeVariable()) {
			return true;
		} else {
			try {
				t.getBinaryValue(cp);
				return true;
			} catch (TermIsNotABinary e) {
				return false;
			}
		}
	}
	//
	public boolean isEqualTo(DomainAlternative a, HashSet<PrologDomainPair> stack) {
		return a.isEqualToBinary();
	}
	public boolean isEqualToBinary() {
		return true;
	}
	public boolean coversAlternative(DomainAlternative a, PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) {
		return a.isCoveredByBinary();
	}
	public boolean isCoveredByBinary() {
		return true;
	}
	//
	public String toString(CharsetEncoder encoder) {
		return PrologDomainName.tagDomainAlternative_Binary;
	}
}
