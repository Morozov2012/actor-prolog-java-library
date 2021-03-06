// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.domains.*;
import morozov.domains.signals.*;
import morozov.run.*;
import morozov.system.*;
import morozov.terms.errors.*;
import morozov.terms.signals.*;
import morozov.worlds.*;

import java.io.Serializable;
import java.nio.charset.CharsetEncoder;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

public abstract class Term implements Cloneable, Serializable {
	//
	private static final long serialVersionUID= 0x3F2E7183284885E7L; // 4552701081448646119L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.terms","Term");
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
		if (o2 instanceof Term) {
			return ((Term)o2).compareWithClass(this.getClass())==0;
		} else {
			return false;
		}
	}
	public int compare(Object o2) {
		if (o2 instanceof Term) {
			return -((Term)o2).compareWithClass(this.getClass());
		} else {
			return 1;
		}
	}
	public boolean isEqualToInteger(BigInteger v2) {
		return false;
	}
	public boolean isEqualToReal(Double v2) {
		return false;
	}
	public boolean isEqualToString(String v2) {
		return false;
	}
	public boolean isEqualToBinary(byte[] v2) {
		return false;
	}
	public boolean isEqualToSymbol(long v2) {
		return false;
	}
	public boolean isEqualToEmptyList() {
		return false;
	}
	public boolean isEqualToEmptySet() {
		return false;
	}
	public boolean isEqualToNoValue() {
		return false;
	}
	public boolean isEqualToUnknownValue() {
		return false;
	}
	public boolean isEqualToStructure(long f2, Term[] a2) {
		return false;
	}
	public boolean isEqualToList(Term h2, Term t2) {
		return false;
	}
	public boolean isEqualToSet(UnderdeterminedSet o2) {
		return false;
	}
	public boolean isEqualToWorld(GlobalWorldIdentifier id2) {
		return false;
	}
	public int compareWithInteger(BigInteger v2) {
		return compareWithClass(PrologInteger.class);
	}
	public int compareWithReal(Double v2) {
		return compareWithClass(PrologReal.class);
	}
	public int compareWithString(String v2) {
		return compareWithClass(PrologString.class);
	}
	public int compareWithBinary(byte[] v2) {
		return compareWithClass(PrologBinary.class);
	}
	public int compareWithSymbol(long v2) {
		return compareWithClass(PrologSymbol.class);
	}
	public int compareWithEmptyList() {
		return compareWithClass(PrologEmptyList.class);
	}
	public int compareWithEmptySet() {
		return compareWithClass(PrologEmptySet.class);
	}
	public int compareWithNoValue() {
		return compareWithClass(PrologNoValue.class);
	}
	public int compareWithUnknownValue() {
		return compareWithClass(PrologUnknownValue.class);
	}
	public int compareWithStructure(long f2, Term[] a2) {
		return compareWithClass(PrologStructure.class);
	}
	public int compareWithList(Term h2, Term t2) {
		return compareWithClass(PrologList.class);
	}
	public int compareWithSet(UnderdeterminedSet o2) {
		return compareWithClass(UnderdeterminedSet.class);
	}
	public int compareWithWorld(GlobalWorldIdentifier id2) {
		return compareWithClass(AbstractWorld.class);
	}
	public int compareWithClass(Class c2) {
		String n1= getClass().getName();
		String n2= c2.getName();
		return n1.compareTo(n2);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void isInteger(PrologInteger v, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public void isInteger(int v, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public void isInteger(BigInteger v, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public void isReal(PrologReal v, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public void isReal(double v, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public void isSymbol(PrologSymbol v, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public void isSymbol(long v, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public void isString(PrologString v, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public void isString(String v, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public void isBinary(PrologBinary v, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public void isBinary(byte[] v, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public Term[] isStructure(long aFunctor, int arity, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public void isEmptyList(ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public boolean thisIsEmptyList() {
		return false;
	}
	public void isOutputEmptyList(ChoisePoint cp) {
		throw new WrongArgumentIsNotArgumentList(this);
	}
	public void isEmptySet(ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public boolean thisIsEmptySet() {
		return false;
	}
	public boolean thisIsUnderdeterminedSet() {
		return false;
	}
	public void isUnknownValue(ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public boolean thisIsUnknownValue() {
		return false;
	}
	public void isNoValue(ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public boolean thisIsNoValue() {
		return false;
	}
	public void isWorld(Term v, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public boolean thisIsOwnWorld() {
		return false;
	}
	public boolean thisIsForeignWorld() {
		return false;
	}
	public boolean thisIsProcess() {
		return false;
	}
	public boolean thisIsActorNumber() {
		return false;
	}
	public boolean thisIsSlotVariable() {
		return false;
	}
	public boolean thisIsFreeVariable() {
		return false;
	}
	public boolean thisIsArgumentNumber() {
		return false;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public long getNumber() {
		throw new WrongArgumentIsNotArgumentNumber(this);
	}
	public ArgumentNumber add(long deltaN) {
		throw new WrongArgumentIsNotArgumentNumber(this);
	}
	public ArgumentNumber subtract(long deltaN) {
		throw new WrongArgumentIsNotArgumentNumber(this);
	}
	public void isArityEqualTo(long v, ChoisePoint cp) throws Backtracking {
		throw new WrongArgumentIsNotArgumentNumber(this);
	}
	public void isArityMoreOrEqualTo(long v, ChoisePoint cp) throws Backtracking {
		throw new WrongArgumentIsNotArgumentNumber(this);
	}
	public void verifyListOfRestValues(long deltaN, Term list, ChoisePoint cp) {
		throw new WrongArgumentIsNotArgumentNumber(this);
	}
	public long getPosition() {
		return -1;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term getValue(ChoisePoint cp) {
		return this;
	}
	public Term dereferenceValue(ChoisePoint cp) {
		return this;
	}
	public Term extractSlotVariable() {
		return this;
	}
	public BigInteger getIntegerValue(ChoisePoint cp) throws TermIsNotAnInteger {
		throw TermIsNotAnInteger.instance;
	}
	public int getSmallIntegerValue(ChoisePoint cp) throws TermIsNotAnInteger {
		throw TermIsNotAnInteger.instance;
	}
	public long getLongIntegerValue(ChoisePoint cp) throws TermIsNotAnInteger {
		throw TermIsNotAnInteger.instance;
	}
	public double getRealValue(ChoisePoint cp) throws TermIsNotAReal {
		throw TermIsNotAReal.instance;
	}
	public String getStringValue(ChoisePoint cp) throws TermIsNotAString {
		throw TermIsNotAString.instance;
	}
	public byte[] getBinaryValue(ChoisePoint cp) throws TermIsNotABinary {
		throw TermIsNotABinary.instance;
	}
	public long getSymbolValue(ChoisePoint cp) throws TermIsNotASymbol {
		throw TermIsNotASymbol.instance;
	}
	public long getStructureFunctor(ChoisePoint cp) throws TermIsNotAStructure {
		throw TermIsNotAStructure.instance;
	}
	public Term[] getStructureArguments(ChoisePoint cp) throws TermIsNotAStructure {
		throw TermIsNotAStructure.instance;
	}
	public Term getListHead(ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public Term getListTail(ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public Term getNextListHead(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		throw TermIsNotAList.instance;
	}
	public Term getNextListTail(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		throw TermIsNotAList.instance;
	}
	public Term getExistentHead() {
		throw new WrongArgumentIsNotArgumentList(this);
	}
	public Term getExistentTail() {
		throw new WrongArgumentIsNotArgumentList(this);
	}
	public Term getOutputHead(ChoisePoint cp) {
		throw new WrongArgumentIsNotArgumentList(this);
	}
	public Term getOutputTail(ChoisePoint cp) {
		throw new WrongArgumentIsNotArgumentList(this);
	}
	public Term getNextListHeadSafely(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		throw TermIsNotAList.instance;
	}
	public Term getNextListTailSafely(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		throw TermIsNotAList.instance;
	}
	public long getNextPairName(ChoisePoint cp) throws EndOfSet, TermIsNotASet {
		throw TermIsNotASet.instance;
	}
	public Term getNextPairValue(ChoisePoint cp) throws EndOfSet, TermIsNotASet, SetElementIsProhibited {
		throw TermIsNotASet.instance;
	}
	public Term getNextSetTail(ChoisePoint cp) throws EndOfSet, TermIsNotASet {
		throw TermIsNotASet.instance;
	}
	public Term getNamedElement(long aName, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public void checkIfTermIsASet(ChoisePoint cp) throws TermIsNotASet {
		throw TermIsNotASet.instance;
	}
	public Term retrieveSetElementValue(ChoisePoint cp) throws Backtracking, TermIsNotSetElement {
		throw TermIsNotSetElement.instance;
	}
	public Term excludeNamedElements(long[] aNames, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public void hasNoMoreElements(long[] aNames, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public void prohibitNamedElement(long aName, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public void verifySet(long[] aNames, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public Term exploreSetPositiveElements(HashMap<Long,Term> positiveMap, ChoisePoint cp) {
		return this;
	}
	public Term exploreSet(HashMap<Long,Term> positiveMap, HashSet<Long> negativeMap, ChoisePoint cp) {
		return this;
	}
	public Term exploreSet(ChoisePoint cp) {
		return this;
	}
	public void appendNamedElement(long aName, Term aValue, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public void appendNamedElementProhibition(long aName, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public AbstractInternalWorld getInternalWorld(ChoisePoint cp) throws Backtracking, TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		throw TermIsNotAWorld.instance;
	}
	//
	public AbstractInternalWorld internalWorld(AbstractProcess process, ChoisePoint cp) throws Backtracking {
		throw new WrongArgumentIsDataItem(this);
	}
	//
	public AbstractInternalWorld internalWorld(ChoisePoint cp) {
		throw new WrongArgumentIsDataItem(this);
	}
	//
	public GlobalWorldIdentifier getGlobalWorldIdentifier(ChoisePoint cp) throws TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		throw TermIsNotAWorld.instance;
	}
	//
	public long[] getClassHierarchy() {
		return new long[0];
	}
	public long[] getInterfaceHierarchy() {
		return new long[0];
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void unifyWithStructure(long aFunctor, Term[] arguments, Term structure, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public void unifyWithList(Term aHead, Term aTail, Term aList, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public void unifyWithSet(long aName, Term aValue, Term aTail, Term aSet, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public void unifyWithProhibitedElement(long aName, Term aTail, Term aSet, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public void unifyWithOptimizedSet(PrologOptimizedSet set, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public void inheritSetElements(PrologOptimizedSet set, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public void unifyWithSetElement(Term aElement, Term setElement, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	abstract public void unifyWith(Term t, ChoisePoint cp) throws Backtracking;
	//
	///////////////////////////////////////////////////////////////
	//
	public void clear() {
		throw new WrongArgumentIsNotBoundVariable(this);
	}
	public void setBacktrackableValue(Term v, ChoisePoint iX) {
		throw new WrongArgumentIsNotFreeVariable(this);
	}
	public void setNonBacktrackableValue(Term v) {
		throw new WrongArgumentIsNotFreeVariable(this);
	}
	public void registerNewSlotVariable(HashSet<SlotVariable> slotVariables) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void extractWorlds(AbstractProcess process, LinkedHashSet<AbstractInternalWorld> list) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void registerVariables(ActiveWorld process, boolean isSuspending, boolean isProtecting) {
	}
	public void registerTargetWorlds(HashSet<AbstractWorld> worlds, ChoisePoint cp) {
	}
	public Term circumscribe() {
		return this;
	}
	public Term copyValue(ChoisePoint cp, TermCircumscribingMode mode) {
		return this;
	}
	public Term copyGroundValue(ChoisePoint cp) throws TermIsUnboundVariable {
		return this;
	}
	public Term substituteWorlds(HashMap<AbstractWorld,Term> map, ChoisePoint cp) {
		return this;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean isCoveredByDomain(PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		return baseDomain.coversTerm(this,cp,ignoreFreeVariables);
	}
	public boolean isCoveredBySetDomain(long functor, PrologDomain headDomain, PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		return false;
	}
	public boolean isCoveredByEmptySetDomain(PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		return false;
	}
	public Term checkSetTerm(long functor, PrologDomain headDomain, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		throw new DomainAlternativeDoesNotCoverTerm(initialValue.getPosition());
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void compareWithTerm(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public void compareWithBigInteger(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public void compareWithLong(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public void compareWithDouble(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public void compareWithString(String a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public void compareWithBinary(byte[] a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public void compareWithDate(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public void compareTermWith(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public void compareBigIntegerWith(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public void compareLongWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public void compareDoubleWith(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public void compareStringWith(String a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public void compareBinaryWith(byte[] a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public void compareDateWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public void compareListWith(Term aHead, Term aTail, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term reactWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public Term reactWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public Term reactWithLong(long a, ChoisePoint iX, BinaryOperation op) {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public Term reactWithDouble(double a, ChoisePoint iX, BinaryOperation op) {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public Term reactWithString(String a, ChoisePoint iX, BinaryOperation op) {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public Term reactWithBinary(byte[] a, ChoisePoint iX, BinaryOperation op) {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public Term reactWithDate(long a, ChoisePoint iX, BinaryOperation op) {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public Term reactWithTime(long a, ChoisePoint iX, BinaryOperation op) {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public Term reactBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public Term reactLongWith(long a, ChoisePoint iX, BinaryOperation op) {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public Term reactDoubleWith(double a, ChoisePoint iX, BinaryOperation op) {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public Term reactStringWith(String a, ChoisePoint iX, BinaryOperation op) {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public Term reactBinaryWith(byte[] a, ChoisePoint iX, BinaryOperation op) {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public Term reactDateWith(long a, ChoisePoint iX, BinaryOperation op) {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public Term reactTimeWith(long a, ChoisePoint iX, BinaryOperation op) {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public Term reactListWith(Term aHead, Term aTail, ChoisePoint iX, BinaryOperation op) {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term blitWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public Term blitWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public Term blitWithLong(long a, ChoisePoint iX, BinaryOperation op) {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public Term blitWithDouble(double a, ChoisePoint iX, BinaryOperation op) {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public Term blitBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public Term blitLongWith(long a, ChoisePoint iX, BinaryOperation op) {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public Term blitDoubleWith(double a, ChoisePoint iX, BinaryOperation op) {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public Term blitListWith(Term aHead, Term aTail, ChoisePoint iX, BinaryOperation op) {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term evaluate(ChoisePoint iX, UnaryOperation op) {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, boolean encodeWorlds, CharsetEncoder encoder) {
		if (cp==null) {
			return super.toString();
		} else {
			return toString();
		}
	}
	public String toString(ChoisePoint cp) {
		return toString(cp,false,false,true,null);
	}
	@Override
	public String toString() {
		return toString(null,true,false,false,null);
	}
}
