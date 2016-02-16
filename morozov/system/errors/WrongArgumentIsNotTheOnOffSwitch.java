// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotTheOnOffSwitch extends WrongArgument {
	public WrongArgumentIsNotTheOnOffSwitch(Term value) {
		super(value);
	}
}
