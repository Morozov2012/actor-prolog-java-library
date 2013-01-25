// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax;

public class UnexpectedEndOfTokenList extends ParserError {
	public UnexpectedEndOfTokenList(int p) {
		super(p);
	}
}
