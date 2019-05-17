// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner.errors;

public class WrongTokenIsNotASymbol extends LexicalScannerError {
	public WrongTokenIsNotASymbol(int p) {
		super(p);
	}
}
