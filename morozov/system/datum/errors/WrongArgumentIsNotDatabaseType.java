// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.datum.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotDatabaseType extends WrongArgument {
	public WrongArgumentIsNotDatabaseType(Term value) {
		super(value);
	}
}
