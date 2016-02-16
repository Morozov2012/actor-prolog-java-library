// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.worlds.*;

public abstract class SuspendedCall {
	public boolean isReleased= false;
	public SuspendedCall() {
	}
	public abstract Continuation formContinuation(ActiveWorld currentProcess, ChoisePoint iX, Continuation c0);
}
