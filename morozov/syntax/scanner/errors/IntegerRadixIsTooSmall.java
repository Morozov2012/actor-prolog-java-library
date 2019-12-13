// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner.errors;

import java.math.BigInteger;

public class IntegerRadixIsTooSmall extends LexicalScannerError {
	//
	protected BigInteger radix;
	//
	///////////////////////////////////////////////////////////////
	//
	public IntegerRadixIsTooSmall(BigInteger r, int p) {
		super(p);
		radix= r;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(" + radix.toString() + ")";
	}
}
