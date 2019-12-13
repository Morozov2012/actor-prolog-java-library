// (c) 2009 IRE RAS Alexei A. Morozov

package morozov.system.converters.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotAColor extends WrongArgument {
	public WrongArgumentIsNotAColor(Term value) {
		super(value);
	}
}
