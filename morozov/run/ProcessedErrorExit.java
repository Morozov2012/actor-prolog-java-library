// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.terms.*;

public class ProcessedErrorExit extends ErrorExit {
	//
	protected ErrorExit processedException;
	protected Continuation continuation;
	//
	public ProcessedErrorExit(ErrorExit e, Continuation c) {
		super(e);
		processedException= e;
		continuation= c;
	}
	//
	public ErrorExit getProcessedException() {
		return processedException;
	};
	public Continuation getContinuation() {
		return continuation;
	}
	//
	@Override
	public boolean isNormalTerminationOfTheProgram() {
		return processedException.isNormalTerminationOfTheProgram();
	}
	//
	@Override
	public Term createTerm() {
		return processedException.createTerm();
	}
	@Override
	public String toString() {
		return "ProcessedErrorExit(" + processedException.toString() + ")";
	}
}
