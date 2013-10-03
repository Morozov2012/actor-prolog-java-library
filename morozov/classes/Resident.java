// (c) 2009 IRE RAS Alexei A. Morozov

package morozov.classes;

import morozov.classes.errors.*;
import morozov.run.*;
import morozov.terms.*;

import java.util.Iterator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

class AnAttemptToExtractWorldFromResident extends RuntimeException {}
class AnAttemptToExtractSlotFromResident extends RuntimeException {}
class AnAttemptToExtractPackageCodeFromResident extends RuntimeException {}
class AnAttemptToExtractClassHierarchyFromResident extends RuntimeException {}
class AnAttemptToExtractInterfaceHierarchyFromResident extends RuntimeException {}

public class Resident extends ActiveWorld {
	//
	protected Term output;
	protected Term target;
	protected long domainSignature;
	// protected String functor;
	protected Term[] arguments;
	//
	protected HashSet<AbstractWorld> targetWorlds= new HashSet<AbstractWorld>();
	protected HashMap<AbstractWorld,Term> resultLists= new HashMap<AbstractWorld,Term>();
	protected AtomicBoolean hasNewResults= new AtomicBoolean(false);
	//
	public Resident() {
		super(true,true);
		currentProcess= this;
	}
	//
	public void initiate(AbstractProcess dummy, StaticContext context, Term aOutput, Term aTarget, long signature, Term... args) {
		// thread= new ThreadHolder(this);
		staticContext= context;
		output= aOutput;
		target= aTarget;
		domainSignature= signature;
		// functor= aFunctor;
		arguments= (Term[])args;
		output.registerVariables(this,false,true);
		target.registerVariables(this,false,false);
		for (int i= 0; i < arguments.length; i++) {
			arguments[i].registerVariables(this,false,false);
		}
	}
	//
	public void actualize(ChoisePoint iX) {
	}
	//
	public void startProcesses() {
		start();
	}
	public void closeFiles() {
		// stop();
	}
	public void stopProcesses() {
		stop();
	}
	//
	private void prepareTargetWorlds(Term target, ChoisePoint cp) {
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
	private Term[] prepareArguments(ChoisePoint cp) {
		Term[] argumentList= new Term[arguments.length];
		for (int i= 0; i < arguments.length; i++) {
			argumentList[i]= arguments[i].copyValue(cp,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
		};
		return argumentList;
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
	// ACCEPT MESSAGES
	// Process Flow Messages
	protected void processFlowMessages(ChoisePoint iX) {
		// System.out.printf("Resident::processFlowMessages\n");
		phaseInitiation();
		boolean hasUpdatedPorts= acceptPortValues();
		//
		prepareTargetWorlds(target,iX);
		Term[] argumentList= prepareArguments(iX);
		//
		fixSlotVariables(true);
		// sendActualFlowMessages();
		storeSlotVariables();
		// isProven= true;
		//
		Iterator<AbstractWorld> targetWorldsIterator= targetWorlds.iterator();
		while (targetWorldsIterator.hasNext()) {
			AbstractWorld world= targetWorldsIterator.next();
			// System.out.printf("Resident::%s.sendResidentRequest(%s,%s,%s)\n",world,this,get_domain_signature(),argumentList[0]);
			world.sendResidentRequest(this,domainSignature,argumentList,true,iX);
		};
		hasNewResults.set(true);
		wakeUp();
	}
	//
	public void returnResultList(AbstractWorld target, Term list) {
		// System.out.printf("Resident::returnResultList:%s\n",list);
		synchronized(resultLists) {
			resultLists.put(target,list);
		};
		hasNewResults.set(true);
		// System.out.printf("Resident::wakeUp\n");
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
	public void acceptDirectMessage() {
		// System.out.printf("Resident::acceptDirectMessage()\n");
		if (!hasNewResults.compareAndSet(true,false)) {
			return;
		};
		boolean processWasSuspended= isSuspended;
		if (isToBeSuspended()) {
			if (!processWasSuspended) {
				// sendEmptyFlowMessages();
				// resetResidentOwners();
				// unsuccessfullyFinishPhase(true);
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
		// resetTrail();
		// trail.clear();
		// recentChoisePointNumber= 0;
	}
	//
	protected void acceptTimerMessage() {
	}
	protected void sendStateOfProcess() {
	}
	//
	public Continuation createActorNeutralizationNode(Continuation aC) {
		return aC;
	}
	//
	public void extractWorlds(LinkedHashSet<AbstractWorld> list) {
	}
	//
	public void extractWorlds(AbstractProcess process, LinkedHashSet<AbstractWorld> list) {
	}
	final public long getPackageCode() {
		throw new AnAttemptToExtractPackageCodeFromResident();
	}
	final public long[] getClassHierarchy() {
		throw new AnAttemptToExtractClassHierarchyFromResident();
	}
	final public long[] getInterfaceHierarchy() {
		throw new AnAttemptToExtractInterfaceHierarchyFromResident();
	}
	// public void staticBinding(String name, ChoisePoint iX, Term[] arguments, Continuation competion) {
	//	throw new AnAttemptToExtractWorldFromResident();
	// }
	public Term getSlotByName(String name) {
		throw new AnAttemptToExtractSlotFromResident();
	}
	public long entry_s_Goal_0() {
		throw new AnAttemptToExtractDomainSignatureFromResident();
	}
	public long entry_s_Alarm_1_i() {
		throw new AnAttemptToExtractDomainSignatureFromResident();
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
	public Continuation createContinuation(Continuation aC) {
		throw new AnAttemptToConvertProcessToContinuation();
	}
	protected void informInternalWorldsAboutFailure() {
	}
	public boolean debugThisProcess() {
		return false;
		//return true;
	}
}
