// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

public class NumberSignIsNotAllowedHere extends LexicalScannerError {
	public NumberSignIsNotAllowedHere(int p) {
		super(p);
	}
}
