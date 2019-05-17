// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.run.*;
import morozov.syntax.scanner.errors.*;
import morozov.syntax.scanner.interfaces.*;
import morozov.terms.*;

public class TokenString extends PrologToken {
	//
	protected String value;
	//
	///////////////////////////////////////////////////////////////
	//
	public TokenString(String s, int position) {
		super(position);
		value= s;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public PrologTokenType getType() {
		return PrologTokenType.STRING;
	}
	public String getStringValue(LexicalScannerMasterInterface master, ChoisePoint iX) throws LexicalScannerError {
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toActorPrologTerm() {
		Term[] arguments= new Term[]{new PrologString(value)};
		return new PrologStructure(SymbolCodes.symbolCode_E_string,arguments);
	}
	public String toString() {
		return value;
	}
}
