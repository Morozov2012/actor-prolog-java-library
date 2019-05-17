// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.terms.*;

public class ProcessedErrorExit extends ErrorExit {
	//
	public ErrorExit processedException;
	public Continuation continuation;
	//
	public ProcessedErrorExit(ErrorExit e, Continuation c) {
		super(e);
		processedException= e;
		continuation= c;
	}
	//
	public boolean isNormalTerminationOfTheProgram() {
		return processedException.isNormalTerminationOfTheProgram();
	}
	//
	public Term createTerm() {
		return processedException.createTerm();
	}
	public String toString() {
		return "ProcessedErrorExit(" + processedException.toString() + ")";
	}
}
