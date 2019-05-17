// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.errors;

import morozov.syntax.errors.*;

public class FunctionVariableIsAlreadyAssigned extends ParserError {
	//
	public int proposedNumber;
	public int presentNumber;
	//
	public FunctionVariableIsAlreadyAssigned(int n, int o, int position) {
		super(position);
		proposedNumber= n;
		presentNumber= o;
	}
	public String toString() {
		return this.getClass().toString() + "(" + Integer.toString(proposedNumber) + "," + Integer.toString(presentNumber) + ")";
	}
}
