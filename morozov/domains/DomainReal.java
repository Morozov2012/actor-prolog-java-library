// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashSet;

public class DomainReal extends DomainAlternative {
	//
	private static final long serialVersionUID= 0xADDEADD59D02CFC1L; // -5918101727344406591L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.domains","DomainReal");
	// }
	//
	public DomainReal() {
	}
	//
	@Override
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
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
	//
	@Override
	public boolean isEqualTo(DomainAlternative a, HashSet<PrologDomainPair> stack) {
		return a.isEqualToReal();
	}
	@Override
	public boolean isEqualToReal() {
		return true;
	}
	@Override
	public boolean coversAlternative(DomainAlternative a, PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) {
		return a.isCoveredByReal();
	}
	@Override
	public boolean isCoveredByReal() {
		return true;
	}
	//
	@Override
	public String toString(CharsetEncoder encoder) {
		return PrologDomainName.tagDomainAlternative_Real;
	}
}
