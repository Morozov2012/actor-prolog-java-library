// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

import morozov.syntax.scanner.*;

public class UnexpectedEndOfTokenList extends ParserError {
	public UnexpectedEndOfTokenList(PrologToken[] tokens) {
		super(tokens[tokens.length-1].getPosition());
	}
}
