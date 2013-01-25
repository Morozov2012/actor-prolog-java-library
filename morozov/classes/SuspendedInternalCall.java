// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.classes;

import target.*;

import morozov.run.*;
import morozov.terms.*;

public class SuspendedInternalCall extends SuspendedCall {
	public Term target;
	public AbstractWorld currentWorld;
	public long domainSignatureNumber;
	public Term[] arguments;
	public SuspendedInternalCall(Term world, AbstractWorld aCW, long number, Term[] list) {
		target= world;
		currentWorld= aCW;
		domainSignatureNumber= number;
		arguments= list;
	}
	public Continuation formContinuation(ActiveWorld currentProcess, ChoisePoint iX, Continuation c0) {
		if (!isReleased) {
			Term targetWorld= target.dereferenceValue(iX);
			if (!targetWorld.thisIsFreeVariable()) {
				// System.out.printf("AbstractProcess::currentProcess=%s\n",currentProcess);
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
