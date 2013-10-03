// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotATransform3D extends WrongArgument {
	public WrongArgumentIsNotATransform3D(Term value) {
		super(value);
	}
}
