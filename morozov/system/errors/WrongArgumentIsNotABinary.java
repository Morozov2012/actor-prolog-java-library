// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotABinary extends WrongArgument {
	public WrongArgumentIsNotABinary(Term value) {
		super(value);
	}
}
