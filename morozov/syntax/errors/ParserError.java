// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

public class ParserError extends RuntimeException {
	protected int position;
	public ParserError(int p) {
		position= p;
	}
	public ParserError(int p, Throwable e) {
		super(e);
		position= p;
	}
	public int getPosition() {
		return position;
	}
	public String toString() {
		return this.getClass().toString() + "(position:" + Integer.toString(position) + ")";
	}
}
