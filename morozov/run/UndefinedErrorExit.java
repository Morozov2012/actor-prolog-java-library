// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.run;

import target.*;

import morozov.terms.*;

public class UndefinedErrorExit extends ErrorExit {
	//
	public UndefinedErrorExit(ChoisePoint cp) {
		choisePoint= cp;
	}
	//
	@Override
	public Term createTerm() {
		return new PrologSymbol(SymbolCodes.symbolCode_E_undefined_exception);
	}
	@Override
	public String toString() {
		SymbolName name= SymbolNames.retrieveSymbolName(SymbolCodes.symbolCode_E_undefined_exception);
		return "'" + name.toRawString(null) + "'";
	}
}
