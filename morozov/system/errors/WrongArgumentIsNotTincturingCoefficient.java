// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotTincturingCoefficient extends WrongArgument {
	public WrongArgumentIsNotTincturingCoefficient(Term value) {
		super(value);
	}
}
