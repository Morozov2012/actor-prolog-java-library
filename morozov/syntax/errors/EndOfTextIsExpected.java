// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

public class EndOfTextIsExpected extends ParserError {
	public EndOfTextIsExpected(int p) {
		super(p);
	}
}
