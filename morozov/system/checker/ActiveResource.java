// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.checker;

import morozov.run.*;
import morozov.system.checker.errors.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.HashMap;

public class ActiveResource extends ActiveWorld {
	//
	protected SlotVariable specialPort;
	//
	private static final long serialVersionUID= 0x7C208A4473091FD8L; // 8944300886550192088L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.worlds","ActiveResource");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public ActiveResource(SlotVariable sP) {
		super(false,true);
		thread= new URL_Checker(this);
		thread.setDaemon(true);
		specialPort= sP;
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
	public void addFutureEvent(BigInteger eT, ActorNumber actor, URL_Attributes attributes, BigDecimal rP, BigDecimal aP) {
		((URL_Checker)thread).addFutureEvent(eT,actor,attributes,rP,aP);
	}
	public void addFutureEvents(HashMap<ActorAndURI,WebReceptorRecord> actualRecords, BigInteger currentTime) {
		((URL_Checker)thread).addFutureEvents(actualRecords,currentTime);
	}
	public void checkAndAddFutureEvents(HashMap<ActorAndURI,WebReceptorRecord> actualRecords, BigInteger currentTime) {
		((URL_Checker)thread).checkAndAddFutureEvents(actualRecords,currentTime);
	}
	public void forgetEvents(ActorNumber actor) {
		((URL_Checker)thread).forgetEvents(actor);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void publishResults(BigInteger currentTime) {
		phaseInitiation();
		try {
			specialPort.unifyWith(new PrologInteger(currentTime),rootCP);
			fixSlotVariables(false);
			sendActualFlowMessages();
			storeSlotVariables();
		} catch (Backtracking b) {
		};
		rootCP.freeTrail();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void actualize(ChoisePoint iX) {
	}
	public void startProcesses() {
	}
	public void releaseSystemResources() {
	}
	public void stopProcesses() {
	}
	//
	public MethodSignature[] getMethodSignatures() {
		return emptySignatureList;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void extractWorlds(AbstractProcess process, LinkedHashSet<AbstractInternalWorld> list) {
	}
	//
	public AbstractInternalWorld internalWorld() {
		throw new AnAttemptToExtractInternalWorldFromActiveResource();
	}
	//
	final public long getPackageCode() {
		throw new AnAttemptToExtractPackageCodeFromActiveResource();
	}
	final public long[] getClassHierarchy() {
		throw new AnAttemptToExtractClassHierarchyFromActiveResource();
	}
	final public long[] getInterfaceHierarchy() {
		throw new AnAttemptToExtractInterfaceHierarchyFromActiveResource();
	}
	final public Term getSlotByName(String name) {
		throw new AnAttemptToExtractSlotFromActiveResource();
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
	}
	//
	public void acceptDirectMessage() {
	}
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
