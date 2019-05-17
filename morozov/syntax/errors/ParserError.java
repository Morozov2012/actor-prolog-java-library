// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

import morozov.syntax.scanner.errors.*;

public class ParserError extends SyntaxError {
	//
	public ParserError(int p) {
		super(p);
	}
	public ParserError(int p, Throwable e) {
		super(p,e);
	}
}
