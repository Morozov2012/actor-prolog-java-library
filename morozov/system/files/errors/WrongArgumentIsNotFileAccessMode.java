// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.files.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotFileAccessMode extends WrongArgument {
	public WrongArgumentIsNotFileAccessMode(Term value) {
		super(value);
	}
}
