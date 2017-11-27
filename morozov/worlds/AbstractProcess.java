// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.worlds;

import target.*;

import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;

public abstract class AbstractProcess extends ActiveWorld {
	//
	protected AbstractInternalWorld[] worldArray;
	protected AbstractInternalWorld[] specialWorldArray;
	private boolean isProven= false;
	public ArrayList<SuspendedCall> suspendedCallTable= new ArrayList<SuspendedCall>();
	private ArrayList<AsyncCall> asyncCallTable= new ArrayList<AsyncCall>();
	private ArrayList<PredefinedClassRecord> predefinedClassesState= new ArrayList<PredefinedClassRecord>();
	public ArrayList<AsyncCall> inputControlDirectMessages= new ArrayList<AsyncCall>();
	public ArrayList<AsyncCall> inputInformationDirectMessages= new ArrayList<AsyncCall>();
	public LinkedHashSet<AsyncCall> inputControlDirectMessagesHash= new LinkedHashSet<AsyncCall>();
	public LinkedHashSet<AsyncCall> inputInformationDirectMessagesHash= new LinkedHashSet<AsyncCall>();
	public ArrayList<ResidentRequest> inputResidentRequests= new ArrayList<ResidentRequest>();
	public ArrayList<ResidentRequest> processedResidentRequests= new ArrayList<ResidentRequest>();
	private LinkedHashSet<ProcessStateRequest> stateRequests= new LinkedHashSet<ProcessStateRequest>();
	private LinkedHashSet<AbstractInternalWorld> timerMessages= new LinkedHashSet<AbstractInternalWorld>();
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public AbstractInternalWorld getMainWorld();
	//
	///////////////////////////////////////////////////////////////
	//
	public AbstractProcess() {
	}
	public AbstractProcess(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void startProcesses() {
		start();
	}
	public void closeFiles() {
	}
	public void stopProcesses() {
		stop();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public AbstractInternalWorld getInternalWorld(ChoisePoint cp) throws Backtracking, TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		return getMainWorld();
	}
	//
	public AbstractInternalWorld internalWorld(AbstractProcess process, ChoisePoint cp) throws Backtracking {
		if (this==process) {
			return getMainWorld();
		} else {
			throw Backtracking.instance;
		}
	}
	//
	public AbstractInternalWorld internalWorld(ChoisePoint iX) {
		return getMainWorld();
	}
	//
	public AbstractInternalWorld internalWorld() {
		return getMainWorld();
	}
	//
	public boolean thisIsProcess() {
		return true;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public MethodSignature[] getMethodSignatures() {
		return getMainWorld().getMethodSignatures();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void extractWorlds(LinkedHashSet<AbstractInternalWorld> list) {
	}
	//
	public void extractWorlds(AbstractProcess process, LinkedHashSet<AbstractInternalWorld> list) {
	}
	//
	public Term substituteWorlds(HashMap<AbstractWorld,Term> map, ChoisePoint cp) {
		Term value1= map.get(this);
		if (value1 != null) {
			return value1;
		} else {
			return PrologUnknownValue.instance;
		}
	}
	//
	public static Continuation toContinuation(AbstractInternalWorld[] worlds, Continuation Rest) {
		int n= worlds.length;
		Continuation cs= Rest;
		for(int i= 0; i < n; i++) {
			cs= worlds[i].createContinuation(cs);
		};
		return cs;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void pushSuspendedCall(Term target, AbstractInternalWorld currentWorld, long domainSignatureNumber, Term[] arguments) {
		registerBinding(new SuspendedCallTableState(suspendedCallTable));
		suspendedCallTable.add(new SuspendedInternalCall(target,currentWorld,domainSignatureNumber,arguments));
	}
	//
	public void pushAsyncCall(ChoisePoint iX, long domainSignatureNumber, Term target, AbstractInternalWorld currentWorld, boolean isControlCall, boolean useBuffer, Term[] arguments) {
		target= target.dereferenceValue(iX);
		if (!target.thisIsUnknownValue()) {
			if (target.thisIsFreeVariable()) {
				registerBinding(new SuspendedCallTableState(suspendedCallTable));
				suspendedCallTable.add(new SuspendedAsyncCall(false,domainSignatureNumber,target,currentWorld,isControlCall,useBuffer,arguments));
			} else {
				AbstractWorld targetWorld;
				if (target instanceof AbstractWorld) {
					targetWorld= (AbstractWorld)target;
				} else {
					targetWorld= currentWorld;
				};
				registerBinding(new AsyncCallTableState(asyncCallTable));
				asyncCallTable.add(new AsyncCall(domainSignatureNumber,target,targetWorld,isControlCall,useBuffer,arguments,false));
			}
		}
	}
	//
	public void pushSuspendedAsyncCall(long domainSignatureNumber, Term target, AbstractInternalWorld currentWorld, boolean isControlCall, boolean useBuffer, Term[] arguments) {
		registerBinding(new SuspendedCallTableState(suspendedCallTable));
		suspendedCallTable.add(new SuspendedAsyncCall(true,domainSignatureNumber,target,currentWorld,isControlCall,useBuffer,arguments));
	}
	//
	public void pushPredefinedClassRecord(PredefinedClassRecord record) {
		registerBinding(new PredefinedClassBacktrackableState(predefinedClassesState));
		predefinedClassesState.add(record);
	}
	//
	public Continuation collectSuspendedCalls(Continuation c0, ChoisePoint iX) {
		int length= suspendedCallTable.size();
		Continuation c1= c0;
		for (int i=length-1; i>=0; i--) {
			SuspendedCall call= suspendedCallTable.get(i);
			c1= call.formContinuation(this,iX,c1);
		};
		return c1;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void sendAsyncCall(ChoisePoint iX, long domainSignatureNumber, Term target, AbstractInternalWorld currentWorld, boolean isControlCall, boolean useBuffer, Term[] arguments) {
		target= target.dereferenceValue(iX);
		if (!target.thisIsUnknownValue()) {
			if (target instanceof AbstractWorld) {
				AbstractWorld receiver= (AbstractWorld)target;
				AsyncCall item= new AsyncCall(domainSignatureNumber,target,receiver,isControlCall,useBuffer,arguments,false);
				receiver.transmitAsyncCall(item,iX);
			} else if (target.thisIsFreeVariable()) {
				currentWorld.pushSuspendedAsyncCall(domainSignatureNumber,target,currentWorld,isControlCall,useBuffer,arguments);
			} else {
				AsyncCall item= new AsyncCall(domainSignatureNumber,target,currentWorld,isControlCall,useBuffer,arguments,false);
				currentWorld.transmitAsyncCall(item,iX);
			}
		}
	}
	//
	public void sendAsyncCall(ChoisePoint iX, AsyncCall item1) {
		Term target= item1.target.dereferenceValue(iX);
		if (!target.thisIsFreeVariable() && !target.thisIsUnknownValue()) {
			if (target instanceof AbstractWorld) {
				AbstractWorld receiver= (AbstractWorld)target;
				receiver.transmitAsyncCall(item1,iX);
			} else {
				AbstractWorld currentWorld= item1.reserveTarget;
				currentWorld.transmitAsyncCall(item1,iX);
			}
		}
	}
	//
	public void sendAsyncCalls(ChoisePoint iX) {
		for(int i= 0; i < asyncCallTable.size(); i++) {
			AsyncCall item= asyncCallTable.get(i);
			if (item.isControlCall) {
				sendAsyncCall(iX,item);
			}
		};
		for(int i= 0; i < asyncCallTable.size(); i++) {
			AsyncCall item= asyncCallTable.get(i);
			if (!item.isControlCall) {
				sendAsyncCall(iX,item);
			}
		};
		asyncCallTable.clear();
	}
	//
	public void printAsyncCallTable() {
		System.out.printf("The %s process will send direct messages: %s\n",this,asyncCallTable);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void sendResidentRequest(Resident resident, long domainSignature, Term[] arguments, boolean sortAndReduceResultList) {
		sendResidentRequest(this,resident,domainSignature,arguments,sortAndReduceResultList);
	}
	//
	public void sendResidentRequest(AbstractWorld target, Resident resident, long domainSignature, Term[] arguments, boolean sortAndReduceResultList) {
		synchronized(inputResidentRequests) {
			boolean addNewRequest= true;
			Iterator<ResidentRequest> inputResidentRequestIterator= inputResidentRequests.iterator();
			while (inputResidentRequestIterator.hasNext()) {
				ResidentRequest currentRequest= inputResidentRequestIterator.next();
				if	(currentRequest.resident==resident &&
					currentRequest.target==target) {
					if	(currentRequest.domainSignature==domainSignature &&
						currentRequest.arguments.length==arguments.length) {
						try {
							for (int i= 0; i < arguments.length; i++) {
								currentRequest.arguments[i].unifyWith(arguments[i],null);
							};
							addNewRequest= false;
						} catch (Backtracking b) {
							inputResidentRequestIterator.remove();
						}
					} else {
						inputResidentRequestIterator.remove();
					};
					break;
				}
			};
			Iterator<ResidentRequest> processedResidentRequestIterator= processedResidentRequests.iterator();
			while (processedResidentRequestIterator.hasNext()) {
				ResidentRequest currentRequest= processedResidentRequestIterator.next();
				if	(currentRequest.resident==resident &&
					currentRequest.target==target) {
					if	(currentRequest.domainSignature==domainSignature &&
						currentRequest.arguments.length==arguments.length) {
						try {
							for (int i= 0; i < arguments.length; i++) {
								currentRequest.arguments[i].unifyWith(arguments[i],null);
							};
							addNewRequest= false;
						} catch (Backtracking b) {
							processedResidentRequestIterator.remove();
						}
					} else {
						processedResidentRequestIterator.remove();
					};
					break;
				}
			};
			if (addNewRequest) {
				inputResidentRequests.add(new ResidentRequest(target,resident,domainSignature,arguments,sortAndReduceResultList));
			}
		};
		wakeUp();
	}
	//
	public void withdrawRequest(Resident resident) {
		withdrawRequest(this,resident);
	}
	//
	public void withdrawRequest(AbstractWorld target, Resident resident) {
		synchronized(inputResidentRequests) {
			forgetRequest(target,resident);
		}
	}
	//
	private void forgetRequest(AbstractWorld target, Resident resident) {
		Iterator<ResidentRequest> inputResidentRequestIterator= inputResidentRequests.iterator();
		while (inputResidentRequestIterator.hasNext()) {
			ResidentRequest currentRequest= inputResidentRequestIterator.next();
			if	(currentRequest.resident==resident &&
				currentRequest.target==target) {
				inputResidentRequestIterator.remove();
				break;
			}
		};
		Iterator<ResidentRequest> processedResidentRequestIterator= processedResidentRequests.iterator();
		while (processedResidentRequestIterator.hasNext()) {
			ResidentRequest currentRequest= processedResidentRequestIterator.next();
			if (currentRequest.resident==resident) {
				processedResidentRequestIterator.remove();
				break;
			}
		}
	}
	//
	protected void resetResidentOwners() {
		synchronized(inputResidentRequests) {
			inputResidentRequests.addAll(processedResidentRequests);
			processedResidentRequests.clear();
			Iterator<ResidentRequest> inputResidentRequestIterator= inputResidentRequests.iterator();
			while (inputResidentRequestIterator.hasNext()) {
				ResidentRequest currentRequest= inputResidentRequestIterator.next();
				currentRequest.resident.cancelResultList(currentRequest.target);
			}
		}
	}
	//
	public void sendStateRequest(ProcessStateListener listener, String identifier) {
		synchronized(stateRequests) {
			stateRequests.add(new ProcessStateRequest(listener,identifier));
		};
		enableStateSending();
		wakeUp();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void receiveAsyncCall(AsyncCall newItem) {
		if (newItem.isControlCall) {
			synchronized(inputControlDirectMessages) {
				if (newItem.useBuffer) {
					inputControlDirectMessages.add(newItem);
				} else {
					if (inputControlDirectMessagesHash.contains(newItem)) {
						int index= inputControlDirectMessages.indexOf(newItem);
						inputControlDirectMessages.set(index,newItem);
						// return;
					} else {
						inputControlDirectMessages.add(newItem);
						inputControlDirectMessagesHash.add(newItem);
					}
				}
			}
		} else {
			synchronized(inputInformationDirectMessages) {
				if (newItem.useBuffer) {
					inputInformationDirectMessages.add(newItem);
				} else {
					if (inputInformationDirectMessagesHash.contains(newItem)) {
						int index= inputInformationDirectMessages.indexOf(newItem);
						inputInformationDirectMessages.set(index,newItem);
						// return;
					} else {
						inputInformationDirectMessages.add(newItem);
						inputInformationDirectMessagesHash.add(newItem);
					}
				}
			}
		};
		wakeUp();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void receiveTimerMessage(AbstractInternalWorld target) {
		synchronized(timerMessages) {
			timerMessages.add(target);
		};
		wakeUp();
	}
	//
	public void cancelTimerMessage(AbstractInternalWorld target) {
		synchronized(timerMessages) {
			timerMessages.remove(target);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void acceptDirectMessage() {
		boolean hasControlDirectMessage= false;
		boolean hasResidentRequest= false;
		boolean hasInformationDirectMessage= false;
		AsyncCall directMessage= null;
		ResidentRequest residentRequest= null;
		synchronized(inputControlDirectMessages) {
			if (inputControlDirectMessages.size() > 0) {
				hasControlDirectMessage= true;
				directMessage= inputControlDirectMessages.get(0);
			}
		};
		if (!hasControlDirectMessage && isProven) {
			synchronized(inputResidentRequests) {
				if (inputResidentRequests.size() > 0) {
					hasResidentRequest= true;
					residentRequest= inputResidentRequests.get(0);
				}
			}
		};
		if (!hasControlDirectMessage && !hasResidentRequest && isProven) {
			synchronized(inputInformationDirectMessages) {
				if (inputInformationDirectMessages.size() > 0) {
					hasInformationDirectMessage= true;
					directMessage= inputInformationDirectMessages.get(0);
				} else {
					return;
				}
			}
		};
		if (hasControlDirectMessage) {
			if (debugThisProcess()) {
				System.out.printf("%s: (1) CALL PHASE INITIATION (for D.M.);\n",this);
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
			if (hasUpdatedPorts) {
				if (debugThisProcess()) {
					System.out.printf("%s: ============================================\n",this);
					System.out.printf("%s: CALL FLOW MESSAGE INSTEAD OF C.D.M.; hasUpdatedPorts= %s, processWasProvedOneTimeAtLeast=%s\n",this,hasUpdatedPorts,processWasProvedOneTimeAtLeast);
					System.out.printf("%s: ============================================\n",this);
					printSlotVariables(this);
				};
				processFlowMessages(rootCP);
			} else {
				if (debugThisProcess()) {
					System.out.printf("%s: ============================================\n",this);
					System.out.printf("%s: CALL CONTROL DIRECT MESSAGE; hasUpdatedPorts= %s, processWasProvedOneTimeAtLeast=%s\n",this,hasUpdatedPorts,processWasProvedOneTimeAtLeast);
					System.out.printf("%s: ============================================\n",this);
				};
				boolean recentState= isProven;
				isProven= false;
				synchronized(inputControlDirectMessages) {
					if (inputControlDirectMessages.contains(directMessage)) {
						inputControlDirectMessages.remove(directMessage);
						if (!directMessage.useBuffer) {
							inputControlDirectMessagesHash.remove(directMessage);
						}
					};
					wakeUp();
				};
				try {
					if (directMessage.isTheVerifyCommand()) {
						processFlowMessages(rootCP);
					} else if (directMessage.isTheResetCommand()) {
					} else {
						Continuation c1= new PhaseTermination();
						c1= new NeutralizeActors(c1);
						if (!processWasProvedOneTimeAtLeast) {
							c1= addGoals(c1,rootCP);
						};
						processDirectMessageAndHandleBreak(
							c1,
							directMessage.domainSignatureNumber,
							directMessage.target,
							directMessage.reserveTarget,
							directMessage.arguments,
							rootCP);
					}
				} finally {
					// synchronized(inputControlDirectMessages) {
						// if (inputControlDirectMessages.contains(directMessage)) {
						//	inputControlDirectMessages.remove(directMessage);
						//	if (!directMessage.useBuffer) {
						//		inputControlDirectMessagesHash.remove(directMessage);
						//	}
						// };
						// wakeUp();
					// };
					if (!isProven) {
						if (recentState) {
							unsuccessfullyFinishPhase();
						} else {
							unsuccessfullyFinishPhaseAgain();
						}
					// This check cannot determine if the process
					// was suspended before this phase.
					};
					enableStateSending();
				}
				if (debugThisProcess()) {
					System.out.printf("%s: ============================================\n",this);
					System.out.printf("%s: END OF C.F.M MESSAGE; hasUpdatedPorts= %s, processWasProvedOneTimeAtLeast=%s\n",this,hasUpdatedPorts,processWasProvedOneTimeAtLeast);
					System.out.printf("%s: ============================================\n",this);
				};
			}
		} else if (hasResidentRequest) {
			boolean processWasSuspended= isSuspended;
			if (isToBeSuspended()) {
				if (!processWasSuspended) {
					finishPhaseBySuspension();
				};
				return;
			};
			phaseInitiation();
			boolean hasUpdatedPorts= acceptPortValues();
			if (hasUpdatedPorts || !processWasProvedOneTimeAtLeast) {
				if (debugThisProcess()) {
					System.out.printf("%s: ============================================\n",this);
					System.out.printf("%s: CALL FLOW MESSAGE INSTEAD OF R.R.; hasUpdatedPorts= %s, processWasProvedOneTimeAtLeast=%s\n",this,hasUpdatedPorts,processWasProvedOneTimeAtLeast);
					System.out.printf("%s: ============================================\n",this);
					printSlotVariables(this);
				};
				processFlowMessages(rootCP);
			} else {
				if (debugThisProcess()) {
					System.out.printf("%s: ============================================\n",this);
					System.out.printf("%s: CALL RESIDENT REQUEST; hasUpdatedPorts= %s, processWasProvedOneTimeAtLeast=%s\n",this,hasUpdatedPorts,processWasProvedOneTimeAtLeast);
					System.out.printf("%s: ============================================\n",this);
				};
				try {
					Term[] arguments= residentRequest.arguments;
					final PrologVariable result= new PrologVariable();
					Term[] argumentList= new Term[arguments.length+1];
					argumentList[0]= result;
					for (int i= 0; i < arguments.length; i++) {
						argumentList[i+1]= arguments[i].dereferenceValue(rootCP);
					};
					Continuation completion;
					Iterator<Term> resultSetIterator;
					if (residentRequest.sortAndReduceResultList) {
						final TreeSet<Term> resultSet= new TreeSet<Term>(new TermComparator(true));
						completion= new Continuation() {
							public void execute(ChoisePoint iX) throws Backtracking {
								Term newResult= result.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
								resultSet.add(newResult);
								throw Backtracking.instance;
							}
							public boolean isPhaseTermination() {
								return false;
							}
							public String toString() {
								return "CollectResults;Backtrack;";
							}
						};
						AbstractInternalWorld targetWorld= residentRequest.target.internalWorld();
						Continuation c1= new NeutralizeActors(completion);
						processDirectMessageAndHandleBreak(
							c1,
							residentRequest.domainSignature,
							targetWorld,
							targetWorld,
							argumentList,
							rootCP);
						resultSetIterator= resultSet.descendingIterator();
					} else {
						final ArrayList<Term> resultArray= new ArrayList<Term>();
						completion= new Continuation() {
							public void execute(ChoisePoint iX) throws Backtracking {
								Term newResult= result.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
								resultArray.add(0,newResult);
								throw Backtracking.instance;
							}
							public boolean isPhaseTermination() {
								return false;
							}
							public String toString() {
								return "CollectResults;Backtrack;";
							}
						};
						AbstractInternalWorld targetWorld= residentRequest.target.internalWorld();
						Continuation c1= new NeutralizeActors(completion);
						processDirectMessageAndHandleBreak(
							c1,
							residentRequest.domainSignature,
							targetWorld,
							targetWorld,
							argumentList,
							rootCP);
						resultSetIterator= resultArray.iterator();
					};
					Term resultList= PrologEmptyList.instance;
					while (resultSetIterator.hasNext()) {
						Term currentResult= resultSetIterator.next();
						resultList= new PrologList(currentResult,resultList);
					};
					residentRequest.resident.returnResultList(residentRequest.target,resultList);
					//withdrawRequest(residentRequest.resident);
				} finally {
					synchronized(inputResidentRequests) {
						if (inputResidentRequests.contains(residentRequest)) {
							inputResidentRequests.remove(residentRequest);
							processedResidentRequests.add(residentRequest);
						};
						wakeUp();
					}
				}
				if (debugThisProcess()) {
					System.out.printf("%s: ============================================\n",this);
					System.out.printf("%s: END OF R.R.; hasUpdatedPorts= %s, processWasProvedOneTimeAtLeast=%s\n",this,hasUpdatedPorts,processWasProvedOneTimeAtLeast);
					System.out.printf("%s: ============================================\n",this);
				}
			}
		} else if (hasInformationDirectMessage) {
			boolean processWasSuspended= isSuspended;
			if (isToBeSuspended()) {
				if (!processWasSuspended) {
					finishPhaseBySuspension();
				};
				return;
			};
			phaseInitiation();
			boolean hasUpdatedPorts= acceptPortValues();
			if (hasUpdatedPorts || !processWasProvedOneTimeAtLeast) {
				if (debugThisProcess()) {
					System.out.printf("%s: ============================================\n",this);
					System.out.printf("%s: CALL FLOW MESSAGE INSTEAD OF I.D.M.; hasUpdatedPorts= %s, processWasProvedOneTimeAtLeast=%s\n",this,hasUpdatedPorts,processWasProvedOneTimeAtLeast);
					System.out.printf("%s: ============================================\n",this);
					printSlotVariables(this);
				};
				processFlowMessages(rootCP);
			} else {
				if (debugThisProcess()) {
					System.out.printf("%s: ============================================\n",this);
					System.out.printf("%s: CALL I.F.M MESSAGE; hasUpdatedPorts= %s, processWasProvedOneTimeAtLeast=%s\n",this,hasUpdatedPorts,processWasProvedOneTimeAtLeast);
					System.out.printf("%s: ============================================\n",this);
				};
				try {
					Continuation c1= new PhaseTermination();
					c1= new NeutralizeActors(c1);
					processDirectMessageAndHandleBreak(
						c1,
						directMessage.domainSignatureNumber,
						directMessage.target,
						directMessage.reserveTarget,
						directMessage.arguments,
						rootCP);
				} finally {
					synchronized(inputInformationDirectMessages) {
						if (inputInformationDirectMessages.contains(directMessage)) {
							inputInformationDirectMessages.remove(directMessage);
							if (!directMessage.useBuffer) {
								inputInformationDirectMessagesHash.remove(directMessage);
							}
						};
						wakeUp();
					}
				}
				if (debugThisProcess()) {
					System.out.printf("%s: ============================================\n",this);
					System.out.printf("%s: END OF I.F.M MESSAGE; hasUpdatedPorts= %s, processWasProvedOneTimeAtLeast=%s\n",this,hasUpdatedPorts,processWasProvedOneTimeAtLeast);
					System.out.printf("%s: ============================================\n",this);
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void acceptTimerMessage() {
		boolean hasUpdatedPorts= false;
		AbstractInternalWorld targetWorld= null;
		Iterator<AbstractInternalWorld> iterator= null;
		boolean flowMessageIsToBeProcessed= false;
		synchronized(timerMessages) {
			if (timerMessages.size() > 0) {
				boolean processWasSuspended= isSuspended;
				if (isToBeSuspended()) {
					if (!processWasSuspended) {
						finishPhaseBySuspension();
					};
					return;
				};
				phaseInitiation();
				hasUpdatedPorts= acceptPortValues();
				if (hasUpdatedPorts) {
					flowMessageIsToBeProcessed= true;
				} else {
					iterator= timerMessages.iterator();
					targetWorld= iterator.next();
					iterator.remove();
				}
			} else {
				return;
			}
		};
		if (flowMessageIsToBeProcessed) {
			if (debugThisProcess()) {
				System.out.printf("%s: ===========================================\n",this);
				System.out.printf("%s: CALL FLOW MESSAGE INSTEAD OF TIMER MESSAGE; hasUpdatedPorts= %s, processWasProvedOneTimeAtLeast=%s\n",this,hasUpdatedPorts,processWasProvedOneTimeAtLeast);
				System.out.printf("%s: ===========================================\n",this);
				printSlotVariables(this);
			};
			processFlowMessages(rootCP);
		} else {
			if (debugThisProcess()) {
				System.out.printf("%s: ============================================\n",this);
				System.out.printf("%s: CALL TIMER MESSAGE; hasUpdatedPorts= %s, processWasProvedOneTimeAtLeast=%s\n",this,hasUpdatedPorts,processWasProvedOneTimeAtLeast);
				System.out.printf("%s: ============================================\n",this);
				printSlotVariables();
			};
			boolean recentState= isProven;
			isProven= false;
			try {
				Continuation c1= new PhaseTermination();
				c1= new NeutralizeActors(c1);
				if (!processWasProvedOneTimeAtLeast) {
					c1= addGoals(c1,rootCP);
				};
				long domainSignature= ((morozov.built_in.Timer)targetWorld).entry_s_Tick_0();
				processDirectMessageAndHandleBreak(
					c1,
					domainSignature,
					targetWorld,
					targetWorld,
					new Term[0],
					rootCP);
			} finally {
				synchronized(timerMessages) {
					if (timerMessages.size() > 0) {
						wakeUp();
					}
				};
				if (!isProven) {
					if (recentState) {
						unsuccessfullyFinishPhase();
					} else {
						unsuccessfullyFinishPhaseAgain();
					}
				// This check cannot determine if the process
				// was suspended before this phase.
				};
				enableStateSending();
			}
		}
		if (debugThisProcess()) {
			System.out.printf("%s: O.K. TIMER MESSAGE;\n",this);
		}
	}
	//
	protected void processDirectMessageAndHandleBreak(Continuation c1, long domainSignature, Term target, AbstractWorld reserveTarget, Term[] arguments, ChoisePoint iX) {
		AbstractInternalWorld reserveWorld= reserveTarget.internalWorld();
		c1= new DomainSwitch(c1,domainSignature,target,reserveWorld,arguments);
		executeDirectMessageAndHandleBreak(c1,reserveWorld,iX);
	}
	//
	protected void executeDirectMessageAndHandleBreak(Continuation c1, AbstractInternalWorld targetWorld, ChoisePoint iX) {
		try {
			c1.execute(iX);
		} catch (Backtracking b) {
			backtrack(iX);
		} catch (ProcessedErrorExit e1) {
			if (c1.containsNode(e1.continuation)) {
				throw e1;
			} else {
				iX.freeTrail();
				try {
					long domainSignature= targetWorld.entry_s_Alarm_1_i();
					Continuation c2= new DomainSwitch(new DummyContinuation(),domainSignature,targetWorld,targetWorld,new Term[]{e1.createTerm()});
					c2.execute(iX);
					iX.freeTrail();
				} catch (Backtracking b) {
					throw new ProcessedErrorExit(e1.processedException,c1);
				} catch (ErrorExit e2) {
					throw new ProcessedErrorExit(e2,c1);
				} catch (Throwable e2) {
					throw new ProcessedErrorExit(new LowLevelErrorExit(iX,e2),c1);
				}
			}
		} catch (ErrorExit ee) {
			iX.freeTrail();
			try {
				long domainSignature= targetWorld.entry_s_Alarm_1_i();
				Continuation c2= new DomainSwitch(new DummyContinuation(),domainSignature,targetWorld,targetWorld,new Term[]{ee.createTerm()});
				c2.execute(iX);
				iX.freeTrail();
			} catch (Backtracking b) {
				throw new ProcessedErrorExit(ee,c1);
			} catch (ErrorExit e2) {
				throw new ProcessedErrorExit(e2,c1);
			} catch (Throwable e2) {
				throw new ProcessedErrorExit(new LowLevelErrorExit(iX,e2),c1);
			}
		} catch (Throwable re) {
			iX.freeTrail();
			try {
				long domainSignature= targetWorld.entry_s_Alarm_1_i();
				Continuation c2= new DomainSwitch(new DummyContinuation(),domainSignature,targetWorld,targetWorld,new Term[]{new PrologString(re.toString())});
				c2.execute(iX);
				iX.freeTrail();
			} catch (Backtracking b) {
				throw new ProcessedErrorExit(new LowLevelErrorExit(iX,re),c1);
			} catch (ErrorExit e2) {
				throw new ProcessedErrorExit(e2,c1);
			} catch (Throwable e2) {
				throw new ProcessedErrorExit(new LowLevelErrorExit(iX,e2),c1);
			}
		}
	}
	//
	public void sendStateOfProcess() {
		if (!stateIsToBeSend.compareAndSet(true,false)) {
			return;
		};
		synchronized(stateRequests) {
			Iterator<ProcessStateRequest> iterator= stateRequests.iterator();
			while (iterator.hasNext()) {
				ProcessStateRequest request= iterator.next();
				request.listener.rememberStateOfProcess(request.identifier,isProven,isSuspended);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void processFlowMessages(ChoisePoint iX) {
		AbstractInternalWorld[] neutralizedActors= neutralizeActors(iX);
		Continuation c1= new PhaseTermination();
		c1= toContinuation(neutralizedActors,c1);
		registerActorsToBeProved(neutralizedActors,iX);
		if (!processWasProvedOneTimeAtLeast) {
			c1= addGoals(c1,iX);
		};
		boolean recentState= isProven;
		isProven= false;
		try {
			if (debugThisProcess()) {
				System.out.printf("%s: CALL FLOW MESSAGE c1=%s;\n",this,c1);
			};
			c1.execute(iX);
			if (debugThisProcess()) {
				System.out.printf("%s: ============================================\n",this);
				System.out.printf("%s: O.K. FLOW MESSAGE; processWasProvedOneTimeAtLeast=%s\n",this,processWasProvedOneTimeAtLeast);
				System.out.printf("%s: ============================================\n",this);
			}
		} catch (Backtracking b) {
			if (debugThisProcess()) {
				System.out.printf("%s: ============================================\n",this);
				System.out.printf("%s: FAILURE IN: FLOW MESSAGE; processWasProvedOneTimeAtLeast=%s\n",this,processWasProvedOneTimeAtLeast);
				System.out.printf("%s: ============================================\n",this);
			};
			backtrack(iX);
		};
		if (!isProven) {
			if (recentState) {
				unsuccessfullyFinishPhase();
			} else {
				unsuccessfullyFinishPhaseAgain();
			}
		};
		enableStateSending();
	}
	//
	private Continuation addGoals(Continuation c1, ChoisePoint iX) {
		LinkedHashSet<AbstractInternalWorld> worlds= new LinkedHashSet<AbstractInternalWorld>();
		extractWorlds(worlds);
		worldArray= new AbstractInternalWorld[worlds.size()];
		worlds.toArray(worldArray);
		LinkedHashSet<AbstractInternalWorld> specialWorlds= new LinkedHashSet<AbstractInternalWorld>();
		if (worldArray != null) {
			for (int n=0; n < worldArray.length; n++) {
				if (worldArray[n].isSpecialWorld()) {
					specialWorlds.add(worldArray[n]);
				}
			}
		};
		specialWorldArray= new AbstractInternalWorld[specialWorlds.size()];
		specialWorlds.toArray(specialWorldArray);
		Continuation c2= toContinuation(worldArray,c1);
		registerActorsToBeProved(worldArray,iX);
		if (debugThisProcess()) {
			System.out.printf("%s: I will INITIATE process: %s\n",this,c1);
		};
		return c2;
	}
	//
	protected boolean removeNewlyProvedOldActors(HashSet<ActorNumber> oldActors) {
		boolean hasProvenActors= false;
		Iterator<ActorNumber> oldActorsIterator= oldActors.iterator();
		while (oldActorsIterator.hasNext()) {
			ActorNumber actor= oldActorsIterator.next();
			if (actorsToBeProved.contains(actor)) {
				oldActorsIterator.remove();
			} else {
				hasProvenActors= true;
			}
		};
		return hasProvenActors;
	}
	//
	public void clearActorStore() {
		actorsToBeProved.clear();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void registerActorToBeProved(ActorNumber actorNumber, ChoisePoint cp) {
		if (!actorsToBeProved.contains(actorNumber)) {
			actorsToBeProved.add(actorNumber);
			registerBinding(new ActorTableState(actorsToBeProved,actorNumber));
		}
	}
	//
	public void registerActorsToBeProved(AbstractWorld[] worldArray, ChoisePoint cp) {
		for(int i= 0; i < worldArray.length; i++)
			registerActorToBeProved(worldArray[i],cp);
	}
	//
	public void printActorsToBeProved() {
		System.out.println(actorsToBeProved);
	}
	//
	public Continuation createActorNeutralizationNode(Continuation aC) {
		return new NeutralizeActors(aC);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public class NeutralizeActors extends Continuation {
		private Continuation c0;
		public NeutralizeActors(Continuation aC) {
			c0= aC;
		}
		public void execute(ChoisePoint iX) throws Backtracking {
			neutralizeActorsAndContinue(c0,iX);
		}
		public String toString() {
			return "NeutrActors;" + c0.toString();
		}
	}
	//
	public class PhaseTermination extends Continuation {
		public void execute(ChoisePoint iX) throws Backtracking {
			successfullyFinishPhase(iX);
		}
		public boolean isPhaseTermination() {
			return true;
		}
		public String toString() {
			return "PhaseTermination;";
		}
	}
	//
	public void neutralizeActorsAndContinue(Continuation c0, ChoisePoint iX) throws Backtracking {
		ChoisePoint newIndex= new ChoisePoint(iX);
		boolean finishPhase= false;
		if (c0.isPhaseTermination()) {
			finishPhase= true;
		};
		HashSet<AbstractInternalWorld> neutralizedActors= convolveActualSlotValues(newIndex);
		if (finishPhase && neutralizedActors.size() == 0) {
			convolveNonactivatedSlotValues(neutralizedActors);
			successfullyFinishPhase(iX);
		} else {
			backtrack(newIndex);
			AbstractInternalWorld[] Array= new AbstractInternalWorld[neutralizedActors.size()];
			neutralizedActors.toArray(Array);
			Continuation c1= toContinuation(Array,c0);
			registerActorsToBeProved(Array,newIndex);
			c1.execute(newIndex);
		}
	}
	//
	public AbstractInternalWorld[] neutralizeActors(ChoisePoint iX) {
		ChoisePoint newIndex= new ChoisePoint(iX);
		HashSet<AbstractInternalWorld> neutralizedActors= convolveActualSlotValues(newIndex);
		backtrack(newIndex);
		AbstractInternalWorld[] Array= new AbstractInternalWorld[neutralizedActors.size()];
		neutralizedActors.toArray(Array);
		return Array;
	}
	//
	private void convolveNonactivatedSlotValues(HashSet<AbstractInternalWorld> neutralizedActors) {
		SlotVariable slotVariable;
		SlotVariableValue slotValue;
		HashSet<ActorNumber> newActors;
		HashSet<ActorNumber> oldActors;
		Term newValue;
		Term oldValue;
		Iterator<SlotVariable> slotVariablesIterator= slotVariables.iterator();
		while (slotVariablesIterator.hasNext()) {
			slotVariable= slotVariablesIterator.next();
			slotValue= slotVariable.get(this);
			if (slotValue==null) {
				continue;
			};
			newActors= slotValue.newActors;
			if (!newActors.isEmpty()) {
				continue;
			};
			oldActors= slotValue.oldActors;
			boolean skipActor= true;
			Iterator<ActorNumber> oldActorsIterator;
			oldActorsIterator= oldActors.iterator();
			while (oldActorsIterator.hasNext()) {
				ActorNumber actor= oldActorsIterator.next();
				if (neutralizedActors.contains(actor)) {
					continue;
				};
				if (!actorsToBeProved.contains(actor)) {
					skipActor= false;
					break;
				}
			};
			if (!skipActor) {
				slotValue.actualValue= slotValue.recentValue;
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void successfullyFinishPhase(ChoisePoint iX) {
		suspendedCallTable.clear();
		fixSlotVariables(true);
		storePredefinedClassRecords();
		sendActualFlowMessages();
		sendAsyncCalls(iX);
		if (specialWorldArray != null) {
			for (int n=0; n < specialWorldArray.length; n++) {
				specialWorldArray[n].finishPhaseSuccessfully();
			}
		};
		storeSlotVariables();
		isProven= true;
		processWasProvedOneTimeAtLeast= true;
		wakeUp();
		synchronized(inputResidentRequests) {
			inputResidentRequests.addAll(processedResidentRequests);
			processedResidentRequests.clear();
		};
		phaseInitiation();
		enableStateSending();
	}
	//
	public void unsuccessfullyFinishPhaseAgain() {
		informInternalWorldsAboutFailure();
	}
	//
	protected void informInternalWorldsAboutFailure() {
		if (specialWorldArray != null) {
			for (int n=0; n < specialWorldArray.length; n++) {
				specialWorldArray[n].finishPhaseUnsuccessfully();
			}
		}
	}
	//
	public void storePredefinedClassRecords() {
		for(int i= 0; i < predefinedClassesState.size(); i++) {
			PredefinedClassRecord item= predefinedClassesState.get(i);
			AbstractInternalWorld receiver= item.target;
			receiver.storeBacktrackableRecord(item);
		};
		predefinedClassesState.clear();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void debugInfo(long position, long unit, int fileNumber) {
		debugPosition= position;
		debugUnit= unit;
		debugFileNumber= fileNumber;
	}
}
