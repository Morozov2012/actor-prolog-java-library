// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.domains.*;
import morozov.domains.signals.*;
import morozov.run.*;
import morozov.run.errors.*;
import morozov.system.*;
import morozov.terms.errors.*;
import morozov.terms.signals.*;
import morozov.worlds.*;

import java.nio.charset.CharsetEncoder;
import java.math.BigInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SlotVariable extends Term {
	public HashMap<ActiveWorld,SlotVariableValue> processTable= new HashMap<ActiveWorld,SlotVariableValue>();
	public ReentrantReadWriteLock processTableLock= new ReentrantReadWriteLock(false);
	public Term globalValue;
	public boolean globalValueIsProtected= false;
	public ActiveWorld globalValueOwner;
	//
	///////////////////////////////////////////////////////////////
	//
	public int hashCode() {
		return System.identityHashCode(this);
	}
	public boolean equals(Object o2) {
		throw new SlotVariableCannotBeCompared();
	}
	public int compare(Object o2) {
		throw new SlotVariableCannotBeCompared();
	}
	//
	///////////////////////////////////////////////////////////////
	//
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
		if (cp==null) {
			throw new WrongArgumentIsNotBoundVariable(this);
		};
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
	///////////////////////////////////////////////////////////////
	//
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
	//
	///////////////////////////////////////////////////////////////
	//
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
			throw TermIsNotAList.instance;
		}
	}
	public Term getNextListTailSafely(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		if (cp != null) {
			Term value= getValue(cp);
			return value.getNextListTail(cp);
		} else {
			throw TermIsNotAList.instance;
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
	//
	///////////////////////////////////////////////////////////////
	//
	public AbstractInternalWorld getInternalWorld(ChoisePoint cp) throws Backtracking, TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		Term value= getValue(cp);
		return value.getInternalWorld(cp);
	}
	//
	public AbstractInternalWorld internalWorld(AbstractProcess process, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		return value.internalWorld(process,cp);
	}
	//
	public AbstractInternalWorld internalWorld(ChoisePoint cp) {
		Term value= getValue(cp);
		return value.internalWorld(cp);
	}
	//
	public GlobalWorldIdentifier getGlobalWorldIdentifier(ChoisePoint cp) throws TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		Term value= getValue(cp);
		return value.getGlobalWorldIdentifier(cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
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
	//
	///////////////////////////////////////////////////////////////
	//
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp); // To link slot value with the actor.
		if (this == t) {
			return;
		};
		value.unifyWith(t,cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void registerVariables(ActiveWorld process, boolean isSuspending, boolean isProtecting) {
		registerVariables(process,false,isSuspending,isProtecting);
	}
	public void registerVariables(ActiveWorld process, boolean rewriteOldDefinition, boolean isSuspending, boolean isProtecting) {
		process.registerVariable(this);
		//
		SlotVariableValue slotValue= null;
		processTableLock.readLock().lock();
		try {
			slotValue= processTable.get(process);
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
						if (rewriteOldDefinition) {
							slotValue.isSuspendingPort= false;
						};
						slotValue.isProtectingPort= true;
					} else if (isSuspending) {
						slotValue.isSuspendingPort= true;
						slotValue.isProtectingPort= false;
					} else {
						if (rewriteOldDefinition) {
							slotValue.isSuspendingPort= false;
							slotValue.isProtectingPort= false;
						}
					}
				}
			} finally {
				processTableLock.writeLock().unlock();
			}
		} else {
			if (isProtecting) {
				if (rewriteOldDefinition) {
					slotValue.isSuspendingPort= false;
				};
				slotValue.isProtectingPort= true;
			} else if (isSuspending) {
				slotValue.isSuspendingPort= true;
				slotValue.isProtectingPort= false;
			} else {
				if (rewriteOldDefinition) {
					slotValue.isSuspendingPort= false;
					slotValue.isProtectingPort= false;
				}
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
	public Term copyGroundValue(ChoisePoint cp) throws TermIsUnboundVariable {
		Term value= getValue(cp);
		return value.copyGroundValue(cp);
	}
	public Term substituteWorlds(HashMap<AbstractWorld,Term> map, ChoisePoint cp) {
		Term value= getValue(cp);
		return value.substituteWorlds(map,cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean isCoveredByDomain(PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		Term value= getValue(cp);
		return baseDomain.coversTerm(value,cp,ignoreFreeVariables);
	}
	public Term checkSetTerm(long functor, PrologDomain headDomain, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		Term value= getValue(cp);
		return value.checkSetTerm(functor,headDomain,initialValue,cp,baseDomain);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void compareWithTerm(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		Term value= getValue(iX);
		value.compareWithTerm(a,iX,op);
	}
	public void compareWithBigInteger(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		Term value= getValue(iX);
		value.compareWithBigInteger(a,iX,op);
	}
	public void compareWithLong(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		Term value= getValue(iX);
		value.compareWithLong(a,iX,op);
	}
	public void compareWithDouble(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		Term value= getValue(iX);
		value.compareWithDouble(a,iX,op);
	}
	public void compareWithString(String a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		Term value= getValue(iX);
		value.compareWithString(a,iX,op);
	}
	public void compareWithDate(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		Term value= getValue(iX);
		value.compareWithDate(a,iX,op);
	}
	public void compareTermWith(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		Term value= getValue(iX);
		value.compareTermWith(a,iX,op);
	}
	public void compareBigIntegerWith(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		Term value= getValue(iX);
		value.compareBigIntegerWith(a,iX,op);
	}
	public void compareLongWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		Term value= getValue(iX);
		value.compareLongWith(a,iX,op);
	}
	public void compareDoubleWith(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		Term value= getValue(iX);
		value.compareDoubleWith(a,iX,op);
	}
	public void compareStringWith(String a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		Term value= getValue(iX);
		value.compareStringWith(a,iX,op);
	}
	public void compareDateWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		Term value= getValue(iX);
		value.compareDateWith(a,iX,op);
	}
	public void compareListWith(Term aHead, Term aTail, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		Term value= getValue(iX);
		value.compareListWith(aHead,aTail,iX,op);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term reactWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.reactWithTerm(a,iX,op);
	}
	public Term reactWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.reactWithBigInteger(a,iX,op);
	}
	public Term reactWithLong(long a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.reactWithLong(a,iX,op);
	}
	public Term reactWithDouble(double a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.reactWithDouble(a,iX,op);
	}
	public Term reactWithString(String a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.reactWithString(a,iX,op);
	}
	public Term reactWithDate(long a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.reactWithDate(a,iX,op);
	}
	public Term reactWithTime(long a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.reactWithTime(a,iX,op);
	}
	public Term reactBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.reactBigIntegerWith(a,iX,op);
	}
	public Term reactLongWith(long a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.reactLongWith(a,iX,op);
	}
	public Term reactDoubleWith(double a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.reactDoubleWith(a,iX,op);
	}
	public Term reactStringWith(String a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.reactStringWith(a,iX,op);
	}
	public Term reactDateWith(long a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.reactDateWith(a,iX,op);
	}
	public Term reactTimeWith(long a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.reactTimeWith(a,iX,op);
	}
	public Term reactListWith(Term aHead, Term aTail, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.reactListWith(aHead,aTail,iX,op);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term blitWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.blitWithTerm(a,iX,op);
	}
	public Term blitWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.blitWithBigInteger(a,iX,op);
	}
	public Term blitWithLong(long a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.blitWithLong(a,iX,op);
	}
	public Term blitWithDouble(double a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.blitWithDouble(a,iX,op);
	}
	public Term blitBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.blitBigIntegerWith(a,iX,op);
	}
	public Term blitLongWith(long a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.blitLongWith(a,iX,op);
	}
	public Term blitDoubleWith(double a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.blitDoubleWith(a,iX,op);
	}
	public Term blitListWith(Term aHead, Term aTail, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.blitListWith(aHead,aTail,iX,op);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term evaluate(ChoisePoint iX, UnaryOperation op) {
		Term value= getValue(iX);
		return value.evaluate(iX,op);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, boolean encodeWorlds, CharsetEncoder encoder) {
		if (cp==null) {
			return toString();
		} else {
			Term value= getValue(cp);
			return value.toString(cp,isInner,provideStrictSyntax,encodeWorlds,encoder);
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
		return "(" + text + ")";
	}
}
