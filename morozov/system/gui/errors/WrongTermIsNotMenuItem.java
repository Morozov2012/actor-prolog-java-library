// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongTermIsNotMenuItem extends WrongArgument {
	public WrongTermIsNotMenuItem(Term value) {
		super(value);
	}
}
