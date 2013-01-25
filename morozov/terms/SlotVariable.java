// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.classes.*;
import morozov.domains.*;
import morozov.run.*;

import java.nio.charset.CharsetEncoder;
import java.math.BigInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SlotVariable extends Term {
	public HashMap<ActiveWorld,SlotVariableValue> processTable= new HashMap<ActiveWorld,SlotVariableValue>();
	// public ReentrantReadWriteLock processTableLock= new ReentrantReadWriteLock(true);
	public ReentrantReadWriteLock processTableLock= new ReentrantReadWriteLock(false);
	public Term globalValue;
	public boolean globalValueIsProtected= false;
	public ActiveWorld globalValueOwner;
	// public boolean isTemporary() {
	//	return true;
	// }
	//
	public boolean equals(Object o) {
		// System.out.printf("EQUALS:\n%s\n%s\n",this,o);
		return this==o;
	}
	public int hashCode() {
		return System.identityHashCode(this);
	}
	// Slot value access
	public SlotVariableValue get(ActiveWorld process) {
		SlotVariableValue value= null;
		processTableLock.readLock().lock();
		try {
			value= processTable.get(process);
			// processTableLock.readLock().unlock();
		} finally {
			processTableLock.readLock().unlock();
		};
		return value;
	}
	//
	public final Term getValue(ChoisePoint cp) {
		ActorRegister aR= cp.actorRegister;
		ActiveWorld currentProcess= aR.currentProcess;
		ActorNumber actorNumber= aR.currentActorNumber;
		if (actorNumber==null) {
			throw new IllegalActorNumber();
		};
		SlotVariableValue slotValue= null;
		processTableLock.readLock().lock();
		try {
			slotValue= processTable.get(currentProcess);
			// processTableLock.readLock().unlock();
		} finally {
			processTableLock.readLock().unlock();
		};
		if (slotValue == null) {
			processTableLock.writeLock().lock();
			try {
				slotValue= processTable.get(currentProcess);
				if (slotValue == null) {
					slotValue= new SlotVariableValue(currentProcess);
					slotValue.actualValue= new PrologVariable();
					processTable.put(currentProcess,slotValue);
				} else {
					if (slotValue.actualValue == null) {
						slotValue.actualValue= new PrologVariable();
					}
				}
				// processTableLock.writeLock().unlock();
			} finally {
				processTableLock.writeLock().unlock();
			};
		} else {
			if (slotValue.actualValue == null) {
				slotValue.actualValue= new PrologVariable();
			}
		};
		if (!slotValue.newActors.contains(actorNumber)) {
                        slotValue.newActors.add(actorNumber);
			cp.pushTrail(new SlotVariableValueState(slotValue.newActors,actorNumber));
		};
		return slotValue.actualValue;
	}
	//
	public final void wakeUp() {
		processTableLock.readLock().lock();
		try {
			Set<ActiveWorld> keySet= processTable.keySet();
			Iterator<ActiveWorld> iterator= keySet.iterator();
			while (iterator.hasNext()) {
				ActiveWorld process= iterator.next();
				if (process!=null) {
					process.wakeUp();
				}
			}
			// processTableLock.readLock().unlock();
		} finally {
			processTableLock.readLock().unlock();
		}
	}
	//
	// "Is a ..." functions
	public void isInteger(int v, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.isInteger(v,cp);
	}
	public void isInteger(BigInteger v, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.isInteger(v,cp);
	}
	public void isReal(double v, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.isReal(v,cp);
	}
	public void isSymbol(long v, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.isSymbol(v,cp);
	}
	public void isString(String v, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.isString(v,cp);
	}
	public Term[] isStructure(long aFunctor, int aArity, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		return value.isStructure(aFunctor,aArity,cp);
	}
	public void isEmptyList(ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.isEmptyList(cp);
	}
	public void isEmptySet(ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.isEmptySet(cp);
	}
	public void isUnknownValue(ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.isUnknownValue(cp);
	}
	public void isWorld(Term v, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.isWorld(v,cp);
	}
	public boolean thisIsSlotVariable() {
		return true;
	}
	// "Get ... value" functions
	public Term dereferenceValue(ChoisePoint cp) {
		Term value= getValue(cp);
		return value.dereferenceValue(cp);
	}
	public BigInteger getIntegerValue(ChoisePoint cp) throws TermIsNotAnInteger {
		Term value= getValue(cp);
		return value.getIntegerValue(cp);
	}
	public int getSmallIntegerValue(ChoisePoint cp) throws TermIsNotAnInteger {
		Term value= getValue(cp);
		return value.getSmallIntegerValue(cp);
	}
	public long getLongIntegerValue(ChoisePoint cp) throws TermIsNotAnInteger {
		Term value= getValue(cp);
		return value.getLongIntegerValue(cp);
	}
	public double getRealValue(ChoisePoint cp) throws TermIsNotAReal {
		Term value= getValue(cp);
		return value.getRealValue(cp);
	}
	public long getSymbolValue(ChoisePoint cp) throws TermIsNotASymbol {
		Term value= getValue(cp);
		return value.getSymbolValue(cp);
	}
	public String getStringValue(ChoisePoint cp) throws TermIsNotAString {
		Term value= getValue(cp);
		return value.getStringValue(cp);
	}
	public long getStructureFunctor(ChoisePoint cp) throws TermIsNotAStructure {
		Term value= getValue(cp);
		return value.getStructureFunctor(cp);
	}
	public Term[] getStructureArguments(ChoisePoint cp) throws TermIsNotAStructure {
		Term value= getValue(cp);
		return value.getStructureArguments(cp);
	}
	public Term getListHead(ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		return value.getListHead(cp);
	}
	public Term getListTail(ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		return value.getListTail(cp);
	}
	public Term getNextListHead(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		Term value= getValue(cp);
		return value.getNextListHead(cp);
	}
	public Term getNextListTail(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		Term value= getValue(cp);
		return value.getNextListTail(cp);
	}
	public Term getNextListHeadSafely(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		if (cp != null) {
			Term value= getValue(cp);
			return value.getNextListHead(cp);
		} else {
			throw new TermIsNotAList();
		}
	}
	public Term getNextListTailSafely(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		if (cp != null) {
			Term value= getValue(cp);
			return value.getNextListTail(cp);
		} else {
			throw new TermIsNotAList();
		}
	}
	public long getNextPairName(ChoisePoint cp) throws EndOfSet, TermIsNotASet {
		Term value= getValue(cp);
		return value.getNextPairName(cp);
	}
	public Term getNextPairValue(ChoisePoint cp) throws EndOfSet, TermIsNotASet, SetElementIsProhibited {
		Term value= getValue(cp);
		return value.getNextPairValue(cp);
	}
	public Term getNextSetTail(ChoisePoint cp) throws EndOfSet, TermIsNotASet {
		Term value= getValue(cp);
		return value.getNextSetTail(cp);
	}
	public Term getNamedElement(long aName, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		return value.getNamedElement(aName,cp);
	}
	// public Term checkSetAndGetNamedElement(long aName, ChoisePoint cp) throws Backtracking, TermIsNotASet {
	//	Term value= getValue(cp);
	//	return value.checkSetAndGetNamedElement(aName,cp);
	// }
	public void checkIfTermIsASet(ChoisePoint cp) throws TermIsNotASet {
		Term value= getValue(cp);
		value.checkIfTermIsASet(cp);
	}
	public Term retrieveSetElementValue(ChoisePoint cp) throws Backtracking, TermIsNotSetElement {
		Term value= getValue(cp);
		return value.retrieveSetElementValue(cp);
	}
	public Term excludeNamedElements(long[] aNames, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		return value.excludeNamedElements(aNames,cp);
	}
	public void hasNoMoreElements(long[] aNames, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.hasNoMoreElements(aNames,cp);
	}
	public void prohibitNamedElement(long aName, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.prohibitNamedElement(aName,cp);
	}
	public void verifySet(long[] aNames, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.verifySet(aNames,cp);
	}
	public Term exploreSetPositiveElements(HashMap<Long,Term> positiveMap, ChoisePoint cp) {
		Term value= getValue(cp);
		return value.exploreSetPositiveElements(positiveMap,cp);
	}
	public Term exploreSet(HashMap<Long,Term> positiveMap, HashSet<Long> negativeMap, ChoisePoint cp) {
		Term value= getValue(cp);
		return value.exploreSet(positiveMap,negativeMap,cp);
	}
	public Term exploreSet(ChoisePoint cp) {
		Term value= getValue(cp);
		return value.exploreSet(cp);
	}
	public void appendNamedElement(long aName, Term aValue, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.appendNamedElement(aName,aValue,cp);
	}
	public void appendNamedElementProhibition(long aName, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.appendNamedElementProhibition(aName,cp);
	}
	// public long getAbstractWorldNumber(ChoisePoint cp) throws TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
	//	Term value= getValue(cp);
	//	return value.getAbstractWorldNumber(cp);
	// }
	public long getInternalWorldClass(AbstractWorld currentClass, ChoisePoint cp) throws Backtracking, TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		Term value= getValue(cp);
		return value.getInternalWorldClass(currentClass,cp);
	}
	//
	public AbstractWorld internalWorld(AbstractProcess process, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		return value.internalWorld(process,cp);
	}
	//
	public AbstractWorld internalWorld(ChoisePoint cp) {
		Term value= getValue(cp);
		return value.internalWorld(cp);
	}
	//
	public AbstractWorld world(ChoisePoint cp) {
		Term value= getValue(cp);
		return value.world(cp);
	}
	// "Unify with ..." functions
	public void unifyWithStructure(long aFunctor, Term[] values, Term structure, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.unifyWithStructure(aFunctor,values,structure,cp);
	}
	public void unifyWithList(Term aHead, Term aTail, Term aList, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.unifyWithList(aHead,aTail,aList,cp);
	}
	public void unifyWithSet(long aName, Term aValue, Term aTail, Term aSet, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.unifyWithSet(aName,aValue,aTail,aSet,cp);
	}
	public void unifyWithProhibitedElement(long aName, Term aTail, Term aSet, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.unifyWithProhibitedElement(aName,aTail,aSet,cp);
	}
	public void unifyWithSetElement(Term aElement, Term setElement, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.unifyWithSetElement(aElement,setElement,cp);
	}
	// General "Unify With" function
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp); // To link slot value with the actor.
		if (this == t) {
			return;
		};
		value.unifyWith(t,cp);
	}
	// Operations on slot variables
	public void registerVariables(ActiveWorld process, boolean isSuspending, boolean isProtecting) {
		process.registerVariable(this);
		//
		SlotVariableValue slotValue= null;
		processTableLock.readLock().lock();
		try {
			slotValue= processTable.get(process);
			// processTableLock.readLock().unlock();
		} finally {
			processTableLock.readLock().unlock();
		};
		//
		if (slotValue == null) {
			processTableLock.writeLock().lock();
			try {
				slotValue= processTable.get(process);
				if (slotValue == null) {
					slotValue= new SlotVariableValue(process,isSuspending,isProtecting);
					slotValue.portValue= globalValue;
					slotValue.portValueIsProtected= globalValueIsProtected;
					slotValue.portValueOwner= globalValueOwner;
					if (globalValue!=null || globalValueOwner!=null) {
						slotValue.portIsUpdated= true;
					};
					processTable.put(process,slotValue);
				} else {
					if (isProtecting) {
						slotValue.isProtectingPort= true;
					} else if (isSuspending) {
						slotValue.isSuspendingPort= true;
						slotValue.isProtectingPort= false;
					}
				}
				// processTableLock.writeLock().unlock();
			} finally {
				processTableLock.writeLock().unlock();
			}
		} else {
			if (isProtecting) {
				slotValue.isProtectingPort= true;
			} else if (isSuspending) {
				slotValue.isSuspendingPort= true;
				slotValue.isProtectingPort= false;
			}
		}
	}
	public void registerTargetWorlds(HashSet<AbstractWorld> worlds, ChoisePoint cp) {
		Term value= getValue(cp);
		value.registerTargetWorlds(worlds,cp);
	}
	public Term copyValue(ChoisePoint cp, TermCircumscribingMode mode) {
		Term value= getValue(cp);
		return value.copyValue(cp,mode);
	}
	public Term substituteWorlds(HashMap<AbstractWorld,Term> map, ChoisePoint cp) {
		Term value= getValue(cp);
		return value.substituteWorlds(map,cp);
	}
	// Domain check
	public boolean isCoveredByDomain(PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		Term value= getValue(cp);
		return baseDomain.coversTerm(value,cp,ignoreFreeVariables);
	}
	public Term checkSetTerm(long functor, PrologDomain headDomain, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		Term value= getValue(cp);
		return value.checkSetTerm(functor,headDomain,initialValue,cp,baseDomain);
	}
	// Converting Term to String
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, CharsetEncoder encoder) {
		if (cp==null) {
			return toString();
		} else {
			Term value= getValue(cp);
			// return "&" + value.toString(cp,isInner,provideStrictSyntax,encoder);
			return value.toString(cp,isInner,provideStrictSyntax,encoder);
		}
	}
	public String toString() {
		String text= null;
		processTableLock.readLock().lock();
		try {
			text= processTable.toString();
		} finally {
			processTableLock.readLock().unlock();
		};
		// return "&(" + text + ")";
		return "(" + text + ")";
	}
}
