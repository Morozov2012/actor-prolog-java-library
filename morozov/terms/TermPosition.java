// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.domains.*;
import morozov.domains.signals.*;
import morozov.run.*;
import morozov.system.*;
import morozov.terms.signals.*;
import morozov.worlds.*;

import java.nio.charset.CharsetEncoder;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class TermPosition extends Term {
	//
	protected Term value;
	protected long position;
	//
	public TermPosition(Term t, long p) {
		value= t;
		position= p;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int hashCode() {
		return value.hashCode();
	}
	public boolean equals(Object o2) {
		return value.equals(o2);
	}
	public int compare(Object o2) {
		return value.compare(o2);
	}
	public boolean isEqualToInteger(BigInteger v2) {
		return value.isEqualToInteger(v2);
	}
	public boolean isEqualToReal(Double v2) {
		return value.isEqualToReal(v2);
	}
	public boolean isEqualToSymbol(long v2) {
		return value.isEqualToSymbol(v2);
	}
	public boolean isEqualToString(String v2) {
		return value.isEqualToString(v2);
	}
	public boolean isEqualToBinary(byte[] v2) {
		return value.isEqualToBinary(v2);
	}
	public boolean isEqualToEmptyList() {
		return value.isEqualToEmptyList();
	}
	public boolean isEqualToEmptySet() {
		return value.isEqualToEmptySet();
	}
	public boolean isEqualToNoValue() {
		return value.isEqualToNoValue();
	}
	public boolean isEqualToUnknownValue() {
		return value.isEqualToUnknownValue();
	}
	public boolean isEqualToStructure(long f2, Term[] a2) {
		return value.isEqualToStructure(f2,a2);
	}
	public boolean isEqualToList(Term h2, Term t2) {
		return value.isEqualToList(h2,t2);
	}
	public boolean isEqualToSet(UnderdeterminedSet o2) {
		return value.isEqualToSet(o2);
	}
	public boolean isEqualToWorld(GlobalWorldIdentifier id2) {
		return value.isEqualToWorld(id2);
	}
	public int compareWithInteger(BigInteger v2) {
		return value.compareWithInteger(v2);
	}
	public int compareWithReal(Double v2) {
		return value.compareWithReal(v2);
	}
	public int compareWithString(String v2) {
		return value.compareWithString(v2);
	}
	public int compareWithBinary(byte[] v2) {
		return value.compareWithBinary(v2);
	}
	public int compareWithSymbol(long v2) {
		return value.compareWithSymbol(v2);
	}
	public int compareWithEmptyList() {
		return value.compareWithEmptyList();
	}
	public int compareWithEmptySet() {
		return value.compareWithEmptySet();
	}
	public int compareWithNoValue() {
		return value.compareWithNoValue();
	}
	public int compareWithUnknownValue() {
		return value.compareWithUnknownValue();
	}
	public int compareWithStructure(long f2, Term[] a2) {
		return value.compareWithStructure(f2,a2);
	}
	public int compareWithList(Term h2, Term t2) {
		return value.compareWithList(h2,t2);
	}
	public int compareWithSet(UnderdeterminedSet o2) {
		return value.compareWithSet(o2);
	}
	public int compareWithWorld(GlobalWorldIdentifier id2) {
		return value.compareWithWorld(id2);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void isInteger(PrologInteger v, ChoisePoint cp) throws Backtracking {
		value.isInteger(v,cp);
	}
	public void isInteger(int v, ChoisePoint cp) throws Backtracking {
		value.isInteger(v,cp);
	}
	public void isInteger(BigInteger v, ChoisePoint cp) throws Backtracking {
		value.isInteger(v,cp);
	}
	public void isReal(PrologReal v, ChoisePoint cp) throws Backtracking {
		value.isReal(v,cp);
	}
	public void isReal(double v, ChoisePoint cp) throws Backtracking {
		value.isReal(v,cp);
	}
	public void isSymbol(PrologSymbol v, ChoisePoint cp) throws Backtracking {
		value.isSymbol(v,cp);
	}
	public void isSymbol(long v, ChoisePoint cp) throws Backtracking {
		value.isSymbol(v,cp);
	}
	public void isString(PrologString v, ChoisePoint cp) throws Backtracking {
		value.isString(v,cp);
	}
	public void isString(String v, ChoisePoint cp) throws Backtracking {
		value.isString(v,cp);
	}
	public void isBinary(PrologBinary v, ChoisePoint cp) throws Backtracking {
		value.isBinary(v,cp);
	}
	public void isBinary(byte[] v, ChoisePoint cp) throws Backtracking {
		value.isBinary(v,cp);
	}
	public Term[] isStructure(long aFunctor, int arity, ChoisePoint cp) throws Backtracking {
		return value.isStructure(aFunctor,arity,cp);
	}
	public void isEmptyList(ChoisePoint cp) throws Backtracking {
		value.isEmptyList(cp);
	}
	// public boolean thisIsEmptyList() {
	//	return value.thisIsEmptyList();
	// }
	public void isOutputEmptyList(ChoisePoint cp) {
		value.isOutputEmptyList(cp);
	}
	public void isEmptySet(ChoisePoint cp) throws Backtracking {
		value.isEmptySet(cp);
	}
	// public boolean thisIsEmptySet() {
	//	return value.thisIsEmptySet();
	// }
	// public boolean thisIsUnderdeterminedSet() {
	//	return value.thisIsUnderdeterminedSet();
	// }
	public void isUnknownValue(ChoisePoint cp) throws Backtracking {
		value.isUnknownValue(cp);
	}
	// public boolean thisIsUnknownValue() {
	//	return value.thisIsUnknownValue();
	// }
	// public void isNoValue(ChoisePoint cp) throws Backtracking {
	//	value.isNoValue(cp);
	// }
	// public boolean thisIsNoValue() {
	//	return value.thisIsNoValue();
	// }
	public void isWorld(Term v, ChoisePoint cp) throws Backtracking {
		value.isWorld(v,cp);
	}
	// public boolean thisIsOwnWorld() {
	//	return value.thisIsOwnWorld();
	// }
	// public boolean thisIsForeignWorld() {
	//	return value.thisIsForeignWorld();
	// }
	// public boolean thisIsProcess() {
	//	return value.thisIsProcess();
	// }
	// public boolean thisIsSlotVariable() {
	//	return value.thisIsSlotVariable();
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public Term getValue(ChoisePoint cp) {
		return value.getValue(cp);
	}
	public Term dereferenceValue(ChoisePoint cp) {
		return value.dereferenceValue(cp);
	}
	public Term extractSlotVariable() {
		return value.extractSlotVariable();
	}
	public BigInteger getIntegerValue(ChoisePoint cp) throws TermIsNotAnInteger {
		return value.getIntegerValue(cp);
	}
	public int getSmallIntegerValue(ChoisePoint cp) throws TermIsNotAnInteger {
		return value.getSmallIntegerValue(cp);
	}
	public long getLongIntegerValue(ChoisePoint cp) throws TermIsNotAnInteger {
		return value.getLongIntegerValue(cp);
	}
	public double getRealValue(ChoisePoint cp) throws TermIsNotAReal {
		return value.getRealValue(cp);
	}
	public String getStringValue(ChoisePoint cp) throws TermIsNotAString {
		return value.getStringValue(cp);
	}
	public byte[] getBinaryValue(ChoisePoint cp) throws TermIsNotABinary {
		return value.getBinaryValue(cp);
	}
	public long getSymbolValue(ChoisePoint cp) throws TermIsNotASymbol {
		return value.getSymbolValue(cp);
	}
	public long getStructureFunctor(ChoisePoint cp) throws TermIsNotAStructure {
		return value.getStructureFunctor(cp);
	}
	public Term[] getStructureArguments(ChoisePoint cp) throws TermIsNotAStructure {
		return value.getStructureArguments(cp);
	}
	public Term getListHead(ChoisePoint cp) throws Backtracking {
		return value.getListHead(cp);
	}
	public Term getListTail(ChoisePoint cp) throws Backtracking {
		return value.getListTail(cp);
	}
	public Term getNextListHead(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		return value.getNextListHead(cp);
	}
	public Term getNextListTail(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		return value.getNextListTail(cp);
	}
	public Term getExistentHead() {
		return value.getExistentHead();
	}
	public Term getExistentTail() {
		return value.getExistentTail();
	}
	public Term getOutputHead(ChoisePoint cp) {
		return value.getOutputHead(cp);
	}
	public Term getOutputTail(ChoisePoint cp) {
		return value.getOutputTail(cp);
	}
	public Term getNextListHeadSafely(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		return value.getNextListHeadSafely(cp);
	}
	public Term getNextListTailSafely(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		return value.getNextListTailSafely(cp);
	}
	public long getNextPairName(ChoisePoint cp) throws EndOfSet, TermIsNotASet {
		return value.getNextPairName(cp);
	}
	public Term getNextPairValue(ChoisePoint cp) throws EndOfSet, TermIsNotASet, SetElementIsProhibited {
		return value.getNextPairValue(cp);
	}
	public Term getNextSetTail(ChoisePoint cp) throws EndOfSet, TermIsNotASet {
		return value.getNextSetTail(cp);
	}
	public Term getNamedElement(long aName, ChoisePoint cp) throws Backtracking {
		return value.getNamedElement(aName,cp);
	}
	// public Term checkSetAndGetNamedElement(long aName, ChoisePoint cp) throws Backtracking, TermIsNotASet {
	//	return value.checkSetAndGetNamedElement(aName,cp);
	// }
	public void checkIfTermIsASet(ChoisePoint cp) throws TermIsNotASet {
		value.checkIfTermIsASet(cp);
	}
	public Term retrieveSetElementValue(ChoisePoint cp) throws Backtracking, TermIsNotSetElement {
		return value.retrieveSetElementValue(cp);
	}
	public Term excludeNamedElements(long[] aNames, ChoisePoint cp) throws Backtracking {
		return value.excludeNamedElements(aNames,cp);
	}
	public void hasNoMoreElements(long[] aNames, ChoisePoint cp) throws Backtracking {
		value.hasNoMoreElements(aNames,cp);
	}
	public void prohibitNamedElement(long aName, ChoisePoint cp) throws Backtracking {
		value.prohibitNamedElement(aName,cp);
	}
	public void verifySet(long[] aNames, ChoisePoint cp) throws Backtracking {
		value.verifySet(aNames,cp);
	}
	public Term exploreSetPositiveElements(HashMap<Long,Term> positiveMap, ChoisePoint cp) {
		return value.exploreSetPositiveElements(positiveMap,cp);
	}
	public Term exploreSet(HashMap<Long,Term> positiveMap, HashSet<Long> negativeMap, ChoisePoint cp) {
		return value.exploreSet(positiveMap,negativeMap,cp);
	}
	public Term exploreSet(ChoisePoint cp) {
		return value.exploreSet(cp);
	}
	public void appendNamedElement(long aName, Term aValue, ChoisePoint cp) throws Backtracking {
		value.appendNamedElement(aName,aValue,cp);
	}
	public void appendNamedElementProhibition(long aName, ChoisePoint cp) throws Backtracking {
		value.appendNamedElementProhibition(aName,cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public AbstractInternalWorld getInternalWorld(ChoisePoint cp) throws Backtracking, TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		return value.getInternalWorld(cp);
	}
	//
	public AbstractInternalWorld internalWorld(AbstractProcess process, ChoisePoint cp) throws Backtracking {
		return value.internalWorld(process,cp);
	}
	//
	public AbstractInternalWorld internalWorld(ChoisePoint cp) {
		return value.internalWorld(cp);
	}
	//
	public GlobalWorldIdentifier getGlobalWorldIdentifier(ChoisePoint cp) throws TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		return value.getGlobalWorldIdentifier(cp);
	}
	//
	public long[] getClassHierarchy() {
		return value.getClassHierarchy();
	}
	public long[] getInterfaceHierarchy() {
		return value.getInterfaceHierarchy();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void unifyWithStructure(long aFunctor, Term[] arguments, Term structure, ChoisePoint cp) throws Backtracking {
		value.unifyWithStructure(aFunctor,arguments,structure,cp);
	}
	public void unifyWithList(Term aHead, Term aTail, Term aList, ChoisePoint cp) throws Backtracking {
		value.unifyWithList(aHead,aTail,aList,cp);
	}
	public void unifyWithSet(long aName, Term aValue, Term aTail, Term aSet, ChoisePoint cp) throws Backtracking {
		value.unifyWithSet(aName,aValue,aTail,aSet,cp);
	}
	public void unifyWithProhibitedElement(long aName, Term aTail, Term aSet, ChoisePoint cp) throws Backtracking {
		value.unifyWithProhibitedElement(aName,aTail,aSet,cp);
	}
	public void unifyWithOptimizedSet(PrologOptimizedSet set, ChoisePoint cp) throws Backtracking {
		value.unifyWithOptimizedSet(set,cp);
	}
	public void inheritSetElements(PrologOptimizedSet set, ChoisePoint cp) throws Backtracking {
		value.inheritSetElements(set,cp);
	}
	public void unifyWithSetElement(Term aElement, Term setElement, ChoisePoint cp) throws Backtracking {
		value.unifyWithSetElement(aElement,setElement,cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		value.unifyWith(t,cp);
	}
	//
	// public boolean thisIsFreeVariable() {
	//	return value.thisIsFreeVariable();
	// }
	// Internal operations
	// public boolean thisIsArgumentNumber() {
	//	return value.thisIsArgumentNumber();
	// }
	public long getNumber() {
		return value.getNumber();
	}
	public ArgumentNumber add(long deltaN) {
		return value.add(deltaN);
	}
	public ArgumentNumber subtract(long deltaN) {
		return value.subtract(deltaN);
	}
	public void isArityEqualTo(long v, ChoisePoint cp) throws Backtracking {
		value.isArityEqualTo(v,cp);
	}
	public void isArityMoreOrEqualTo(long v, ChoisePoint cp) throws Backtracking {
		value.isArityMoreOrEqualTo(v,cp);
	}
	public void verifyListOfRestValues(long deltaN, Term list, ChoisePoint cp) {
		value.verifyListOfRestValues(deltaN,list,cp);
	}
	public long getPosition() {
		return position;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void clear() {
		value.clear();
	}
	public void setBacktrackableValue(Term v, ChoisePoint iX) {
		value.setBacktrackableValue(v,iX);
	}
	public void setNonBacktrackableValue(Term v) {
		value.setNonBacktrackableValue(v);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void registerNewSlotVariable(HashSet<SlotVariable> slotVariables) {
		value.registerNewSlotVariable(slotVariables);
	}
	public void extractWorlds(AbstractProcess process, LinkedHashSet<AbstractInternalWorld> list) {
		value.extractWorlds(process,list);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void registerVariables(ActiveWorld process, boolean isSuspending, boolean isProtecting) {
		value.registerVariables(process,isSuspending,isProtecting);
	}
	public void registerTargetWorlds(HashSet<AbstractWorld> worlds, ChoisePoint cp) {
		value.registerTargetWorlds(worlds,cp);
	}
	public Term circumscribe() {
		return value.circumscribe();
	}
	public Term copyValue(ChoisePoint cp, TermCircumscribingMode mode) {
		return new TermPosition(value.copyValue(cp,mode),position);
	}
	public Term copyGroundValue(ChoisePoint cp) throws TermIsUnboundVariable {
		return new TermPosition(value.copyGroundValue(cp),position);
	}
	public Term substituteWorlds(HashMap<AbstractWorld,Term> map, ChoisePoint cp) {
		return new TermPosition(value.substituteWorlds(map,cp),position);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean isCoveredByDomain(PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		return value.isCoveredByDomain(baseDomain,cp,ignoreFreeVariables);
	}
	public Term checkSetTerm(long functor, PrologDomain headDomain, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		return value.checkSetTerm(functor,headDomain,initialValue,cp,baseDomain);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void compareWithTerm(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		value.compareWithTerm(a,iX,op);
	}
	public void compareWithBigInteger(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		value.compareWithBigInteger(a,iX,op);
	}
	public void compareWithLong(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		value.compareWithLong(a,iX,op);
	}
	public void compareWithDouble(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		value.compareWithDouble(a,iX,op);
	}
	public void compareWithString(String a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		value.compareWithString(a,iX,op);
	}
	public void compareWithBinary(byte[] a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		value.compareWithBinary(a,iX,op);
	}
	public void compareWithDate(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		value.compareWithDate(a,iX,op);
	}
	public void compareTermWith(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		value.compareTermWith(a,iX,op);
	}
	public void compareBigIntegerWith(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		value.compareBigIntegerWith(a,iX,op);
	}
	public void compareLongWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		value.compareLongWith(a,iX,op);
	}
	public void compareDoubleWith(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		value.compareDoubleWith(a,iX,op);
	}
	public void compareStringWith(String a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		value.compareStringWith(a,iX,op);
	}
	public void compareBinaryWith(byte[] a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		value.compareBinaryWith(a,iX,op);
	}
	public void compareDateWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		value.compareDateWith(a,iX,op);
	}
	public void compareListWith(Term aHead, Term aTail, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		value.compareListWith(aHead,aTail,iX,op);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term reactWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		return value.reactWithTerm(a,iX,op);
	}
	public Term reactWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return value.reactWithBigInteger(a,iX,op);
	}
	public Term reactWithLong(long a, ChoisePoint iX, BinaryOperation op) {
		return value.reactWithLong(a,iX,op);
	}
	public Term reactWithDouble(double a, ChoisePoint iX, BinaryOperation op) {
		return value.reactWithDouble(a,iX,op);
	}
	public Term reactWithString(String a, ChoisePoint iX, BinaryOperation op) {
		return value.reactWithString(a,iX,op);
	}
	public Term reactWithBinary(byte[] a, ChoisePoint iX, BinaryOperation op) {
		return value.reactWithBinary(a,iX,op);
	}
	public Term reactWithDate(long a, ChoisePoint iX, BinaryOperation op) {
		return value.reactWithDate(a,iX,op);
	}
	public Term reactWithTime(long a, ChoisePoint iX, BinaryOperation op) {
		return value.reactWithTime(a,iX,op);
	}
	public Term reactBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return value.reactBigIntegerWith(a,iX,op);
	}
	public Term reactLongWith(long a, ChoisePoint iX, BinaryOperation op) {
		return value.reactLongWith(a,iX,op);
	}
	public Term reactDoubleWith(double a, ChoisePoint iX, BinaryOperation op) {
		return value.reactDoubleWith(a,iX,op);
	}
	public Term reactStringWith(String a, ChoisePoint iX, BinaryOperation op) {
		return value.reactStringWith(a,iX,op);
	}
	public Term reactBinaryWith(byte[] a, ChoisePoint iX, BinaryOperation op) {
		return value.reactBinaryWith(a,iX,op);
	}
	public Term reactDateWith(long a, ChoisePoint iX, BinaryOperation op) {
		return value.reactDateWith(a,iX,op);
	}
	public Term reactTimeWith(long a, ChoisePoint iX, BinaryOperation op) {
		return value.reactTimeWith(a,iX,op);
	}
	public Term reactListWith(Term aHead, Term aTail, ChoisePoint iX, BinaryOperation op) {
		return value.reactListWith(aHead,aTail,iX,op);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term blitWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		return value.blitWithTerm(a,iX,op);
	}
	public Term blitWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return value.blitWithBigInteger(a,iX,op);
	}
	public Term blitWithLong(long a, ChoisePoint iX, BinaryOperation op) {
		return value.blitWithLong(a,iX,op);
	}
	public Term blitWithDouble(double a, ChoisePoint iX, BinaryOperation op) {
		return value.blitWithDouble(a,iX,op);
	}
	public Term blitBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return value.blitBigIntegerWith(a,iX,op);
	}
	public Term blitLongWith(long a, ChoisePoint iX, BinaryOperation op) {
		return value.blitLongWith(a,iX,op);
	}
	public Term blitDoubleWith(double a, ChoisePoint iX, BinaryOperation op) {
		return value.blitDoubleWith(a,iX,op);
	}
	public Term blitListWith(Term aHead, Term aTail, ChoisePoint iX, BinaryOperation op) {
		return value.blitListWith(aHead,aTail,iX,op);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term evaluate(ChoisePoint iX, UnaryOperation op) {
		return value.evaluate(iX,op);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, boolean encodeWorlds, CharsetEncoder encoder) {
		return value.toString(cp,isInner,provideStrictSyntax,encodeWorlds,encoder);
	}
	public String toString(ChoisePoint cp) {
		return value.toString(cp);
	}
	public String toString() {
		return value.toString();
	}
}
