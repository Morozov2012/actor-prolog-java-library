// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.indices;

import morozov.terms.*;

public class WrongTermIsNotIndexRange extends WrongArgument {
	public WrongTermIsNotIndexRange(Term value) {
		super(value);
	}
}
