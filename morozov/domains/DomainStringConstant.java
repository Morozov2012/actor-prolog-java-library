// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.run.*;
import morozov.system.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashSet;

public class DomainStringConstant extends DomainAlternative {
	//
	protected String constantText;
	//
	private static final long serialVersionUID= 0x1233F66031952131L; // 1311662809504096561L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.domains","DomainStringConstant");
	// }
	//
	public DomainStringConstant(String text) {
		constantText= text;
	}
	//
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
	//
	public boolean isEqualTo(DomainAlternative a, HashSet<PrologDomainPair> stack) {
		return a.isEqualToStringConstant(constantText);
	}
	public boolean isEqualToStringConstant(String value) {
		return constantText.compareTo(value)==0;
	}
	public boolean coversAlternative(DomainAlternative a, PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) {
		return false;
	}
	public boolean isCoveredByString() {
		return true;
	}
	//
	public String toString(CharsetEncoder encoder) {
		return PrologDomainName.tagDomainAlternative_StringConstant + "(\"" + FormatOutput.encodeString(constantText,false,encoder) + "\")";
	}
}
