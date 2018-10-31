// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.run.*;
import morozov.worlds.*;

public abstract class PrologArray extends Term {
	//
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		if (t != this)
			throw Backtracking.instance;
	}
	//
	abstract public AbstractWorld createWorld();
	abstract public void initiateWorld(AbstractWorld world);
}
