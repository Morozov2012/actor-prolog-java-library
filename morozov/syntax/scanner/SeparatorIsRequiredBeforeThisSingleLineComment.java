// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

public class SeparatorIsRequiredBeforeThisSingleLineComment extends LexicalScannerError {
	public SeparatorIsRequiredBeforeThisSingleLineComment(int p) {
		super(p);
	}
}
