// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.checker;

import morozov.classes.*;
import morozov.classes.errors.*;
import morozov.run.*;
import morozov.terms.*;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.HashMap;

class AnAttemptToExtractWorldFromActiveResource extends RuntimeException {}
class AnAttemptToExtractSlotFromActiveResource extends RuntimeException {}
class AnAttemptToExtractPackageCodeFromActiveResource extends RuntimeException {}
class AnAttemptToExtractClassHierarchyFromActiveResource extends RuntimeException {}
class AnAttemptToExtractInterfaceHierarchyFromActiveResource extends RuntimeException {}
class AnAttemptToExtractDomainSignatureFromActiveResource extends RuntimeException {}

public class ActiveResource extends ActiveWorld {
	// protected URL_Checker specialProcess;
	private static BigDecimal oneMillion= BigDecimal.valueOf(1000000);
	protected SlotVariable specialPort;
	//
	public ActiveResource(SlotVariable sP) {
		super(false,true);
		currentProcess= this;
		thread= new URL_Checker(this);
		thread.setDaemon(true);
		specialPort= sP;
	}
	//
	public void initiate(ActiveWorld dummy, StaticContext context) {
		staticContext= context;
	}
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
	public void actualize(ChoisePoint iX) {
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
	}
	//
	public void acceptDirectMessage() {
	}
	protected void acceptTimerMessage() {
	}
	protected void sendStateOfProcess() {
	}
	public void extractWorlds(AbstractProcess process, LinkedHashSet<AbstractWorld> list) {
	}
	final public long getPackageCode() {
		throw new AnAttemptToExtractPackageCodeFromActiveResource();
	}
	final public long[] getClassHierarchy() {
		throw new AnAttemptToExtractClassHierarchyFromActiveResource();
	}
	final public long[] getInterfaceHierarchy() {
		throw new AnAttemptToExtractInterfaceHierarchyFromActiveResource();
	}
	// public void staticBinding(String name, ChoisePoint iX, Term[] arguments, Continuation competion) {
	//	throw new AnAttemptToExtractWorldFromActiveResource();
	// }
	public Term getSlotByName(String name) {
		throw new AnAttemptToExtractSlotFromActiveResource();
	}
	public long entry_s_Goal_0() {
		throw new AnAttemptToExtractDomainSignatureFromActiveResource();
	}
	public long entry_s_Alarm_1_i() {
		throw new AnAttemptToExtractDomainSignatureFromActiveResource();
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
}
