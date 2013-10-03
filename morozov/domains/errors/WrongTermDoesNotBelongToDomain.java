// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongTermDoesNotBelongToDomain extends WrongArgument {
	public WrongTermDoesNotBelongToDomain(Term value) {
		super(value);
	}
}
