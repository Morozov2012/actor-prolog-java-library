// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner.errors;

public class MultilineCommentIsNotTerminated extends LexicalScannerError {
	public MultilineCommentIsNotTerminated(int p) {
		super(p);
	}
}
