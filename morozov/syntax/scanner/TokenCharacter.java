// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.terms.*;

import java.math.BigInteger;

public class TokenCharacter extends TokenInteger10 {
	//
	public TokenCharacter(int code, int position) {
		super(BigInteger.valueOf(code),position);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		Term[] arguments= new Term[]{new PrologInteger(value)};
		return new PrologStructure(SymbolCodes.symbolCode_E_character,arguments);
	}
}
