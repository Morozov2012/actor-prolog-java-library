// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.terms.errors;

import morozov.terms.*;

public class OperationIsNotDefinedForTheArgument extends WrongArgument {
	public OperationIsNotDefinedForTheArgument(Term value) {
		super(value);
	}
}
