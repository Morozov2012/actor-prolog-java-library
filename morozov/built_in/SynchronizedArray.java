// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.converters.*;
import morozov.system.indices.*;
import morozov.system.indices.errors.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class SynchronizedArray extends LambdaArray {
	//
	protected Boolean implementProtectingPorts= false;
	protected Boolean implementSuspendingPorts= false;
	//
	protected Map<ArrayIndices,SlotVariable> synchronizedVolume= Collections.synchronizedMap(new HashMap<ArrayIndices,SlotVariable>());
	protected AtomicBoolean contentIsAccepted= new AtomicBoolean(false);
	protected AtomicBoolean isSelfContained= new AtomicBoolean(false);
	protected AtomicBoolean isClient= new AtomicBoolean(false);
	protected AtomicBoolean isServer= new AtomicBoolean(false);
	//
	///////////////////////////////////////////////////////////////
	//
	public SynchronizedArray() {
	}
	public SynchronizedArray(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term getBuiltInSlot_E_content();
	abstract public Term getBuiltInSlot_E_implement_protecting_ports();
	abstract public Term getBuiltInSlot_E_implement_suspending_ports();
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set implementProtectingPorts
	//
	public void setImplementProtectingPorts1s(ChoisePoint iX, Term a1) {
		setImplementProtectingPorts(YesNoConverters.termYesNo2Boolean(a1,iX));
	}
	public void setImplementProtectingPorts(boolean value) {
		implementProtectingPorts= value;
	}
	public void getImplementProtectingPorts0ff(ChoisePoint iX, PrologVariable result) {
		boolean value= getImplementProtectingPorts(iX);
		result.setNonBacktrackableValue(YesNoConverters.boolean2TermYesNo(value));
	}
	public void getImplementProtectingPorts0fs(ChoisePoint iX) {
	}
	public boolean getImplementProtectingPorts(ChoisePoint iX) {
		if (implementProtectingPorts != null) {
			return implementProtectingPorts;
		} else {
			Term value= getBuiltInSlot_E_implement_protecting_ports();
			return YesNoConverters.termYesNo2Boolean(value,iX);
		}
	}
	//
	// get/set implementSuspendingPorts
	//
	public void setImplementSuspendingPorts1s(ChoisePoint iX, Term a1) {
		setImplementSuspendingPorts(YesNoConverters.termYesNo2Boolean(a1,iX));
	}
	public void setImplementSuspendingPorts(boolean value) {
		implementSuspendingPorts= value;
	}
	public void getImplementSuspendingPorts0ff(ChoisePoint iX, PrologVariable result) {
		boolean value= getImplementSuspendingPorts(iX);
		result.setNonBacktrackableValue(YesNoConverters.boolean2TermYesNo(value));
	}
	public void getImplementSuspendingPorts0fs(ChoisePoint iX) {
	}
	public boolean getImplementSuspendingPorts(ChoisePoint iX) {
		if (implementSuspendingPorts != null) {
			return implementSuspendingPorts;
		} else {
			Term value= getBuiltInSlot_E_implement_suspending_ports();
			return YesNoConverters.termYesNo2Boolean(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected Term accessArrayElement(ArrayIndices arrayIndices, ChoisePoint iX) throws Backtracking {
		boolean isProtectingPort= getImplementProtectingPorts(iX);
		boolean isSuspendingPort= getImplementSuspendingPorts(iX);
		synchronized (this) {
			SlotVariable variable= synchronizedVolume.get(arrayIndices);
			if (variable==null) {
				Term content= getBuiltInSlot_E_content();
				content= content.dereferenceValue(iX);
				if (content instanceof SynchronizedArray) {
					SynchronizedArray externalWorld= (SynchronizedArray)content;
					checkArrayUsageMode(externalWorld);
					variable= externalWorld.accessSlotVariable(arrayIndices,this,iX);
					synchronizedVolume.put(arrayIndices,variable);
					variable.registerVariables(currentProcess,isSuspendingPort,isProtectingPort);
				} else {
					throw new ContentIsNotAnInstanceOfSynchronizedArray();
				}
			} else {
				checkArrayUsageMode(arrayIndices,iX);
				variable.registerVariables(currentProcess,isSuspendingPort,isProtectingPort);
			};
			return variable;
		}
	}
	public SlotVariable accessSlotVariable(ArrayIndices arrayIndices, SynchronizedArray client, ChoisePoint iX) throws Backtracking {
		synchronized (this) {
			if (client != this) {
				declareExternalServer();
			} else {
				declarePrivateServer();
			};
			SlotVariable variable= synchronizedVolume.get(arrayIndices);
			if (variable==null) {
				variable= new SlotVariable();
				synchronizedVolume.put(arrayIndices,variable);
			};
			return variable;
		}
	}
	protected void checkArrayUsageMode(ArrayIndices arrayIndices, ChoisePoint iX) {
		if (contentIsAccepted.get()) {
			if (!isSelfContained.get()) {
				declareClient();
			} else {
				declarePrivateServer();
			}
		} else {
			Term content= getBuiltInSlot_E_content();
			content= content.dereferenceValue(iX);
			if (content instanceof SynchronizedArray) {
				SynchronizedArray externalWorld= (SynchronizedArray)content;
				checkArrayUsageMode(externalWorld);
				try {
					SlotVariable variable= externalWorld.accessSlotVariable(arrayIndices,this,iX);
					synchronizedVolume.put(arrayIndices,variable);
				} catch (Backtracking b) {
				}
			} else {
				throw new ContentIsNotAnInstanceOfSynchronizedArray();
			}
		}
	}
	protected void checkArrayUsageMode(SynchronizedArray externalWorld) {
		if (externalWorld != this) {
			isSelfContained.set(false);
			declareClient();
		} else {
			isSelfContained.set(true);
			declarePrivateServer();
		};
		contentIsAccepted.set(true);
	}
	public void declareClient() {
		if (isServer.get()) {
			throw new TheWorldIsAlreadyAPrimaryArray();
		} else {
			isClient.set(true);
		}
	}
	public void declareExternalServer() {
		if (isClient.get()) {
			throw new TheContentIsAlreadyASecondaryArray();
		} else {
			isServer.set(true);
		}
	}
	public void declarePrivateServer() {
		if (isClient.get()) {
			throw new TheWorldIsAlreadyASecondaryArray();
		} else {
			isServer.set(true);
		}
	}
	//
	public void isPlainPort1ms(ChoisePoint iX, Term... givenIndices) throws Backtracking {
		ArrayIndices arrayIndices= collectArrayIndices((Term[])givenIndices,iX);
		SlotVariable variable= synchronizedVolume.get(arrayIndices);
		if (variable != null) {
			SlotVariableValue slotValue= variable.get(currentProcess);
			if (slotValue != null) {
				if (!slotValue.isPlainPort()) {
					throw Backtracking.instance;
				}
			} else {
				throw Backtracking.instance;
			}
		} else {
			throw Backtracking.instance;
		}
	}
	//
	public void isProtectingPort1ms(ChoisePoint iX, Term... givenIndices) throws Backtracking {
		ArrayIndices arrayIndices= collectArrayIndices((Term[])givenIndices,iX);
		SlotVariable variable= synchronizedVolume.get(arrayIndices);
		if (variable != null) {
			SlotVariableValue slotValue= variable.get(currentProcess);
			if (slotValue != null) {
				if (!slotValue.isProtectingPort()) {
					throw Backtracking.instance;
				}
			} else {
				throw Backtracking.instance;
			}
		} else {
			throw Backtracking.instance;
		}
	}
	//
	public void isSuspendingPort1ms(ChoisePoint iX, Term... givenIndices) throws Backtracking {
		ArrayIndices arrayIndices= collectArrayIndices((Term[])givenIndices,iX);
		SlotVariable variable= synchronizedVolume.get(arrayIndices);
		if (variable != null) {
			SlotVariableValue slotValue= variable.get(currentProcess);
			if (slotValue != null) {
				if (!slotValue.isSuspendingPort()) {
					throw Backtracking.instance;
				}
			} else {
				throw Backtracking.instance;
			}
		} else {
			throw Backtracking.instance;
		}
	}
	//
	public void definePlainPort1ms(ChoisePoint iX, Term... givenIndices) {
		ArrayIndices arrayIndices= collectArrayIndices((Term[])givenIndices,iX);
		synchronized (this) {
			SlotVariable variable= synchronizedVolume.get(arrayIndices);
			if (variable==null) {
				Term content= getBuiltInSlot_E_content();
				content= content.dereferenceValue(iX);
				if (content instanceof SynchronizedArray) {
					SynchronizedArray externalWorld= (SynchronizedArray)content;
					checkArrayUsageMode(externalWorld);
					try {
						variable= externalWorld.accessSlotVariable(arrayIndices,this,iX);
						synchronizedVolume.put(arrayIndices,variable);
						variable.registerVariables(currentProcess,true,false,false);
					} catch (Backtracking b) {
					}
				} else {
					throw new ContentIsNotAnInstanceOfSynchronizedArray();
				}
			} else {
				checkArrayUsageMode(arrayIndices,iX);
				variable.registerVariables(currentProcess,true,false,false);
			}
		}
	}
	//
	public void defineProtectingPort1ms(ChoisePoint iX, Term... givenIndices) {
		ArrayIndices arrayIndices= collectArrayIndices((Term[])givenIndices,iX);
		synchronized (this) {
			SlotVariable variable= synchronizedVolume.get(arrayIndices);
			if (variable==null) {
				Term content= getBuiltInSlot_E_content();
				content= content.dereferenceValue(iX);
				if (content instanceof SynchronizedArray) {
					SynchronizedArray externalWorld= (SynchronizedArray)content;
					checkArrayUsageMode(externalWorld);
					try {
						variable= externalWorld.accessSlotVariable(arrayIndices,this,iX);
						synchronizedVolume.put(arrayIndices,variable);
						variable.registerVariables(currentProcess,true,false,true);
					} catch (Backtracking b) {
					}
				} else {
					throw new ContentIsNotAnInstanceOfSynchronizedArray();
				}
			} else {
				checkArrayUsageMode(arrayIndices,iX);
				variable.registerVariables(currentProcess,true,false,true);
			}
		}
	}
	//
	public void defineSuspendingPort1ms(ChoisePoint iX, Term... givenIndices) {
		ArrayIndices arrayIndices= collectArrayIndices((Term[])givenIndices,iX);
		synchronized (this) {
			SlotVariable variable= synchronizedVolume.get(arrayIndices);
			if (variable==null) {
				Term content= getBuiltInSlot_E_content();
				content= content.dereferenceValue(iX);
				if (content instanceof SynchronizedArray) {
					SynchronizedArray externalWorld= (SynchronizedArray)content;
					checkArrayUsageMode(externalWorld);
					try {
						variable= externalWorld.accessSlotVariable(arrayIndices,this,iX);
						synchronizedVolume.put(arrayIndices,variable);
						variable.registerVariables(currentProcess,true,true,false);
					} catch (Backtracking b) {
					}
				} else {
					throw new ContentIsNotAnInstanceOfSynchronizedArray();
				}
			} else {
				checkArrayUsageMode(arrayIndices,iX);
				variable.registerVariables(currentProcess,true,true,false);
			}
		}
	}
}
