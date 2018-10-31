// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotIntegerAttribute extends WrongArgument {
	public WrongArgumentIsNotIntegerAttribute(Term value) {
		super(value);
	}
}
