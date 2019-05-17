// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.worlds;

import morozov.run.*;
import morozov.terms.*;

public class TemporaryActor extends ActorNumber {
	//
	private static final long serialVersionUID= 0x1A97BC07CAA54490L; // 1916206908120056976L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.worlds","TemporaryActor");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean isNumberOfTemporaryActor() {
		return true;
	}
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		if (t != this)
			throw Backtracking.instance;
	}
}
