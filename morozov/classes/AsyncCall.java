// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.classes;

import morozov.run.*;
import morozov.terms.*;

public class AsyncCall {
	public long domainSignatureNumber;
	public Term target;
	// public AbstractWorld currentWorld;
	public AbstractWorld initialTarget;
	public AbstractWorld targetWorld= null;
	public boolean isControlCall;
	public boolean useBuffer;
	public Term[] arguments;
	// public AsyncCallSender sender= null;
	protected boolean argumentsAreCircumscribed= false;
	//
	public AsyncCall(long aSignatureNumber, AbstractWorld world, boolean type, boolean buffer, Term[] list, boolean argumentMode) {
		domainSignatureNumber= aSignatureNumber;
		target= world;
		initialTarget= world;
		isControlCall= type;
		useBuffer= buffer;
		arguments= list;
		argumentsAreCircumscribed= argumentMode;
	}
	public AsyncCall(long aSignatureNumber, Term dataItem, AbstractWorld world, boolean type, boolean buffer, Term[] list, boolean argumentMode) {
		domainSignatureNumber= aSignatureNumber;
		target= dataItem;
		initialTarget= world;
		isControlCall= type;
		useBuffer= buffer;
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
		target= target.internalWorldOrTerm(null);
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
	// public void sendConfirmation() {
	//	if (sender != null) {
	//		sender.confirmAsyncCall(this);
	//	}
	// }
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
