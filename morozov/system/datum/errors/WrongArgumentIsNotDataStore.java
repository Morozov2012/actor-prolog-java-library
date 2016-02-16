// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.datum.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotDataStore extends WrongArgument {
	public WrongArgumentIsNotDataStore(Term value) {
		super(value);
	}
}
