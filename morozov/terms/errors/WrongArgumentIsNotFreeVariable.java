// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms.errors;

import morozov.terms.*;

public class WrongArgumentIsNotFreeVariable extends WrongArgument {
	public WrongArgumentIsNotFreeVariable(Term value) {
		super(value);
	}
}
