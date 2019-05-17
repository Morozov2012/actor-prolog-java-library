// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.frames.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotReadWriteBuffer extends WrongArgument {
	public WrongArgumentIsNotReadWriteBuffer(Term value) {
		super(value);
	}
}
