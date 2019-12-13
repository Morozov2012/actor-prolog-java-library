// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotEndedSetOfAttributes extends WrongArgument {
	public WrongArgumentIsNotEndedSetOfAttributes(Term value) {
		super(value);
	}
}
