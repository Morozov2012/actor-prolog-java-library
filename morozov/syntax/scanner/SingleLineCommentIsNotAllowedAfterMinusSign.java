// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

public class SingleLineCommentIsNotAllowedAfterMinusSign extends LexicalScannerError {
	public SingleLineCommentIsNotAllowedAfterMinusSign(int p) {
		super(p);
	}
}
