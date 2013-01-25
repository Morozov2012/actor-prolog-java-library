// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

public class UnexpectedEndOfText extends LexicalScannerError {
	public UnexpectedEndOfText(int p) {
		super(p);
	}
}
