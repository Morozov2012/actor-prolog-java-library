// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.terms.*;
import morozov.worlds.errors.*;

public class SuspendedCallState extends Term {
	//
	protected SuspendedCall call;
	//
	private static final long serialVersionUID= 0x6D84C2E767342917L; // 7891646746184657175L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.run","SuspendedCallState");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public SuspendedCallState(SuspendedCall value) {
		call= value;
	}
	//
	@Override
	public void clear() {
		call.isReleased= false;
	}
	// General "Unify With" function:
	@Override
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		throw new SpecialTermCannotBeUnified();
	}
	@Override
	public String toString() {
		return "SuspendedCallState";
	}
}
