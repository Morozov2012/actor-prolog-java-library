// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.indices;

import morozov.terms.*;

public class WrongTermIsNotIndexBound extends WrongArgument {
	public WrongTermIsNotIndexBound(Term value) {
		super(value);
	}
}
