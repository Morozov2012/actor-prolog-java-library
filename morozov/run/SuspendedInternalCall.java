// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.run;

import target.*;

import morozov.terms.*;
import morozov.worlds.*;

public class SuspendedInternalCall extends SuspendedCall {
	//
	public Term target;
	public AbstractInternalWorld currentWorld;
	public long domainSignatureNumber;
	public Term[] arguments;
	//
	private static final long serialVersionUID= 0xADFB8C2C76565377L; // -5909975963401825417L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.run","SuspendedInternalCall");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public SuspendedInternalCall(Term world, AbstractInternalWorld aCW, long number, Term[] list) {
		target= world;
		currentWorld= aCW;
		domainSignatureNumber= number;
		arguments= list;
	}
	public Continuation formContinuation(ActiveWorld currentProcess, ChoisePoint iX, Continuation c0) {
		if (!isReleased) {
			Term targetWorld= target.dereferenceValue(iX);
			if (!targetWorld.thisIsFreeVariable()) {
				currentProcess.registerBinding(new SuspendedCallState(this));
				isReleased= true;
				return new DomainSwitch(
					c0,
					domainSignatureNumber,
					targetWorld,
					currentWorld, // AbstractWorld.this,
					arguments);
			} else {
				return c0;
			}
		} else {
			return c0;
		}
	}
}
