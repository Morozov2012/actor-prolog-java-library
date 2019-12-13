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
	private static final long serialVersionUID= 0x43CE5BE646BC6B7CL; // 4885943690330925948L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.terms","TermPosition");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public TermPosition(Term t, long p) {
		value= t;
		position= p;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public int hashCode() {
		return value.hashCode();
	}
	@Override
	public boolean equals(Object o2) {
		return value.equals(o2);
	}
	@Override
	public int compare(Object o2) {
		return value.compare(o2);
	}
	@Override
	public boolean isEqualToInteger(BigInteger v2) {
		return value.isEqualToInteger(v2);
	}
	@Override
	public boolean isEqualToReal(Double v2) {
		return value.isEqualToReal(v2);
	}
	@Override
	public boolean isEqualToSymbol(long v2) {
		return value.isEqualToSymbol(v2);
	}
	@Override
	public boolean isEqualToString(String v2) {
		return value.isEqualToString(v2);
	}
	@Override
	public boolean isEqualToBinary(byte[] v2) {
		return value.isEqualToBinary(v2);
	}
	@Override
	public boolean isEqualToEmptyList() {
		return value.isEqualToEmptyList();
	}
	@Override
	public boolean isEqualToEmptySet() {
		return value.isEqualToEmptySet();
	}
	@Override
	public boolean isEqualToNoValue() {
		return value.isEqualToNoValue();
	}
	@Override
	public boolean isEqualToUnknownValue() {
		return value.isEqualToUnknownValue();
	}
	@Override
	public boolean isEqualToStructure(long f2, Term[] a2) {
		return value.isEqualToStructure(f2,a2);
	}
	@Override
	public boolean isEqualToList(Term h2, Term t2) {
		return value.isEqualToList(h2,t2);
	}
	@Override
	public boolean isEqualToSet(UnderdeterminedSet o2) {
		return value.isEqualToSet(o2);
	}
	@Override
	public boolean isEqualToWorld(GlobalWorldIdentifier id2) {
		return value.isEqualToWorld(id2);
	}
	@Override
	public int compareWithInteger(BigInteger v2) {
		return value.compareWithInteger(v2);
	}
	@Override
	public int compareWithReal(Double v2) {
		return value.compareWithReal(v2);
	}
	@Override
	public int compareWithString(String v2) {
		return value.compareWithString(v2);
	}
	@Override
	public int compareWithBinary(byte[] v2) {
		return value.compareWithBinary(v2);
	}
	@Override
	public int compareWithSymbol(long v2) {
		return value.compareWithSymbol(v2);
	}
	@Override
	public int compareWithEmptyList() {
		return value.compareWithEmptyList();
	}
	@Override
	public int compareWithEmptySet() {
		return value.compareWithEmptySet();
	}
	@Override
	public int compareWithNoValue() {
		return value.compareWithNoValue();
	}
	@Override
	public int compareWithUnknownValue() {
		return value.compareWithUnknownValue();
	}
	@Override
	public int compareWithStructure(long f2, Term[] a2) {
		return value.compareWithStructure(f2,a2);
	}
	@Override
	public int compareWithList(Term h2, Term t2) {
		return value.compareWithList(h2,t2);
	}
	@Override
	public int compareWithSet(UnderdeterminedSet o2) {
		return value.compareWithSet(o2);
	}
	@Override
	public int compareWithWorld(GlobalWorldIdentifier id2) {
		return value.compareWithWorld(id2);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void isInteger(PrologInteger v, ChoisePoint cp) throws Backtracking {
		value.isInteger(v,cp);
	}
	@Override
	public void isInteger(int v, ChoisePoint cp) throws Backtracking {
		value.isInteger(v,cp);
	}
	@Override
	public void isInteger(BigInteger v, ChoisePoint cp) throws Backtracking {
		value.isInteger(v,cp);
	}
	@Override
	public void isReal(PrologReal v, ChoisePoint cp) throws Backtracking {
		value.isReal(v,cp);
	}
	@Override
	public void isReal(double v, ChoisePoint cp) throws Backtracking {
		value.isReal(v,cp);
	}
	@Override
	public void isSymbol(PrologSymbol v, ChoisePoint cp) throws Backtracking {
		value.isSymbol(v,cp);
	}
	@Override
	public void isSymbol(long v, ChoisePoint cp) throws Backtracking {
		value.isSymbol(v,cp);
	}
	@Override
	public void isString(PrologString v, ChoisePoint cp) throws Backtracking {
		value.isString(v,cp);
	}
	@Override
	public void isString(String v, ChoisePoint cp) throws Backtracking {
		value.isString(v,cp);
	}
	@Override
	public void isBinary(PrologBinary v, ChoisePoint cp) throws Backtracking {
		value.isBinary(v,cp);
	}
	@Override
	public void isBinary(byte[] v, ChoisePoint cp) throws Backtracking {
		value.isBinary(v,cp);
	}
	@Override
	public Term[] isStructure(long aFunctor, int arity, ChoisePoint cp) throws Backtracking {
		return value.isStructure(aFunctor,arity,cp);
	}
	@Override
	public void isEmptyList(ChoisePoint cp) throws Backtracking {
		value.isEmptyList(cp);
	}
	@Override
	public void isOutputEmptyList(ChoisePoint cp) {
		value.isOutputEmptyList(cp);
	}
	@Override
	public void isEmptySet(ChoisePoint cp) throws Backtracking {
		value.isEmptySet(cp);
	}
	@Override
	public void isUnknownValue(ChoisePoint cp) throws Backtracking {
		value.isUnknownValue(cp);
	}
	@Override
	public void isWorld(Term v, ChoisePoint cp) throws Backtracking {
		value.isWorld(v,cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term getValue(ChoisePoint cp) {
		return value.getValue(cp);
	}
	@Override
	public Term dereferenceValue(ChoisePoint cp) {
		return value.dereferenceValue(cp);
	}
	@Override
	public Term extractSlotVariable() {
		return value.extractSlotVariable();
	}
	@Override
	public BigInteger getIntegerValue(ChoisePoint cp) throws TermIsNotAnInteger {
		return value.getIntegerValue(cp);
	}
	@Override
	public int getSmallIntegerValue(ChoisePoint cp) throws TermIsNotAnInteger {
		return value.getSmallIntegerValue(cp);
	}
	@Override
	public long getLongIntegerValue(ChoisePoint cp) throws TermIsNotAnInteger {
		return value.getLongIntegerValue(cp);
	}
	@Override
	public double getRealValue(ChoisePoint cp) throws TermIsNotAReal {
		return value.getRealValue(cp);
	}
	@Override
	public String getStringValue(ChoisePoint cp) throws TermIsNotAString {
		return value.getStringValue(cp);
	}
	@Override
	public byte[] getBinaryValue(ChoisePoint cp) throws TermIsNotABinary {
		return value.getBinaryValue(cp);
	}
	@Override
	public long getSymbolValue(ChoisePoint cp) throws TermIsNotASymbol {
		return value.getSymbolValue(cp);
	}
	@Override
	public long getStructureFunctor(ChoisePoint cp) throws TermIsNotAStructure {
		return value.getStructureFunctor(cp);
	}
	@Override
	public Term[] getStructureArguments(ChoisePoint cp) throws TermIsNotAStructure {
		return value.getStructureArguments(cp);
	}
	@Override
	public Term getListHead(ChoisePoint cp) throws Backtracking {
		return value.getListHead(cp);
	}
	@Override
	public Term getListTail(ChoisePoint cp) throws Backtracking {
		return value.getListTail(cp);
	}
	@Override
	public Term getNextListHead(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		return value.getNextListHead(cp);
	}
	@Override
	public Term getNextListTail(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		return value.getNextListTail(cp);
	}
	@Override
	public Term getExistentHead() {
		return value.getExistentHead();
	}
	@Override
	public Term getExistentTail() {
		return value.getExistentTail();
	}
	@Override
	public Term getOutputHead(ChoisePoint cp) {
		return value.getOutputHead(cp);
	}
	@Override
	public Term getOutputTail(ChoisePoint cp) {
		return value.getOutputTail(cp);
	}
	@Override
	public Term getNextListHeadSafely(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		return value.getNextListHeadSafely(cp);
	}
	@Override
	public Term getNextListTailSafely(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		return value.getNextListTailSafely(cp);
	}
	@Override
	public long getNextPairName(ChoisePoint cp) throws EndOfSet, TermIsNotASet {
		return value.getNextPairName(cp);
	}
	@Override
	public Term getNextPairValue(ChoisePoint cp) throws EndOfSet, TermIsNotASet, SetElementIsProhibited {
		return value.getNextPairValue(cp);
	}
	@Override
	public Term getNextSetTail(ChoisePoint cp) throws EndOfSet, TermIsNotASet {
		return value.getNextSetTail(cp);
	}
	@Override
	public Term getNamedElement(long aName, ChoisePoint cp) throws Backtracking {
		return value.getNamedElement(aName,cp);
	}
	@Override
	public void checkIfTermIsASet(ChoisePoint cp) throws TermIsNotASet {
		value.checkIfTermIsASet(cp);
	}
	@Override
	public Term retrieveSetElementValue(ChoisePoint cp) throws Backtracking, TermIsNotSetElement {
		return value.retrieveSetElementValue(cp);
	}
	@Override
	public Term excludeNamedElements(long[] aNames, ChoisePoint cp) throws Backtracking {
		return value.excludeNamedElements(aNames,cp);
	}
	@Override
	public void hasNoMoreElements(long[] aNames, ChoisePoint cp) throws Backtracking {
		value.hasNoMoreElements(aNames,cp);
	}
	@Override
	public void prohibitNamedElement(long aName, ChoisePoint cp) throws Backtracking {
		value.prohibitNamedElement(aName,cp);
	}
	@Override
	public void verifySet(long[] aNames, ChoisePoint cp) throws Backtracking {
		value.verifySet(aNames,cp);
	}
	@Override
	public Term exploreSetPositiveElements(HashMap<Long,Term> positiveMap, ChoisePoint cp) {
		return value.exploreSetPositiveElements(positiveMap,cp);
	}
	@Override
	public Term exploreSet(HashMap<Long,Term> positiveMap, HashSet<Long> negativeMap, ChoisePoint cp) {
		return value.exploreSet(positiveMap,negativeMap,cp);
	}
	@Override
	public Term exploreSet(ChoisePoint cp) {
		return value.exploreSet(cp);
	}
	@Override
	public void appendNamedElement(long aName, Term aValue, ChoisePoint cp) throws Backtracking {
		value.appendNamedElement(aName,aValue,cp);
	}
	@Override
	public void appendNamedElementProhibition(long aName, ChoisePoint cp) throws Backtracking {
		value.appendNamedElementProhibition(aName,cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public AbstractInternalWorld getInternalWorld(ChoisePoint cp) throws Backtracking, TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		return value.getInternalWorld(cp);
	}
	//
	@Override
	public AbstractInternalWorld internalWorld(AbstractProcess process, ChoisePoint cp) throws Backtracking {
		return value.internalWorld(process,cp);
	}
	//
	@Override
	public AbstractInternalWorld internalWorld(ChoisePoint cp) {
		return value.internalWorld(cp);
	}
	//
	@Override
	public GlobalWorldIdentifier getGlobalWorldIdentifier(ChoisePoint cp) throws TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		return value.getGlobalWorldIdentifier(cp);
	}
	//
	@Override
	public long[] getClassHierarchy() {
		return value.getClassHierarchy();
	}
	@Override
	public long[] getInterfaceHierarchy() {
		return value.getInterfaceHierarchy();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void unifyWithStructure(long aFunctor, Term[] arguments, Term structure, ChoisePoint cp) throws Backtracking {
		value.unifyWithStructure(aFunctor,arguments,structure,cp);
	}
	@Override
	public void unifyWithList(Term aHead, Term aTail, Term aList, ChoisePoint cp) throws Backtracking {
		value.unifyWithList(aHead,aTail,aList,cp);
	}
	@Override
	public void unifyWithSet(long aName, Term aValue, Term aTail, Term aSet, ChoisePoint cp) throws Backtracking {
		value.unifyWithSet(aName,aValue,aTail,aSet,cp);
	}
	@Override
	public void unifyWithProhibitedElement(long aName, Term aTail, Term aSet, ChoisePoint cp) throws Backtracking {
		value.unifyWithProhibitedElement(aName,aTail,aSet,cp);
	}
	@Override
	public void unifyWithOptimizedSet(PrologOptimizedSet set, ChoisePoint cp) throws Backtracking {
		value.unifyWithOptimizedSet(set,cp);
	}
	@Override
	public void inheritSetElements(PrologOptimizedSet set, ChoisePoint cp) throws Backtracking {
		value.inheritSetElements(set,cp);
	}
	@Override
	public void unifyWithSetElement(Term aElement, Term setElement, ChoisePoint cp) throws Backtracking {
		value.unifyWithSetElement(aElement,setElement,cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		value.unifyWith(t,cp);
	}
	@Override
	public long getNumber() {
		return value.getNumber();
	}
	@Override
	public ArgumentNumber add(long deltaN) {
		return value.add(deltaN);
	}
	@Override
	public ArgumentNumber subtract(long deltaN) {
		return value.subtract(deltaN);
	}
	@Override
	public void isArityEqualTo(long v, ChoisePoint cp) throws Backtracking {
		value.isArityEqualTo(v,cp);
	}
	@Override
	public void isArityMoreOrEqualTo(long v, ChoisePoint cp) throws Backtracking {
		value.isArityMoreOrEqualTo(v,cp);
	}
	@Override
	public void verifyListOfRestValues(long deltaN, Term list, ChoisePoint cp) {
		value.verifyListOfRestValues(deltaN,list,cp);
	}
	@Override
	public long getPosition() {
		return position;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void clear() {
		value.clear();
	}
	@Override
	public void setBacktrackableValue(Term v, ChoisePoint iX) {
		value.setBacktrackableValue(v,iX);
	}
	@Override
	public void setNonBacktrackableValue(Term v) {
		value.setNonBacktrackableValue(v);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void registerNewSlotVariable(HashSet<SlotVariable> slotVariables) {
		value.registerNewSlotVariable(slotVariables);
	}
	@Override
	public void extractWorlds(AbstractProcess process, LinkedHashSet<AbstractInternalWorld> list) {
		value.extractWorlds(process,list);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void registerVariables(ActiveWorld process, boolean isSuspending, boolean isProtecting) {
		value.registerVariables(process,isSuspending,isProtecting);
	}
	@Override
	public void registerTargetWorlds(HashSet<AbstractWorld> worlds, ChoisePoint cp) {
		value.registerTargetWorlds(worlds,cp);
	}
	@Override
	public Term circumscribe() {
		return value.circumscribe();
	}
	@Override
	public Term copyValue(ChoisePoint cp, TermCircumscribingMode mode) {
		return new TermPosition(value.copyValue(cp,mode),position);
	}
	@Override
	public Term copyGroundValue(ChoisePoint cp) throws TermIsUnboundVariable {
		return new TermPosition(value.copyGroundValue(cp),position);
	}
	@Override
	public Term substituteWorlds(HashMap<AbstractWorld,Term> map, ChoisePoint cp) {
		return new TermPosition(value.substituteWorlds(map,cp),position);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean isCoveredByDomain(PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		return value.isCoveredByDomain(baseDomain,cp,ignoreFreeVariables);
	}
	@Override
	public Term checkSetTerm(long functor, PrologDomain headDomain, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		return value.checkSetTerm(functor,headDomain,initialValue,cp,baseDomain);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void compareWithTerm(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		value.compareWithTerm(a,iX,op);
	}
	@Override
	public void compareWithBigInteger(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		value.compareWithBigInteger(a,iX,op);
	}
	@Override
	public void compareWithLong(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		value.compareWithLong(a,iX,op);
	}
	@Override
	public void compareWithDouble(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		value.compareWithDouble(a,iX,op);
	}
	@Override
	public void compareWithString(String a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		value.compareWithString(a,iX,op);
	}
	@Override
	public void compareWithBinary(byte[] a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		value.compareWithBinary(a,iX,op);
	}
	@Override
	public void compareWithDate(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		value.compareWithDate(a,iX,op);
	}
	@Override
	public void compareTermWith(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		value.compareTermWith(a,iX,op);
	}
	@Override
	public void compareBigIntegerWith(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		value.compareBigIntegerWith(a,iX,op);
	}
	@Override
	public void compareLongWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		value.compareLongWith(a,iX,op);
	}
	@Override
	public void compareDoubleWith(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		value.compareDoubleWith(a,iX,op);
	}
	@Override
	public void compareStringWith(String a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		value.compareStringWith(a,iX,op);
	}
	@Override
	public void compareBinaryWith(byte[] a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		value.compareBinaryWith(a,iX,op);
	}
	@Override
	public void compareDateWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		value.compareDateWith(a,iX,op);
	}
	@Override
	public void compareListWith(Term aHead, Term aTail, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		value.compareListWith(aHead,aTail,iX,op);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term reactWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		return value.reactWithTerm(a,iX,op);
	}
	@Override
	public Term reactWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return value.reactWithBigInteger(a,iX,op);
	}
	@Override
	public Term reactWithLong(long a, ChoisePoint iX, BinaryOperation op) {
		return value.reactWithLong(a,iX,op);
	}
	@Override
	public Term reactWithDouble(double a, ChoisePoint iX, BinaryOperation op) {
		return value.reactWithDouble(a,iX,op);
	}
	@Override
	public Term reactWithString(String a, ChoisePoint iX, BinaryOperation op) {
		return value.reactWithString(a,iX,op);
	}
	@Override
	public Term reactWithBinary(byte[] a, ChoisePoint iX, BinaryOperation op) {
		return value.reactWithBinary(a,iX,op);
	}
	@Override
	public Term reactWithDate(long a, ChoisePoint iX, BinaryOperation op) {
		return value.reactWithDate(a,iX,op);
	}
	@Override
	public Term reactWithTime(long a, ChoisePoint iX, BinaryOperation op) {
		return value.reactWithTime(a,iX,op);
	}
	@Override
	public Term reactBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return value.reactBigIntegerWith(a,iX,op);
	}
	@Override
	public Term reactLongWith(long a, ChoisePoint iX, BinaryOperation op) {
		return value.reactLongWith(a,iX,op);
	}
	@Override
	public Term reactDoubleWith(double a, ChoisePoint iX, BinaryOperation op) {
		return value.reactDoubleWith(a,iX,op);
	}
	@Override
	public Term reactStringWith(String a, ChoisePoint iX, BinaryOperation op) {
		return value.reactStringWith(a,iX,op);
	}
	@Override
	public Term reactBinaryWith(byte[] a, ChoisePoint iX, BinaryOperation op) {
		return value.reactBinaryWith(a,iX,op);
	}
	@Override
	public Term reactDateWith(long a, ChoisePoint iX, BinaryOperation op) {
		return value.reactDateWith(a,iX,op);
	}
	@Override
	public Term reactTimeWith(long a, ChoisePoint iX, BinaryOperation op) {
		return value.reactTimeWith(a,iX,op);
	}
	@Override
	public Term reactListWith(Term aHead, Term aTail, ChoisePoint iX, BinaryOperation op) {
		return value.reactListWith(aHead,aTail,iX,op);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term blitWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		return value.blitWithTerm(a,iX,op);
	}
	@Override
	public Term blitWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return value.blitWithBigInteger(a,iX,op);
	}
	@Override
	public Term blitWithLong(long a, ChoisePoint iX, BinaryOperation op) {
		return value.blitWithLong(a,iX,op);
	}
	@Override
	public Term blitWithDouble(double a, ChoisePoint iX, BinaryOperation op) {
		return value.blitWithDouble(a,iX,op);
	}
	@Override
	public Term blitBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return value.blitBigIntegerWith(a,iX,op);
	}
	@Override
	public Term blitLongWith(long a, ChoisePoint iX, BinaryOperation op) {
		return value.blitLongWith(a,iX,op);
	}
	@Override
	public Term blitDoubleWith(double a, ChoisePoint iX, BinaryOperation op) {
		return value.blitDoubleWith(a,iX,op);
	}
	@Override
	public Term blitListWith(Term aHead, Term aTail, ChoisePoint iX, BinaryOperation op) {
		return value.blitListWith(aHead,aTail,iX,op);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term evaluate(ChoisePoint iX, UnaryOperation op) {
		return value.evaluate(iX,op);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, boolean encodeWorlds, CharsetEncoder encoder) {
		return value.toString(cp,isInner,provideStrictSyntax,encodeWorlds,encoder);
	}
	@Override
	public String toString(ChoisePoint cp) {
		return value.toString(cp);
	}
	@Override
	public String toString() {
		return value.toString();
	}
}
