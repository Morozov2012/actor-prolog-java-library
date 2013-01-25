// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import target.*;

import morozov.run.*;
import morozov.system.*;

import java.nio.charset.CharsetEncoder;

public class PrologSymbol extends Term {
	private long value;
	public PrologSymbol(long v) {
		value= v;
	}
	public int hashCode() {
		return (int)value;
	}
	public void isSymbol(long v, ChoisePoint cp) throws Backtracking {
		if (value != v)
			throw new Backtracking();
	}
	public long getSymbolValue(ChoisePoint cp) throws TermIsNotASymbol {
		return value;
	}
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.isSymbol(value,cp);
	}
	// Converting Term to String
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, CharsetEncoder encoder) {
		SymbolName name= SymbolNames.retrieveSymbolName(value);
		if (isInner) {
			return "'" + FormatOutput.encodeString(name.identifier,true,encoder) + "'";
		} else if (provideStrictSyntax) {
			return name.toString(encoder);
		} else {
			return name.identifier;
		}
	}
}
