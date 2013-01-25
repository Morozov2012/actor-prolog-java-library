// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax;

public class UnexpectedToken extends ParserError {
	public UnexpectedToken(int p) {
		super(p);
	}
}
