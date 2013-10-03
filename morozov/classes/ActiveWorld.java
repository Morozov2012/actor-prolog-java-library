// (c) 2009 IRE RAS Alexei A. Morozov

package morozov.classes;

import morozov.run.*;
import morozov.terms.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Collection;
import java.util.HashSet;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ActiveWorld extends AbstractWorld {
	protected boolean isSuspended= false;
	public ArrayList<Term> trail= new ArrayList<Term>();
	public long recentChoisePointNumber= 0;
	public HashSet<SlotVariable> slotVariables= new HashSet<SlotVariable>();
	public AtomicBoolean hasUpdatedPort= new AtomicBoolean(false);
	public ThreadHolder thread; // = new ThreadHolder(this);
	private ActorRegister rootActor= new ActorRegister(this,(ActorNumber)this);
	protected ChoisePoint rootCP= new ChoisePoint(null,rootActor);
	private boolean processIsFormed= false;
	protected boolean processWasProvedOneTimeAtLeast= false;
	protected HashSet<ActorNumber> actorsToBeProved= new HashSet<ActorNumber>();
	protected AtomicBoolean stateIsToBeSend= new AtomicBoolean(false);
	public long debugPosition= -1;
	public long debugUnit= -1;
	public int debugFileNumber= -1;
	//
	public ActiveWorld() {
		this(true,false);
	}
	public ActiveWorld(boolean createThreadHolder, boolean isDaemon) {
		if (createThreadHolder) {
			thread= new ThreadHolder(this);
			thread.setDaemon(isDaemon);
		}
	}
	// public ActiveWorld(ThreadHolder holder, boolean isDaemon) {
	//	thread= holder;
	//	thread.setDaemon(isDaemon);
	// }
	// CONTROL LOCAL TRAIL AND BACKTRACKING OF PROCESS
	public void registerBinding(Term v) {
		trail.add(v);
	}
	public void forgetBindings(int previousSize) {
		int currentSize= trail.size();
		if (previousSize<currentSize) {
			ListIterator<Term> iterator= trail.listIterator(previousSize);
			while (iterator.hasNext()) {
				Term t= iterator.next();
				t.clear();
			};
			trail.subList(previousSize,currentSize).clear();
		}
	}
	public void backtrack(ChoisePoint baseCP) {
		baseCP.freeTrail();
	}
	public void backtrack(long choisePointNumber, int initialSizeOfTrail) {
		forgetBindings(initialSizeOfTrail);
		recentChoisePointNumber= choisePointNumber - 1;
	}
	// public void backtrack(ChoisePoint iX) {
	//	if (trail!=null) {
	//		trail.freeTrail(iX);
	//	}
	// }
	protected void resetTrail() {
		trail.clear();
		recentChoisePointNumber= 0;
	}
	//
	abstract public void receiveTimerMessage(AbstractWorld target);
	abstract public void cancelTimerMessage(AbstractWorld target);
	//
	abstract public Continuation collectSuspendedCalls(Continuation c0, ChoisePoint iX);
	// Sending Flow Messages
	public void sendActualFlowMessages() {
		sendFlowMessages(false);
	}
	public void sendEmptyFlowMessages() {
		sendFlowMessages(true);
	}
	public void sendFlowMessages(boolean sendEmptyValues) {
		if (debugAllProcesses() || debugThisProcess()) {
			System.out.printf("%s:##########################;\n",this);
			System.out.printf("%s: I will SEND Flow Messages;\n",this);
			System.out.printf("%s:##########################;\n",this);
			printSlotVariables(this);
			System.out.printf("%s:##########################;\n",this);
		};
		ChoisePoint iX= new ChoisePoint(rootCP);
		Iterator<SlotVariable> slotVariablesIterator= slotVariables.iterator();
		int slotN= 0;
		while (slotVariablesIterator.hasNext()) {
			slotN= slotN + 1;
			if (debugThisProcess()) {
				System.out.printf("%s: I will SEND F.M.; variable [%d]\n",this,slotN);
			};
			SlotVariable slotVariable= slotVariablesIterator.next();
			sendFlowMessage(slotVariable,iX,sendEmptyValues);
		};
		slotVariablesIterator= slotVariables.iterator();
		while (slotVariablesIterator.hasNext()) {
			SlotVariable slotVariable= slotVariablesIterator.next();
		};
		if (debugAllProcesses() || debugThisProcess()) {
			System.out.printf("%s:##########################;\n",this);
			System.out.printf("%s: I HAVE SEND Flow Messages;\n",this);
			System.out.printf("%s:##########################;\n",this);
			printSlotVariables(this);
			System.out.printf("%s:##########################;\n",this);
		};
	}
	public void sendFlowMessage(SlotVariable slotVariable, ChoisePoint iX, boolean sendEmptyValues) {
		synchronized(slotVariable) {
			SlotVariableValue slotValue= slotVariable.get(this);
			if (slotValue==null) {
				if (debugThisProcess()) {
					System.out.printf("%s: I will SEND F.M.; slotValue==NULL;\n",this);
				};
				// continue;
				return;
			};
			Term newValue= null;
			boolean flowMessageIsProtected= false;
			if (!sendEmptyValues) {
				newValue= slotValue.actualValue;
				flowMessageIsProtected= slotValue.isProtectingPort;
			// } else {
			//	newValue= new PrologVariable();
			};
			if (newValue==null) {
				flowMessageIsProtected= false;
			};
			// boolean flowMessageIsProtected= (newValue!=null && slotValue.isProtectingPort);
			Term portValue= slotValue.portValue;
			boolean portValueIsProtected= slotValue.portValueIsProtected;
			ActiveWorld portValueOwner= slotValue.portValueOwner;
			// Path I, II:
			// ActorRegister register= new ActorRegister(this,(ActorNumber)this);
			// ChoisePoint iX= new ChoisePoint(null,rootActor);
			boolean valuesAreEqual= false;
			// Later:
			if (newValue==null && portValue==null) {
				valuesAreEqual= true;
				if (debugThisProcess()) {
					System.out.printf("%s: I will SEND F.M.; newValue==null && portValue==null;\n",this);
				}
			} else if (newValue!=null && portValue!=null) {
				if (debugThisProcess()) {
					System.out.printf("%s: I will SEND F.M.; newValue!=null && portValue!=null;\n",this);
				};
				try {
					newValue.unifyWith(portValue,iX);
					valuesAreEqual= true;
				} catch (Backtracking b) {
					backtrack(iX);
				}
			};
			if (debugThisProcess()) {
				System.out.printf("%s: I will SEND F.M.; valuesAreEqual=%s;\n",this,valuesAreEqual);
			};
			boolean sortsAreEqual= false;
			if (portValueIsProtected==flowMessageIsProtected) {
				sortsAreEqual= true;
			};
			if (valuesAreEqual && sortsAreEqual) {
				slotValue.portIsUpdated= false;
				// continue;
				return;
			};
			boolean ownersAreEqual= false;
			if (portValueOwner==null || portValueOwner==this) {
				ownersAreEqual= true;
			};
			if (portValueIsProtected && !flowMessageIsProtected && !ownersAreEqual) {
				// continue;
				return;
			};
			slotValue.portValue= newValue;
			slotValue.portValueIsProtected= flowMessageIsProtected;
			slotValue.portValueOwner= this;
			slotValue.portIsUpdated= false;
			// Path III:
			Term globalValue= slotVariable.globalValue;
			valuesAreEqual= false;
			if (newValue==null && globalValue==null) {
				valuesAreEqual= true;
			} else if (newValue!=null && globalValue!=null) {
				try {
					newValue.unifyWith(globalValue,iX);
					valuesAreEqual= true;
				} catch (Backtracking b) {
					backtrack(iX);
				}
			};
			boolean globalValueIsProtected= slotVariable.globalValueIsProtected;
			sortsAreEqual= false;
			if (globalValueIsProtected==flowMessageIsProtected) {
				sortsAreEqual= true;
			};
			if (valuesAreEqual && sortsAreEqual) {
				// continue;
				return;
			};
			ActiveWorld globalValueOwner= slotVariable.globalValueOwner;
			ownersAreEqual= false;
			if (globalValueOwner==null || globalValueOwner==this) {
				ownersAreEqual= true;
			};
			if (globalValueIsProtected && !flowMessageIsProtected && !ownersAreEqual) {
				// continue;
				return;
			};
			slotVariable.globalValue= newValue;
			slotVariable.globalValueIsProtected= flowMessageIsProtected;
			slotVariable.globalValueOwner= this;
			// Path IV:
			HashMap<ActiveWorld,SlotVariableValue> processTable= slotVariable.processTable;
			//
			slotVariable.processTableLock.readLock().lock();
			try {
				Collection<SlotVariableValue> tableValuesCollection= processTable.values();
				Iterator<SlotVariableValue> tableValuesIterator= tableValuesCollection.iterator();
				while (tableValuesIterator.hasNext()) {
					SlotVariableValue tableItem= tableValuesIterator.next();
					if (tableItem==null) {
						continue;
					};
					ActiveWorld slotValueOwner= tableItem.slotValueOwner;
					if (slotValueOwner==this) {
						continue;
					};
					Term targetValue= tableItem.portValue;
					valuesAreEqual= false;
					if (newValue==null && targetValue==null) {
						valuesAreEqual= true;
					} else if (newValue!=null && targetValue!=null) {
						try {
							newValue.unifyWith(targetValue,iX);
							valuesAreEqual= true;
						} catch (Backtracking b) {
							backtrack(iX);
						}
					};
					boolean targetValueIsProtected= tableItem.portValueIsProtected;
					sortsAreEqual= false;
					if (targetValueIsProtected==flowMessageIsProtected) {
						sortsAreEqual= true;
					};
					ActiveWorld targetValueOwner= tableItem.portValueOwner;
					ownersAreEqual= false;
					if (targetValueOwner==null || targetValueOwner==this) {
						ownersAreEqual= true;
					};
					if (valuesAreEqual && sortsAreEqual) {
						if (!ownersAreEqual) {
							tableItem.portValueOwner= this;
						};
						continue;
					};
					boolean hasLocalOwner= false;
					if (targetValueOwner!=null && targetValueOwner==slotValueOwner) {
						hasLocalOwner= true;
					};
					if (targetValueIsProtected && !flowMessageIsProtected && hasLocalOwner) {
						continue;
					};
					if (debugThisProcess()) {
						System.out.printf("%s: I will SEND F.M.; (%s).portValue:=newValue = %s;\n",this,tableItem.slotValueOwner,newValue);
						System.out.printf("%s: I will SEND F.M.; (%s).flowMessageIsProtected:= %s;\n",this,tableItem.slotValueOwner,flowMessageIsProtected);
						System.out.printf("%s: I will SEND F.M.; (%s).portValueOwner:= %s;\n",this,tableItem.slotValueOwner,this);
					};
					tableItem.portValue= newValue;
					tableItem.portValueIsProtected= flowMessageIsProtected;
					tableItem.portValueOwner= this;
					tableItem.portIsUpdated= true;
					slotValueOwner.hasUpdatedPort.set(true);
					slotValueOwner.wakeUp();
				}
			} finally {
				slotVariable.processTableLock.readLock().unlock();
			}
		}
	}
	// Start & Wake Up
	public void start() {
		if (thread!=null) {
			thread.start();
		}
	}
	public void wakeUp() {
		if (thread!=null) {
			thread.wakeUp();
		}
	}
	public void stop() {
		if (thread!=null) {
			thread.terminate();
		}
	}
	// ACCEPT MESSAGES
	abstract protected void acceptDirectMessage();
	abstract protected void acceptTimerMessage();
	abstract protected void sendStateOfProcess();
	// Process Flow Message
	public void acceptFlowMessages() {
		// System.out.printf("ActiveWorld::acceptFlowMessages[1]: %s\n",this);
		if (!hasUpdatedPort.compareAndSet(true,false)) {
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
		if (!processIsFormed) {
			actualize(rootCP);
			processIsFormed= true;
		};
		if (debugThisProcess()) {
			System.out.printf("%s: (3) CALL PHASE INITIATION (for F.M.);\n",this);
		};
		phaseInitiation();
		if (debugThisProcess()) {
			System.out.printf("%s: ============================================\n",this);
			System.out.printf("%s: CALL acceptPortValues(); processWasProvedOneTimeAtLeast=%s\n",this,processWasProvedOneTimeAtLeast);
			System.out.printf("%s: ============================================\n",this);
			printSlotVariables(this);
		};
		boolean hasUpdatedPorts= acceptPortValues();
		if (debugThisProcess()) {
			System.out.printf("%s: ============================================\n",this);
			System.out.printf("%s: CALL FLOW MESSAGE; hasUpdatedPorts= %s, processWasProvedOneTimeAtLeast=%s\n",this,hasUpdatedPorts,processWasProvedOneTimeAtLeast);
			System.out.printf("%s: ============================================\n",this);
			printSlotVariables(this);
		};
		processFlowMessages(rootCP);
		if (debugThisProcess()) {
			System.out.printf("%s: === O.K. === acceptFlowMessage;\n",this);
		}
	}
	//
	public boolean isToBeSuspended() {
		Iterator<SlotVariable> slotVariablesIterator= slotVariables.iterator();
		while (slotVariablesIterator.hasNext()) {
			SlotVariable slotVariable= slotVariablesIterator.next();
			SlotVariableValue slotValue= slotVariable.get(this);
			if (slotValue==null) {
				continue;
			} else if (slotValue.isSuspendingPort) {
				Term portValue= slotValue.portValue;
				if (portValue==null || portValue.dereferenceValue(null).thisIsUnknownValue()) {
					isSuspended= true;
					return true;
				}
			}
		};
		isSuspended= false;
		return false;
	}
	//
	public void phaseInitiation() {
		// System.out.printf("ActiveWorld::phaseInitiation()[1]: %s\n",this);
		if (debugThisProcess()) {
			System.out.printf("%s: I will PREPARE variables;\n",this);
		};
		Iterator<SlotVariable> slotVariablesIterator= slotVariables.iterator();
		while (slotVariablesIterator.hasNext()) {
			SlotVariable slotVariable= slotVariablesIterator.next();
			SlotVariableValue slotValue= slotVariable.get(this);
			if (slotValue==null) {
				continue;
			};
			if (debugAllProcesses() || debugThisProcess()) {
				System.out.printf("%s:##########################;\n",this);
				System.out.printf("%s:slotValue.newActors=%s;\n",this,slotValue.newActors);
				System.out.printf("%s:slotValue.newActors.clear();\n",this);
				System.out.printf("%s:##########################;\n",this);
			};
			slotValue.actualValue= null;
			slotValue.newActors.clear();
		};
		resetTrail();
		// actorsToBeProved.clear();
		clearActorStore();
	}
	//
	public void storeSlotVariables() {
		if (debugThisProcess()) {
			System.out.printf("%s: //////////////////////;\n",this);
			System.out.printf("%s: I will STORE variables;\n",this);
			System.out.printf("%s: //////////////////////;\n",this);
		};
		Iterator<SlotVariable> slotVariablesIterator= slotVariables.iterator();
		int slotN= 0;
		while (slotVariablesIterator.hasNext()) {
			slotN= slotN + 1;
			if (debugThisProcess()) {
				System.out.printf("%s: VARIABLE STORE; variable #%d;\n",this,slotN);
			};
			SlotVariable slotVariable= slotVariablesIterator.next();
			SlotVariableValue slotValue= slotVariable.get(this);
			if (slotValue==null) {
				if (debugThisProcess()) {
					System.out.printf("%s: VARIABLE STORE; slotValue==null;\n",this);
				};
				continue;
			};
			HashSet<ActorNumber> oldActors= slotValue.oldActors;
			boolean hasProvenActors= removeNewlyProvedOldActors(oldActors);
			if (debugThisProcess()) {
				System.out.printf("%s: VARIABLE STORE; hasProvenActors: %s;\nSlotValue= %s;\n",this,hasProvenActors,slotValue);
			};
			//
			HashSet<ActorNumber> newActors= slotValue.newActors;
			if (newActors.isEmpty()) {
				if (debugThisProcess()) {
					System.out.printf("%s: [%d]newActors.isEmpty();\n",this,slotN);
				};
				if (!hasProvenActors) {
					slotValue.recentValue= PrologUnknownValue.instance;
					if (debugThisProcess()) {
						System.out.printf("%s: [%d]slotValue.recentValue:= PrologUnknownValue.instance = %s;\n",this,slotN,slotValue.recentValue);
					}
				}
			} else {
				slotValue.recentValue= slotValue.actualValue;	// 25.08.2009
				if (debugThisProcess()) {
					System.out.printf("%s: [%d]slotValue.recentValue= slotValue.actualValue = %s;\n",this,slotN,slotValue.recentValue);
				};
				boolean hasNewTemporaryActor= false;
				Iterator<ActorNumber> newActorsIterator= newActors.iterator();
				while (newActorsIterator.hasNext()) {
					ActorNumber currentNewActor= newActorsIterator.next();
					if (currentNewActor.isTemporary()) {
						if (!hasNewTemporaryActor) {
							hasNewTemporaryActor= true;
							boolean hasOldTemporaryActor= false;
							Iterator<ActorNumber> oldActorsIterator= oldActors.iterator();
							while (oldActorsIterator.hasNext()) {
								ActorNumber currentOldActor= oldActorsIterator.next();
								if (currentOldActor.isTemporary()) {
									hasOldTemporaryActor= true;
									break;
								}
							};
							if (!hasOldTemporaryActor) {
								oldActors.add(currentNewActor);
							}
						}
					} else {
						oldActors.add(currentNewActor);
					}
				}
			};
			slotValue.actualValue= null;
			//
			oldActors.remove((AbstractWorld)this);
			if (debugAllProcesses() || debugThisProcess()) {
				System.out.printf("%s:##########################;\n",this);
				System.out.printf("%s:newActors.clear();;\n",this);
				System.out.printf("%s:##########################;\n",this);
			};
			newActors.clear();
			//
			if (debugThisProcess()) {
				System.out.printf("{2} %s: I have STORED variable: %s;\n",this,slotValue);
			}
		};
		// actorsToBeProved.clear();
		clearActorStore();
	}
	//
	abstract protected boolean removeNewlyProvedOldActors(HashSet<ActorNumber> oldActors);
	//
	abstract protected void clearActorStore();
	// public void clearActorStore() {
	//	actorsToBeProved.clear();
	// }
	//
	abstract protected void processFlowMessages(ChoisePoint iX);
	//
	// abstract public void sendResidentRequest(AbstractWorld target, Resident resident, long domainSignature, Term[] arguments, ChoisePoint iX);
	abstract public void sendResidentRequest(AbstractWorld target, Resident resident, long domainSignature, Term[] arguments, boolean sortAndReduceResultList, ChoisePoint iX);
	abstract public void withdrawRequest(AbstractWorld target, Resident resident);
	//
	protected boolean acceptPortValues() {
		boolean hasUpdatedPorts= false;
		Iterator<SlotVariable> slotVariablesIterator= slotVariables.iterator();
		int slotN= 0;
		while (slotVariablesIterator.hasNext()) {
			slotN= slotN + 1;
			if (debugThisProcess()) {
				System.out.printf("%s: F.M.; Phase 0; #%d;\n",this,slotN);
			};
			SlotVariable slotVariable= slotVariablesIterator.next();
			hasUpdatedPorts= acceptPortValue(slotVariable) || hasUpdatedPorts;
		};
		return hasUpdatedPorts;
		// recent
	}
	protected boolean acceptPortValue(SlotVariable slotVariable) {
		boolean portIsUpdated= false;
		synchronized(slotVariable) {
			SlotVariableValue slotValue= slotVariable.get(this);
			if (slotValue==null) {
				if (debugThisProcess()) {
					System.out.printf("%s: F.M.; Phase 0; slotValue=NULL=%s;\n",this,slotValue);
				};
				// continue;
				return portIsUpdated;
			};
			// Path I
			Term portValue= slotValue.portValue;
			if (debugThisProcess()) {
				System.out.printf("%s: F.M.; Phase I; portValue=%s\n",this,portValue);
				System.out.printf("%s: F.M.; Phase I; portValueOwner=%s\n",this,slotValue.portValueOwner);
				System.out.printf("%s: F.M.; Phase I; portValueIsProtected=%s\n",this,slotValue.portValueIsProtected);
				System.out.printf("%s: F.M.; Phase I; portIsUpdated=%s\n",this,slotValue.portIsUpdated);
			};
			if (portValue==null) {
				if (debugThisProcess()) {
					System.out.printf("%s: F.M.; Phase I; portValue=NULL\n",this);
				}
				// continue;
			} else if (slotValue.portValueOwner==this) {
				if (debugThisProcess()) {
					System.out.printf("%s: F.M.; Phase I; slotValue.portValueOwner==THIS\n",this);
				}
				continueKeepingRecentValue(slotValue);
			} else if (slotValue.portIsUpdated) {
				portIsUpdated= true;
				if (slotValue.isProtectingPort && !slotValue.portValueIsProtected) {
					if (debugThisProcess()) {
						System.out.printf("%s: F.M.; Phase I; slotValue.isProtectingPort && !slotValue.portValueIsProtected\n",this);
					}
					continueKeepingRecentValue(slotValue);
				} else {
					if (debugThisProcess()) {
						System.out.printf("%s: F.M.; Phase I; slotValue.actualValue:= slotValue.portValue = %s;\n",this,slotValue.portValue);
					};
					slotValue.actualValue= slotValue.portValue;
					ActorNumber newActor= new TemporaryActor();
					slotValue.newActors.add(newActor);
					registerActorToBeProved(newActor,rootCP);
				}
			} else if (slotValue.isSuspendingPort) {
				if (debugThisProcess()) {
					System.out.printf("%s: F.M.; Phase I; slotValue.actualValue:= slotValue.portValue = %s;\n",this,slotValue.portValue);
				}
				slotValue.actualValue= slotValue.portValue;
				ActorNumber newActor= new TemporaryActor();
				slotValue.newActors.add(newActor);
				registerActorToBeProved(newActor,rootCP);
			} else if (!slotValue.isProtectingPort && slotValue.portValueIsProtected) {
				if (debugThisProcess()) {
					System.out.printf("%s: F.M.; Phase I; slotValue.actualValue:= slotValue.portValue = %s;\n",this,slotValue.portValue);
				}
				slotValue.actualValue= slotValue.portValue;
				ActorNumber newActor= new TemporaryActor();
				slotValue.newActors.add(newActor);
				registerActorToBeProved(newActor,rootCP);
			};
			// Path II
			slotValue.portIsUpdated= false;
			// Path III
			// ActorNumber newActor= new morozov.terms.TemporaryActor();
			// ActorNumber newActor= new TemporaryActor();
			// slotValue.newActors.add(newActor);
			// registerActorToBeProved(newActor,rootCP);
		};
		if (debugThisProcess()) {
			System.out.printf("%s: F.M.; Phase 0; O.K.;\n",this);
		};
		return portIsUpdated;
	}
	protected void continueKeepingRecentValue(SlotVariableValue slotValue) {
		if (slotValue.recentValue!=null) {
			boolean hasTemporaryActor= false;
			Iterator<ActorNumber> oldActorsIterator= slotValue.oldActors.iterator();
			while (oldActorsIterator.hasNext()) {
				ActorNumber currentOldActor= oldActorsIterator.next();
				if (currentOldActor.isTemporary()) {
					hasTemporaryActor= true;
					break;
				}
			};
			if (!hasTemporaryActor) {
				// ActorNumber oldActor= new morozov.terms.TemporaryActor();
				ActorNumber oldActor= new TemporaryActor();
				slotValue.oldActors.add(oldActor);
			}
		}
	}
	//
	abstract protected void resetResidentOwners();
	//
	abstract protected void registerActorToBeProved(ActorNumber actorNumber, ChoisePoint cp);
	abstract public Continuation createActorNeutralizationNode(Continuation aC);
	// SLOT VARIABLES REGISTER
	public void registerVariable(Term item) {
		item= item.extractSlotVariable();
		SlotVariable v= (SlotVariable)item;
		slotVariables.add(v);
	}
	//
	public void fixSlotVariables(boolean circumscribeVariables) {
		Iterator<Term> trailIterator= trail.iterator();
		while (trailIterator.hasNext()) {
			Term trailItem= trailIterator.next();
			trailItem.registerNewSlotVariable(slotVariables);
		};
		Iterator<SlotVariable> slotVariablesIterator= slotVariables.iterator();
		while (slotVariablesIterator.hasNext()) {
			SlotVariable slotVariable= slotVariablesIterator.next();
			SlotVariableValue slotValue= slotVariable.get(this);
			if (slotValue==null) {
				continue;
			};
			if (slotValue.actualValue==null) {
				if (slotValue.oldActors.isEmpty()) {
					slotValue.actualValue= PrologUnknownValue.instance;
				} else {
					slotValue.actualValue= slotValue.recentValue;
				}
			} else {
				if (circumscribeVariables) {
					slotValue.actualValue= slotValue.actualValue.circumscribe();
				} else {
					slotValue.actualValue= slotValue.actualValue.dereferenceValue(rootCP);
				}
			}
		}
	}
	public void printSlotVariables() {
		System.out.printf("%s SLOT VARIABLES: %s\n",this,slotVariables);
	}
	//
	public void printSlotVariables(ActiveWorld process) {
		Iterator<SlotVariable> slotVariablesIterator= slotVariables.iterator();
		int slotN= 0;
		while (slotVariablesIterator.hasNext()) {
			slotN= slotN + 1;
			SlotVariable slotVariable= slotVariablesIterator.next();
			SlotVariableValue slotValue= slotVariable.get(process);
			if (slotValue==null) {
				continue;
			}
		}
	}
	//
	protected void actualizeValues(ChoisePoint iX, Term... args) throws Backtracking {
		ChoisePoint newIndex= new ChoisePoint(iX);
		HashSet<AbstractWorld> actorsToBeProved= convolveActualSlotValues(newIndex);
		if (actorsToBeProved.isEmpty()) {
			Term[] copies= new Term[args.length];
			for (int i= 0; i < args.length; i++) {
				copies[i]= args[i].copyValue(newIndex,TermCircumscribingMode.CLONE_FREE_VARIABLES);
			};
			newIndex.freeTrail();
			for (int i= 0; i < args.length; i++) {
				args[i].unifyWith(copies[i],newIndex);
			}
		} else {
			newIndex.freeTrail();
			throw Backtracking.instance;
		}
	}
	protected HashSet<AbstractWorld> convolveActualSlotValues(ChoisePoint iX) {
		SlotVariable slotVariable;
		SlotVariableValue slotValue;
		HashSet<ActorNumber> newActors;
		HashSet<ActorNumber> oldActors;
		HashSet<AbstractWorld> neutralizedActors= new HashSet<AbstractWorld>();
		Term newValue;
		Term oldValue;
		ChoisePoint baseIx= new ChoisePoint(iX);
		ChoisePoint newIx= baseIx;
		Iterator<SlotVariable> slotVariablesIterator= slotVariables.iterator();
		while (slotVariablesIterator.hasNext()) {
			slotVariable= slotVariablesIterator.next();
			slotValue= slotVariable.get(this);
			if (slotValue==null) {
				continue;
			};
			if (debugActorsOfThisProcess()) {
				System.out.printf("%s: NEUTR(1).; SlotValue.recentValue=%s;\n",this,slotValue.recentValue);
				System.out.printf("%s: NEUTR(1).; SlotValue.actualValue=%s;\n",this,slotValue.actualValue);
				System.out.printf("%s: NEUTR(1).; SlotValue.oldActors=%s;\n",this,ActorNumber.toCompactString(slotValue.oldActors));
				System.out.printf("%s: NEUTR(1).; SlotValue.newActors=%s;\n",this,ActorNumber.toCompactString(slotValue.newActors));
				System.out.printf("%s: NEUTR(1).; SlotValue.portIsUpdated=%s;\n",this,slotValue.portIsUpdated);
				System.out.printf("%s: NEUTR(1).; SlotValue.portValue=%s;\n",this,slotValue.portValue);
				System.out.printf("%s: NEUTR(1).; SlotValue.portValueOwner=%s;\n",this,slotValue.portValueOwner);
			};
			newActors= slotValue.newActors;
			if (newActors.isEmpty()) {
				if (debugActorsOfThisProcess()) {
					System.out.printf("%s: NEUTR(2).; newActors.isEmpty();\n",this);
				};
				continue;
			};
			oldActors= slotValue.oldActors;
			boolean skipSlot= true;
			Iterator<ActorNumber> oldActorsIterator;
			oldActorsIterator= oldActors.iterator();
			while (oldActorsIterator.hasNext()) {
				ActorNumber actor= oldActorsIterator.next();
				if (neutralizedActors.contains(actor)) {
					if (debugActorsOfThisProcess()) {
						System.out.printf("%s: NEUTR(3).; neutralizedActors.contains(%s);\n",this,actor);
					};
					continue;
				};
				if (!actorsToBeProved.contains(actor)) {
					if (debugActorsOfThisProcess()) {
						System.out.printf("%s: NEUTR(3).; !actorsToBeProved.contains(%s);\n",this,actor);
					};
					skipSlot= false;
					break;
				}
			}
			if (debugActorsOfThisProcess()) {
				System.out.printf("%s: NEUTR(3).; skipSlot= %s;\n",this,skipSlot);
			};
			if (skipSlot) {
				continue;
			};
			oldValue= slotValue.recentValue;
			if (oldValue != null) {
				if (slotValue.actualValue == null) {
					slotValue.actualValue= new PrologVariable();
				};
				newValue= slotValue.actualValue;
				newIx= new ChoisePoint(newIx);
				try {
					oldValue.unifyWith(newValue,newIx);	// DEBUG:2010.04.25
				} catch (Backtracking b) {
					backtrack(newIx);
					oldActorsIterator= oldActors.iterator();
					while (oldActorsIterator.hasNext()) {
						ActorNumber actor= oldActorsIterator.next();
						if (actorsToBeProved.contains(actor)) {
							continue;
						} else {
							if (!actor.isTemporary()) {
								if (debugActorsOfThisProcess()) {
									System.out.printf("%s: NEUTR(4): neutralizedActors.add(%s)\n",this,actor);
								};
								neutralizedActors.add((AbstractWorld)actor);
							}
						}
					}
				}
			}
		}
		// if (!finishPhase || neutralizedActors.size() > 0) {
		//	backtrack(baseIx);
		// };
		return neutralizedActors;
	}
	//
	public HashSet<ActorNumber> getActorIsToBeProved() {
		return actorsToBeProved;
	}
	//
	public void finishPhaseBySuspension() {
		sendEmptyFlowMessages();
		resetResidentOwners();
		// stateIsToBeSend.set(true);
		enableStateSending();
	}
	public void unsuccessfullyFinishPhase() {
		sendEmptyFlowMessages();
		resetResidentOwners();
		informInternalWorldsAboutFailure();
	}
	public void enableStateSending() {
		stateIsToBeSend.set(true);
	}
	abstract protected void informInternalWorldsAboutFailure();
	//
	// AUXILIARY METHODS
	abstract public void actualize(ChoisePoint iX);
	//
	public boolean isTemporary() {
		return true;
	}
	//
	public boolean debugThisProcess() {
		return false;
		// return true;
	}
	public boolean debugActorsOfThisProcess() {
		return false;
		// return true;
	}
	public boolean debugAllProcesses() {
		return false;
		// return true;
	}
	public String toString() {
		return "(" + super.toString() + ")";
	}
}
