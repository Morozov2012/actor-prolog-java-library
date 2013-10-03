// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotAReal extends WrongArgument {
	public WrongArgumentIsNotAReal(Term value) {
		super(value);
	}
}
