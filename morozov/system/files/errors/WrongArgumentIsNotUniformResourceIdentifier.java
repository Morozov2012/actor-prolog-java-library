// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.files.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotUniformResourceIdentifier extends WrongArgument {
	public WrongArgumentIsNotUniformResourceIdentifier(Term value) {
		super(value);
	}
}
