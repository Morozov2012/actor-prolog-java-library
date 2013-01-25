// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

public class IntegerRadixIsTooSmall extends LexicalScannerError {
	public IntegerRadixIsTooSmall(int p) {
		super(p);
	}
}
