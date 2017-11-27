// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner.errors;

import java.math.BigInteger;

public class IntegerRadixIsTooBig extends LexicalScannerError {
	//
	BigInteger radix;
	//
	///////////////////////////////////////////////////////////////
	//
	public IntegerRadixIsTooBig(BigInteger r, int p) {
		super(p);
		radix= r;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public String toString() {
		return this.getClass().toString() + "(" + radix.toString() + ")";
	}
}
