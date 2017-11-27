// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

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
	public String getStringValue() {
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		Term[] arguments= new Term[]{new PrologString(value)};
		return new PrologStructure(SymbolCodes.symbolCode_E_string,arguments);
	}
	public String toString() {
		return value;
	}
}
