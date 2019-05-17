// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.terms.*;

public class LowLevelErrorExit extends ErrorExit {
	//
	private Throwable exception;
	//
	public LowLevelErrorExit(ChoisePoint cp, Throwable e) {
		super(e);
		choisePoint= cp;
		exception= e;
	}
	//
	public Term createTerm() {
		// return new PrologString("LowLevelErrorExit("+exception.toString()+")");
		return new PrologString(exception.toString());
	}
	public String toString() {
		return exception.toString();
	}
}
