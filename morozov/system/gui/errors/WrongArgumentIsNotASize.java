// (c) 2009 IRE RAS Alexei A. Morozov

package morozov.system.gui.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotASize extends WrongArgument {
	public WrongArgumentIsNotASize(Term value) {
		super(value);
	}
}
