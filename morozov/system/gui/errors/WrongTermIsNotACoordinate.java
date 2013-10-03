// (c) 2009 IRE RAS Alexei A. Morozov

package morozov.system.gui.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongTermIsNotACoordinate extends WrongArgument {
	public WrongTermIsNotACoordinate(Term value) {
		super(value);
	}
}
