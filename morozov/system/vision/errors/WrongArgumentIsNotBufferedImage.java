// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.vision.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotBufferedImage extends WrongArgument {
	public WrongArgumentIsNotBufferedImage(Term value) {
		super(value);
	}
}
