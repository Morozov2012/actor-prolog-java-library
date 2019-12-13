// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.run;

public class DummyContinuation extends Continuation {
	//
	@Override
	public void execute(ChoisePoint iX) throws Backtracking {
	}
	@Override
	public String toString() {
		return "DummyContinuation";
	}
}
