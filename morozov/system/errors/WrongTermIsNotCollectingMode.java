// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongTermIsNotCollectingMode extends WrongArgument {
	public WrongTermIsNotCollectingMode(Term value) {
		super(value);
	}
}
