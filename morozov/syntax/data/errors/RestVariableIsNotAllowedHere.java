// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.errors;

import morozov.syntax.errors.*;

public class RestVariableIsNotAllowedHere extends ParserError {
	public RestVariableIsNotAllowedHere(int p) {
		super(p);
	}
}
