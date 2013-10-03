// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.files.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongTermIsNotCharacterSet extends WrongArgument {
	public WrongTermIsNotCharacterSet(Term value) {
		super(value);
	}
}
