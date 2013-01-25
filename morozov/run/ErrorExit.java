// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.terms.*;

public abstract class ErrorExit extends RuntimeException {
	public ErrorExit() {
	}
	public ErrorExit(Throwable cause) {
		super(cause);
	}
	protected ChoisePoint choisePoint;
	abstract public Term createTerm();
}
