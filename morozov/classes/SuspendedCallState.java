// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.classes;

import morozov.classes.errors.*;
import morozov.run.*;
import morozov.terms.*;

public class SuspendedCallState extends Term {
	private SuspendedCall call;
	public SuspendedCallState(SuspendedCall value) {
		call= value;
	}
	public void clear() {
		call.isReleased= false;
	}
	// General "Unify With" function
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		throw new SpecialTermCannotBeUnified();
	}
	public String toString() {
		return "SuspendedCallState";
	}
}
