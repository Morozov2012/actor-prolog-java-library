// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner.errors;

public class SeparatorIsRequiredBeforeThisSingleLineComment extends LexicalScannerError {
	public SeparatorIsRequiredBeforeThisSingleLineComment(int p) {
		super(p);
	}
}
