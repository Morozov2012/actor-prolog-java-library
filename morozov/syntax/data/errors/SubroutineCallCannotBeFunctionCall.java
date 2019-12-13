// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.errors;

import morozov.syntax.errors.*;

public class SubroutineCallCannotBeFunctionCall extends ParserError {
	//
	protected int proposedNumber;
	//
	public SubroutineCallCannotBeFunctionCall(int n, int position) {
		super(position);
		proposedNumber= n;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(" + Integer.toString(proposedNumber) + ")";
	}
}
