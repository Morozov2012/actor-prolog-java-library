// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner.errors;

public class SingleLineCommentIsNotTerminatedByNewline extends LexicalScannerError {
	public SingleLineCommentIsNotTerminatedByNewline(int p) {
		super(p);
	}
}
