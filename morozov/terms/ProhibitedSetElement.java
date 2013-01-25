// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import target.*;

import morozov.classes.*;
import morozov.domains.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.HashSet;

public class ProhibitedSetElement extends UnderdeterminedSetItem {
	public ProhibitedSetElement(long aName, Term aTail) {
		name= aName;
		tail= aTail;
	}
	public int hashCode() {
		return (int)name + tail.hashCode();
	}
	public void isEmptySet(ChoisePoint cp) throws Backtracking {
		tail.isEmptySet(cp);
	}
	public void inheritSetElements(PrologOptimizedSet set, ChoisePoint cp) throws Backtracking {
		set.prohibitGivenElement(name,tail,cp);
	}
	public Term getNamedElement(long aName, ChoisePoint cp) throws Backtracking {
		if (name == aName) {
			throw new Backtracking();
		} else {
			return tail.getNamedElement(aName,cp);
		}
	}
	// public Term checkSetAndGetNamedElement(long aName, ChoisePoint cp) throws Backtracking, TermIsNotASet {
	//	if (name == aName) {
	//		throw new Backtracking();
	//	} else {
	//		return tail.checkSetAndGetNamedElement(aName,cp);
	//	}
	// }
	public void checkIfTermIsASet(ChoisePoint cp) throws TermIsNotASet {
		tail.checkIfTermIsASet(cp);
	}
	public Term excludeNamedElements(long[] aNames, ChoisePoint cp) throws Backtracking {
		int elementPosition= -1;
		for (int i= 0; i < aNames.length; i++) {
			if (aNames[i] == name) {
				elementPosition= i;
				break;
			}
		};
		if (elementPosition >= 0) {
			aNames[elementPosition]= 0;
		};
		return new ProhibitedSetElement(name,tail.excludeNamedElements(aNames,cp));
	}
	public void hasNoMoreElements(long[] aNames, ChoisePoint cp) throws Backtracking {
		tail.hasNoMoreElements(aNames,cp);
	}
	public void prohibitNamedElement(long aName, ChoisePoint cp) throws Backtracking {
		if (name == aName) {
			return;
		} else {
			tail.prohibitNamedElement(aName,cp);
		}
	}
	public void verifySet(long[] aNames, ChoisePoint cp) throws Backtracking {
		int elementPosition= -1;
		for (int i= 0; i < aNames.length; i++) {
			if (aNames[i] == name) {
				elementPosition= i;
				break;
			}
		};
		if (elementPosition >= 0) {
			aNames[elementPosition]= 0;
		};
		tail.verifySet(aNames,cp);
	}
	public long getNextPairName(ChoisePoint cp) throws EndOfSet, TermIsNotASet {
		return name;
	}
	public Term getNextPairValue(ChoisePoint cp) throws EndOfSet, TermIsNotASet, SetElementIsProhibited {
		throw new SetElementIsProhibited();
	}
	public Term getNextSetTail(ChoisePoint cp) throws EndOfSet, TermIsNotASet {
		return tail;
	}
	public Term exploreSetPositiveElements(HashMap<Long,Term> positiveMap, ChoisePoint cp) {
		return tail.exploreSetPositiveElements(positiveMap,cp);
	}
	public Term exploreSet(HashMap<Long,Term> positiveMap, HashSet<Long> negativeMap, ChoisePoint cp) {
		negativeMap.add(name);
		return tail.exploreSet(positiveMap,negativeMap,cp);
	}
	public Term exploreSet(ChoisePoint cp) {
		return tail.exploreSet(cp);
	}
	public void appendNamedElement(long aName, Term aValue, ChoisePoint cp) throws Backtracking {
		if (name == aName) {
			throw new Backtracking();
		} else {
			tail.appendNamedElement(aName,aValue,cp);
		}
	}
	public void appendNamedElementProhibition(long aName, ChoisePoint cp) throws Backtracking {
		if (name != aName) {
			tail.appendNamedElementProhibition(aName,cp);
		}
	}
	// "Unify with ..." functions
	public void unifyWithSet(long aName, Term aValue, Term aTail, Term aSet, ChoisePoint cp) throws Backtracking {
		if (name == aName) {
			throw new Backtracking();
		} else {
			HashMap<Long,Term> leftSetPositiveMap= new HashMap<Long,Term>();
			HashSet<Long> leftSetNegativeMap= new HashSet<Long>();
			HashMap<Long,Term> rightSetPositiveMap= new HashMap<Long,Term>();
			HashSet<Long> rightSetNegativeMap= new HashSet<Long>();
			leftSetNegativeMap.add(name);
			rightSetPositiveMap.put(aName,aValue.dereferenceValue(cp));
			unify_with_set(
				aTail,cp,
				leftSetPositiveMap,
				leftSetNegativeMap,
				rightSetPositiveMap,
				rightSetNegativeMap);
		}
	}
	public void unifyWithProhibitedElement(long aName, Term aTail, Term aSet, ChoisePoint cp) throws Backtracking {
		if (name == aName) {
			tail.unifyWith(aTail,cp);
		} else {
			HashMap<Long,Term> leftSetPositiveMap= new HashMap<Long,Term>();
			HashSet<Long> leftSetNegativeMap= new HashSet<Long>();
			HashMap<Long,Term> rightSetPositiveMap= new HashMap<Long,Term>();
			HashSet<Long> rightSetNegativeMap= new HashSet<Long>();
			leftSetNegativeMap.add(name);
			rightSetNegativeMap.add(aName);
			unify_with_set(
				aTail,cp,
				leftSetPositiveMap,
				leftSetNegativeMap,
				rightSetPositiveMap,
				rightSetNegativeMap);
		}
	}
	public void unifyWithOptimizedSet(PrologOptimizedSet set, ChoisePoint cp) throws Backtracking {
		set.unifyWithProhibitedElement(name,tail,this,cp);
	}
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.unifyWithProhibitedElement(name,tail,this,cp);
	}
	public void registerVariables(ActiveWorld process, boolean isSuspending, boolean isProtecting) {
		tail.registerVariables(process,isSuspending,isProtecting);
	}
	public void registerTargetWorlds(HashSet<AbstractWorld> worlds, ChoisePoint cp) {
		tail.registerTargetWorlds(worlds,cp);
	}
	public ProhibitedSetElement circumscribe() {
		return new ProhibitedSetElement(
			name,
			tail.circumscribe());
	}
	public ProhibitedSetElement copyValue(ChoisePoint cp, TermCircumscribingMode mode) {
		return new ProhibitedSetElement(
			name,
			tail.copyValue(cp,mode));
	}
	public ProhibitedSetElement substituteWorlds(HashMap<AbstractWorld,Term> map, ChoisePoint cp) {
		return new ProhibitedSetElement(
			name,
			tail.substituteWorlds(map,cp));
	}
	// Domain check
	public boolean isCoveredBySetDomain(long functor, PrologDomain headDomain, PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		return baseDomain.coversTerm(tail,cp,ignoreFreeVariables);
	}
	public boolean isCoveredByEmptySetDomain(PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		return tail.isCoveredByEmptySetDomain(baseDomain,cp,ignoreFreeVariables);
	}
	public Term checkSetTerm(long functor, PrologDomain headDomain, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		if (functor == name) {
			return baseDomain.checkTerm(tail,cp);
		} else {
			throw new DomainAlternativeDoesNotCoverTerm(initialValue.getPosition());
		}
	}
	// Converting Term to String
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, CharsetEncoder encoder) {
		if (provideStrictSyntax) {
			return tail.toString(cp,isInner,provideStrictSyntax,encoder);
		} else {
			StringBuilder buffer= new StringBuilder("{");
			try {
				buffer.append(
					(name < 0 ?
						SymbolNames.retrieveSymbolName(-name).toString(encoder) :
						String.format("%d",name) ) +
					":" + PrologNoValue.namePrologNoValue);
				long nextName;
				Term nextValue;
				Term currentTail= tail;
				try {
					while (true) {
						nextName= currentTail.getNextPairName(cp);
						buffer.append(",");
						buffer.append(
							(nextName < 0 ?
								SymbolNames.retrieveSymbolName(-nextName).toString(encoder) :
								String.format("%d",nextName) ) +
							":");
						try {
							nextValue= currentTail.getNextPairValue(cp);
							buffer.append(nextValue.toString(cp,true,provideStrictSyntax,encoder));
						} catch (SetElementIsProhibited e) {
							buffer.append(PrologNoValue.namePrologNoValue);
						};
						currentTail= currentTail.getNextSetTail(cp);
					}
				} catch (EndOfSet e) {
					buffer.append("}");
					return buffer.toString();
				} catch (TermIsNotASet e) {
					buffer.append("|");
					buffer.append(currentTail.toString(cp,true,provideStrictSyntax,encoder));
					buffer.append("}");
					return buffer.toString();
				}
			} catch (Throwable e) {
				buffer= new StringBuilder();
				buffer.append(
					String.format("{%s:%s|%s}",
						(name < 0 ?
							SymbolNames.retrieveSymbolName(-name).toString(encoder) :
							String.format("%d",name) ),
						PrologNoValue.namePrologNoValue,
						tail.toString(cp,true,provideStrictSyntax,encoder)));
				return buffer.toString();
			}
		}
	}
}
