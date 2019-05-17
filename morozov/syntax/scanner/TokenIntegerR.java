// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.terms.*;

import java.math.BigInteger;

public class TokenIntegerR extends TokenInteger10 {
	//
	protected BigInteger radix;
	//
	///////////////////////////////////////////////////////////////
	//
	public TokenIntegerR(BigInteger r, BigInteger n, boolean isExtended, int position) {
		super(n,isExtended,position);
		radix= r;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public BigInteger getRadix() {
		return radix;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toActorPrologTerm() {
		Term[] arguments= new Term[]{new PrologInteger(radix),new PrologInteger(value)};
		return new PrologStructure(SymbolCodes.symbolCode_E_integer_R,arguments);
	}
}
