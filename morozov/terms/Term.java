// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.classes.*;
import morozov.domains.*;
import java.nio.charset.CharsetEncoder;import java.math.BigInteger;import java.util.HashMap;
import java.util.HashSet;import java.util.LinkedHashSet;public abstract class Term implements Cloneable {
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
		throw new Backtracking();
	}
	public void isInteger(BigInteger v, ChoisePoint cp) throws Backtracking {
		throw new Backtracking();
	}
	public void isReal(double v, ChoisePoint cp) throws Backtracking {
		throw new Backtracking();
	}
	public void isSymbol(long v, ChoisePoint cp) throws Backtracking {
		throw new Backtracking();
	}
	public void isString(String v, ChoisePoint cp) throws Backtracking {
		throw new Backtracking();
	}
	public Term[] isStructure(long aFunctor, int arity, ChoisePoint cp) throws Backtracking {
		throw new Backtracking();
	}
	public void isEmptyList(ChoisePoint cp) throws Backtracking {		throw new Backtracking();	}	public boolean thisIsEmptyList() {		return false;	}	public void isOutputEmptyList(ChoisePoint cp) {		throw new WrongTermIsNotArgumentList(this);	}	public void isEmptySet(ChoisePoint cp) throws Backtracking {		throw new Backtracking();	}	public boolean thisIsEmptySet() {		return false;	}	public boolean thisIsUnderdeterminedSet() {		return false;	}	public void isUnknownValue(ChoisePoint cp) throws Backtracking {		throw new Backtracking();	}	public boolean thisIsUnknownValue() {		return false;	}	public void isNoValue(ChoisePoint cp) throws Backtracking {		throw new Backtracking();	}	public boolean thisIsNoValue() {		return false;	}	public void isWorld(Term v, ChoisePoint cp) throws Backtracking {		throw new Backtracking();	}	public boolean thisIsWorld() {		return false;	}	public boolean thisIsProcess() {		return false;	}	public boolean thisIsSlotVariable() {		return false;	}	// "Get ... value" functions	public Term getValue(ChoisePoint cp) {		return this;	}	public Term dereferenceValue(ChoisePoint cp) {
		return this;
	}
	public Term extractSlotVariable() {
		return this;
	}
	public BigInteger getIntegerValue(ChoisePoint cp) throws TermIsNotAnInteger {
		throw new TermIsNotAnInteger();
	}
	public int getSmallIntegerValue(ChoisePoint cp) throws TermIsNotAnInteger {
		throw new TermIsNotAnInteger();
	}
	public long getLongIntegerValue(ChoisePoint cp) throws TermIsNotAnInteger {
		throw new TermIsNotAnInteger();
	}
	public double getRealValue(ChoisePoint cp) throws TermIsNotAReal {
		throw new TermIsNotAReal();
	}
	public long getSymbolValue(ChoisePoint cp) throws TermIsNotASymbol {
		throw new TermIsNotASymbol();
	}
	public String getStringValue(ChoisePoint cp) throws TermIsNotAString {
		throw new TermIsNotAString();
	}
	public long getStructureFunctor(ChoisePoint cp) throws TermIsNotAStructure {
		throw new TermIsNotAStructure();
	}
	public Term[] getStructureArguments(ChoisePoint cp) throws TermIsNotAStructure {
		throw new TermIsNotAStructure();
	}
	public Term getListHead(ChoisePoint cp) throws Backtracking {
		throw new Backtracking();
	}
	public Term getListTail(ChoisePoint cp) throws Backtracking {
		throw new Backtracking();
	}
	public Term getNextListHead(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		throw new TermIsNotAList();
	}
	public Term getNextListTail(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		throw new TermIsNotAList();
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
		throw new TermIsNotAList();
	}
	public Term getNextListTailSafely(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		throw new TermIsNotAList();
	}
	public long getNextPairName(ChoisePoint cp) throws EndOfSet, TermIsNotASet {
		throw new TermIsNotASet();
	}
	public Term getNextPairValue(ChoisePoint cp) throws EndOfSet, TermIsNotASet, SetElementIsProhibited {
		throw new TermIsNotASet();
	}
	public Term getNextSetTail(ChoisePoint cp) throws EndOfSet, TermIsNotASet {
		throw new TermIsNotASet();
	}
	public Term getNamedElement(long aName, ChoisePoint cp) throws Backtracking {
		throw new Backtracking();
	}
	// public Term checkSetAndGetNamedElement(long aName, ChoisePoint cp) throws Backtracking, TermIsNotASet {
	//	throw new TermIsNotASet();
	// }
	public void checkIfTermIsASet(ChoisePoint cp) throws TermIsNotASet {
		throw new TermIsNotASet();
	}
	public Term retrieveSetElementValue(ChoisePoint cp) throws Backtracking, TermIsNotSetElement {
		throw new TermIsNotSetElement();
	}
	public Term excludeNamedElements(long[] aNames, ChoisePoint cp) throws Backtracking {
		throw new Backtracking();
	}
	public void hasNoMoreElements(long[] aNames, ChoisePoint cp) throws Backtracking {
		throw new Backtracking();
	}
	public void prohibitNamedElement(long aName, ChoisePoint cp) throws Backtracking {
		throw new Backtracking();
	}
	public void verifySet(long[] aNames, ChoisePoint cp) throws Backtracking {
		throw new Backtracking();
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
		throw new Backtracking();
	}
	public void appendNamedElementProhibition(long aName, ChoisePoint cp) throws Backtracking {
		throw new Backtracking();
	}
	public long getInternalWorldClass(AbstractWorld currentClass, ChoisePoint cp) throws Backtracking, TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		throw new TermIsNotAWorld();
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
	public AbstractWorld world(ChoisePoint cp) {
		throw new WrongTermIsDataItem(this);
	}
	public long[] getClassHierarchy() {
		// System.out.printf("Term::getClassHierarchy()\n\n");
		// throw new RuntimeException();
		return new long[0];
	}
	public long[] getInterfaceHierarchy() {		return new long[0];
	}
	// "Unify with ..." functions
	public void unifyWithStructure(long aFunctor, Term[] arguments, Term structure, ChoisePoint cp) throws Backtracking {
		throw new Backtracking();
	}
	public void unifyWithList(Term aHead, Term aTail, Term aList, ChoisePoint cp) throws Backtracking {
		throw new Backtracking();
	}
	public void unifyWithSet(long aName, Term aValue, Term aTail, Term aSet, ChoisePoint cp) throws Backtracking {
		throw new Backtracking();
	}
	public void unifyWithProhibitedElement(long aName, Term aTail, Term aSet, ChoisePoint cp) throws Backtracking {
		throw new Backtracking();
	}
	public void unifyWithOptimizedSet(PrologOptimizedSet set, ChoisePoint cp) throws Backtracking {
		throw new Backtracking();
	}
	public void inheritSetElements(PrologOptimizedSet set, ChoisePoint cp) throws Backtracking {
		throw new Backtracking();
	}
	public void unifyWithSetElement(Term aElement, Term setElement, ChoisePoint cp) throws Backtracking {
		throw new Backtracking();
	}
	// General "Unify With" function
	abstract public void unifyWith(Term t, ChoisePoint cp) throws Backtracking;	//	public boolean thisIsFreeVariable() {
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
	public void clear() {		throw new WrongTermIsNotBoundVariable(this);	}	public void registerNewSlotVariable(HashSet<SlotVariable> slotVariables) {	}	public void setValue(Term newNode) {		throw new WrongTermIsNotFreeVariable(this);	}	// Operations on class instances	public void extractWorlds(AbstractProcess process, LinkedHashSet<AbstractWorld> list) {	}	// Operations on slot variables	public void registerVariables(ActiveWorld process, boolean isSuspending, boolean isProtecting) {	}	public void registerTargetWorlds(HashSet<AbstractWorld> worlds, ChoisePoint cp) {	}	public Term circumscribe() {		return this;	}	public Term copyValue(ChoisePoint cp, TermCircumscribingMode mode) {		return this;	}	public Term substituteWorlds(HashMap<AbstractWorld,Term> map, ChoisePoint cp) {		return this;	}	// Domain check	public boolean isCoveredByDomain(PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {		return baseDomain.coversTerm(this,cp,ignoreFreeVariables);	}	// Domain check	public boolean isCoveredBySetDomain(long functor, PrologDomain headDomain, PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {		return false;	}	public boolean isCoveredByEmptySetDomain(PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {		return false;	}	public Term checkSetTerm(long functor, PrologDomain headDomain, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {		throw new DomainAlternativeDoesNotCoverTerm(initialValue.getPosition());	}	// Converting Term to String	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, CharsetEncoder encoder) {		if (cp==null) {			return super.toString();		} else {			return toString();		}	}	public String toString(ChoisePoint cp) {		return toString(cp,false,false,null);	}	public String toString() {		return toString(null,true,false,null);	}}