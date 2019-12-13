// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.run;

public class FailureTermination extends Continuation {
	//
	@Override
	public void execute(ChoisePoint iX) throws Backtracking {
		throw Backtracking.instance;
	}
	@Override
	public String toString() {
		return "FailureTermination;";
	}
}
