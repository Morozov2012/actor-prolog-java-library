// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.terms.*;

public class WrongTermIsNotACommand extends WrongArgument {
	public WrongTermIsNotACommand(Term value) {
		super(value);
	}
}
