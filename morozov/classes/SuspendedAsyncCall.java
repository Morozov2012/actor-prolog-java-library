// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.classes;

import morozov.run.*;
import morozov.terms.*;

public class SuspendedAsyncCall extends SuspendedCall {
	public long domainSignatureNumber;
	public Term target;
	public AbstractWorld currentWorld;
	public boolean isControlCall;
	public boolean sendImmediately;
	public boolean useBuffer;
	// public String procName;
	public Term[] arguments;
	public SuspendedAsyncCall(boolean isQuick, long aSignatureNumber, Term world, AbstractWorld aCW, boolean type, boolean buffer, Term[] list) {
		sendImmediately= isQuick;
		domainSignatureNumber= aSignatureNumber;
		target= world;
		currentWorld= aCW;
		isControlCall= type;
		useBuffer= buffer;
		// procName= name;
		arguments= list;
	}
	public Continuation formContinuation(ActiveWorld currentProcess, ChoisePoint iX, Continuation c0) {
		// System.out.printf("SuspendedAsyncCall(%s); isReleased= %s\n",this,isReleased);
		if (!isReleased) {
			Term targetWorld= target.dereferenceValue(iX);
			if (!targetWorld.thisIsFreeVariable()) {
				currentProcess.registerBinding(new SuspendedCallState(this));
				isReleased= true;
				return new Continuation(c0) {
						public void execute(ChoisePoint iX) throws Backtracking {
							// System.out.printf("\nexecute(%s); sendImmediately= %s\n",this,sendImmediately);
							if (sendImmediately) {
								currentWorld.sendAsyncCall(iX,domainSignatureNumber,target,currentWorld,isControlCall,useBuffer,arguments);
							} else {
								currentWorld.pushAsyncCall(iX,domainSignatureNumber,target,currentWorld,isControlCall,useBuffer,arguments);
							};
							c0.execute(iX);
						}
					};
			} else {
				return c0;
			}
		} else {
			return c0;
		}
	}
}
