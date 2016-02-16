// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.run;

public class FailureTermination extends Continuation {
	public void execute(ChoisePoint iX) throws Backtracking {
		throw Backtracking.instance;
	}
	public String toString() {
		return "FailureTermination;";
	}
}
