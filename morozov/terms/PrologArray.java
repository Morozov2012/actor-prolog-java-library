// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.classes.*;

public abstract class PrologArray extends Term {
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		if (t != this)
			throw new Backtracking();
	}
	abstract public AbstractWorld createWorld();
	abstract public void initiateWorld(AbstractWorld world);
}
