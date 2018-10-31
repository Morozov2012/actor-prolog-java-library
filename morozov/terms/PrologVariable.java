// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.domains.*;
import morozov.domains.signals.*;
import morozov.run.*;
import morozov.system.*;
import morozov.terms.errors.*;
import morozov.terms.signals.*;
import morozov.worlds.*;

import java.nio.charset.CharsetEncoder;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;

public class PrologVariable extends Term {
	//
	private Term value;
	//
	///////////////////////////////////////////////////////////////
	//
	public int hashCode() {
		if (value != null) {
			return value.hashCode();
		} else {
			// The hash code for the null reference is zero.
			return 0;
		}
	}
	public boolean equals(Object o2) {
		if (value != null) {
			return value.equals(o2);
		} else {
			throw new FreeVariableCannotBeCompared();
		}
	}
	public int compare(Object o2) {
		if (value != null) {
			return value.compare(o2);
		} else {
			throw new FreeVariableCannotBeCompared();
		}
	}
	public boolean isEqualToInteger(BigInteger v2) {
		if (value != null) {
			return value.isEqualToInteger(v2);
		} else {
			throw new FreeVariableCannotBeCompared();
		}
	}
	public boolean isEqualToReal(Double v2) {
		if (value != null) {
			return value.isEqualToReal(v2);
		} else {
			throw new FreeVariableCannotBeCompared();
		}
	}
	public boolean isEqualToString(String v2) {
		if (value != null) {
			return value.isEqualToString(v2);
		} else {
			throw new FreeVariableCannotBeCompared();
		}
	}
	public boolean isEqualToBinary(byte[] v2) {
		if (value != null) {
			return value.isEqualToBinary(v2);
		} else {
			throw new FreeVariableCannotBeCompared();
		}
	}
	public boolean isEqualToSymbol(long v2) {
		if (value != null) {
			return value.isEqualToSymbol(v2);
		} else {
			throw new FreeVariableCannotBeCompared();
		}
	}
	public boolean isEqualToEmptyList() {
		if (value != null) {
			return value.isEqualToEmptyList();
		} else {
			throw new FreeVariableCannotBeCompared();
		}
	}
	public boolean isEqualToEmptySet() {
		if (value != null) {
			return value.isEqualToEmptySet();
		} else {
			throw new FreeVariableCannotBeCompared();
		}
	}
	public boolean isEqualToNoValue() {
		if (value != null) {
			return value.isEqualToNoValue();
		} else {
			throw new FreeVariableCannotBeCompared();
		}
	}
	public boolean isEqualToUnknownValue() {
		if (value != null) {
			return value.isEqualToUnknownValue();
		} else {
			throw new FreeVariableCannotBeCompared();
		}
	}
	public boolean isEqualToStructure(long f2, Term[] a2) {
		if (value != null) {
			return value.isEqualToStructure(f2,a2);
		} else {
			throw new FreeVariableCannotBeCompared();
		}
	}
	public boolean isEqualToList(Term h2, Term t2) {
		if (value != null) {
			return value.isEqualToList(h2,t2);
		} else {
			throw new FreeVariableCannotBeCompared();
		}
	}
	public boolean isEqualToSet(UnderdeterminedSet o2) {
		if (value != null) {
			return value.isEqualToSet(o2);
		} else {
			throw new FreeVariableCannotBeCompared();
		}
	}
	public boolean isEqualToWorld(GlobalWorldIdentifier id2) {
		if (value != null) {
			return value.isEqualToWorld(id2);
		} else {
			throw new FreeVariableCannotBeCompared();
		}
	}
	public int compareWithInteger(BigInteger v2) {
		if (value != null) {
			return value.compareWithInteger(v2);
		} else {
			throw new FreeVariableCannotBeCompared();
		}
	}
	public int compareWithReal(Double v2) {
		if (value != null) {
			return value.compareWithReal(v2);
		} else {
			throw new FreeVariableCannotBeCompared();
		}
	}
	public int compareWithString(String v2) {
		if (value != null) {
			return value.compareWithString(v2);
		} else {
			throw new FreeVariableCannotBeCompared();
		}
	}
	public int compareWithBinary(byte[] v2) {
		if (value != null) {
			return value.compareWithBinary(v2);
		} else {
			throw new FreeVariableCannotBeCompared();
		}
	}
	public int compareWithSymbol(long v2) {
		if (value != null) {
			return value.compareWithSymbol(v2);
		} else {
			throw new FreeVariableCannotBeCompared();
		}
	}
	public int compareWithEmptyList() {
		if (value != null) {
			return value.compareWithEmptyList();
		} else {
			throw new FreeVariableCannotBeCompared();
		}
	}
	public int compareWithEmptySet() {
		if (value != null) {
			return value.compareWithEmptySet();
		} else {
			throw new FreeVariableCannotBeCompared();
		}
	}
	public int compareWithNoValue() {
		if (value != null) {
			return value.compareWithNoValue();
		} else {
			throw new FreeVariableCannotBeCompared();
		}
	}
	public int compareWithUnknownValue() {
		if (value != null) {
			return value.compareWithUnknownValue();
		} else {
			throw new FreeVariableCannotBeCompared();
		}
	}
	public int compareWithStructure(long f2, Term[] a2) {
		if (value != null) {
			return value.compareWithStructure(f2,a2);
		} else {
			throw new FreeVariableCannotBeCompared();
		}
	}
	public int compareWithList(Term h2, Term t2) {
		if (value != null) {
			return value.compareWithList(h2,t2);
		} else {
			throw new FreeVariableCannotBeCompared();
		}
	}
	public int compareWithSet(UnderdeterminedSet o2) {
		if (value != null) {
			return value.compareWithSet(o2);
		} else {
			throw new FreeVariableCannotBeCompared();
		}
	}
	public int compareWithWorld(GlobalWorldIdentifier id2) {
		if (value != null) {
			return value.compareWithWorld(id2);
		} else {
			throw new FreeVariableCannotBeCompared();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void isInteger(PrologInteger v, ChoisePoint cp) throws Backtracking {
		if (value != null) {
			value.isInteger(v,cp);
		} else {
			value= v;
			cp.pushTrail(this);
		}
	}
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
	public void isReal(PrologReal v, ChoisePoint cp) throws Backtracking {
		if (value != null) {
			value.isReal(v,cp);
		} else {
			value= v;
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
	public void isSymbol(PrologSymbol v, ChoisePoint cp) throws Backtracking {
		if (value != null) {
			value.isSymbol(v,cp);
		} else {
			value= v;
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
	public void isString(PrologString v, ChoisePoint cp) throws Backtracking {
		if (value != null) {
			value.isString(v,cp);
		} else {
			value= v;
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
	public void isBinary(PrologBinary v, ChoisePoint cp) throws Backtracking {
		if (value != null) {
			value.isBinary(v,cp);
		} else {
			value= v;
			cp.pushTrail(this);
		}
	}
	public void isBinary(byte[] v, ChoisePoint cp) throws Backtracking {
		if (value != null) {
			value.isBinary(v,cp);
		} else {
			value= new PrologBinary(v);
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
	public boolean thisIsOwnWorld() {
		if (value != null) {
			return value.thisIsOwnWorld();
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public boolean thisIsForeignWorld() {
		if (value != null) {
			return value.thisIsForeignWorld();
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
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
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public int getSmallIntegerValue(ChoisePoint cp) throws TermIsNotAnInteger {
		if (value != null) {
			return value.getSmallIntegerValue(cp);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public long getLongIntegerValue(ChoisePoint cp) throws TermIsNotAnInteger {
		if (value != null) {
			return value.getLongIntegerValue(cp);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public double getRealValue(ChoisePoint cp) throws TermIsNotAReal {
		if (value != null) {
			return value.getRealValue(cp);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public long getSymbolValue(ChoisePoint cp) throws TermIsNotASymbol {
		if (value != null) {
			return value.getSymbolValue(cp);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public String getStringValue(ChoisePoint cp) throws TermIsNotAString {
		if (value != null) {
			return value.getStringValue(cp);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public byte[] getBinaryValue(ChoisePoint cp) throws TermIsNotABinary {
		if (value != null) {
			return value.getBinaryValue(cp);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public long getStructureFunctor(ChoisePoint cp) throws TermIsNotAStructure {
		if (value != null) {
			return value.getStructureFunctor(cp);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public Term[] getStructureArguments(ChoisePoint cp) throws TermIsNotAStructure {
		if (value != null) {
			return value.getStructureArguments(cp);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
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
			throw new WrongArgumentIsNotArgumentList(this);
		}
	}
	public Term getExistentTail() {
		if (value != null) {
			return value.getExistentTail();
		} else {
			throw new WrongArgumentIsNotArgumentList(this);
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
	//
	///////////////////////////////////////////////////////////////
	//
	public AbstractInternalWorld getInternalWorld(ChoisePoint cp) throws Backtracking, TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		if (value != null) {
			return value.getInternalWorld(cp);
		} else {
			throw TermIsUnboundVariable.instance;
		}
	}
	//
	public AbstractInternalWorld internalWorld(AbstractProcess process, ChoisePoint cp) throws Backtracking {
		if (value != null) {
			return value.internalWorld(process,cp);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	//
	public AbstractInternalWorld internalWorld(ChoisePoint cp) {
		if (value != null) {
			return value.internalWorld(cp);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	//
	public GlobalWorldIdentifier getGlobalWorldIdentifier(ChoisePoint cp) throws TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		if (value != null) {
			return value.getGlobalWorldIdentifier(cp);
		} else {
			throw TermIsUnboundVariable.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
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
	//
	///////////////////////////////////////////////////////////////
	//
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		if (this == t) {
			return;
		} else if (value != null) {
			value.unifyWith(t,cp);
		} else if (t.thisIsFreeVariable()) {
			PrologVariable newNode= new PrologVariable();
			value= newNode;
			cp.pushTrail(this);
			t.setBacktrackableValue(newNode,cp);
			// cp.pushTrail(t);
		} else {
			value= t;
			cp.pushTrail(this);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term getValueOfVariable() {
		return value;
	}
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
	public void setBacktrackableValue(Term v, ChoisePoint iX) {
		value= v;
		iX.pushTrail(this);
	}
	public void setNonBacktrackableValue(Term v) {
		value= v;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void registerVariables(ActiveWorld process, boolean isSuspending, boolean isProtecting) {
		if (value != null) {
			value.registerVariables(process,isSuspending,isProtecting);
		} else {
			throw new WrongArgumentIsNotConstructorArgument(this);
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
			throw new WrongArgumentIsNotBoundVariable(this);
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
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean isCoveredByDomain(PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		if (value != null) {
			return value.isCoveredByDomain(baseDomain,cp,ignoreFreeVariables);
		} else {
			if (ignoreFreeVariables) {
				return true;
			} else {
				throw new WrongArgumentIsNotBoundVariable(this);
			}
		}
	}
	public Term checkSetTerm(long functor, PrologDomain headDomain, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		if (value != null) {
			return value.checkSetTerm(functor,headDomain,initialValue,cp,baseDomain);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void compareWithTerm(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (value != null) {
			value.compareWithTerm(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public void compareWithBigInteger(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (value != null) {
			value.compareWithBigInteger(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public void compareWithLong(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (value != null) {
			value.compareWithLong(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public void compareWithDouble(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (value != null) {
			value.compareWithDouble(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public void compareWithString(String a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (value != null) {
			value.compareWithString(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public void compareWithBinary(byte[] a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (value != null) {
			value.compareWithBinary(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public void compareWithDate(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (value != null) {
			value.compareWithDate(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public void compareTermWith(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (value != null) {
			value.compareTermWith(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public void compareBigIntegerWith(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (value != null) {
			value.compareBigIntegerWith(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public void compareLongWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (value != null) {
			value.compareLongWith(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public void compareDoubleWith(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (value != null) {
			value.compareDoubleWith(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public void compareStringWith(String a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (value != null) {
			value.compareStringWith(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public void compareBinaryWith(byte[] a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (value != null) {
			value.compareBinaryWith(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public void compareDateWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (value != null) {
			value.compareDateWith(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public void compareListWith(Term aHead, Term aTail, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (value != null) {
			value.compareListWith(aHead,aTail,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term reactWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.reactWithTerm(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public Term reactWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.reactWithBigInteger(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public Term reactWithLong(long a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.reactWithLong(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public Term reactWithDouble(double a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.reactWithDouble(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public Term reactWithString(String a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.reactWithString(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public Term reactWithBinary(byte[] a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.reactWithBinary(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public Term reactWithDate(long a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.reactWithDate(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public Term reactWithTime(long a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.reactWithTime(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public Term reactBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.reactBigIntegerWith(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public Term reactLongWith(long a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.reactLongWith(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public Term reactDoubleWith(double a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.reactDoubleWith(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public Term reactStringWith(String a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.reactStringWith(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public Term reactBinaryWith(byte[] a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.reactBinaryWith(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public Term reactDateWith(long a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.reactDateWith(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public Term reactTimeWith(long a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.reactTimeWith(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public Term reactListWith(Term aHead, Term aTail, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.reactListWith(aHead,aTail,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term blitWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.blitWithTerm(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public Term blitWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.blitWithBigInteger(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public Term blitWithLong(long a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.blitWithLong(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public Term blitWithDouble(double a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.blitWithDouble(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public Term blitBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.blitBigIntegerWith(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public Term blitLongWith(long a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.blitLongWith(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public Term blitDoubleWith(double a, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.blitDoubleWith(a,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	public Term blitListWith(Term aHead, Term aTail, ChoisePoint iX, BinaryOperation op) {
		if (value != null) {
			return value.blitListWith(aHead,aTail,iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term evaluate(ChoisePoint iX, UnaryOperation op) {
		if (value != null) {
			return value.evaluate(iX,op);
		} else {
			throw new WrongArgumentIsNotBoundVariable(this);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, boolean encodeWorlds, CharsetEncoder encoder) {
		if (value == null) {
			// return "_" + toNativeString();
			return "_";
		} else {
			return value.toString(cp,isInner,provideStrictSyntax,encodeWorlds,encoder);
		}
	}
}
