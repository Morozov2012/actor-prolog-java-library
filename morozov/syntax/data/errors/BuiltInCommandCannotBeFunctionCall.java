// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.errors;

import morozov.syntax.errors.*;

public class BuiltInCommandCannotBeFunctionCall extends ParserError {
	//
	public int proposedNumber;
	//
	public BuiltInCommandCannotBeFunctionCall(int n, int position) {
		super(position);
		proposedNumber= n;
	}
	public String toString() {
		return this.getClass().toString() + "(" + Integer.toString(proposedNumber) + ")";
	}
}
