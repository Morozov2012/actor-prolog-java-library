// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.system.*;
import morozov.terms.*;

import java.math.BigInteger;

public class TokenIntegerR extends TokenInteger10 {
	//
	protected BigInteger radix;
	protected String mantissa;
	protected BigInteger exponent;
	//
	// protected static Term termIntegerZero= new PrologInteger(0);
	//
	///////////////////////////////////////////////////////////////
	//
	public TokenIntegerR(BigInteger n, BigInteger r, String m, BigInteger e, int position) {
		super(n,position);
		radix= r;
		mantissa= m;
		exponent= e;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		Term[] arguments= new Term[]{new PrologInteger(value),new PrologInteger(radix),new PrologString(mantissa),new PrologInteger(exponent)};
		return new PrologStructure(SymbolCodes.symbolCode_E_integer_R,arguments);
	}
}
