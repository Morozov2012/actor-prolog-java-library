// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.errors;

import morozov.syntax.errors.*;

public class FunctionVariableIsAlreadyAssigned extends ParserError {
	//
	protected int proposedNumber;
	protected int presentNumber;
	//
	public FunctionVariableIsAlreadyAssigned(int n, int o, int position) {
		super(position);
		proposedNumber= n;
		presentNumber= o;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(" + Integer.toString(proposedNumber) + "," + Integer.toString(presentNumber) + ")";
	}
}
