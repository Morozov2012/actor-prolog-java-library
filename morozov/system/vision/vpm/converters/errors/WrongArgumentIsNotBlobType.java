// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.converters.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotBlobType extends WrongArgument {
	public WrongArgumentIsNotBlobType(Term value) {
		super(value);
	}
}
