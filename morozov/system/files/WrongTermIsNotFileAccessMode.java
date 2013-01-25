// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.files;

import morozov.terms.*;

public class WrongTermIsNotFileAccessMode extends WrongArgument {
	public WrongTermIsNotFileAccessMode(Term value) {
		super(value);
	}
}
