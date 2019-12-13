// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashSet;

public class DomainBinary extends DomainAlternative {
	//
	private static final long serialVersionUID= 0x5B1464F051171CD4L; // 6562981540301053140L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.domains","DomainBinary");
	// }
	//
	public DomainBinary() {
	}
	//
	@Override
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
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
	@Override
	public boolean isEqualTo(DomainAlternative a, HashSet<PrologDomainPair> stack) {
		return a.isEqualToBinary();
	}
	@Override
	public boolean isEqualToBinary() {
		return true;
	}
	@Override
	public boolean coversAlternative(DomainAlternative a, PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) {
		return a.isCoveredByBinary();
	}
	@Override
	public boolean isCoveredByBinary() {
		return true;
	}
	//
	@Override
	public String toString(CharsetEncoder encoder) {
		return PrologDomainName.tagDomainAlternative_Binary;
	}
}
