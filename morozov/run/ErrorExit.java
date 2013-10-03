// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.terms.*;

public abstract class ErrorExit extends RuntimeException {
	protected ChoisePoint choisePoint;
	public ErrorExit() {
	}
	public ErrorExit(Throwable cause) {
		super(cause);
	}
	public boolean isNormalTerminationOfTheProgram() {
		return false;
	}
	abstract public Term createTerm();
}
