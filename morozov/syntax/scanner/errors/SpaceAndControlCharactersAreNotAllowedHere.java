// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner.errors;

public class SpaceAndControlCharactersAreNotAllowedHere extends LexicalScannerError {
	public SpaceAndControlCharactersAreNotAllowedHere(int p) {
		super(p);
	}
}
