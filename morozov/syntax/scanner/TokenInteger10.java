// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.system.*;
import morozov.terms.*;

import java.math.BigInteger;

public class TokenInteger10 extends PrologToken {
	//
	protected BigInteger value;
	//
	///////////////////////////////////////////////////////////////
	//
	public TokenInteger10(BigInteger n, int position) {
		super(position);
		value= n;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public PrologTokenType getType() {
		return PrologTokenType.INTEGER;
	}
	public BigInteger getIntegerValue() {
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		Term[] arguments= new Term[]{new PrologInteger(value)};
		return new PrologStructure(SymbolCodes.symbolCode_E_integer_10,arguments);
	}
	public String toString() {
		return FormatOutput.integerToString(value);
	}
}
