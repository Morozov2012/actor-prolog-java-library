// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.system.*;

import java.nio.charset.CharsetEncoder;

public class PrologString extends Term {
	private String value;
	public PrologString(String v) {
		value= v;
	}
	public int hashCode() {
		return value.hashCode();
	}
	public void isString(String v, ChoisePoint cp) throws Backtracking {
		if ( !value.equals(v) )
			throw new Backtracking();
	}
	public String getStringValue(ChoisePoint cp) throws TermIsNotAString {
		return value;
	}
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.isString(value,cp);
	}
	// Converting Term to String
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, CharsetEncoder encoder) {
		// System.out.printf("PrologString: >>>%s<<<\n",value.toString());
		if (isInner || provideStrictSyntax) {
			return "\"" + FormatOutput.encodeString(value,false,encoder) + "\"";
		} else {
			return value;
		}
	}
}
