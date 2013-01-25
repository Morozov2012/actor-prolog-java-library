// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.classes;

import morozov.run.*;
import morozov.terms.*;

import java.util.*;

public abstract class AbstractWorld extends ActorNumber {
	// public AbstractProcess currentProcess;
	public ActiveWorld currentProcess;
	public StaticContext staticContext;
	//
	public void startProcesses() {
	}
	//
	public void closeFiles() {
	}
	//
	public void stopProcesses() {
	}
	//
	public AbstractWorld internalWorld(AbstractProcess process, ChoisePoint cp) throws Backtracking {
		if (currentProcess==process) {
			return this;
		} else {
			throw new Backtracking();
		}
	}
	//
	public AbstractWorld internalWorld(ChoisePoint cp) {
		return this;
	}
	//
	// public AbstractWorld world(ChoisePoint cp) {
	//	return this;
	// }
	//
	public abstract Continuation createContinuation(Continuation aC);
	//
	public void isWorld(Term v, ChoisePoint cp) throws Backtracking {
		if (this != v) {
			throw new Backtracking();
		}
	}
	public boolean thisIsWorld() {
		return true;
	}
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.isWorld(this,cp);
	}
	//
	public abstract void extractWorlds(AbstractProcess process, LinkedHashSet<AbstractWorld> list);
	//
	public void registerTargetWorlds(HashSet<AbstractWorld> worlds, ChoisePoint cp) {
		worlds.add(this);
	}
	//
	public Term substituteWorlds(HashMap<AbstractWorld,Term> map, ChoisePoint cp) {
		Term value= map.get(this);
		if (value != null) {
			return value;
		} else {
			return new PrologUnknownValue();
		}
	}
	//
	public abstract long entry_s_Goal_0();
	public abstract long entry_s_Alarm_1_i();
	//
	public void pushSuspendedCall(Term target, AbstractWorld currentWorld, long domainSignatureNumber, Term[] arguments) {
		currentProcess.pushSuspendedCall(target,currentWorld,domainSignatureNumber,arguments);
	}
	public void pushAsyncCall(ChoisePoint iX, long domainSignatureNumber, Term target, AbstractWorld currentWorld, boolean isControlCall, boolean useBuffer, Term[] arguments) {
		currentProcess.pushAsyncCall(iX,domainSignatureNumber,target,currentWorld,isControlCall,useBuffer,arguments);
	}
	public void pushSuspendedAsyncCall(long aSignatureNumber, Term world, AbstractWorld aCW, boolean type, boolean useBuffer, Term[] list) {
		currentProcess.pushSuspendedAsyncCall(aSignatureNumber,world,aCW,type,useBuffer,list);
	}
	public void pushPredefinedClassRecord(PredefinedClassRecord record) {
		currentProcess.pushPredefinedClassRecord(record);
	}
	//
	public void sendAsyncCall(ChoisePoint iX, long domainSignatureNumber, Term target, AbstractWorld currentWorld, boolean isControlCall, boolean useBuffer, Term[] arguments) {
		currentProcess.sendAsyncCall(iX,domainSignatureNumber,target,currentWorld,isControlCall,useBuffer,arguments);
	}
	public long entry_s_Show_2_ii() {
		throw new TheShowEntryIsNotDefined();
	}
	public void receiveAsyncCall(AsyncCall item) {
		currentProcess.receiveAsyncCall(item);
	}
	//
	// public void sendResidentRequest(Resident resident, long domainSignature, Term[] arguments, ChoisePoint iX) {
	//	currentProcess.sendResidentRequest(this,resident,domainSignature,arguments,iX);
	// }
	public void sendResidentRequest(Resident resident, long domainSignature, Term[] arguments, boolean sortAndReduceResultList, ChoisePoint iX) {
		currentProcess.sendResidentRequest(this,resident,domainSignature,arguments,sortAndReduceResultList,iX);
	}
	//
	public void withdrawRequest(Resident resident) {
		currentProcess.withdrawRequest(this,resident);
	}
	//
	public void sendStateRequest(ProcessStateListener listener, String identifier) {
		currentProcess.sendStateRequest(listener,identifier);
	}
	//
	protected void storeBacktrackableRecord(PredefinedClassRecord record) {
		throw new WorldIsNotInstanceOfPredefinedClass();
	}
	//
	public boolean isSpecialWorld() {
		return false;
	}
	public void finishPhaseSuccessfully() {
	}
	public void finishPhaseUnsuccessfully() {
	}
	//
	public abstract long getPackageCode();
	// public abstract long[] getClassHierarchy();
	// public abstract long[] getInterfaceHierarchy();
	// public abstract void staticBinding(String name, ChoisePoint iX, Term[] arguments, Continuation competion);
	public abstract Term getSlotByName(String name);
	//
	protected void actualizeValues(ChoisePoint iX, Term... args) throws Backtracking {
		currentProcess.actualizeValues(iX,args);
	}
	// CONTROL LOCAL TRAIL AND BACKTRACKING OF PROCESS
	public void registerBinding(Term v) {
		currentProcess.registerBinding(v);
	}
	// NEUTRALIZATION OF ACTORS
	public void neutralizeActorsAndContinue(Continuation c0, ChoisePoint iX) throws Backtracking {
		currentProcess.neutralizeActorsAndContinue(c0,iX);
	}
	public class NeutralizeActorsAndContinue extends Continuation {
		// private Continuation c0;
		public NeutralizeActorsAndContinue(Continuation aC) {
			c0= aC;
		}
		public void execute(ChoisePoint iX) throws Backtracking {
			currentProcess.neutralizeActorsAndContinue(c0,iX);
		}
		public String toString() {
			return "NeutrActorsAndCont " + c0.toString();
		}
	}
	//
	public class CheckSuspendedCalls extends Continuation {
		// private Continuation c0;
		// private Continuation c1;
		// private ChoisePoint pS;
		public CheckSuspendedCalls(Continuation aC) {
			c0= aC;
			// pS= aCP;
		}
		public void execute(ChoisePoint iX) throws Backtracking {
			Continuation c1= currentProcess.collectSuspendedCalls(c0,iX);
			// ArrayList<SuspendedCall> table= currentProcess.suspendedCallTable;
			// int length= table.size();
			// Continuation c1= c0;
			// for (int i=length-1; i>=0; i--) {
			//	SuspendedCall call= table.get(i);
			//	c1= call.formContinuation(currentProcess,iX,c1);
			// };
			c1.execute(iX);
		}
		public String toString() {
			return "CheckSuspendedCalls " + c0.toString();
		}
	}
	//
	// public Continuation collectSuspendedCalls(Continuation c0, ChoisePoint iX) {
	//	return currentProcess.collectSuspendedCalls(c0,iX);
	// }
	//
	public void debugInfo(long position, long unit, int fileNumber) {
		currentProcess.debugInfo(position,unit,fileNumber);
	}
}
