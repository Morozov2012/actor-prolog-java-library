// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.worlds;

import morozov.run.*;
import morozov.terms.*;

public class TemporaryActor extends ActorNumber {
	public boolean isNumberOfTemporaryActor() {
		return true;
	}
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		if (t != this)
			throw Backtracking.instance;
	}
}
