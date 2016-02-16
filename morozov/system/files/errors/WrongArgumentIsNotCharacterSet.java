// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.files.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotCharacterSet extends WrongArgument {
	public WrongArgumentIsNotCharacterSet(Term value) {
		super(value);
	}
}
