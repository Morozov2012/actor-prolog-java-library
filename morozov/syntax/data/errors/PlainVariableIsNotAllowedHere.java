// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.errors;

import morozov.syntax.errors.*;

public class PlainVariableIsNotAllowedHere extends ParserError {
	public PlainVariableIsNotAllowedHere(int p) {
		super(p);
	}
}
