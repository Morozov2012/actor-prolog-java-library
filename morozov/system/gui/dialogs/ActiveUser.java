// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs;

import morozov.built_in.*;
import morozov.run.*;
import morozov.system.gui.dialogs.errors.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.util.Iterator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.HashMap;

public class ActiveUser extends ActiveWorld {
	//
	protected AbstractDialog targetDialog= null;
	protected HashMap<SlotVariable,DialogEntry> slotMap= new HashMap<>();
	protected HashSet<DialogEvent> userInterfaceMessages= new HashSet<>();
	//
	private static final long serialVersionUID= 0x4DB5653AB96D4F75L; // 5599493014598537077L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.worlds","ActiveUser");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public ActiveUser() {
		super(true,true);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void initiate(ActiveWorld dummy, StaticContext context) {
		staticContext= context;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void actualize(ChoisePoint iX) {
	}
	@Override
	public void startProcesses() {
		start();
	}
	@Override
	public void releaseSystemResources() {
	}
	@Override
	public void stopProcesses() {
		stop();
	}
	//
	@Override
	public MethodSignature[] getMethodSignatures() {
		return emptySignatureList;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void extractWorlds(AbstractProcess process, LinkedHashSet<AbstractInternalWorld> list) {
	}
	@Override
	public AbstractInternalWorld internalWorld() {
		throw new AnAttemptToExtractInternalWorldFromActiveUser();
	}
	//
	final public long getPackageCode() {
		throw new AnAttemptToExtractPackageCodeFromActiveUser();
	}
	@Override
	final public long[] getClassHierarchy() {
		throw new AnAttemptToExtractClassHierarchyFromActiveUser();
	}
	@Override
	final public long[] getInterfaceHierarchy() {
		throw new AnAttemptToExtractInterfaceHierarchyFromActiveUser();
	}
	final public Term getSlotByName(String name) {
		throw new AnAttemptToExtractSlotFromActiveUser();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void receiveAsyncCall(AsyncCall item) {
	}
	@Override
	public void receiveTimerMessage(AbstractInternalWorld target) {
	}
	@Override
	public void cancelTimerMessage(AbstractInternalWorld target) {
	}
	@Override
	public void sendResidentRequest(Resident resident, long domainSignature, Term[] arguments, boolean sortAndReduceResultList) {
	}
	@Override
	public void withdrawRequest(Resident resident) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// Receiving User Interface Messages
	//
	public void receiveUserInterfaceMessage(DialogEntry entry, boolean sendFlowMessage, DialogEventType isControlModificationEvent) {
		synchronized (userInterfaceMessages) {
			userInterfaceMessages.add(new DialogEvent(entry,sendFlowMessage,isControlModificationEvent));
		};
		wakeUp();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void prepareFlowMessage(SlotVariable output, Term resultValue) {
		try {
			output.unifyWith(resultValue,rootCP);
		} catch (Backtracking b) {
		}
	}
	//
	public void freeTrail() {
		rootCP.freeTrail();
	}
	//
	public void registerPortsAndRecoverPortValues(AbstractDialog dialog, Dialog targetWorld, DialogEntry[] userDefinedSlots, DialogEntry[] systemSlots) {
		synchronized (this) {
			quicklyRegisterPortsAndRecoverPortValues(dialog,targetWorld,userDefinedSlots,systemSlots);
		}
	}
	public void quicklyRegisterPortsAndRecoverPortValues(AbstractDialog dialog, Dialog targetWorld, DialogEntry[] userDefinedSlots, DialogEntry[] systemSlots) {
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
				item.putValue(DialogControlOperation.VALUE,slotVariable.copyValue(rootCP,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES),rootCP);
			} catch (Backtracking b) {
			};
		};
		initiateDialogEntries(targetWorld,userDefinedSlots);
		initiateDialogEntries(targetWorld,systemSlots);
		sendControlCreationMessages(userDefinedSlots);
	}
	//
	protected void initiateSlotValues(Dialog targetWorld, DialogEntry[] slots) {
		for (int i= 0; i < slots.length; i++) {
			DialogEntry item= slots[i];
			if (item.isSlotName) {
				Term slot= item.getSlotByName(targetWorld,rootCP);
				slot= slot.extractSlotVariable();
				if (slot.thisIsSlotVariable()) {
					slot.registerVariables(this,false,item.isInsistent);
					slotMap.put((SlotVariable)slot,item);
					try {
						Term initialValue= item.getExistedValue();
						slot.unifyWith(initialValue,rootCP);
					} catch (Backtracking b) {
					}
				}
			}
		}
	}
	//
	protected void initiateDialogEntries(Dialog targetWorld, DialogEntry[] slots) {
		for (int i= 0; i < slots.length; i++) {
			DialogEntry item= slots[i];
			if (item.isSlotName) {
				Term value= item.getSlotByName(targetWorld,rootCP);
				value= value.extractSlotVariable();
				item.putValue(DialogControlOperation.VALUE,value.copyValue(rootCP,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES),rootCP);
			}
		}
	}
	//
	protected void sendControlCreationMessages(DialogEntry[] slots) {
		for (int i= 0; i < slots.length; i++) {
			DialogEntry item= slots[i];
			receiveUserInterfaceMessage(item,false,DialogEventType.CREATED_CONTROL);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	// ACCEPT MESSAGES                                           //
	///////////////////////////////////////////////////////////////
	//
	// Process Flow Messages
	//
	@Override
	protected void processFlowMessages(ChoisePoint iX) {
		synchronized (this) {
			quicklyProcessFlowMessages(iX);
		}
	}
	protected void quicklyProcessFlowMessages(ChoisePoint iX) {
		Iterator<SlotVariable> slotVariablesIterator= slotVariables.iterator();
		while (slotVariablesIterator.hasNext()) {
			SlotVariable slotVariable= slotVariablesIterator.next();
			try {
				actualizeValues(iX,slotVariable.getValue(iX));
				DialogEntry item= slotMap.get(slotVariable);
				item.putValue(DialogControlOperation.VALUE,slotVariable.copyValue(rootCP,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES),rootCP);
			} catch (Backtracking b) {
			}
		}
	}
	//
	// Process One Direct Message
	//
	@Override
	public void acceptDirectMessage() {
	}
	//
	@Override
	protected void acceptTimerMessage() {
		synchronized (this) {
			quicklyAcceptTimerMessage();
		}
	}
	protected void quicklyAcceptTimerMessage() {
		DialogEvent[] messagesToBeProcessed= new DialogEvent[0];
		synchronized (userInterfaceMessages) {
			if (userInterfaceMessages.size() > 0) {
				messagesToBeProcessed= userInterfaceMessages.toArray(messagesToBeProcessed);
				userInterfaceMessages.clear();
			}
		};
		if (messagesToBeProcessed.length > 0) {
			for (int n=0; n < messagesToBeProcessed.length; n++) {
				messagesToBeProcessed[n].transmitEntryValue(targetDialog,rootCP);
			};
			targetDialog.prepareAndSendFlowMessages();
		};
		if (messagesToBeProcessed.length > 0) {
			for (int n=0; n < messagesToBeProcessed.length; n++) {
				messagesToBeProcessed[n].sendCreatedControlMessage(targetDialog,rootCP);
			}
		}
	}
	//
	@Override
	protected void sendStateOfProcess() {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected void resetResidentOwners() {
	}
	@Override
	protected void informInternalWorldsAboutFailure() {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Continuation collectSuspendedCalls(Continuation c0, ChoisePoint iX) {
		return c0;
	}
	//
	@Override
	public Continuation createActorNeutralizationNode(Continuation aC) {
		return aC;
	}
	//
	@Override
	protected boolean removeNewlyProvedOldActors(HashSet<ActorNumber> oldActors) {
		return false;
	}
	//
	@Override
	public void clearActorStore() {
	}
	//
	@Override
	public void registerActorToBeProved(ActorNumber actorNumber, ChoisePoint cp) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void putFieldValue(DialogControlOperation operation, Term identifier, Term fieldValue, ChoisePoint iX) {
		synchronized (this) {
			targetDialog.quicklyPutFieldValue(operation,identifier,fieldValue,iX);
		}
	}
	public Term getFieldValue(DialogControlOperation operation, Term identifier, ChoisePoint iX) {
		synchronized (this) {
			return targetDialog.quicklyGetFieldValue(operation,identifier,iX);
		}
	}
}
