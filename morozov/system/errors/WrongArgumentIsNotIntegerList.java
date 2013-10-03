// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotIntegerList extends WrongArgument {
	public WrongArgumentIsNotIntegerList(Term value) {
		super(value);
	}
}
