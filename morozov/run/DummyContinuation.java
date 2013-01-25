// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.terms.*;

public class DummyContinuation extends Continuation {
	public void execute(ChoisePoint iX) throws Backtracking {
	}
	public String toString() {
		return "DummyContinuation";
	}
}
