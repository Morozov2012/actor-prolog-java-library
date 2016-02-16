// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner.errors;

public class LexicalScannerError extends RuntimeException {
	protected int position;
	public LexicalScannerError(int p) {
		position= p;
	}
	public int getPosition() {
		return position;
	}
	public String toString() {
		return this.getClass().toString() + "(position:" + Integer.toString(position) + ")";
	}
}
