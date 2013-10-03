// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongTermIsNotTheOnOffSwitch extends WrongArgument {
	public WrongTermIsNotTheOnOffSwitch(Term value) {
		super(value);
	}
}
