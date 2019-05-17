// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.worlds.*;

import java.io.Serializable;

public abstract class SuspendedCall implements Serializable {
	//
	public boolean isReleased= false;
	//
	private static final long serialVersionUID= 0x36881E8F9C8C5F08L; // 3929424277036359432L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.run","SuspendedCall");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public SuspendedCall() {
	}
	//
	abstract public Continuation formContinuation(ActiveWorld currentProcess, ChoisePoint iX, Continuation c0);
}
