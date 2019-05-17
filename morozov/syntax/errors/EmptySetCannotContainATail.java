// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

public class EmptySetCannotContainATail extends ParserError {
	public EmptySetCannotContainATail(int p) {
		super(p);
	}
}
