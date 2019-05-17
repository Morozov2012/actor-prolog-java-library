// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.errors;

import morozov.syntax.errors.*;

public class PrimaryFunctionVariableIsNotAllowedHere extends ParserError {
	public PrimaryFunctionVariableIsNotAllowedHere(int p) {
		super(p);
	}
}
