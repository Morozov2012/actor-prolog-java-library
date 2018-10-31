// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import target.*;

import morozov.domains.*;
import morozov.domains.signals.*;
import morozov.run.*;
import morozov.run.errors.*;
import morozov.terms.signals.*;
import morozov.worlds.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.HashSet;

public class PrologSet extends UnderdeterminedSetItem {
	//
	private Term value;
	//
	public PrologSet(long aName, Term aValue, Term aTail) {
		name= aName;
		value= aValue;
		tail= aTail;
	}
	public PrologSet(long aName, Term aValue, Term aTail, ChoisePoint cp) throws Backtracking {
		name= aName;
		value= aValue;
		tail= aTail;
		tail.prohibitNamedElement(aName,cp);
	}
	public PrologSet(long aName, Term aValue, Term aTail, ChoisePoint cp, boolean reportErrors) {
		name= aName;
		value= aValue;
		tail= aTail;
		try {
			tail.prohibitNamedElement(aName,cp);
		} catch (Backtracking b) {
			if (reportErrors) {
				throw new ImperativeProcedureFailed();
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int hashCode() {
		return PrologSymbol.calculateHashCode(name) + value.hashCode() + tail.hashCode();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void inheritSetElements(PrologOptimizedSet set, ChoisePoint cp) throws Backtracking {
		set.setGivenElement(name,value,tail,cp);
	}
	public Term getNamedElement(long aName, ChoisePoint cp) throws Backtracking {
		if (name == aName) {
			return value;
		} else {
			return tail.getNamedElement(aName,cp);
		}
	}
	// public Term checkSetAndGetNamedElement(long aName, ChoisePoint cp) throws Backtracking, TermIsNotASet {
	//	if (name == aName) {
	//		return value;
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
			return tail.excludeNamedElements(aNames,cp);
		} else {
			return new PrologSet(name,value,tail.excludeNamedElements(aNames,cp));
		}
	}
	public void hasNoMoreElements(long[] aNames, ChoisePoint cp) throws Backtracking {
		boolean isUnexpectedElement= true;
		for (int i= 0; i < aNames.length; i++) {
			if (aNames[i] == name) {
				isUnexpectedElement= false;
				break;
			}
		};
		if (isUnexpectedElement) {
			throw Backtracking.instance;
		} else {
			tail.hasNoMoreElements(aNames,cp);
		}
	}
	public void prohibitNamedElement(long aName, ChoisePoint cp) throws Backtracking {
		if (name == aName) {
			throw Backtracking.instance;
		} else {
			tail.prohibitNamedElement(aName,cp);
		}
	}
	public void verifySet(long[] aNames, ChoisePoint cp) throws Backtracking {
		for (int i= 0; i < aNames.length; i++) {
			if (aNames[i] == name) {
				throw Backtracking.instance;
			}
		};
		tail.verifySet(aNames,cp);
	}
	public long getNextPairName(ChoisePoint cp) throws EndOfSet, TermIsNotASet {
		return name;
	}
	public Term getNextPairValue(ChoisePoint cp) throws EndOfSet, TermIsNotASet, SetElementIsProhibited {
		return value;
	}
	public Term getNextSetTail(ChoisePoint cp) throws EndOfSet, TermIsNotASet {
		return tail;
	}
	public Term exploreSetPositiveElements(HashMap<Long,Term> positiveMap, ChoisePoint cp) {
		// positiveMap.put(name,value.dereferenceValue(cp));
		positiveMap.put(name,value); // See DomainOptimizedSet
		return tail.exploreSetPositiveElements(positiveMap,cp);
	}
	public Term exploreSet(HashMap<Long,Term> positiveMap, HashSet<Long> negativeMap, ChoisePoint cp) {
		positiveMap.put(name,value.dereferenceValue(cp));
		return tail.exploreSet(positiveMap,negativeMap,cp);
	}
	public Term exploreSet(ChoisePoint cp) {
		return tail.exploreSet(cp);
	}
	public void appendNamedElement(long aName, Term aValue, ChoisePoint cp) throws Backtracking {
		if (name == aName) {
			value.unifyWith(aValue,cp);
		} else {
			tail.appendNamedElement(aName,aValue,cp);
		}
	}
	public void appendNamedElementProhibition(long aName, ChoisePoint cp) throws Backtracking {
		tail.appendNamedElementProhibition(aName,cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void unifyWithSet(long aName, Term aValue, Term aTail, Term aSet, ChoisePoint cp) throws Backtracking {
		if (name == aName) {
			value.unifyWith(aValue,cp);
			tail.unifyWith(aTail,cp);
		} else {
			HashMap<Long,Term> leftSetPositiveMap= new HashMap<Long,Term>();
			HashSet<Long> leftSetNegativeMap= new HashSet<Long>();
			HashMap<Long,Term> rightSetPositiveMap= new HashMap<Long,Term>();
			HashSet<Long> rightSetNegativeMap= new HashSet<Long>();
			leftSetPositiveMap.put(name,value.dereferenceValue(cp));
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
			throw Backtracking.instance;
		} else {
			HashMap<Long,Term> leftSetPositiveMap= new HashMap<Long,Term>();
			HashSet<Long> leftSetNegativeMap= new HashSet<Long>();
			HashMap<Long,Term> rightSetPositiveMap= new HashMap<Long,Term>();
			HashSet<Long> rightSetNegativeMap= new HashSet<Long>();
			leftSetPositiveMap.put(name,value.dereferenceValue(cp));
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
		set.unifyWithSet(name,value,tail,this,cp);
	}
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.unifyWithSet(name,value,tail,this,cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void registerVariables(ActiveWorld process, boolean isSuspending, boolean isProtecting) {
		value.registerVariables(process,isSuspending,isProtecting);
		tail.registerVariables(process,isSuspending,isProtecting);
	}
	public void registerTargetWorlds(HashSet<AbstractWorld> worlds, ChoisePoint cp) {
		value.registerTargetWorlds(worlds,cp);
		tail.registerTargetWorlds(worlds,cp);
	}
	public PrologSet circumscribe() {
		return new PrologSet(
			name,
			value.circumscribe(),
			tail.circumscribe());
	}
	public PrologSet copyValue(ChoisePoint cp, TermCircumscribingMode mode) {
		return new PrologSet(
			name,
			value.copyValue(cp,mode),
			tail.copyValue(cp,mode));
	}
	public PrologSet copyGroundValue(ChoisePoint cp) throws TermIsUnboundVariable {
		return new PrologSet(
			name,
			value.copyGroundValue(cp),
			tail.copyGroundValue(cp));
	}
	public PrologSet substituteWorlds(HashMap<AbstractWorld,Term> map, ChoisePoint cp) {
		return new PrologSet(
			name,
			value.substituteWorlds(map,cp),
			tail.substituteWorlds(map,cp));
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean isCoveredBySetDomain(long functor, PrologDomain headDomain, PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		if (functor == name) {
			if (headDomain.coversTerm(value,cp,ignoreFreeVariables)) {
				return baseDomain.coversTerm(tail,cp,ignoreFreeVariables);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	public PrologSet checkSetTerm(long functor, PrologDomain headDomain, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		if (functor == name) {
			Term headValue= headDomain.checkTerm(value,cp);
			Term tailValue= baseDomain.checkTerm(tail,cp);
			return new PrologSet(functor,headValue,tailValue);
		} else {
			throw new DomainAlternativeDoesNotCoverTerm(initialValue.getPosition());
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, boolean encodeWorlds, CharsetEncoder encoder) {
		StringBuilder buffer= new StringBuilder("{");
		try {
			buffer.append(
				(name < 0 ?
					SymbolNames.retrieveSymbolName(-name).toSafeString(encoder) :
					String.format("%d",name) ) +
				":" +
				value.toString(cp,true,provideStrictSyntax,encodeWorlds,encoder));
			long nextName;
			Term nextValue;
			Term currentTail= tail;
			try {
				while (true) {
					nextName= currentTail.getNextPairName(cp);
					String elementText= null;
					boolean appendElement= true;
					try {
						nextValue= currentTail.getNextPairValue(cp);
						elementText= nextValue.toString(cp,true,provideStrictSyntax,encodeWorlds,encoder);
					} catch (SetElementIsProhibited e) {
						if (provideStrictSyntax) {
							appendElement= false;
						} else {
							elementText= PrologNoValue.namePrologNoValue;
						}
					};
					if (appendElement) {
						buffer.append(",");
						buffer.append(
							(nextName < 0 ?
								SymbolNames.retrieveSymbolName(-nextName).toSafeString(encoder) :
								String.format("%d",nextName) ) +
							":" +
							elementText);
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
					value.toString(cp,true,provideStrictSyntax,encodeWorlds,encoder),
					tail.toString(cp,true,provideStrictSyntax,encodeWorlds,encoder)));
			return buffer.toString();
		}
	}
}
