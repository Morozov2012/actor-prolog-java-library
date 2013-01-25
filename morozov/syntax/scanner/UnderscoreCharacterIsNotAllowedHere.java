// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

public class UnderscoreCharacterIsNotAllowedHere extends LexicalScannerError {
	public UnderscoreCharacterIsNotAllowedHere(int p) {
		super(p);
	}
}
