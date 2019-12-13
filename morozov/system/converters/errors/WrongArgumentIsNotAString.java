// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.converters.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotAString extends WrongArgument {
	public WrongArgumentIsNotAString(Term value) {
		super(value);
	}
}
