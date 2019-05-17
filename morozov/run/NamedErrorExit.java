// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.run;

import target.*;

import morozov.terms.*;

public class NamedErrorExit extends ErrorExit {
	//
	private long nameCode;
	//
	public NamedErrorExit(ChoisePoint cp, long code) {
		choisePoint= cp;
		nameCode= code;
	}
	//
	public Term createTerm() {
		return new PrologSymbol(nameCode);
	}
	public String toString() {
		SymbolName name= SymbolNames.retrieveSymbolName(nameCode);
		return "'" + name.toRawString(null) + "'";
	}
}
