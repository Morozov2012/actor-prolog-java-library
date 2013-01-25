// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

public class TemporaryActor extends ActorNumber {
	public boolean isTemporary() {
		return true;
	}
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		if (t != this)
			throw new Backtracking();
	}
}
