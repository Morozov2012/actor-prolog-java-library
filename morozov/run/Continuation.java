// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.run;

public abstract class Continuation {
	//
	protected static Continuation dummy= new DummyContinuation();
	protected Continuation c0= dummy;
	//
	public Continuation(Continuation aC) {
		c0= aC;
	}
	public Continuation() {
	}
	//
	abstract public void execute(ChoisePoint iX) throws Backtracking;
	public boolean isPhaseTermination() {
			return false;
		}
	//
	public boolean containsNode(Continuation c) {
		if (this==c) {
			return true;
		} else if (c0 != null) {
			return c0.containsNode(c);
		} else {
			return false;
		}
	}
	//
	public String toString() {
		if (c0 != null) {
			return super.toString() + "->" + c0.toString();
		} else {
			return super.toString() + "->null";
		}
	}
}
