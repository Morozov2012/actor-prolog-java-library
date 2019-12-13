// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.converters.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotDeviceIdentifier extends WrongArgument {
	public WrongArgumentIsNotDeviceIdentifier(Term value) {
		super(value);
	}
}
