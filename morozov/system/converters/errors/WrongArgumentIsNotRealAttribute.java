// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.converters.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotRealAttribute extends WrongArgument {
	public WrongArgumentIsNotRealAttribute(Term value) {
		super(value);
	}
}
