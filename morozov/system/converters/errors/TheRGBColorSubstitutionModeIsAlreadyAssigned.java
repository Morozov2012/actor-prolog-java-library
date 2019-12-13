// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.converters.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class TheRGBColorSubstitutionModeIsAlreadyAssigned extends WrongArgument {
	public TheRGBColorSubstitutionModeIsAlreadyAssigned(Term value) {
		super(value);
	}
}
