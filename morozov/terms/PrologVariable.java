// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.classes.*;
import morozov.domains.*;
import morozov.domains.signals.*;
import morozov.run.*;
import morozov.system.*;
import morozov.terms.errors.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;

public class PrologVariable extends Term {
	public Term value;
	// private Term value;
	public int hashCode() {
		if (value != null) {
			return value.hashCode();
		} else {
			return super.hashCode();
		}
	}
	// "Is a ..." functions
	public void isInteger(int v, ChoisePoint cp) throws Backtracking {
		if (value != null) {
			value.isInteger(v,cp);
		} else {
			value= new PrologInteger(v);
			cp.pushTrail(this);
		}
	}
	public void isInteger(BigInteger v, ChoisePoint cp) throws Backtracking {
		if (value != null) {
			value.isInteger(v,cp);
		} else {
			value= new PrologInteger(v);
			cp.pushTrail(this);
		}
	}
	public void isReal(double v, ChoisePoint cp) throws Backtracking {
		if (value != null) {
			value.isReal(v,cp);
		} else {
			value= new PrologReal(v);
			cp.pushTrail(this);
		}
	}
	public void isSymbol(long v, ChoisePoint cp) throws Backtracking {
		if (value != null) {
			value.isSymbol(v,cp);
		} else {
			value= new PrologSymbol(v);
			cp.pushTrail(this);
		}
	}
	public void isString(String v, ChoisePoint cp) throws Backtracking {
		if (value != null) {
			value.isString(v,cp);
		} else {
			value= new PrologString(v);
			cp.pushTrail(this);
		}
	}
	public Term[] isStructure(long aFunctor, int aArity, ChoisePoint cp) throws Backtracking {
		if (value != null) {
			return value.isStructure(aFunctor,aArity,cp);
		} else {
			Term[] contents= new PrologVariable[aArity];
			for (int i= 0; i < aArity; i++) {
				contents[i]= new PrologVariable();
			};
			value= new PrologStructure(aFunctor,contents);
			cp.pushTrail(this);
			return contents;
		}
	}
	public void isEmptyList(ChoisePoint cp) throws Backtracking {
		if (value != null) {
			value.isEmptyList(cp);
		} else {
			value= PrologEmptyList.instance;
			cp.pushTrail(this);
		}
	}
	public void isOutputEmptyList(ChoisePoint cp) {
		if (value != null) {
			value.isOutputEmptyList(cp);
		} else {
			value= PrologEmptyList.instance;
			cp.pushTrail(this);
		}
	}
	public void isEmptySet(ChoisePoint cp) throws Backtracking {
		if (value != null) {
			value.isEmptySet(cp);
		} else {
			value= PrologEmptySet.instance;
			cp.pushTrail(this);
		}
	}
	public void isUnknownValue(ChoisePoint cp) throws Backtracking {
		if (value != null) {
			value.isUnknownValue(cp);
		} else {
			value= PrologUnknownValue.instance;
			cp.pushTrail(this);
		}
	}
	public void isNoValue(ChoisePoint cp) throws Backtracking {
		if (value != null) {
			value.isNoValue(cp);
		} else {
			value= PrologNoValue.instance;
			cp.pushTrail(this);
		}
	}
	public void isWorld(Term v, ChoisePoint cp) throws Backtracking {
		if (value != null) {
			value.isWorld(v,cp);
		} else {
			value= v;
			cp.pushTrail(this);
		}
	}
	public boolean thisIsWorld() {
		if (value != null) {
			return value.thisIsWorld();
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	// "Get ... value" functions
	public final Term getValue(ChoisePoint cp) {
		if (value != null) {
			return value.getValue(cp);
		} else {
			return this;
		}
	}
	public Term dereferenceValue(ChoisePoint cp) {
		if (value != null) {
			return value.dereferenceValue(cp);
		} else {
			return this;
		}
	}
	public Term extractSlotVariable() {
		if (value != null) {
			return value.extractSlotVariable();
		} else {
			return this;
		}
	}
	public BigInteger getIntegerValue(ChoisePoint cp) throws TermIsNotAnInteger {
		if (value != null) {
			return value.getIntegerValue(cp);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public int getSmallIntegerValue(ChoisePoint cp) throws TermIsNotAnInteger {
		if (value != null) {
			return value.getSmallIntegerValue(cp);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public long getLongIntegerValue(ChoisePoint cp) throws TermIsNotAnInteger {
		if (value != null) {
			return value.getLongIntegerValue(cp);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public double getRealValue(ChoisePoint cp) throws TermIsNotAReal {
		if (value != null) {
			return value.getRealValue(cp);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public long getSymbolValue(ChoisePoint cp) throws TermIsNotASymbol {
		if (value != null) {
			return value.getSymbolValue(cp);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public String getStringValue(ChoisePoint cp) throws TermIsNotAString {
		if (value != null) {
			return value.getStringValue(cp);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public long getStructureFunctor(ChoisePoint cp) throws TermIsNotAStructure {
		if (value != null) {
			return value.getStructureFunctor(cp);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public Term[] getStructureArguments(ChoisePoint cp) throws TermIsNotAStructure {
		if (value != null) {
			return value.getStructureArguments(cp);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public Term getListHead(ChoisePoint cp) throws Backtracking {
		if (value != null) {
			return value.getListHead(cp);
		} else {
			Term head= new PrologVariable();
			Term tail= new PrologVariable();
			value= new PrologList(head,tail);
			cp.pushTrail(this);
			return head;
		}
	}
	public Term getListTail(ChoisePoint cp) throws Backtracking {
		if (value != null) {
			return value.getListTail(cp);
		} else {
			Term head= new PrologVariable();
			Term tail= new PrologVariable();
			value= new PrologList(head,tail);
			cp.pushTrail(this);
			return tail;
		}
	}
	public Term getNextListHead(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		if (value != null) {
			return value.getNextListHead(cp);
		} else {
			throw TermIsNotAList.instance;
		}
	}
	public Term getNextListTail(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		if (value != null) {
			return value.getNextListTail(cp);
		} else {
			throw TermIsNotAList.instance;
		}
	}
	public Term getExistentHead() {
		if (value != null) {
			return value.getExistentHead();
		} else {
			throw new WrongTermIsNotArgumentList(this);
		}
	}
	public Term getExistentTail() {
		if (value != null) {
			return value.getExistentTail();
		} else {
			throw new WrongTermIsNotArgumentList(this);
		}
	}
	public Term getOutputHead(ChoisePoint cp) {
		if (value != null) {
			return value.getOutputHead(cp);
		} else {
			Term head= new PrologVariable();
			Term tail= new PrologVariable();
			value= new PrologList(head,tail);
			cp.pushTrail(this);
			return head;
		}
	}
	public Term getOutputTail(ChoisePoint cp) {
		if (value != null) {
			return value.getOutputTail(cp);
		} else {
			Term head= new PrologVariable();
			Term tail= new PrologVariable();
			value= new PrologList(head,tail);
			cp.pushTrail(this);
			return tail;
		}
	}
	public Term getNextListHeadSafely(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		if (value != null) {
			return value.getNextListHeadSafely(cp);
		} else {
			throw TermIsNotAList.instance;
		}
	}
	public Term getNextListTailSafely(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		if (value != null) {
			return value.getNextListTailSafely(cp);
		} else {
			throw TermIsNotAList.instance;
		}
	}
	public Term getNamedElement(long aName, ChoisePoint cp) throws Backtracking {
		if (value != null) {
			return value.getNamedElement(aName,cp);
		} else {
			Term itemValue= new PrologVariable();
			Term tail= new PrologVariable();
			value= new PrologSet(aName,itemValue,tail);
			cp.pushTrail(this);
			return itemValue;
		}
	}
	// public Term checkSetAndGetNamedElement(long aName, ChoisePoint cp) throws Backtracking, TermIsNotASet {
	//	if (value != null) {
	//		return value.checkSetAndGetNamedElement(aName,cp);
	//	} else {
	//		Term itemValue= new PrologVariable();
	//		Term tail= new PrologVariable();
	//		value= new PrologSet(aName,itemValue,tail);
	//		cp.pushTrail(this);
	//		return itemValue;
	//	}
	// }
	public void checkIfTermIsASet(ChoisePoint cp) throws TermIsNotASet {
		if (value != null) {
			value.checkIfTermIsASet(cp);
		} else {
			throw TermIsNotASet.instance;
		}
	}
	public Term retrieveSetElementValue(ChoisePoint cp) throws Backtracking, TermIsNotSetElement {
		if (value != null) {
			return value.retrieveSetElementValue(cp);
		} else {
			Term elementValue= new PrologVariable();
			value= new PrologSetElement(elementValue);
			cp.pushTrail(this);
			return elementValue;
		}
	}
	public Term excludeNamedElements(long[] aNames, ChoisePoint cp) throws Backtracking {
		if (value != null) {
			return value.excludeNamedElements(aNames,cp);
		} else {
			boolean arrayIsEmpty= true;
			for (int i= 0; i < aNames.length; i++) {
				if (aNames[i] != 0) {
					arrayIsEmpty= false;
					break;
				}
			};
			if (!arrayIsEmpty) {
				Term tail= new PrologVariable();
				for (int i= 0; i < aNames.length; i++) {
					if (aNames[i] != 0) {
						tail= new ProhibitedSetElement(aNames[i],tail);
					}
				};
				value= tail;
				cp.pushTrail(this);
			};
			return this;
		}
	}
	public void hasNoMoreElements(long[] aNames, ChoisePoint cp) throws Backtracking {
		if (value != null) {
			value.hasNoMoreElements(aNames,cp);
		} else {
			value= PrologEmptySet.instance;
			cp.pushTrail(this);
		}
	}
	public void prohibitNamedElement(long aName, ChoisePoint cp) throws Backtracking {
		if (value != null) {
			value.prohibitNamedElement(aName,cp);
		} else {
			Term tail= new PrologVariable();
			value= new ProhibitedSetElement(aName,tail);
			cp.pushTrail(this);
		}
	}
	public void verifySet(long[] aNames, ChoisePoint cp) throws Backtracking {
		if (value != null) {
			value.verifySet(aNames,cp);
		} else {
			boolean arrayIsEmpty= true;
			for (int i= 0; i < aNames.length; i++) {
				if (aNames[i] != 0) {
					arrayIsEmpty= false;
					break;
				}
			};
			if (!arrayIsEmpty) {
				Term tail= new PrologVariable();
				for (int i= 0; i < aNames.length; i++) {
					if (aNames[i] != 0) {
						tail= new ProhibitedSetElement(aNames[i],tail);
					}
				};
				value= tail;
				cp.pushTrail(this);
			}
		}
	}
	public long getNextPairName(ChoisePoint cp) throws EndOfSet, TermIsNotASet {
		if (value != null) {
			return value.getNextPairName(cp);
		} else {
			throw TermIsNotASet.instance;
		}
	}
	public Term getNextPairValue(ChoisePoint cp) throws EndOfSet, TermIsNotASet, SetElementIsProhibited {
		if (value != null) {
			return value.getNextPairValue(cp);
		} else {
			throw TermIsNotASet.instance;
		}
	}
	public Term getNextSetTail(ChoisePoint cp) throws EndOfSet, TermIsNotASet {
		if (value != null) {
			return value.getNextSetTail(cp);
		} else {
			throw TermIsNotASet.instance;
		}
	}
	public Term exploreSetPositiveElements(HashMap<Long,Term> positiveMap, ChoisePoint cp) {
		if (value != null) {
			return value.exploreSetPositiveElements(positiveMap,cp);
		} else {
			return this;
		}
	}
	public Term exploreSet(HashMap<Long,Term> positiveMap, HashSet<Long> negativeMap, ChoisePoint cp) {
		if (value != null) {
			return value.exploreSet(positiveMap,negativeMap,cp);
		} else {
			return this;
		}
	}
	public Term exploreSet(ChoisePoint cp) {
		if (value != null) {
			return value.exploreSet(cp);
		} else {
			return this;
		}
	}
	public void appendNamedElement(long aName, Term aValue, ChoisePoint cp) throws Backtracking {
		if (value != null) {
			value.appendNamedElement(aName,aValue,cp);
		} else {
			Term tail= new PrologVariable();
			value= new PrologSet(aName,aValue,tail);
			cp.pushTrail(this);
		}
	}
	public void appendNamedElementProhibition(long aName, ChoisePoint cp) throws Backtracking {
		if (value != null) {
			value.appendNamedElementProhibition(aName,cp);
		} else {
			Term tail= new PrologVariable();
			value= new ProhibitedSetElement(aName,tail);
			cp.pushTrail(this);
		}
	}
	// public long getAbstractWorldNumber(ChoisePoint cp) throws TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
	//	if (value != null)
	//		return value.getAbstractWorldNumber(cp);
	//	else
	//		throw TermIsUnboundVariable.instance;
	// }
	public long getInternalWorldClass(AbstractWorld currentClass, ChoisePoint cp) throws Backtracking, TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		if (value != null) {
			return value.getInternalWorldClass(currentClass,cp);
		} else {
			throw TermIsUnboundVariable.instance;
		}
	}
	//
	public AbstractWorld internalWorld(AbstractProcess process, ChoisePoint cp) throws Backtracking {
		if (value != null) {
			return value.internalWorld(process,cp);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	//
	public AbstractWorld internalWorld(ChoisePoint cp) {
		if (value != null) {
			return value.internalWorld(cp);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	//
	public Term internalWorldOrTerm(ChoisePoint cp) {
		if (value != null) {
			return value.internalWorldOrTerm(cp);
		} else {
			return this;
		}
	}
	//
	// public AbstractWorld world(ChoisePoint cp) {
	//	if (value != null) {
	//		return value.world(cp);
	//	} else {
	//		throw new WrongTermIsNotBoundVariable(this);
	//	}
	// }
	// "Unify with ..." functions
	public void unifyWithStructure(long aFunctor, Term[] values, Term structure, ChoisePoint cp) throws Backtracking {
		if (value != null) {
			value.unifyWithStructure(aFunctor,values,structure,cp);
		} else {
			value= structure;
			cp.pushTrail(this);
		}
	}
	public void unifyWithList(Term aHead, Term aTail, Term aList, ChoisePoint cp) throws Backtracking {
		if (value != null) {
			value.unifyWithList(aHead,aTail,aList,cp);
		} else {
			value= aList;
			cp.pushTrail(this);
		}
	}
	public void unifyWithSet(long aName, Term aValue, Term aTail, Term aSet, ChoisePoint cp) throws Backtracking {
		if (value != null) {
			value.unifyWithSet(aName,aValue,aTail,aSet,cp);
		} else {
			value= aSet;
			cp.pushTrail(this);
		}
	}
	public void unifyWithProhibitedElement(long aName, Term aTail, Term aSet, ChoisePoint cp) throws Backtracking {
		if (value != null) {
			value.unifyWithProhibitedElement(aName,aTail,aSet,cp);
		} else {
			value= aSet;
			cp.pushTrail(this);
		}
	}
	public void unifyWithOptimizedSet(PrologOptimizedSet aSet, ChoisePoint cp) throws Backtracking {
		if (value != null) {
			value.unifyWithOptimizedSet(aSet,cp);
		} else {
			value= aSet;
			cp.pushTrail(this);
		}
	}
	public void inheritSetElements(PrologOptimizedSet set, ChoisePoint cp) throws Backtracking {
		if (value != null) {
			value.inheritSetElements(set,cp);
		} else {
			value= new PrologOptimizedSet(set,cp);
			cp.pushTrail(this);
			// value.inheritSetElements(set,cp);
		}
	}
	public void unifyWithSetElement(Term aElement, Term setElement, ChoisePoint cp) throws Backtracking {
		if (value != null) {
			value.unifyWithSetElement(aElement,setElement,cp);
		} else {
			value= setElement;
			cp.pushTrail(this);
		}
	}
	// General "Unify With" function
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		if (this == t) {
			return;
		} else if (value != null) {
			value.unifyWith(t,cp);
		} else if (t.thisIsFreeVariable()) {
			PrologVariable newNode= new PrologVariable();
			value= newNode;
			t.setValue(newNode);
			cp.pushTrail(this);
			cp.pushTrail(t);
		} else {
			// t.unifyWith(this,cp);
			value= t;
			cp.pushTrail(this);
		}
	}
	// Operations on variables
	public boolean thisIsFreeVariable() {
		if (value == null) {
			return true;
		} else {
			return false;
		}
	}
	public void clear() {
		value= null;
	}
	public void setValue(Term newNode) {
		value= newNode;
	}
	public void registerVariables(ActiveWorld process, boolean isSuspending, boolean isProtecting) {
		if (value != null) {
			value.registerVariables(process,isSuspending,isProtecting);
		} else {
			throw new WrongTermIsNotConstructorArgument(this);
		}
	}
	public void registerTargetWorlds(HashSet<AbstractWorld> worlds, ChoisePoint cp) {
		if (value != null) {
			value.registerTargetWorlds(worlds,cp);
		}
	}
	public Term circumscribe() {
		if (value != null) {
			return value.circumscribe();
		} else {
			return PrologUnknownValue.instance;
		}
	}
	public Term copyValue(ChoisePoint cp, TermCircumscribingMode mode) {
		if (value != null) {
			return value.copyValue(cp,mode);
		} else if (mode==TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES) {
			return PrologUnknownValue.instance;
		} else if (mode==TermCircumscribingMode.CLONE_FREE_VARIABLES) {
			return new PrologVariable();
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public Term copyGroundValue(ChoisePoint cp) throws TermIsUnboundVariable {
		if (value != null) {
			return value.copyGroundValue(cp);
		} else {
			throw TermIsUnboundVariable.instance;
		}
	}
	public Term substituteWorlds(HashMap<AbstractWorld,Term> map, ChoisePoint cp) {
		if (value != null) {
			return value.substituteWorlds(map,cp);
		} else {
			return PrologUnknownValue.instance;
		}
	}
	// Domain check
	public boolean isCoveredByDomain(PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		if (value != null) {
			return value.isCoveredByDomain(baseDomain,cp,ignoreFreeVariables);
		} else {
			if (ignoreFreeVariables) {
				return true;
			} else {
				throw new WrongTermIsNotBoundVariable(this);
			}
		}
	}
	public Term checkSetTerm(long functor, PrologDomain headDomain, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		if (value != null) {
			return value.checkSetTerm(functor,headDomain,initialValue,cp,baseDomain);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	// Comparison operations
	public void compareWithTerm(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (value != null) {
			value.compareWithTerm(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public void compareWithBigInteger(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (value != null) {
			value.compareWithBigInteger(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public void compareWithLong(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (value != null) {
			value.compareWithLong(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public void compareWithDouble(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (value != null) {
			value.compareWithDouble(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public void compareWithString(String a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (value != null) {
			value.compareWithString(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public void compareWithDate(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (value != null) {
			value.compareWithDate(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public void compareTermWith(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (value != null) {
			value.compareTermWith(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public void compareBigIntegerWith(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (value != null) {
			value.compareBigIntegerWith(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public void compareLongWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (value != null) {
			value.compareLongWith(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public void compareDoubleWith(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (value != null) {
			value.compareDoubleWith(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public void compareStringWith(String a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (value != null) {
			value.compareStringWith(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public void compareDateWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (value != null) {
			value.compareDateWith(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public void compareListWith(Term aHead, Term aTail, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (value != null) {
			value.compareListWith(aHead,aTail,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	// Arithmetic operations
	public Term reactWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.reactWithTerm(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public Term reactWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.reactWithBigInteger(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public Term reactWithLong(long a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.reactWithLong(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public Term reactWithDouble(double a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.reactWithDouble(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public Term reactWithString(String a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.reactWithString(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public Term reactWithDate(long a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.reactWithDate(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public Term reactWithTime(long a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.reactWithTime(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public Term reactBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.reactBigIntegerWith(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public Term reactLongWith(long a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.reactLongWith(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public Term reactDoubleWith(double a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.reactDoubleWith(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public Term reactStringWith(String a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.reactStringWith(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public Term reactDateWith(long a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.reactDateWith(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public Term reactTimeWith(long a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.reactTimeWith(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public Term reactListWith(Term aHead, Term aTail, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.reactListWith(aHead,aTail,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	// Bitwise operations
	public Term blitWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.blitWithTerm(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public Term blitWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.blitWithBigInteger(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public Term blitWithLong(long a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.blitWithLong(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public Term blitWithDouble(double a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.blitWithDouble(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public Term blitBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.blitBigIntegerWith(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public Term blitLongWith(long a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.blitLongWith(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public Term blitDoubleWith(double a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.blitDoubleWith(a,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	public Term blitListWith(Term aHead, Term aTail, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.blitListWith(aHead,aTail,iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	// Unary operations
	public Term evaluate(ChoisePoint iX, UnaryOperation op) {
		if (value != null) {
			return value.evaluate(iX,op);
		} else {
			throw new WrongTermIsNotBoundVariable(this);
		}
	}
	// Converting Term to String
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, CharsetEncoder encoder) {
		if (value == null) {
			// return "_" + toNativeString();
			return "_";
		} else {
			// return "&" + value.toString(cp,isInner,provideStrictSyntax,encoder);
			return value.toString(cp,isInner,provideStrictSyntax,encoder);
		}
	}
}
