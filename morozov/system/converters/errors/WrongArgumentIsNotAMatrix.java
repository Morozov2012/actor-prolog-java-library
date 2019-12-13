// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.converters.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotAMatrix extends WrongArgument {
	public WrongArgumentIsNotAMatrix(Term value) {
		super(value);
	}
}
