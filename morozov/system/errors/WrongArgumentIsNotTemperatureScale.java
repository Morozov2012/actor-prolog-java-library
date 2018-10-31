// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotTemperatureScale extends WrongArgument {
	public WrongArgumentIsNotTemperatureScale(Term value) {
		super(value);
	}
}
