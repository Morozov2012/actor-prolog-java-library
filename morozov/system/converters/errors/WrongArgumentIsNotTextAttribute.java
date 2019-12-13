// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.converters.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotTextAttribute extends WrongArgument {
	public WrongArgumentIsNotTextAttribute(Term value) {
		super(value);
	}
}
