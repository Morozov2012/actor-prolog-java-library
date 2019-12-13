// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import target.*;

import morozov.domains.*;
import morozov.domains.signals.*;
import morozov.run.*;
import morozov.terms.signals.*;
import morozov.worlds.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.HashSet;

public class ProhibitedSetElement extends UnderdeterminedSetItem {
	//
	private static final long serialVersionUID= 0xF9FD881960F3BAA5L; // -433040346575553883L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.terms","ProhibitedSetElement");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public ProhibitedSetElement(long aName, Term aTail) {
		name= aName;
		tail= aTail;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public int hashCode() {
		return tail.hashCode();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void isEmptySet(ChoisePoint cp) throws Backtracking {
		tail.isEmptySet(cp);
	}
	@Override
	public void inheritSetElements(PrologOptimizedSet set, ChoisePoint cp) throws Backtracking {
		set.prohibitGivenElement(name,tail,cp);
	}
	@Override
	public Term getNamedElement(long aName, ChoisePoint cp) throws Backtracking {
		if (name == aName) {
			throw Backtracking.instance;
		} else {
			return tail.getNamedElement(aName,cp);
		}
	}
	@Override
	public void checkIfTermIsASet(ChoisePoint cp) throws TermIsNotASet {
		tail.checkIfTermIsASet(cp);
	}
	@Override
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
	@Override
	public void hasNoMoreElements(long[] aNames, ChoisePoint cp) throws Backtracking {
		tail.hasNoMoreElements(aNames,cp);
	}
	@Override
	public void prohibitNamedElement(long aName, ChoisePoint cp) throws Backtracking {
		if (name == aName) {
			return;
		} else {
			tail.prohibitNamedElement(aName,cp);
		}
	}
	@Override
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
	@Override
	public long getNextPairName(ChoisePoint cp) throws EndOfSet, TermIsNotASet {
		return name;
	}
	@Override
	public Term getNextPairValue(ChoisePoint cp) throws EndOfSet, TermIsNotASet, SetElementIsProhibited {
		throw SetElementIsProhibited.instance;
	}
	@Override
	public Term getNextSetTail(ChoisePoint cp) throws EndOfSet, TermIsNotASet {
		return tail;
	}
	@Override
	public Term exploreSetPositiveElements(HashMap<Long,Term> positiveMap, ChoisePoint cp) {
		return tail.exploreSetPositiveElements(positiveMap,cp);
	}
	@Override
	public Term exploreSet(HashMap<Long,Term> positiveMap, HashSet<Long> negativeMap, ChoisePoint cp) {
		negativeMap.add(name);
		return tail.exploreSet(positiveMap,negativeMap,cp);
	}
	@Override
	public Term exploreSet(ChoisePoint cp) {
		return tail.exploreSet(cp);
	}
	@Override
	public void appendNamedElement(long aName, Term aValue, ChoisePoint cp) throws Backtracking {
		if (name == aName) {
			throw Backtracking.instance;
		} else {
			tail.appendNamedElement(aName,aValue,cp);
		}
	}
	@Override
	public void appendNamedElementProhibition(long aName, ChoisePoint cp) throws Backtracking {
		if (name != aName) {
			tail.appendNamedElementProhibition(aName,cp);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void unifyWithSet(long aName, Term aValue, Term aTail, Term aSet, ChoisePoint cp) throws Backtracking {
		if (name == aName) {
			throw Backtracking.instance;
		} else {
			HashMap<Long,Term> leftSetPositiveMap= new HashMap<>();
			HashSet<Long> leftSetNegativeMap= new HashSet<>();
			HashMap<Long,Term> rightSetPositiveMap= new HashMap<>();
			HashSet<Long> rightSetNegativeMap= new HashSet<>();
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
	@Override
	public void unifyWithProhibitedElement(long aName, Term aTail, Term aSet, ChoisePoint cp) throws Backtracking {
		if (name == aName) {
			tail.unifyWith(aTail,cp);
		} else {
			HashMap<Long,Term> leftSetPositiveMap= new HashMap<>();
			HashSet<Long> leftSetNegativeMap= new HashSet<>();
			HashMap<Long,Term> rightSetPositiveMap= new HashMap<>();
			HashSet<Long> rightSetNegativeMap= new HashSet<>();
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
	@Override
	public void unifyWithOptimizedSet(PrologOptimizedSet set, ChoisePoint cp) throws Backtracking {
		set.unifyWithProhibitedElement(name,tail,this,cp);
	}
	@Override
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.unifyWithProhibitedElement(name,tail,this,cp);
	}
	@Override
	public void registerVariables(ActiveWorld process, boolean isSuspending, boolean isProtecting) {
		tail.registerVariables(process,isSuspending,isProtecting);
	}
	@Override
	public void registerTargetWorlds(HashSet<AbstractWorld> worlds, ChoisePoint cp) {
		tail.registerTargetWorlds(worlds,cp);
	}
	@Override
	public ProhibitedSetElement circumscribe() {
		return new ProhibitedSetElement(
			name,
			tail.circumscribe());
	}
	@Override
	public ProhibitedSetElement copyValue(ChoisePoint cp, TermCircumscribingMode mode) {
		return new ProhibitedSetElement(
			name,
			tail.copyValue(cp,mode));
	}
	@Override
	public ProhibitedSetElement copyGroundValue(ChoisePoint cp) throws TermIsUnboundVariable {
		return new ProhibitedSetElement(
			name,
			tail.copyGroundValue(cp));
	}
	@Override
	public ProhibitedSetElement substituteWorlds(HashMap<AbstractWorld,Term> map, ChoisePoint cp) {
		return new ProhibitedSetElement(
			name,
			tail.substituteWorlds(map,cp));
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean isCoveredBySetDomain(long functor, PrologDomain headDomain, PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		return baseDomain.coversTerm(tail,cp,ignoreFreeVariables);
	}
	@Override
	public boolean isCoveredByEmptySetDomain(PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		return tail.isCoveredByEmptySetDomain(baseDomain,cp,ignoreFreeVariables);
	}
	@Override
	public Term checkSetTerm(long functor, PrologDomain headDomain, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		if (functor == name) {
			return baseDomain.checkTerm(tail,cp);
		} else {
			throw new DomainAlternativeDoesNotCoverTerm(initialValue.getPosition());
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, boolean encodeWorlds, CharsetEncoder encoder) {
		if (provideStrictSyntax) {
			return tail.toString(cp,isInner,provideStrictSyntax,encodeWorlds,encoder);
		} else {
			StringBuilder buffer= new StringBuilder("{");
			try {
				buffer.append(
					name < 0 ?
					SymbolNames.retrieveSymbolName(-name).toSafeString(encoder) :
					String.format("%d",name))
					.append(":")
					.append(PrologNoValue.namePrologNoValue);
				long nextName;
				Term nextValue;
				Term currentTail= tail;
				try {
					while (true) {
						nextName= currentTail.getNextPairName(cp);
						buffer.append(",");
						buffer.append(
							nextName < 0 ?
							SymbolNames.retrieveSymbolName(-nextName).toSafeString(encoder) :
							String.format("%d",nextName))
							.append(":");
						try {
							nextValue= currentTail.getNextPairValue(cp);
							buffer.append(nextValue.toString(cp,true,provideStrictSyntax,encodeWorlds,encoder));
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
					buffer.append(currentTail.toString(cp,true,provideStrictSyntax,encodeWorlds,encoder));
					buffer.append("}");
					return buffer.toString();
				}
			} catch (Throwable e) {
				buffer= new StringBuilder();
				buffer.append(
					String.format("{%s:%s|%s}",
						(name < 0 ?
							SymbolNames.retrieveSymbolName(-name).toSafeString(encoder) :
							String.format("%d",name) ),
						PrologNoValue.namePrologNoValue,
						tail.toString(cp,true,provideStrictSyntax,encodeWorlds,encoder)));
				return buffer.toString();
			}
		}
	}
}
