// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotAList extends WrongArgument {
	public WrongArgumentIsNotAList(Term value) {
		super(value);
	}
}
