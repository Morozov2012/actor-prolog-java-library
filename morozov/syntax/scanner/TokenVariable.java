// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.terms.*;

public class TokenVariable extends PrologToken {
	//
	protected String name;
	//
	///////////////////////////////////////////////////////////////
	//
	public TokenVariable(String s, int position) {
		super(position);
		name= s;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public PrologTokenType getType() {
		return PrologTokenType.VARIABLE;
	}
	public String getVariableName() {
		return name;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		Term[] arguments= new Term[]{new PrologString(name)};
		return new PrologStructure(SymbolCodes.symbolCode_E_vn,arguments);
	}
	public String toString() {
		return name;
	}
}
