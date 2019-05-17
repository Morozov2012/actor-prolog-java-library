// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.errors;

import morozov.syntax.errors.*;

public class SecondaryFunctionVariableIsNotAllowedHere extends ParserError {
	public SecondaryFunctionVariableIsNotAllowedHere(int p) {
		super(p);
	}
}
