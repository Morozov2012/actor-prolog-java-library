// (c) 2009 IRE RAS Alexei A. Morozov

package morozov.worlds;

import morozov.run.*;
import morozov.terms.*;

import java.util.Iterator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

class AnAttemptToExtractInternalWorldFromResident extends RuntimeException {}
class AnAttemptToExtractPackageCodeFromResident extends RuntimeException {}
class AnAttemptToExtractClassHierarchyFromResident extends RuntimeException {}
class AnAttemptToExtractInterfaceHierarchyFromResident extends RuntimeException {}
class AnAttemptToExtractSlotFromResident extends RuntimeException {}

public class Resident extends ActiveWorld {
	//
	protected Term output;
	protected Term target;
	public long domainSignature;
	protected Term[] arguments;
	//
	protected HashSet<AbstractWorld> targetWorlds= new HashSet<AbstractWorld>();
	protected HashMap<AbstractWorld,Term> resultLists= new HashMap<AbstractWorld,Term>();
	protected AtomicBoolean hasNewResults= new AtomicBoolean(false);
	//
	///////////////////////////////////////////////////////////////
	//
	public Resident() {
		super(true,true);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void initiate(AbstractProcess dummy, StaticContext context, Term aOutput, Term aTarget, long signature, Term... args) {
		staticContext= context;
		output= aOutput;
		target= aTarget;
		domainSignature= signature;
		arguments= (Term[])args;
		output.registerVariables(this,false,true);
		target.registerVariables(this,false,false);
		for (int i= 0; i < arguments.length; i++) {
			arguments[i].registerVariables(this,false,false);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void actualize(ChoisePoint iX) {
	}
	//
	public void startProcesses() {
		start();
	}
	//
	public void closeFiles() {
		// stop();
	}
	//
	public void stopProcesses() {
		stop();
	}
	//
	public MethodSignature[] getMethodSignatures() {
		return emptySignatureList;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public AbstractInternalWorld internalWorld() {
		throw new AnAttemptToExtractInternalWorldFromResident();
	}
	//
	public void extractWorlds(AbstractProcess process, LinkedHashSet<AbstractInternalWorld> list) {
	}
	//
	final public long getPackageCode() {
		throw new AnAttemptToExtractPackageCodeFromResident();
	}
	final public long[] getClassHierarchy() {
		throw new AnAttemptToExtractClassHierarchyFromResident();
	}
	final public long[] getInterfaceHierarchy() {
		throw new AnAttemptToExtractInterfaceHierarchyFromResident();
	}
	final public Term getSlotByName(String name) {
		throw new AnAttemptToExtractSlotFromResident();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void receiveAsyncCall(AsyncCall item) {
	}
	public void receiveTimerMessage(AbstractInternalWorld target) {
	}
	public void cancelTimerMessage(AbstractInternalWorld target) {
	}
	public void sendResidentRequest(Resident resident, long domainSignature, Term[] arguments, boolean sortAndReduceResultList) {
	}
	public void withdrawRequest(Resident resident) {
	}
	//
	///////////////////////////////////////////////////////////////
	// ACCEPT MESSAGES                                           //
	///////////////////////////////////////////////////////////////
	//
	// Process Flow Messages
	//
	protected void processFlowMessages(ChoisePoint iX) {
		phaseInitiation();
		boolean hasUpdatedPorts= acceptPortValues();
		//
		prepareTargetWorlds(target,iX);
		Term[] argumentList= prepareArguments(iX);
		//
		fixSlotVariables(true);
		storeSlotVariables();
		//
		Iterator<AbstractWorld> targetWorldsIterator= targetWorlds.iterator();
		while (targetWorldsIterator.hasNext()) {
			AbstractWorld world= targetWorldsIterator.next();
			world.sendResidentRequest(this,domainSignature,argumentList,true);
		};
		hasNewResults.set(true);
		wakeUp();
	}
	//
	protected void prepareTargetWorlds(Term target, ChoisePoint cp) {
		HashSet<AbstractWorld> worlds= new HashSet<AbstractWorld>();
		target.registerTargetWorlds(worlds,cp);
		targetWorlds.removeAll(worlds);
		if (!targetWorlds.isEmpty()) {
			Iterator<AbstractWorld> obsoleteWorldsIterator= targetWorlds.iterator();
			while (obsoleteWorldsIterator.hasNext()) {
				AbstractWorld world= obsoleteWorldsIterator.next();
				world.withdrawRequest(this);
				resultLists.remove(world);
			}
		};
		targetWorlds= worlds;
	}
	//
	protected Term[] prepareArguments(ChoisePoint cp) {
		Term[] argumentList= new Term[arguments.length];
		for (int i= 0; i < arguments.length; i++) {
			argumentList[i]= arguments[i].copyValue(cp,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
		};
		return argumentList;
	}
	//
	public void returnResultList(AbstractWorld target, Term list) {
		synchronized(resultLists) {
			resultLists.put(target,list);
		};
		hasNewResults.set(true);
		wakeUp();
	}
	//
	public void cancelResultList(AbstractWorld target) {
		synchronized(resultLists) {
			resultLists.remove(target);
		};
		hasNewResults.set(true);
		wakeUp();
	}
	//
	// Process One Direct Message
	//
	public void acceptDirectMessage() {
		if (!hasNewResults.compareAndSet(true,false)) {
			return;
		};
		boolean processWasSuspended= isSuspended;
		if (isToBeSuspended()) {
			if (!processWasSuspended) {
				finishPhaseBySuspension();
			};
			return;
		};
		phaseInitiation();
		boolean hasUpdatedPorts= acceptPortValues();
		Term resultValue= target.substituteWorlds(resultLists,rootCP);
		try {
			output.unifyWith(resultValue,rootCP);
			fixSlotVariables(false);
			sendActualFlowMessages();
			storeSlotVariables();
		} catch (Backtracking b) {
		};
		//
		rootCP.freeTrail();
	}
	//
	protected void acceptTimerMessage() {
	}
	protected void sendStateOfProcess() {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void resetResidentOwners() {
	}
	protected void informInternalWorldsAboutFailure() {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Continuation collectSuspendedCalls(Continuation c0, ChoisePoint iX) {
		return c0;
	}
	//
	public Continuation createActorNeutralizationNode(Continuation aC) {
		return aC;
	}
	//
	protected boolean removeNewlyProvedOldActors(HashSet<ActorNumber> oldActors) {
		return false;
	}
	//
	public void clearActorStore() {
	}
	//
	public void registerActorToBeProved(ActorNumber actorNumber, ChoisePoint cp) {
	}
}
