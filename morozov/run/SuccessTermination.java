// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.terms.*;

public class SuccessTermination extends Continuation {
	public void execute(ChoisePoint iX) throws Backtracking {}
	public String toString() {
		return "SuccessTermination;";
	}
}
