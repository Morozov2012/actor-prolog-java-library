// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner.errors;

public class SingleLineCommentIsNotAllowedAfterMinusSign extends LexicalScannerError {
	public SingleLineCommentIsNotAllowedAfterMinusSign(int p) {
		super(p);
	}
}
