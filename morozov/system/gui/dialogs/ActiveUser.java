// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs;

import morozov.built_in.*;
import morozov.classes.*;
import morozov.run.*;
import morozov.terms.*;

import java.util.*;

class AnAttemptToExtractWorldFromActiveUser extends RuntimeException {}
class AnAttemptToExtractSlotFromActiveUser extends RuntimeException {}
class AnAttemptToExtractPackageCodeFromActiveUser extends RuntimeException {}
class AnAttemptToExtractClassHierarchyFromActiveUser extends RuntimeException {}
class AnAttemptToExtractInterfaceHierarchyFromActiveUser extends RuntimeException {}
class AnAttemptToExtractDomainSignatureFromActiveUser extends RuntimeException {}

public class ActiveUser extends ActiveWorld {
	//
	protected AbstractDialog targetDialog= null;
	protected HashMap<SlotVariable,DialogEntry> slotMap= new HashMap<SlotVariable,DialogEntry>();
	private HashSet<DialogEntry> userInterfaceMessages= new HashSet<DialogEntry>();
	//
	public ActiveUser() {
		super(true,true);
		currentProcess= this;
	}
	//
	public void initiate(ActiveWorld dummy, StaticContext context) {
		staticContext= context;
	}
	//
	public void actualize(ChoisePoint iX) {
	}
	//
	public void startProcesses() {
		start();
	}
	public void stopProcesses() {
		stop();
	}
	//
	public void receiveTimerMessage(AbstractWorld target) {
	}
	public void cancelTimerMessage(AbstractWorld target) {
	}
	public Continuation collectSuspendedCalls(Continuation c0, ChoisePoint iX) {
		return c0;
	}
	// public void sendResidentRequest(AbstractWorld target, Resident resident, long domainSignature, Term[] arguments, ChoisePoint iX) {
	// }
	public void sendResidentRequest(AbstractWorld target, Resident resident, long domainSignature, Term[] arguments, boolean sortAndReduceResultList, ChoisePoint iX) {
	}
	public void withdrawRequest(AbstractWorld target, Resident resident) {
	}
	// Receiving User Interface Messages
	public void receiveUserInterfaceMessage(DialogEntry entry) {
		synchronized(userInterfaceMessages) {
			userInterfaceMessages.add(entry);
		};
		wakeUp();
	}
	//
	// ACCEPT MESSAGES
	// Process Flow Messages
	protected void processFlowMessages(ChoisePoint iX) {
		Iterator<SlotVariable> slotVariablesIterator= slotVariables.iterator();
		while (slotVariablesIterator.hasNext()) {
			SlotVariable slotVariable= slotVariablesIterator.next();
			try {
				actualizeValues(iX,slotVariable.getValue(iX));
				DialogEntry item= slotMap.get(slotVariable);
				item.putValue(slotVariable.copyValue(rootCP,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES),rootCP);
			} catch (Backtracking b) {
			};
		}
	}
	//
	// Process One Direct Message
	public void acceptDirectMessage() {
	}
	//
	protected void acceptTimerMessage() {
		DialogEntry[] messagesToBeProcessed= new DialogEntry[0];
		synchronized(userInterfaceMessages) {
			if (userInterfaceMessages.size() > 0) {
				messagesToBeProcessed= userInterfaceMessages.toArray(messagesToBeProcessed);
				userInterfaceMessages.clear();
			}
		};
		if (messagesToBeProcessed.length > 0) {
			for (int n=0; n < messagesToBeProcessed.length; n++) {
				targetDialog.transmitEntryValue(messagesToBeProcessed[n],rootCP);
			};
			targetDialog.prepareAndSendFlowMessages();
		}
	}
	//
	protected void sendStateOfProcess() {
	}
	//
	public void prepareFlowMessage(SlotVariable output, Term resultValue) {
		try {
			output.unifyWith(resultValue,rootCP);
		} catch (Backtracking b) {
		}
	}
	public void freeTrail() {
		rootCP.freeTrail();
	}
	//
	public void extractWorlds(LinkedHashSet<AbstractWorld> list) {
	}
	//
	public void extractWorlds(AbstractProcess process, LinkedHashSet<AbstractWorld> list) {
	}
	final public long getPackageCode() {
		throw new AnAttemptToExtractPackageCodeFromActiveUser();
	}
	final public long[] getClassHierarchy() {
		throw new AnAttemptToExtractClassHierarchyFromActiveUser();
	}
	final public long[] getInterfaceHierarchy() {
		throw new AnAttemptToExtractInterfaceHierarchyFromActiveUser();
	}
	// public void staticBinding(String name, ChoisePoint iX, Term[] arguments, Continuation competion) {
	//	throw new AnAttemptToExtractWorldFromActiveUser();
	// }
	public Term getSlotByName(String name) {
		throw new AnAttemptToExtractSlotFromActiveUser();
	}
	public long entry_s_Goal_0() {
		throw new AnAttemptToExtractDomainSignatureFromActiveUser();
	}
	public long entry_s_Alarm_1_i() {
		throw new AnAttemptToExtractDomainSignatureFromActiveUser();
	}
	public void registerPortsAndRecoverPortValues(AbstractDialog dialog, Dialog targetWorld, DialogEntry[] userDefinedSlots, DialogEntry[] systemSlots) {
		phaseInitiation();
		targetDialog= dialog;
		initiateSlotValues(targetWorld,userDefinedSlots);
		initiateSlotValues(targetWorld,systemSlots);
		fixSlotVariables(true);
		sendActualFlowMessages();
		storeSlotVariables();
		phaseInitiation();
		boolean hasUpdatedPorts= acceptPortValues();
		Iterator<SlotVariable> slotVariablesIterator= slotVariables.iterator();
		while (slotVariablesIterator.hasNext()) {
			SlotVariable slotVariable= slotVariablesIterator.next();
			try {
				actualizeValues(rootCP,slotVariable.getValue(rootCP));
				DialogEntry item= slotMap.get(slotVariable);
				item.putValue(slotVariable.copyValue(rootCP,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES),rootCP);
			} catch (Backtracking b) {
			};
		};
		initiateDialogEntries(targetWorld,userDefinedSlots);
		initiateDialogEntries(targetWorld,systemSlots);
	}
	protected void initiateSlotValues(Dialog targetWorld, DialogEntry[] slots) {
		for (int i= 0; i < slots.length; i++) {
			DialogEntry item= slots[i];
			if (item.isSlotName) {
				Term slot= targetWorld.getSlotByName(item.name);
				slot= slot.extractSlotVariable();
				if (slot.thisIsSlotVariable()) {
					slot.registerVariables(this,false,item.isInsistent);
					slotMap.put((SlotVariable)slot,item);
					Term initialValue= null;
					try {
						initialValue= item.getExistedValue();
						slot.unifyWith(initialValue,rootCP);
					} catch (Backtracking b) {
					}
				}
			}
		}
	}
	protected void initiateDialogEntries(Dialog targetWorld, DialogEntry[] slots) {
		for (int i= 0; i < slots.length; i++) {
			DialogEntry item= slots[i];
			if (item.isSlotName) {
				Term value= targetWorld.getSlotByName(item.name);
				value= value.extractSlotVariable();
				if (!value.thisIsSlotVariable()) {
					item.putValue(value.copyValue(rootCP,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES),rootCP);
				}
				//if (!value.thisIsSlotVariable()) {
				//	item.putValue(value.copyValue(rootCP,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES),rootCP);
				//} else {
				//	item.putValue(value.copyValue(rootCP,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES),rootCP);
				//}

			}
		}
	}
	protected boolean removeNewlyProvedOldActors(HashSet<ActorNumber> oldActors) {
		return false;
	}
	public void clearActorStore() {
	}
	protected void resetResidentOwners() {
	}
	public void registerActorToBeProved(ActorNumber actorNumber, ChoisePoint cp) {
	}
	public Continuation createActorNeutralizationNode(Continuation aC) {
		return aC;
	}
	public Continuation createContinuation(Continuation aC) {
		throw new AnAttemptToConvertProcessToContinuation();
	}
	protected void informInternalWorldsAboutFailure() {
	}
	public boolean debugThisProcess() {
		return false;
		// return true;
	}
}
