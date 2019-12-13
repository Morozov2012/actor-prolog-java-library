// (c) 2009 IRE RAS Alexei A. Morozov

package morozov.worlds;

import morozov.run.*;
import morozov.terms.*;
import morozov.worlds.errors.*;

import java.util.Iterator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class Resident extends ActiveWorld {
	//
	protected Term output;
	protected Term target;
	protected long domainSignature;
	protected Term[] arguments;
	//
	protected HashSet<AbstractWorld> targetWorlds= new HashSet<>();
	protected HashMap<AbstractWorld,Term> resultLists= new HashMap<>();
	protected AtomicBoolean hasNewResults= new AtomicBoolean(false);
	//
	private static final long serialVersionUID= 0x528C7BA260C4371BL; // 5948265145187972891L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.worlds","Resident");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public Resident() {
		super(true,true);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term getOutput() {
		return output;
	}
	public Term getTarget() {
		return target;
	}
	public long getDomainSignature() {
		return domainSignature;
	}
	public Term[] getArguments() {
		return arguments;
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
	@Override
	public void actualize(ChoisePoint iX) {
	}
	//
	@Override
	public void startProcesses() {
		start();
	}
	//
	@Override
	public void releaseSystemResources() {
	}
	//
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
	public AbstractInternalWorld internalWorld() {
		throw new AnAttemptToExtractInternalWorldFromResident();
	}
	//
	@Override
	public void extractWorlds(AbstractProcess process, LinkedHashSet<AbstractInternalWorld> list) {
	}
	//
	final public long getPackageCode() {
		throw new AnAttemptToExtractPackageCodeFromResident();
	}
	@Override
	final public long[] getClassHierarchy() {
		throw new AnAttemptToExtractClassHierarchyFromResident();
	}
	@Override
	final public long[] getInterfaceHierarchy() {
		throw new AnAttemptToExtractInterfaceHierarchyFromResident();
	}
	final public Term getSlotByName(String name) {
		throw new AnAttemptToExtractSlotFromResident();
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
	// ACCEPT MESSAGES                                           //
	///////////////////////////////////////////////////////////////
	//
	// Process Flow Messages
	//
	@Override
	protected void processFlowMessages(ChoisePoint iX) {
		phaseInitiation();
		acceptPortValues();
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
		HashSet<AbstractWorld> worlds= new HashSet<>();
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
		synchronized (resultLists) {
			resultLists.put(target,list);
		};
		hasNewResults.set(true);
		wakeUp();
	}
	//
	public void cancelResultList(AbstractWorld target) {
		synchronized (resultLists) {
			resultLists.remove(target);
		};
		hasNewResults.set(true);
		wakeUp();
	}
	//
	// Process One Direct Message
	//
	@Override
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
		acceptPortValues();
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
	@Override
	protected void acceptTimerMessage() {
	}
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
}
