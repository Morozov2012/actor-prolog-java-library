// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.datum.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotDatabaseAccessMode extends WrongArgument {
	public WrongArgumentIsNotDatabaseAccessMode(Term value) {
		super(value);
	}
}
