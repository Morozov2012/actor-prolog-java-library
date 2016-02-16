// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.command.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotACommand extends WrongArgument {
	public WrongArgumentIsNotACommand(Term value) {
		super(value);
	}
}
