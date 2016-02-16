// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.sadt.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotComponentIdentifier extends WrongArgument {
	public WrongArgumentIsNotComponentIdentifier(Term value) {
		super(value);
	}
}
