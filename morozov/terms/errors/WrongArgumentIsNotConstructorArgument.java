// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms.errors;

import morozov.terms.*;

public class WrongArgumentIsNotConstructorArgument extends WrongArgument {
	public WrongArgumentIsNotConstructorArgument(Term value) {
		super(value);
	}
}
