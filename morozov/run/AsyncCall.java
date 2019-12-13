// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.terms.*;
import morozov.terms.signals.*;
import morozov.worlds.*;

public class AsyncCall {
	//
	protected long domainSignatureNumber;
	protected Term target;
	protected AbstractWorld reserveTarget;
	protected GlobalWorldIdentifier globalWorldIdentifier= null;
	protected boolean isControlCall;
	protected boolean useBuffer;
	protected Term[] arguments;
	protected boolean argumentsAreCircumscribed= false;
	//
	///////////////////////////////////////////////////////////////
	//
	public AsyncCall(long aSignatureNumber, AbstractInternalWorld world, boolean type, boolean buffer, Term[] list, boolean argumentMode) {
		domainSignatureNumber= aSignatureNumber;
		target= world;
		reserveTarget= world;
		isControlCall= type;
		useBuffer= buffer;
		arguments= list;
		argumentsAreCircumscribed= argumentMode;
	}
	public AsyncCall(long aSignatureNumber, Term dataItem, AbstractWorld world, boolean type, boolean buffer, Term[] list, boolean argumentMode) {
		domainSignatureNumber= aSignatureNumber;
		target= dataItem;
		reserveTarget= world;
		isControlCall= type;
		useBuffer= buffer;
		arguments= list;
		argumentsAreCircumscribed= argumentMode;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public long getDomainSignatureNumber() {
		return domainSignatureNumber;
	}
	public Term getTarget() {
		return target;
	}
	public AbstractWorld getReserveTarget() {
		return reserveTarget;
	}
	public GlobalWorldIdentifier getGlobalWorldIdentifier() {
		return globalWorldIdentifier;
	}
	public boolean isControlCall() {
		return isControlCall;
	}
	public boolean useBuffer() {
		return useBuffer;
	}
	public Term[] getArguments() {
		return arguments;
	}
	public boolean argumentsAreCircumscribed() {
		return argumentsAreCircumscribed;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean isTheVerifyCommand() {
		return false;
	}
	public boolean isTheResetCommand() {
		return false;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public AbstractWorld computeHashCodeAndCopyArguments(ChoisePoint iX) throws TermIsDummyWorld, TermIsUnboundVariable {
		target= target.dereferenceValue(iX);
		AbstractWorld receiver;
		try {
			globalWorldIdentifier= target.getGlobalWorldIdentifier(iX);
			receiver= (AbstractWorld)target;
		} catch (TermIsNotAWorld e) {
			globalWorldIdentifier= reserveTarget.getGlobalWorldIdentifier();
			receiver= reserveTarget;
		};
		prepareArguments(iX);
		return receiver;
	}
	//
	protected void prepareArguments(ChoisePoint iX) {
		if (!argumentsAreCircumscribed) {
			for (int i= 0; i < arguments.length; i++) {
				arguments[i]= arguments[i].copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
			};
			argumentsAreCircumscribed= true;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean equals(Object o) {
		if (o==null) {
			return false;
		} else {
			if ( !(o instanceof AsyncCall) ) {
				return false;
			} else {
				AsyncCall i= (AsyncCall) o;
				if (	i.domainSignatureNumber == domainSignatureNumber &&
					i.globalWorldIdentifier == globalWorldIdentifier &&
					i.isControlCall == isControlCall &&
					i.useBuffer == useBuffer &&
					i.arguments.length == arguments.length
						) {
					return true;
				} else {
					return false;
				}
			}
		}
	}
	//
	@Override
	public int hashCode() {
		int code=
			(int)domainSignatureNumber +
			globalWorldIdentifier.hashCode() +
			arguments.length;
		if (useBuffer) {
			code= -code;
		};
		return code;
	}
}
