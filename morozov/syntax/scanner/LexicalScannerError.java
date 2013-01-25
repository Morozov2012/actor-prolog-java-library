// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

public class LexicalScannerError extends RuntimeException {
	protected int position;
	public LexicalScannerError(int p) {
		position= p;
	}
	public int getPosition() {
		return position;
	}
}
