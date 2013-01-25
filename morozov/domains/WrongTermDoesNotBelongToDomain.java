// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.terms.*;

public class WrongTermDoesNotBelongToDomain extends WrongArgument {
	public WrongTermDoesNotBelongToDomain(Term value) {
		super(value);
	}
}
