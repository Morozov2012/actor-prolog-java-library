// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms.errors;

import morozov.terms.*;

public class WrongTermIsNotConstructorArgument extends WrongArgument {
	public WrongTermIsNotConstructorArgument(Term value) {
		super(value);
	}
}
