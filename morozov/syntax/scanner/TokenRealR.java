// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.terms.*;

import java.math.BigInteger;

public class TokenRealR extends TokenReal10 {
	//
	protected BigInteger radix;
	//
	///////////////////////////////////////////////////////////////
	//
	public TokenRealR(BigInteger r, double n, boolean isExtended, int position) {
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
		Term[] arguments= new Term[]{new PrologInteger(radix),new PrologReal(value)};
		return new PrologStructure(SymbolCodes.symbolCode_E_real_R,arguments);
	}
}
