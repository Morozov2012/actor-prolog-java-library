// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.worlds.*;

public abstract class SuspendedCall {
	public boolean isReleased= false;
	public SuspendedCall() {
	}
	abstract public Continuation formContinuation(ActiveWorld currentProcess, ChoisePoint iX, Continuation c0);
}
