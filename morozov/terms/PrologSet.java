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
	protected Term value;
	//
	private static final long serialVersionUID= 0xD287CB9E8A18C5EFL; // -3276426322106595857L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.terms","PrologSet");
	// }
	//
	///////////////////////////////////////////////////////////////
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
	@Override
	public int hashCode() {
		return PrologSymbol.calculateHashCode(name) + value.hashCode() + tail.hashCode();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void inheritSetElements(PrologOptimizedSet set, ChoisePoint cp) throws Backtracking {
		set.setGivenElement(name,value,tail,cp);
	}
	@Override
	public Term getNamedElement(long aName, ChoisePoint cp) throws Backtracking {
		if (name == aName) {
			return value;
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
			return tail.excludeNamedElements(aNames,cp);
		} else {
			return new PrologSet(name,value,tail.excludeNamedElements(aNames,cp));
		}
	}
	@Override
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
	@Override
	public void prohibitNamedElement(long aName, ChoisePoint cp) throws Backtracking {
		if (name == aName) {
			throw Backtracking.instance;
		} else {
			tail.prohibitNamedElement(aName,cp);
		}
	}
	@Override
	public void verifySet(long[] aNames, ChoisePoint cp) throws Backtracking {
		for (int i= 0; i < aNames.length; i++) {
			if (aNames[i] == name) {
				throw Backtracking.instance;
			}
		};
		tail.verifySet(aNames,cp);
	}
	@Override
	public long getNextPairName(ChoisePoint cp) throws EndOfSet, TermIsNotASet {
		return name;
	}
	@Override
	public Term getNextPairValue(ChoisePoint cp) throws EndOfSet, TermIsNotASet, SetElementIsProhibited {
		return value;
	}
	@Override
	public Term getNextSetTail(ChoisePoint cp) throws EndOfSet, TermIsNotASet {
		return tail;
	}
	@Override
	public Term exploreSetPositiveElements(HashMap<Long,Term> positiveMap, ChoisePoint cp) {
		positiveMap.put(name,value); // See DomainOptimizedSet
		return tail.exploreSetPositiveElements(positiveMap,cp);
	}
	@Override
	public Term exploreSet(HashMap<Long,Term> positiveMap, HashSet<Long> negativeMap, ChoisePoint cp) {
		positiveMap.put(name,value.dereferenceValue(cp));
		return tail.exploreSet(positiveMap,negativeMap,cp);
	}
	@Override
	public Term exploreSet(ChoisePoint cp) {
		return tail.exploreSet(cp);
	}
	@Override
	public void appendNamedElement(long aName, Term aValue, ChoisePoint cp) throws Backtracking {
		if (name == aName) {
			value.unifyWith(aValue,cp);
		} else {
			tail.appendNamedElement(aName,aValue,cp);
		}
	}
	@Override
	public void appendNamedElementProhibition(long aName, ChoisePoint cp) throws Backtracking {
		tail.appendNamedElementProhibition(aName,cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void unifyWithSet(long aName, Term aValue, Term aTail, Term aSet, ChoisePoint cp) throws Backtracking {
		if (name == aName) {
			value.unifyWith(aValue,cp);
			tail.unifyWith(aTail,cp);
		} else {
			HashMap<Long,Term> leftSetPositiveMap= new HashMap<>();
			HashSet<Long> leftSetNegativeMap= new HashSet<>();
			HashMap<Long,Term> rightSetPositiveMap= new HashMap<>();
			HashSet<Long> rightSetNegativeMap= new HashSet<>();
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
	@Override
	public void unifyWithProhibitedElement(long aName, Term aTail, Term aSet, ChoisePoint cp) throws Backtracking {
		if (name == aName) {
			throw Backtracking.instance;
		} else {
			HashMap<Long,Term> leftSetPositiveMap= new HashMap<>();
			HashSet<Long> leftSetNegativeMap= new HashSet<>();
			HashMap<Long,Term> rightSetPositiveMap= new HashMap<>();
			HashSet<Long> rightSetNegativeMap= new HashSet<>();
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
	@Override
	public void unifyWithOptimizedSet(PrologOptimizedSet set, ChoisePoint cp) throws Backtracking {
		set.unifyWithSet(name,value,tail,this,cp);
	}
	@Override
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.unifyWithSet(name,value,tail,this,cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void registerVariables(ActiveWorld process, boolean isSuspending, boolean isProtecting) {
		value.registerVariables(process,isSuspending,isProtecting);
		tail.registerVariables(process,isSuspending,isProtecting);
	}
	@Override
	public void registerTargetWorlds(HashSet<AbstractWorld> worlds, ChoisePoint cp) {
		value.registerTargetWorlds(worlds,cp);
		tail.registerTargetWorlds(worlds,cp);
	}
	@Override
	public PrologSet circumscribe() {
		return new PrologSet(
			name,
			value.circumscribe(),
			tail.circumscribe());
	}
	@Override
	public PrologSet copyValue(ChoisePoint cp, TermCircumscribingMode mode) {
		return new PrologSet(
			name,
			value.copyValue(cp,mode),
			tail.copyValue(cp,mode));
	}
	@Override
	public PrologSet copyGroundValue(ChoisePoint cp) throws TermIsUnboundVariable {
		return new PrologSet(
			name,
			value.copyGroundValue(cp),
			tail.copyGroundValue(cp));
	}
	@Override
	public PrologSet substituteWorlds(HashMap<AbstractWorld,Term> map, ChoisePoint cp) {
		return new PrologSet(
			name,
			value.substituteWorlds(map,cp),
			tail.substituteWorlds(map,cp));
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
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
	@Override
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
	@Override
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, boolean encodeWorlds, CharsetEncoder encoder) {
		StringBuilder buffer= new StringBuilder("{");
		try {
			buffer.append(
				name < 0 ?
				SymbolNames.retrieveSymbolName(-name).toSafeString(encoder) :
				String.format("%d",name))
				.append(":")
				.append(value.toString(cp,true,provideStrictSyntax,encodeWorlds,encoder));
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
							nextName < 0 ?
							SymbolNames.retrieveSymbolName(-nextName).toSafeString(encoder) :
							String.format("%d",nextName))
							.append(":")
							.append(elementText);
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
