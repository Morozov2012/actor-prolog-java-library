// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotWaitingInterval extends WrongArgument {
	public WrongArgumentIsNotWaitingInterval(Term value) {
		super(value);
	}
}
