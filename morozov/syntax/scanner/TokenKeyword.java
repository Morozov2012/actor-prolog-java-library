// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.terms.*;

public class TokenKeyword extends TokenSymbol {
	//
	public TokenKeyword(int c, int position) {
		super(c,false,position);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public PrologTokenType getType() {
		return PrologTokenType.KEYWORD;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		Term[] arguments= new Term[]{new PrologSymbol(value)};
		return new PrologStructure(SymbolCodes.symbolCode_E_keyword,arguments);
	}
}
