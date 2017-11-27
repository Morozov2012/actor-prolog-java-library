// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner.errors;

public class KeywordContainsLettersOfIllegalCase extends LexicalScannerError {
	public KeywordContainsLettersOfIllegalCase(int p) {
		super(p);
	}
}
