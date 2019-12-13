// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.terms.*;

public class LowLevelErrorExit extends ErrorExit {
	//
	protected Throwable exception;
	//
	public LowLevelErrorExit(ChoisePoint cp, Throwable e) {
		super(e);
		choisePoint= cp;
		exception= e;
	}
	//
	@Override
	public Term createTerm() {
		return new PrologString(exception.toString());
	}
	@Override
	public String toString() {
		return exception.toString();
	}
}
