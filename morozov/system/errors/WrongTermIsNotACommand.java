// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongTermIsNotACommand extends WrongArgument {
	public WrongTermIsNotACommand(Term value) {
		super(value);
	}
}
