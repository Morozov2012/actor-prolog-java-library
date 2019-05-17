// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner.errors;

public class SyntaxError extends Exception {
	//
	protected int position;
	//
	public SyntaxError(int p) {
		position= p;
	}
	public SyntaxError(int p, Throwable e) {
		super(e);
		position= p;
	}
	//
	public int getPosition() {
		return position;
	}
	//
	public String toString() {
		return this.getClass().toString() + "(position:" + Integer.toString(position) + ")";
	}
}
