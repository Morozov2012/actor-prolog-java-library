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
import java.util.LinkedHashSet;

public abstract class Term implements Cloneable {
	// equals
	public boolean equals(Object o) {
		// System.out.printf("EQUALS:\n%s\n%s\n",this,o);
		// System.out.printf("Term::equals: %s vs %s = %s\n",this,(Term)o,TermComparator.compareTwoTerms(this,(Term)o));
		return TermComparator.compareTwoTerms(this,(Term)o)==0;
	}
	public int hashCode() {
		return 1;
	}
	// "Is a ..." functions
	public void isInteger(int v, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public void isInteger(BigInteger v, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public void isReal(double v, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public void isSymbol(long v, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	public void isString(String v, ChoisePoint cp) throws Backtracking {
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
		throw new WrongTermIsNotArgumentList(this);
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
	public boolean thisIsWorld() {
		return false;
	}
	public boolean thisIsProcess() {
		return false;
	}
	public boolean thisIsSlotVariable() {
		return false;
	}
	// "Get ... value" functions
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
	public long getSymbolValue(ChoisePoint cp) throws TermIsNotASymbol {
		throw TermIsNotASymbol.instance;
	}
	public String getStringValue(ChoisePoint cp) throws TermIsNotAString {
		throw TermIsNotAString.instance;
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
		throw new WrongTermIsNotArgumentList(this);
	}
	public Term getExistentTail() {
		throw new WrongTermIsNotArgumentList(this);
	}
	public Term getOutputHead(ChoisePoint cp) {
		throw new WrongTermIsNotArgumentList(this);
	}
	public Term getOutputTail(ChoisePoint cp) {
		throw new WrongTermIsNotArgumentList(this);
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
	// public Term checkSetAndGetNamedElement(long aName, ChoisePoint cp) throws Backtracking, TermIsNotASet {
	//	throw TermIsNotASet.instance;
	// }
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
	public long getInternalWorldClass(AbstractWorld currentClass, ChoisePoint cp) throws Backtracking, TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		throw TermIsNotAWorld.instance;
	}
	//
	public AbstractWorld internalWorld(AbstractProcess process, ChoisePoint cp) throws Backtracking {
		throw new WrongTermIsDataItem(this);
	}
	//
	public AbstractWorld internalWorld(ChoisePoint cp) {
		throw new WrongTermIsDataItem(this);
	}
	//
	public Term internalWorldOrTerm(ChoisePoint cp) {
		return this;
	}
	//
	// public AbstractWorld world(ChoisePoint cp) {
	//	throw new WrongTermIsDataItem(this);
	// }
	//
	public long[] getClassHierarchy() {
		// System.out.printf("Term::getClassHierarchy()\n\n");
		// throw new RuntimeException();
		return new long[0];
	}
	public long[] getInterfaceHierarchy() {
		return new long[0];
	}
	// "Unify with ..." functions
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
	// General "Unify With" function
	abstract public void unifyWith(Term t, ChoisePoint cp) throws Backtracking;
	//
	public boolean thisIsFreeVariable() {
		return false;
	}
	// Internal operations
	public boolean thisIsArgumentNumber() {
		return false;
	}
	public long getNumber() {
		throw new WrongTermIsNotArgumentNumber(this);
	}
	public ArgumentNumber add(long deltaN) {
		throw new WrongTermIsNotArgumentNumber(this);
	}
	public ArgumentNumber subtract(long deltaN) {
		throw new WrongTermIsNotArgumentNumber(this);
	}
	public void isArityEqualTo(long v, ChoisePoint cp) throws Backtracking {
		throw new WrongTermIsNotArgumentNumber(this);
	}
	public void isArityMoreOrEqualTo(long v, ChoisePoint cp) throws Backtracking {
		throw new WrongTermIsNotArgumentNumber(this);
	}
	public void verifyListOfRestValues(long deltaN, Term list, ChoisePoint cp) {
		throw new WrongTermIsNotArgumentNumber(this);
	}
	public long getPosition() {
		return -1;
	}
	// Operations on variables
	public void clear() {
		throw new WrongTermIsNotBoundVariable(this);
	}
	public void registerNewSlotVariable(HashSet<SlotVariable> slotVariables) {
	}
	public void setValue(Term newNode) {
		throw new WrongTermIsNotFreeVariable(this);
	}
	// Operations on class instances
	public void extractWorlds(AbstractProcess process, LinkedHashSet<AbstractWorld> list) {
	}
	// Operations on slot variables
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
	// Domain check
	public boolean isCoveredByDomain(PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		return baseDomain.coversTerm(this,cp,ignoreFreeVariables);
	}
	// Domain check
	public boolean isCoveredBySetDomain(long functor, PrologDomain headDomain, PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		return false;
	}
	public boolean isCoveredByEmptySetDomain(PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		return false;
	}
	public Term checkSetTerm(long functor, PrologDomain headDomain, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		throw new DomainAlternativeDoesNotCoverTerm(initialValue.getPosition());
	}
	// Comparison operations
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
	public void compareDateWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public void compareListWith(Term aHead, Term aTail, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	// Arithmetic operations
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
	public Term reactDateWith(long a, ChoisePoint iX, BinaryOperation op) {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public Term reactTimeWith(long a, ChoisePoint iX, BinaryOperation op) {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	public Term reactListWith(Term aHead, Term aTail, ChoisePoint iX, BinaryOperation op) {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	// Bitwise operations
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
	// Unary operations
	public Term evaluate(ChoisePoint iX, UnaryOperation op) {
		throw new OperationIsNotDefinedForTheArgument(this);
	}
	// Converting Term to String
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, CharsetEncoder encoder) {
		if (cp==null) {
			return super.toString();
		} else {
			return toString();
		}
	}
	public String toString(ChoisePoint cp) {
		return toString(cp,false,false,null);
	}
	public String toString() {
		return toString(null,true,false,null);
	}
}
