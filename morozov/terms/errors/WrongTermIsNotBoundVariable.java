// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms.errors;

import morozov.terms.*;

public class WrongTermIsNotBoundVariable extends WrongArgument {
	public WrongTermIsNotBoundVariable(Term value) {
		super(value);
	}
}
