// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.indices.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongTermIsNotIndexBound extends WrongArgument {
	public WrongTermIsNotIndexBound(Term value) {
		super(value);
	}
}
