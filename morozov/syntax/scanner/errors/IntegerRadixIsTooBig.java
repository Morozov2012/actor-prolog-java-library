// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner.errors;

public class IntegerRadixIsTooBig extends LexicalScannerError {
	public IntegerRadixIsTooBig(int p) {
		super(p);
	}
}