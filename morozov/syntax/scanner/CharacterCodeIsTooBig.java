// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

public class CharacterCodeIsTooBig extends LexicalScannerError {
	public CharacterCodeIsTooBig(int p) {
		super(p);
	}
}
