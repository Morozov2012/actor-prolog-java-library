// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms.errors;

import morozov.terms.*;

public class WrongArgumentIsNotBoundVariable extends WrongArgument {
	public WrongArgumentIsNotBoundVariable(Term value) {
		super(value);
	}
}
