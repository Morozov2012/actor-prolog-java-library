// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

public class IntegerExponentIsTooBig extends LexicalScannerError {
	public IntegerExponentIsTooBig(int p) {
		super(p);
	}
}
