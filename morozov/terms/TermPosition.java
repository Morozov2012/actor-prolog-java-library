// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.classes.*;
import morozov.domains.*;

import java.nio.charset.CharsetEncoder;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class TermPosition extends Term {
	protected Term value;
	protected long position;
	public TermPosition(Term t, long p) {
		value= t;
		position= p;
	}
	public int hashCode() {
		return value.hashCode();
	}
	// "Is a ..." functions
	public void isInteger(int v, ChoisePoint cp) throws Backtracking {
		value.isInteger(v,cp);
	}
	public void isInteger(BigInteger v, ChoisePoint cp) throws Backtracking {
		value.isInteger(v,cp);
	}
	public void isReal(double v, ChoisePoint cp) throws Backtracking {
		value.isReal(v,cp);
	}
	public void isSymbol(long v, ChoisePoint cp) throws Backtracking {
		value.isSymbol(v,cp);
	}
	public void isString(String v, ChoisePoint cp) throws Backtracking {
		value.isString(v,cp);
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
	public void isNoValue(ChoisePoint cp) throws Backtracking {
		value.isNoValue(cp);
	}
	// public boolean thisIsNoValue() {
	//	return value.thisIsNoValue();
	// }
	public void isWorld(Term v, ChoisePoint cp) throws Backtracking {
		value.isWorld(v,cp);
	}
	// public boolean thisIsWorld() {
	//	return value.thisIsWorld();
	// }
	// public boolean thisIsProcess() {
	//	return value.thisIsProcess();
	// }
	// public boolean thisIsSlotVariable() {
	//	return value.thisIsSlotVariable();
	// }
	// "Get ... value" functions
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
	public long getSymbolValue(ChoisePoint cp) throws TermIsNotASymbol {
		return value.getSymbolValue(cp);
	}
	public String getStringValue(ChoisePoint cp) throws TermIsNotAString {
		return value.getStringValue(cp);
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
	public long getInternalWorldClass(AbstractWorld currentClass, ChoisePoint cp) throws Backtracking, TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		return value.getInternalWorldClass(currentClass,cp);
	}
	//
	public AbstractWorld internalWorld(AbstractProcess process, ChoisePoint cp) throws Backtracking {
		return value.internalWorld(process,cp);
	}
	//
	public AbstractWorld internalWorld(ChoisePoint cp) {
		return value.internalWorld(cp);
	}
	//
	public AbstractWorld world(ChoisePoint cp) {
		return value.world(cp);
	}
	public long[] getClassHierarchy() {
		return value.getClassHierarchy();
	}
	public long[] getInterfaceHierarchy() {
		return value.getInterfaceHierarchy();
	}
	// "Unify with ..." functions
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
	// General "Unify With" function
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
	// Operations on variables
	public void clear() {
		value.clear();
	}
	public void registerNewSlotVariable(HashSet<SlotVariable> slotVariables) {
		value.registerNewSlotVariable(slotVariables);
	}
	public void setValue(Term newNode) {
		value.setValue(newNode);
	}
	// Operations on class instances
	public void extractWorlds(AbstractProcess process, LinkedHashSet<AbstractWorld> list) {
		value.extractWorlds(process,list);
	}
	// Operations on slot variables
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
	public Term substituteWorlds(HashMap<AbstractWorld,Term> map, ChoisePoint cp) {
		return new TermPosition(value.substituteWorlds(map,cp),position);
	}
	// Domain check
	public boolean isCoveredByDomain(PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		return value.isCoveredByDomain(baseDomain,cp,ignoreFreeVariables);
	}
	public Term checkSetTerm(long functor, PrologDomain headDomain, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		return value.checkSetTerm(functor,headDomain,initialValue,cp,baseDomain);
	}
	// Converting Term to String
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, CharsetEncoder encoder) {
		return value.toString(cp,isInner,provideStrictSyntax,encoder);
		// return	"TermPosition[" +
		//		FormatOutput.integerToString(position) + "](" +
		//		value.toString(cp,isInner,provideStrictSyntax,encoder) + ")";
	}
	public String toString(ChoisePoint cp) {
		return value.toString(cp);
	}
	public String toString() {
		return value.toString();
	}
}
