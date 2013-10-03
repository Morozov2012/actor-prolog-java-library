// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms.errors;

import morozov.terms.*;

public class WrongTermIsNotFreeVariable extends WrongArgument {
	public WrongTermIsNotFreeVariable(Term value) {
		super(value);
	}
}
