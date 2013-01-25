// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.classes;

import morozov.terms.*;

public class AsyncCall {
	public long domainSignatureNumber;
	// public Term target;
	// public AbstractWorld currentWorld;
	public AbstractWorld initialTarget;
	public AbstractWorld targetWorld= null;
	public boolean isControlCall;
	public boolean useBuffer;
	public Term[] arguments;
	protected boolean argumentsAreCircumscribed= false;
	//
	public AsyncCall(long aSignatureNumber, AbstractWorld world, boolean type, boolean buffer, Term[] list, boolean argumentMode) {
		domainSignatureNumber= aSignatureNumber;
		// target= world;
		// currentWorld= aCW;
		initialTarget= world;
		// if (targetWorld==null) {
		//	System.out.printf("targetWorld=%s\n",targetWorld);
		//	throw new RuntimeException();
		// };
		isControlCall= type;
		useBuffer= buffer;
		// procName= name;
		arguments= list;
		argumentsAreCircumscribed= argumentMode;
	}
	//
	public boolean isTheVerifyCommand() {
		return false;
	}
	public boolean isTheResetCommand() {
		return false;
	}
	//
	public void computeTargetWorld() {
		targetWorld= initialTarget.internalWorld(null);
	}
	//
	public void prepareArguments(ChoisePoint iX) {
		if (!argumentsAreCircumscribed) {
			for (int i= 0; i < arguments.length; i++) {
				arguments[i]= arguments[i].copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
			};
			argumentsAreCircumscribed= true;
		}
	}
	//
	public boolean equals(Object o) {
		if (o==null) {
			return false;
		} else {
			if ( !(o instanceof AsyncCall) ) {
				return false;
			} else {
				AsyncCall i= (AsyncCall) o;
				if (	i.domainSignatureNumber==domainSignatureNumber &&
					i.isControlCall==isControlCall &&
					i.targetWorld==targetWorld &&
					i.useBuffer==useBuffer &&
					i.arguments.length == arguments.length
					// TermComparator.compareTwoTermArrays(i.arguments,arguments)==0
						) {
					return true;
				} else {
					return false;
				}
			}
		}
	}
	//
	public int hashCode() {
		// int sum= 0;
		// for (int i= 0; i < arguments.length; i++) {
		//	sum+= arguments[i].hashCode();
		// };
		// if (isControlCall) {
		//	sum++;
		// };
		int code=
			(int)domainSignatureNumber +
			targetWorld.hashCode() +
			arguments.length;
		if (useBuffer) {
			code= -code;
		};
		return code;
	}
}
