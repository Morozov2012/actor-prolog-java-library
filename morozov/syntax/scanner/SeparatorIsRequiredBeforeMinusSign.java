// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

public class SeparatorIsRequiredBeforeMinusSign extends LexicalScannerError {
	public SeparatorIsRequiredBeforeMinusSign(int p) {
		super(p);
	}
}
