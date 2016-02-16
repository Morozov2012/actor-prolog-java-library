// (c) 2009 IRE RAS Alexei A. Morozov

package morozov.system.gui.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotACoordinate extends WrongArgument {
	public WrongArgumentIsNotACoordinate(Term value) {
		super(value);
	}
}
