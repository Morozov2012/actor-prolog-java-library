// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner.errors;

import java.math.BigInteger;

public class BigDecimalFormatException extends LexicalScannerError {
	//
	protected BigInteger mantissa;
	protected BigInteger radix;
	//
	///////////////////////////////////////////////////////////////
	//
	public BigDecimalFormatException(BigInteger m, int r, int p) {
		super(p);
		mantissa= m;
		radix= BigInteger.valueOf(r);
	}
	public BigDecimalFormatException(BigInteger m, BigInteger r, int p) {
		super(p);
		mantissa= m;
		radix= r;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public String toString() {
		return this.getClass().toString() + "(" + mantissa + "," + radix.toString() + ")";
	}
}
