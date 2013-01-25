// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import morozov.system.*;

import java.math.BigInteger;

public class TokenInteger extends PrologToken {
	protected BigInteger value;
	public TokenInteger(BigInteger n, int position) {
		super(position);
		value= n;
	}
	public PrologTokenType getType() {
		return PrologTokenType.INTEGER;
	}
	public BigInteger getIntegerValue() {
		return value;
	}
	public String toString() {
		return FormatOutput.integerToString(value);
	}
}
