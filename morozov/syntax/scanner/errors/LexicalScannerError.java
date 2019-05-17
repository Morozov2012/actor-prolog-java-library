// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner.errors;

public class LexicalScannerError extends SyntaxError {
	//
	public LexicalScannerError(int p) {
		super(p);
	}
	public LexicalScannerError(int p, Throwable e) {
		super(p,e);
	}
}
