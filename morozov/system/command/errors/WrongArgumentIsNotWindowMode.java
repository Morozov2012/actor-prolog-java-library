// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.command.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotWindowMode extends WrongArgument {
	public WrongArgumentIsNotWindowMode(Term value) {
		super(value);
	}
}
