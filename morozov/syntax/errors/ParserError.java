// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

public class ParserError extends RuntimeException {
	protected int position;
	public ParserError(int p) {
		position= p;
	}
	public int getPosition() {
		return position;
	}
}
