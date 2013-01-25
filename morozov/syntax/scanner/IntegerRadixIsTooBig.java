// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

public class IntegerRadixIsTooBig extends LexicalScannerError {
	public IntegerRadixIsTooBig(int p) {
		super(p);
	}
}
