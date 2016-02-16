// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.files.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotFileExtension extends WrongArgument {
	public WrongArgumentIsNotFileExtension(Term value) {
		super(value);
	}
}
