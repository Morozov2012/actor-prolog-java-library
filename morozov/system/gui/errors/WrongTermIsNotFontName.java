// (c) 2009 IRE RAS Alexei A. Morozov

package morozov.system.gui.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongTermIsNotFontName extends WrongArgument {
	public WrongTermIsNotFontName(Term value) {
		super(value);
	}
}
