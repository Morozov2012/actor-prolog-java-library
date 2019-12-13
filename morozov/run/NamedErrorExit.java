// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.run;

import target.*;

import morozov.terms.*;

public class NamedErrorExit extends ErrorExit {
	//
	protected boolean useTextName;
	protected String nameText;
	protected long nameCode;
	//
	public NamedErrorExit(ChoisePoint cp, String text) {
		choisePoint= cp;
		nameText= text;
		useTextName= true;
	}
	public NamedErrorExit(ChoisePoint cp, long code) {
		choisePoint= cp;
		nameCode= code;
		useTextName= false;
	}
	//
	@Override
	public Term createTerm() {
		if (useTextName) {
			return new PrologString(nameText);
		} else {
			return new PrologSymbol(nameCode);
		}
	}
	@Override
	public String toString() {
		if (useTextName) {
			return "\"" + nameText + "\"";
		} else {
			SymbolName name= SymbolNames.retrieveSymbolName(nameCode);
			return "'" + name.toRawString(null) + "'";
		}
	}
}
