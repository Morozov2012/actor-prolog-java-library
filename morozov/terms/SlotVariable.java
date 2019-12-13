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
	//
	public HashMap<ActiveWorld,SlotVariableValue> processTable= new HashMap<>();
	public ReentrantReadWriteLock processTableLock= new ReentrantReadWriteLock(false);
	public Term globalValue;
	public boolean globalValueIsProtected= false;
	public ActiveWorld globalValueOwner;
	//
	private static final long serialVersionUID= 0x2EA07D378F71C20L; // 209988937475103776L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.terms","SlotVariable");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public int hashCode() {
		return System.identityHashCode(this);
	}
	@Override
	public boolean equals(Object o2) {
		throw new SlotVariableCannotBeCompared();
	}
	@Override
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
		} finally {
			processTableLock.readLock().unlock();
		};
		return value;
	}
	//
	@Override
	public final Term getValue(ChoisePoint cp) {
		if (cp==null) {
			throw new WrongArgumentIsNotBoundVariable(this);
		};
		ActorRegister aR= cp.getActorRegister();
		ActiveWorld currentProcess= aR.getCurrentProcess();
		ActorNumber actorNumber= aR.getCurrentActorNumber();
		if (actorNumber==null) {
			throw new IllegalActorNumber();
		};
		SlotVariableValue slotValue= null;
		processTableLock.readLock().lock();
		try {
			slotValue= processTable.get(currentProcess);
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
		} finally {
			processTableLock.readLock().unlock();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void isInteger(PrologInteger v, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.isInteger(v,cp);
	}
	@Override
	public void isInteger(int v, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.isInteger(v,cp);
	}
	@Override
	public void isInteger(BigInteger v, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.isInteger(v,cp);
	}
	@Override
	public void isReal(PrologReal v, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.isReal(v,cp);
	}
	@Override
	public void isReal(double v, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.isReal(v,cp);
	}
	@Override
	public void isSymbol(PrologSymbol v, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.isSymbol(v,cp);
	}
	@Override
	public void isSymbol(long v, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.isSymbol(v,cp);
	}
	@Override
	public void isString(PrologString v, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.isString(v,cp);
	}
	@Override
	public void isString(String v, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.isString(v,cp);
	}
	@Override
	public void isBinary(PrologBinary v, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.isBinary(v,cp);
	}
	@Override
	public void isBinary(byte[] v, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.isBinary(v,cp);
	}
	@Override
	public Term[] isStructure(long aFunctor, int aArity, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		return value.isStructure(aFunctor,aArity,cp);
	}
	@Override
	public void isEmptyList(ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.isEmptyList(cp);
	}
	@Override
	public void isEmptySet(ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.isEmptySet(cp);
	}
	@Override
	public void isUnknownValue(ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.isUnknownValue(cp);
	}
	@Override
	public void isWorld(Term v, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.isWorld(v,cp);
	}
	@Override
	public boolean thisIsSlotVariable() {
		return true;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term dereferenceValue(ChoisePoint cp) {
		Term value= getValue(cp);
		return value.dereferenceValue(cp);
	}
	@Override
	public BigInteger getIntegerValue(ChoisePoint cp) throws TermIsNotAnInteger {
		Term value= getValue(cp);
		return value.getIntegerValue(cp);
	}
	@Override
	public int getSmallIntegerValue(ChoisePoint cp) throws TermIsNotAnInteger {
		Term value= getValue(cp);
		return value.getSmallIntegerValue(cp);
	}
	@Override
	public long getLongIntegerValue(ChoisePoint cp) throws TermIsNotAnInteger {
		Term value= getValue(cp);
		return value.getLongIntegerValue(cp);
	}
	@Override
	public double getRealValue(ChoisePoint cp) throws TermIsNotAReal {
		Term value= getValue(cp);
		return value.getRealValue(cp);
	}
	@Override
	public String getStringValue(ChoisePoint cp) throws TermIsNotAString {
		Term value= getValue(cp);
		return value.getStringValue(cp);
	}
	@Override
	public byte[] getBinaryValue(ChoisePoint cp) throws TermIsNotABinary {
		Term value= getValue(cp);
		return value.getBinaryValue(cp);
	}
	@Override
	public long getSymbolValue(ChoisePoint cp) throws TermIsNotASymbol {
		Term value= getValue(cp);
		return value.getSymbolValue(cp);
	}
	@Override
	public long getStructureFunctor(ChoisePoint cp) throws TermIsNotAStructure {
		Term value= getValue(cp);
		return value.getStructureFunctor(cp);
	}
	@Override
	public Term[] getStructureArguments(ChoisePoint cp) throws TermIsNotAStructure {
		Term value= getValue(cp);
		return value.getStructureArguments(cp);
	}
	@Override
	public Term getListHead(ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		return value.getListHead(cp);
	}
	@Override
	public Term getListTail(ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		return value.getListTail(cp);
	}
	@Override
	public Term getNextListHead(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		Term value= getValue(cp);
		return value.getNextListHead(cp);
	}
	@Override
	public Term getNextListTail(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		Term value= getValue(cp);
		return value.getNextListTail(cp);
	}
	@Override
	public Term getNextListHeadSafely(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		if (cp != null) {
			Term value= getValue(cp);
			return value.getNextListHead(cp);
		} else {
			throw TermIsNotAList.instance;
		}
	}
	@Override
	public Term getNextListTailSafely(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		if (cp != null) {
			Term value= getValue(cp);
			return value.getNextListTail(cp);
		} else {
			throw TermIsNotAList.instance;
		}
	}
	@Override
	public long getNextPairName(ChoisePoint cp) throws EndOfSet, TermIsNotASet {
		Term value= getValue(cp);
		return value.getNextPairName(cp);
	}
	@Override
	public Term getNextPairValue(ChoisePoint cp) throws EndOfSet, TermIsNotASet, SetElementIsProhibited {
		Term value= getValue(cp);
		return value.getNextPairValue(cp);
	}
	@Override
	public Term getNextSetTail(ChoisePoint cp) throws EndOfSet, TermIsNotASet {
		Term value= getValue(cp);
		return value.getNextSetTail(cp);
	}
	@Override
	public Term getNamedElement(long aName, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		return value.getNamedElement(aName,cp);
	}
	@Override
	public void checkIfTermIsASet(ChoisePoint cp) throws TermIsNotASet {
		Term value= getValue(cp);
		value.checkIfTermIsASet(cp);
	}
	@Override
	public Term retrieveSetElementValue(ChoisePoint cp) throws Backtracking, TermIsNotSetElement {
		Term value= getValue(cp);
		return value.retrieveSetElementValue(cp);
	}
	@Override
	public Term excludeNamedElements(long[] aNames, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		return value.excludeNamedElements(aNames,cp);
	}
	@Override
	public void hasNoMoreElements(long[] aNames, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.hasNoMoreElements(aNames,cp);
	}
	@Override
	public void prohibitNamedElement(long aName, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.prohibitNamedElement(aName,cp);
	}
	@Override
	public void verifySet(long[] aNames, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.verifySet(aNames,cp);
	}
	@Override
	public Term exploreSetPositiveElements(HashMap<Long,Term> positiveMap, ChoisePoint cp) {
		Term value= getValue(cp);
		return value.exploreSetPositiveElements(positiveMap,cp);
	}
	@Override
	public Term exploreSet(HashMap<Long,Term> positiveMap, HashSet<Long> negativeMap, ChoisePoint cp) {
		Term value= getValue(cp);
		return value.exploreSet(positiveMap,negativeMap,cp);
	}
	@Override
	public Term exploreSet(ChoisePoint cp) {
		Term value= getValue(cp);
		return value.exploreSet(cp);
	}
	@Override
	public void appendNamedElement(long aName, Term aValue, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.appendNamedElement(aName,aValue,cp);
	}
	@Override
	public void appendNamedElementProhibition(long aName, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.appendNamedElementProhibition(aName,cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public AbstractInternalWorld getInternalWorld(ChoisePoint cp) throws Backtracking, TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		Term value= getValue(cp);
		return value.getInternalWorld(cp);
	}
	//
	@Override
	public AbstractInternalWorld internalWorld(AbstractProcess process, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		return value.internalWorld(process,cp);
	}
	//
	@Override
	public AbstractInternalWorld internalWorld(ChoisePoint cp) {
		Term value= getValue(cp);
		return value.internalWorld(cp);
	}
	//
	@Override
	public GlobalWorldIdentifier getGlobalWorldIdentifier(ChoisePoint cp) throws TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		Term value= getValue(cp);
		return value.getGlobalWorldIdentifier(cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void unifyWithStructure(long aFunctor, Term[] values, Term structure, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.unifyWithStructure(aFunctor,values,structure,cp);
	}
	@Override
	public void unifyWithList(Term aHead, Term aTail, Term aList, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.unifyWithList(aHead,aTail,aList,cp);
	}
	@Override
	public void unifyWithSet(long aName, Term aValue, Term aTail, Term aSet, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.unifyWithSet(aName,aValue,aTail,aSet,cp);
	}
	@Override
	public void unifyWithProhibitedElement(long aName, Term aTail, Term aSet, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.unifyWithProhibitedElement(aName,aTail,aSet,cp);
	}
	@Override
	public void unifyWithSetElement(Term aElement, Term setElement, ChoisePoint cp) throws Backtracking {
		Term value= getValue(cp);
		value.unifyWithSetElement(aElement,setElement,cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
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
	@Override
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
	@Override
	public void registerTargetWorlds(HashSet<AbstractWorld> worlds, ChoisePoint cp) {
		Term value= getValue(cp);
		value.registerTargetWorlds(worlds,cp);
	}
	@Override
	public Term copyValue(ChoisePoint cp, TermCircumscribingMode mode) {
		Term value= getValue(cp);
		return value.copyValue(cp,mode);
	}
	@Override
	public Term copyGroundValue(ChoisePoint cp) throws TermIsUnboundVariable {
		Term value= getValue(cp);
		return value.copyGroundValue(cp);
	}
	@Override
	public Term substituteWorlds(HashMap<AbstractWorld,Term> map, ChoisePoint cp) {
		Term value= getValue(cp);
		return value.substituteWorlds(map,cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean isCoveredByDomain(PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		Term value= getValue(cp);
		return baseDomain.coversTerm(value,cp,ignoreFreeVariables);
	}
	@Override
	public Term checkSetTerm(long functor, PrologDomain headDomain, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		Term value= getValue(cp);
		return value.checkSetTerm(functor,headDomain,initialValue,cp,baseDomain);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void compareWithTerm(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		Term value= getValue(iX);
		value.compareWithTerm(a,iX,op);
	}
	@Override
	public void compareWithBigInteger(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		Term value= getValue(iX);
		value.compareWithBigInteger(a,iX,op);
	}
	@Override
	public void compareWithLong(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		Term value= getValue(iX);
		value.compareWithLong(a,iX,op);
	}
	@Override
	public void compareWithDouble(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		Term value= getValue(iX);
		value.compareWithDouble(a,iX,op);
	}
	@Override
	public void compareWithString(String a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		Term value= getValue(iX);
		value.compareWithString(a,iX,op);
	}
	@Override
	public void compareWithBinary(byte[] a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		Term value= getValue(iX);
		value.compareWithBinary(a,iX,op);
	}
	@Override
	public void compareWithDate(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		Term value= getValue(iX);
		value.compareWithDate(a,iX,op);
	}
	@Override
	public void compareTermWith(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		Term value= getValue(iX);
		value.compareTermWith(a,iX,op);
	}
	@Override
	public void compareBigIntegerWith(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		Term value= getValue(iX);
		value.compareBigIntegerWith(a,iX,op);
	}
	@Override
	public void compareLongWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		Term value= getValue(iX);
		value.compareLongWith(a,iX,op);
	}
	@Override
	public void compareDoubleWith(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		Term value= getValue(iX);
		value.compareDoubleWith(a,iX,op);
	}
	@Override
	public void compareStringWith(String a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		Term value= getValue(iX);
		value.compareStringWith(a,iX,op);
	}
	@Override
	public void compareBinaryWith(byte[] a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		Term value= getValue(iX);
		value.compareBinaryWith(a,iX,op);
	}
	@Override
	public void compareDateWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		Term value= getValue(iX);
		value.compareDateWith(a,iX,op);
	}
	@Override
	public void compareListWith(Term aHead, Term aTail, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		Term value= getValue(iX);
		value.compareListWith(aHead,aTail,iX,op);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term reactWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.reactWithTerm(a,iX,op);
	}
	@Override
	public Term reactWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.reactWithBigInteger(a,iX,op);
	}
	@Override
	public Term reactWithLong(long a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.reactWithLong(a,iX,op);
	}
	@Override
	public Term reactWithDouble(double a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.reactWithDouble(a,iX,op);
	}
	@Override
	public Term reactWithString(String a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.reactWithString(a,iX,op);
	}
	@Override
	public Term reactWithBinary(byte[] a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.reactWithBinary(a,iX,op);
	}
	@Override
	public Term reactWithDate(long a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.reactWithDate(a,iX,op);
	}
	@Override
	public Term reactWithTime(long a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.reactWithTime(a,iX,op);
	}
	@Override
	public Term reactBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.reactBigIntegerWith(a,iX,op);
	}
	@Override
	public Term reactLongWith(long a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.reactLongWith(a,iX,op);
	}
	@Override
	public Term reactDoubleWith(double a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.reactDoubleWith(a,iX,op);
	}
	@Override
	public Term reactStringWith(String a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.reactStringWith(a,iX,op);
	}
	@Override
	public Term reactBinaryWith(byte[] a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.reactBinaryWith(a,iX,op);
	}
	@Override
	public Term reactDateWith(long a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.reactDateWith(a,iX,op);
	}
	@Override
	public Term reactTimeWith(long a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.reactTimeWith(a,iX,op);
	}
	@Override
	public Term reactListWith(Term aHead, Term aTail, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.reactListWith(aHead,aTail,iX,op);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term blitWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.blitWithTerm(a,iX,op);
	}
	@Override
	public Term blitWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.blitWithBigInteger(a,iX,op);
	}
	@Override
	public Term blitWithLong(long a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.blitWithLong(a,iX,op);
	}
	@Override
	public Term blitWithDouble(double a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.blitWithDouble(a,iX,op);
	}
	@Override
	public Term blitBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.blitBigIntegerWith(a,iX,op);
	}
	@Override
	public Term blitLongWith(long a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.blitLongWith(a,iX,op);
	}
	@Override
	public Term blitDoubleWith(double a, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.blitDoubleWith(a,iX,op);
	}
	@Override
	public Term blitListWith(Term aHead, Term aTail, ChoisePoint iX, BinaryOperation op) {
		Term value= getValue(iX);
		return value.blitListWith(aHead,aTail,iX,op);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term evaluate(ChoisePoint iX, UnaryOperation op) {
		Term value= getValue(iX);
		return value.evaluate(iX,op);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, boolean encodeWorlds, CharsetEncoder encoder) {
		if (cp==null) {
			return toString();
		} else {
			Term value= getValue(cp);
			return value.toString(cp,isInner,provideStrictSyntax,encodeWorlds,encoder);
		}
	}
	@Override
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
