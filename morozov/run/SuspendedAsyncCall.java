// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.terms.*;
import morozov.worlds.*;

public class SuspendedAsyncCall extends SuspendedCall {
	//
	protected long domainSignatureNumber;
	protected Term target;
	protected AbstractInternalWorld currentWorld;
	protected boolean isControlCall;
	protected boolean sendImmediately;
	protected boolean useBuffer;
	protected Term[] arguments;
	//
	private static final long serialVersionUID= 0xF37E951DB7B69E6CL; // -901118920558731668L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.run","SuspendedAsyncCall");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public SuspendedAsyncCall(boolean isQuick, long aSignatureNumber, Term world, AbstractInternalWorld aCW, boolean type, boolean buffer, Term[] list) {
		sendImmediately= isQuick;
		domainSignatureNumber= aSignatureNumber;
		target= world;
		currentWorld= aCW;
		isControlCall= type;
		useBuffer= buffer;
		arguments= list;
	}
	//
	@Override
	public Continuation formContinuation(ActiveWorld currentProcess, ChoisePoint iX, Continuation c0) {
		if (!isReleased) {
			Term targetWorld= target.dereferenceValue(iX);
			if (!targetWorld.thisIsFreeVariable()) {
				currentProcess.registerBinding(new SuspendedCallState(this));
				isReleased= true;
				return new Continuation(c0) {
						@Override
						public void execute(ChoisePoint iX) throws Backtracking {
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
