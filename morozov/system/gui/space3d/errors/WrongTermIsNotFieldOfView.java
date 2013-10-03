// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongTermIsNotFieldOfView extends WrongArgument {
	public WrongTermIsNotFieldOfView(Term value) {
		super(value);
	}
}
