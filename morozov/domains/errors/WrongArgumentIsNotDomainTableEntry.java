// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotDomainTableEntry extends WrongArgument {
	public WrongArgumentIsNotDomainTableEntry(Term value) {
		super(value);
	}
}
