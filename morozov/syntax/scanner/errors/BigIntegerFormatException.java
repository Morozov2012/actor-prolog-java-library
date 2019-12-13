// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner.errors;

public class BigIntegerFormatException extends LexicalScannerError {
	//
	protected String mantissa;
	protected int radix;
	//
	///////////////////////////////////////////////////////////////
	//
	public BigIntegerFormatException(String m, int r, int p) {
		super(p);
		mantissa= m;
		radix= r;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(" + mantissa + "," + Integer.toString(radix) + ")";
	}
}
