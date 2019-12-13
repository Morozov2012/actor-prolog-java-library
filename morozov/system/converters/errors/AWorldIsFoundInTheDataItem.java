// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.converters.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class AWorldIsFoundInTheDataItem extends WrongArgument {
	public AWorldIsFoundInTheDataItem(Term value) {
		super(value);
	}
}
