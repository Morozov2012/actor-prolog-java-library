// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.terms.errors;

import morozov.terms.*;

public class WrongTermIsNotSetElement extends WrongArgument {
	public WrongTermIsNotSetElement(Term value) {
		super(value);
	}
}
