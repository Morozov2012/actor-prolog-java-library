// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.sadt.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongTermIsNotIdentifierAndComponent extends WrongArgument {
	public WrongTermIsNotIdentifierAndComponent(Term value) {
		super(value);
	}
}