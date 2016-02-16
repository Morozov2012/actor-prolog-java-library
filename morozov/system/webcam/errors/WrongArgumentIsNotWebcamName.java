// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.webcam.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotWebcamName extends WrongArgument {
	public WrongArgumentIsNotWebcamName(Term value) {
		super(value);
	}
}
