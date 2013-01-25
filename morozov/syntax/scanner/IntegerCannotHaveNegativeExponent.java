// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

public class IntegerCannotHaveNegativeExponent extends LexicalScannerError {
	public IntegerCannotHaveNegativeExponent(int p) {
		super(p);
	}
}
