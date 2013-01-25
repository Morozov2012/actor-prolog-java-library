// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.terms.*;

public class FailureTermination extends Continuation {
	public void execute(ChoisePoint iX) throws Backtracking {
		throw new Backtracking();
	}
	public String toString() {
		return "FailureTermination;";
	}
}
