// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.run.*;
import morozov.terms.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashSet;

public class DomainUnknownValue extends DomainAlternative {
	//
	private static final long serialVersionUID= 0x8D4DAB92B8C95AC1L; // -8264761094501213503L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.domains","DomainUnknownValue");
	// }
	//
	public DomainUnknownValue() {
	}
	//
	@Override
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
	//
	@Override
	public boolean isEqualTo(DomainAlternative a, HashSet<PrologDomainPair> stack) {
		return a.isEqualToUnknownValue();
	}
	@Override
	public boolean isEqualToUnknownValue() {
		return true;
	}
	@Override
	public boolean coversAlternative(DomainAlternative a, PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) {
		return false;
	}
	//
	@Override
	public String toString(CharsetEncoder encoder) {
		return PrologDomainName.tagDomainAlternative_UnknownValue;
	}
}
