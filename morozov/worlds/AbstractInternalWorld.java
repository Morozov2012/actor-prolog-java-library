// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.worlds;

import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;
import morozov.worlds.errors.*;

import java.util.LinkedHashSet;

public abstract class AbstractInternalWorld extends OwnWorld {
	//
	public AbstractProcess currentProcess;
	//
	private static final long serialVersionUID= 0xBCEFE35DEDAE866EL; // -4832393882586151314L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.worlds","AbstractInternalWorld");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Continuation createContinuation(Continuation aC);
	@Override
	abstract public void extractWorlds(AbstractProcess process, LinkedHashSet<AbstractInternalWorld> list);
	abstract public long getPackageCode();
	//
	abstract public Term getSlotByName(String name);
	//
	abstract public long entry_s_Goal_0();
	abstract public long entry_s_Alarm_1_i();
	//
	public long entry_s_Show_2_ii() {
		throw new TheShowEntryIsNotDefined();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public AbstractInternalWorld() {
	}
	public AbstractInternalWorld(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void startProcesses() {
	}
	//
	@Override
	public void releaseSystemResources() {
	}
	//
	@Override
	public void stopProcesses() {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean isInternalWorldOf(AbstractProcess process) {
		if (currentProcess==process) {
			return true;
		} else {
			return false;
		}
	}
	//
	@Override
	public AbstractInternalWorld getInternalWorld(ChoisePoint cp) throws Backtracking, TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		return this;
	}
	//
	@Override
	public AbstractInternalWorld internalWorld(AbstractProcess process, ChoisePoint cp) throws Backtracking {
		if (currentProcess==process) {
			return this;
		} else {
			throw Backtracking.instance;
		}
	}
	//
	@Override
	public AbstractInternalWorld internalWorld(ChoisePoint iX) {
		return this;
	}
	//
	@Override
	public AbstractInternalWorld internalWorld() {
		return this;
	}
	//
	@Override
	public boolean isNumberOfTemporaryActor() {
		return false;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void pushSuspendedCall(Term target, AbstractInternalWorld currentWorld, long domainSignatureNumber, Term[] arguments) {
		currentProcess.pushSuspendedCall(target,currentWorld,domainSignatureNumber,arguments);
	}
	public void pushAsyncCall(ChoisePoint iX, long domainSignatureNumber, Term target, AbstractInternalWorld currentWorld, boolean isControlCall, boolean useBuffer, Term[] arguments) {
		currentProcess.pushAsyncCall(iX,domainSignatureNumber,target,currentWorld,isControlCall,useBuffer,arguments);
	}
	public void pushSuspendedAsyncCall(long aSignatureNumber, Term world, AbstractInternalWorld aCW, boolean type, boolean useBuffer, Term[] list) {
		currentProcess.pushSuspendedAsyncCall(aSignatureNumber,world,aCW,type,useBuffer,list);
	}
	public void pushPredefinedClassRecord(PredefinedClassRecord record) {
		currentProcess.pushPredefinedClassRecord(record);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void sendAsyncCall(ChoisePoint iX, long domainSignatureNumber, Term target, AbstractInternalWorld currentWorld, boolean isControlCall, boolean useBuffer, Term[] arguments) {
		currentProcess.sendAsyncCall(iX,domainSignatureNumber,target,currentWorld,isControlCall,useBuffer,arguments);
	}
	//
	@Override
	public void receiveAsyncCall(AsyncCall item) {
		currentProcess.receiveAsyncCall(item);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void sendResidentRequest(Resident resident, long domainSignature, Term[] arguments, boolean sortAndReduceResultList) {
		currentProcess.sendResidentRequest(this,resident,domainSignature,arguments,sortAndReduceResultList);
	}
	//
	@Override
	public void withdrawRequest(Resident resident) {
		currentProcess.withdrawRequest(this,resident);
	}
	//
	public void sendStateRequest(ProcessStateListener listener, String identifier) {
		currentProcess.sendStateRequest(listener,identifier);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void storeBacktrackableRecord(PredefinedClassRecord record) {
		throw new WorldIsNotInstanceOfPredefinedClass();
	}
	//
	public void finishPhaseSuccessfully() {
	}
	//
	public void finishPhaseUnsuccessfully() {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void actualizeValues(ChoisePoint iX, Term... args) throws Backtracking {
		currentProcess.actualizeValues(iX,args);
	}
	//
	// CONTROL LOCAL TRAIL AND BACKTRACKING OF PROCESS
	//
	public void registerBinding(Term v) {
		currentProcess.registerBinding(v);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// NEUTRALIZATION OF ACTORS
	//
	public void neutralizeActorsAndContinue(Continuation c0, ChoisePoint iX) throws Backtracking {
		currentProcess.neutralizeActorsAndContinue(c0,iX);
	}
	//
	public class NeutralizeActorsAndContinue extends Continuation {
		public NeutralizeActorsAndContinue(Continuation aC) {
			c0= aC;
		}
		@Override
		public void execute(ChoisePoint iX) throws Backtracking {
			currentProcess.neutralizeActorsAndContinue(c0,iX);
		}
		@Override
		public String toString() {
			return "NeutrActorsAndCont " + c0.toString();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public class CheckSuspendedCalls extends Continuation {
		public CheckSuspendedCalls(Continuation aC) {
			c0= aC;
		}
		@Override
		public void execute(ChoisePoint iX) throws Backtracking {
			Continuation c1= currentProcess.collectSuspendedCalls(c0,iX);
			c1.execute(iX);
		}
		@Override
		public String toString() {
			return "CheckSuspendedCalls " + c0.toString();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void debugInfo(long position, long unit, int fileNumber) {
		currentProcess.debugInfo(position,unit,fileNumber);
	}
}
