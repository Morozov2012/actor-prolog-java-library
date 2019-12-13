// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.run.*;
import morozov.worlds.*;

public abstract class PrologArray extends Term {
	//
	private static final long serialVersionUID= 0x7BFBEB5B02D1AC9FL; // 8933993061941357727L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.terms","PrologArray");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		if (t != this)
			throw Backtracking.instance;
	}
	//
	abstract public AbstractWorld createWorld();
	abstract public void initiateWorld(AbstractWorld world);
}
