// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

public class ControlCharactersAreNotAllowedHere extends LexicalScannerError {
	public ControlCharactersAreNotAllowedHere(int p) {
		super(p);
	}
}
