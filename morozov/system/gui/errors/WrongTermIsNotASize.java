// (c) 2009 IRE RAS Alexei A. Morozov

package morozov.system.gui.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongTermIsNotASize extends WrongArgument {
	public WrongTermIsNotASize(Term value) {
		super(value);
	}
}
