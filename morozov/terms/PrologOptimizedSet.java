// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.terms;

import target.*;

import morozov.domains.*;
import morozov.domains.signals.*;
import morozov.run.*;
import morozov.run.errors.*;
import morozov.terms.errors.*;
import morozov.terms.signals.*;
import morozov.worlds.*;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class PrologOptimizedSet extends UnderdeterminedSetWithTail {
	//
	private Term[] elements;
	private long[] keys;
	//
	private static final long serialVersionUID= 0xEC1039F9FD1A1464L; // -1436584535275203484L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.terms","PrologOptimizedSet");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public PrologOptimizedSet(long[] aKeys) {
		elements= new Term[aKeys.length];
		keys= aKeys;
		for (int i= 0; i < aKeys.length; i++) {
			elements[i]= new PrologVariable();
		};
		tail= new PrologVariable();
	}
	public PrologOptimizedSet(PrologOptimizedSet set, ChoisePoint cp) throws Backtracking {
		keys= set.keys;
		elements= new Term[keys.length];
		for (int i= 0; i < keys.length; i++) {
			elements[i]= new PrologVariable();
		};
		tail= new PrologVariable();
		// В создаваемом (первом) множестве некоторые
		// переменные, те которые соответствуют
		// определённым элементам второго множества,
		// будут связаны с no_value.
		// Второе множество технологически достраивается.
		inheritSetElements(set,cp);
	}
	public PrologOptimizedSet(Term[] aElements, long[] aKeys) {
		elements= new Term[aElements.length];
		keys= aKeys;
		for (int i= 0; i < aElements.length; i++) {
			Term e= aElements[i];
			if (e==null) {
				elements[i]= PrologNoValue.instance;
			} else {
				elements[i]= new PrologSetElement(e);
			}
		};
		tail= PrologEmptySet.instance;
	}
	public PrologOptimizedSet(Term[] aElements, boolean closeSet, long[] aKeys) {
		elements= new Term[aElements.length];
		keys= aKeys;
		for (int i= 0; i < aElements.length; i++) {
			Term e= aElements[i];
			if (e==null) {
				if (closeSet) {
					elements[i]= PrologNoValue.instance;
				} else {
					elements[i]= new PrologVariable();
				}
			} else {
				elements[i]= new PrologSetElement(e);
			}
		};
		if (closeSet) {
			tail= PrologEmptySet.instance;
		} else {
			tail= new PrologVariable();
		}
	}
	public PrologOptimizedSet(Term[] aElements, Term tail, long[] aKeys, ChoisePoint cp) throws Backtracking {
		elements= new Term[aElements.length];
		keys= aKeys;
		for (int i= 0; i < aElements.length; i++) {
			Term e= aElements[i];
			if (e==null) {
				continue;
			} else {
				elements[i]= new PrologSetElement(e);
			}
		};
		// Создаётся заготовка множества.
		// Потом она технологически достраивается.
		tail.inheritSetElements(this,cp);
	}
	public PrologOptimizedSet(Term[] aElements, Term tail, long[] aKeys) {
		elements= new Term[aElements.length];
		keys= aKeys;
		for (int i= 0; i < aElements.length; i++) {
			Term e= aElements[i];
			if (e==null) {
				continue;
			} else {
				elements[i]= new PrologSetElement(e);
			}
		};
		try {
			// Создаётся заготовка множества.
			// Потом она технологически достраивается.
			tail.inheritSetElements(this,null);
		} catch (Backtracking b) {
			throw new SlotInitiationFailed();
		}
	}
	public PrologOptimizedSet(Term[] aElements, Term tail, long[] aKeys, ChoisePoint cp, boolean reportErrors) {
		elements= new Term[aElements.length];
		keys= aKeys;
		for (int i= 0; i < aElements.length; i++) {
			Term e= aElements[i];
			if (e==null) {
				continue;
			} else {
				elements[i]= new PrologSetElement(e);
			}
		};
		try {
			// Создаётся заготовка множества.
			// Потом она технологически достраивается.
			tail.inheritSetElements(this,cp);
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
		int sum= 0;
		for (int i= 0; i < elements.length; i++) {
			sum+= elements[i].hashCode();
		};
		return sum + tail.hashCode();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected Object clone() {
		try {
			PrologOptimizedSet o= (PrologOptimizedSet)super.clone();
			// Это необходимо, чтобы копию можно было
			// использовать в качестве заготовки
			// для изменённых версий терма.
			o.elements= (Term[])o.elements.clone();
			return o;
		} catch (CloneNotSupportedException e) {
			throw new CannotCloneOptimizedSet();
		}
	}
	public void isEmptySet(ChoisePoint cp) throws Backtracking {
		for (int i= 0; i < elements.length; i++) {
			Term e1= elements[i];
			e1.isNoValue(cp);
		};
		tail.isEmptySet(cp);
	}
	public void inheritSetElements(PrologOptimizedSet set, ChoisePoint cp) throws Backtracking {
		for (int i= 0; i < elements.length; i++) {
			Term e1= elements[i];
			Term e2= set.elements[i];
			if (e2==null) {
				set.elements[i]= e1;
			} else {
				e1.isNoValue(cp);
			}
		};
		set.tail= tail;
	}
	public void setNoValueElements() {
		Term noValue= PrologNoValue.instance;
		for (int i= 0; i < elements.length; i++) {
			Term e2= elements[i];
			if (e2==null) {
				elements[i]= noValue;
			}
		};
		tail= PrologEmptySet.instance;
	}
	public void setGivenElement(long name, Term e1, Term tail, ChoisePoint cp) throws Backtracking {
		for (int i= 0; i < elements.length; i++) {
			if (keys[i]==name) {
				Term e2= elements[i];
				if (e2==null) {
					elements[i]= e1;
				} else {
					// e1.isNoValue(cp);
					throw Backtracking.instance;
				};
				tail.inheritSetElements(this,cp);
				return;
			}
		};
		throw Backtracking.instance;
	}
	public void prohibitGivenElement(long name, Term tail, ChoisePoint cp) throws Backtracking {
		for (int i= 0; i < elements.length; i++) {
			if (keys[i]==name) {
				Term e2= elements[i];
				if (e2==null) {
					elements[i]= PrologNoValue.instance;
				};
				break;
			}
		};
		tail.inheritSetElements(this,cp);
	}
	public Term getNamedElement(long aName, ChoisePoint cp) throws Backtracking {
		for (int i= 0; i < elements.length; i++) {
			if (keys[i]==aName) {
				Term element= elements[i];
				try {
					return element.retrieveSetElementValue(cp);
				} catch (TermIsNotSetElement e) {
					throw new WrongArgumentIsNotSetElement(element);
				}
			}
		};
		return tail.getNamedElement(aName,cp);
	}
	// public Term checkSetAndGetNamedElement(long aName, ChoisePoint cp) throws Backtracking, TermIsNotASet {
	//	for (int i= 0; i < elements.length; i++) {
	//		if (keys[i]==aName) {
	//			Term element= elements[i];
	//			try {
	//				return element.retrieveSetElementValue(cp);
	//			} catch (TermIsNotSetElement e) {
	//				throw new WrongArgumentIsNotSetElement(element);
	//			}
	//		}
	//	};
	//	return tail.checkSetAndGetNamedElement(aName,cp);
	// }
	public void checkIfTermIsASet(ChoisePoint cp) throws TermIsNotASet {
		tail.checkIfTermIsASet(cp);
	}
	public Term excludeNamedElements(long[] aNames, ChoisePoint cp) throws Backtracking {
		PrologOptimizedSet clone= (PrologOptimizedSet)this.clone();
		Term noValue= PrologNoValue.instance;
		HashSet<Long> extraElementsToBeExcluded= null;
		for (int i= 0; i < aNames.length; i++) {
			boolean elementIsFound= false;
			for (int j= 0; j < elements.length; j++) {
				if (aNames[i]==keys[j]) {
					clone.elements[j]= noValue;
					elementIsFound= true;
					break;
				};
			};
			if (!elementIsFound) {
				if (extraElementsToBeExcluded==null) {
					extraElementsToBeExcluded= new HashSet<Long>();
				};
				extraElementsToBeExcluded.add(aNames[i]);
			}
		};
		if (extraElementsToBeExcluded!=null) {
			long[] extraElementNames= new long[extraElementsToBeExcluded.size()];
			Iterator<Long> iterator= extraElementsToBeExcluded.iterator();
			int i= 0;
			while (iterator.hasNext()) {
				extraElementNames[i]= iterator.next();
				i= i + 1;
			};
			clone.tail= tail.excludeNamedElements(extraElementNames,cp);
		};
		return clone;
	}
	public void hasNoMoreElements(long[] aNames, ChoisePoint cp) throws Backtracking {
		for (int i= 0; i < elements.length; i++) {
			boolean isUnexpectedElement= true;
			for (int j= 0; j < aNames.length; j++) {
				if (keys[i]==aNames[j]) {
					isUnexpectedElement= false;
					break;
				}
			};
			if (isUnexpectedElement) {
				elements[i].isNoValue(cp);
			}
		};
		tail.hasNoMoreElements(aNames,cp);
	}
	public void verifySet(long[] aNames, ChoisePoint cp) throws Backtracking {
		for (int i= 0; i < aNames.length; i++) {
			for (int j= 0; j < elements.length; j++) {
				if (aNames[i]==keys[j]) {
					elements[j].isNoValue(cp);
				}
			}
		};
		tail.verifySet(aNames,cp);
	}
	public Term exploreSetPositiveElements(HashMap<Long,Term> positiveMap, ChoisePoint cp) {
		exploreOptimizedPositiveElements(positiveMap,cp);
		return tail.exploreSetPositiveElements(positiveMap,cp);
	}
	public Term exploreSet(HashMap<Long,Term> positiveMap, HashSet<Long> negativeMap, ChoisePoint cp) {
		exploreOptimizedElements(positiveMap,negativeMap,cp);
		return tail.exploreSet(positiveMap,negativeMap,cp);
	}
	public void exploreOptimizedElements(HashMap<Long,Term> positiveMap, HashSet<Long> negativeMap, ChoisePoint cp) {
		for (int i= 0; i < elements.length; i++) {
			Term element= elements[i];
			try {
				Term value= element.retrieveSetElementValue(cp);
				positiveMap.put(new Long(keys[i]),value.dereferenceValue(cp));
			} catch (Backtracking b) {
				negativeMap.add(new Long(keys[i]));
			} catch (TermIsNotSetElement e) {
				throw new WrongArgumentIsNotOptimizedSetElement(element);
			}
		}
	}
	public void exploreOptimizedPositiveElements(HashMap<Long,Term> positiveMap, ChoisePoint cp) {
		for (int i= 0; i < elements.length; i++) {
			Term element= elements[i];
			try {
				Term value= element.retrieveSetElementValue(cp);
				positiveMap.put(new Long(keys[i]),value); // See DomainOptimizedSet
			} catch (Backtracking b) {
			} catch (TermIsNotSetElement e) {
				throw new WrongArgumentIsNotOptimizedSetElement(element);
			}
		}
	}
	public void unifyWithOptimizedSet(PrologOptimizedSet set, ChoisePoint cp) throws Backtracking {
		if (keys==set.keys) {
			for (int i= 0; i < elements.length; i++) {
				Term e1= elements[i];
				Term e2= set.elements[i];
				e1.unifyWith(e2,cp);
			};
			tail.unifyWith(set.tail,cp);
		} else {
			HashMap<Long,Term> leftSetPositiveMap= new HashMap<Long,Term>();
			HashSet<Long> leftSetNegativeMap= new HashSet<Long>();
			HashMap<Long,Term> rightSetPositiveMap= new HashMap<Long,Term>();
			HashSet<Long> rightSetNegativeMap= new HashSet<Long>();
			// leftSetPositiveMap.put(name,value.dereferenceValue(cp));
			exploreOptimizedElements(leftSetPositiveMap,leftSetNegativeMap,cp);
			// rightSetPositiveMap.put(aName,aValue.dereferenceValue(cp));
			set.exploreOptimizedElements(rightSetPositiveMap,rightSetNegativeMap,cp);
			unify_with_set(
				set.tail,cp,
				leftSetPositiveMap,
				leftSetNegativeMap,
				rightSetPositiveMap,
				rightSetNegativeMap);
		}
	}
	public void unifyWithSet(long aName, Term aValue, Term aTail, Term aSet, ChoisePoint cp) throws Backtracking {
		HashMap<Long,Term> leftSetPositiveMap= new HashMap<Long,Term>();
		HashSet<Long> leftSetNegativeMap= new HashSet<Long>();
		HashMap<Long,Term> rightSetPositiveMap= new HashMap<Long,Term>();
		HashSet<Long> rightSetNegativeMap= new HashSet<Long>();
		// leftSetPositiveMap.put(name,value.dereferenceValue(cp));
		exploreOptimizedElements(leftSetPositiveMap,leftSetNegativeMap,cp);
		rightSetPositiveMap.put(aName,aValue.dereferenceValue(cp));
		unify_with_set(
			aTail,cp,
			leftSetPositiveMap,
			leftSetNegativeMap,
			rightSetPositiveMap,
			rightSetNegativeMap);
	}
	public void unifyWithProhibitedElement(long aName, Term aTail, Term aSet, ChoisePoint cp) throws Backtracking {
		HashMap<Long,Term> leftSetPositiveMap= new HashMap<Long,Term>();
		HashSet<Long> leftSetNegativeMap= new HashSet<Long>();
		HashMap<Long,Term> rightSetPositiveMap= new HashMap<Long,Term>();
		HashSet<Long> rightSetNegativeMap= new HashSet<Long>();
		// leftSetPositiveMap.put(name,value.dereferenceValue(cp));
		exploreOptimizedElements(leftSetPositiveMap,leftSetNegativeMap,cp);
		rightSetNegativeMap.add(aName);
		unify_with_set(
			aTail,cp,
			leftSetPositiveMap,
			leftSetNegativeMap,
			rightSetPositiveMap,
			rightSetNegativeMap);
	}
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.unifyWithOptimizedSet(this,cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void registerVariables(ActiveWorld process, boolean isSuspending, boolean isProtecting) {
		for (int i= 0; i < elements.length; i++) {
			elements[i].registerVariables(process,isSuspending,isProtecting);
		};
		tail.registerVariables(process,isSuspending,isProtecting);
	}
	public void registerTargetWorlds(HashSet<AbstractWorld> worlds, ChoisePoint cp) {
		for (int i= 0; i < elements.length; i++) {
			elements[i].registerTargetWorlds(worlds,cp);
		};
		tail.registerTargetWorlds(worlds,cp);
	}
	public PrologOptimizedSet circumscribe() {
		PrologOptimizedSet clone= (PrologOptimizedSet)this.clone();
		for (int i= 0; i < elements.length; i++) {
			clone.elements[i]= elements[i].circumscribe();
		};
		clone.tail= tail.circumscribe();
		return clone;
	}
	public PrologOptimizedSet copyValue(ChoisePoint cp, TermCircumscribingMode mode) {
		PrologOptimizedSet clone= (PrologOptimizedSet)this.clone();
		for (int i= 0; i < elements.length; i++) {
			clone.elements[i]= elements[i].copyValue(cp,mode);
		};
		return clone;
	}
	public PrologOptimizedSet copyGroundValue(ChoisePoint cp) throws TermIsUnboundVariable {
		PrologOptimizedSet clone= (PrologOptimizedSet)this.clone();
		for (int i= 0; i < elements.length; i++) {
			clone.elements[i]= elements[i].copyGroundValue(cp);
		};
		return clone;
	}
	public PrologOptimizedSet substituteWorlds(HashMap<AbstractWorld,Term> map, ChoisePoint cp) {
		PrologOptimizedSet clone= (PrologOptimizedSet)this.clone();
		for (int i= 0; i < elements.length; i++) {
			clone.elements[i]= elements[i].substituteWorlds(map,cp);
		};
		clone.tail= tail.substituteWorlds(map,cp);
		return clone;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean isCoveredByDomain(PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		for (int i= 0; i < elements.length; i++) {
			Term element= elements[i].dereferenceValue(cp);
			if (element.thisIsFreeVariable()) {
				if (ignoreFreeVariables) {
					continue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(element);
				}
			} else {
				try {
					Term value= element.retrieveSetElementValue(cp);
					try {
						PrologDomain elementDomain= baseDomain.getPairDomain(keys[i]);
						if (!value.isCoveredByDomain(elementDomain,cp,ignoreFreeVariables)) {
							return false;
						}
					} catch (UnknownPairName e) {
						return false;
					}
				} catch (Backtracking b) {
				} catch (TermIsNotSetElement b) {
					return false;
				}
			}
		};
		return tail.isCoveredByDomain(baseDomain,cp,ignoreFreeVariables);
	}
	public boolean isCoveredBySetDomain(long functor, PrologDomain headDomain, PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		return baseDomain.coversOptimizedSet(keys,elements,tail,baseDomain,cp,ignoreFreeVariables);
	}
	public boolean isCoveredByEmptySetDomain(PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		for (int i= 0; i < elements.length; i++) {
			Term element= elements[i].dereferenceValue(cp);
			if (element.thisIsFreeVariable()) {
				if (ignoreFreeVariables) {
					continue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(element);
				}
			} else {
				try {
					Term value= element.retrieveSetElementValue(cp);
					return false;
				} catch (Backtracking b) {
				} catch (TermIsNotSetElement b) {
					return false;
				}
			}
		};
		return tail.isCoveredByEmptySetDomain(baseDomain,cp,ignoreFreeVariables);
	}
	public Term checkSetTerm(long functor, PrologDomain headDomain, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		return baseDomain.checkOptimizedSet(keys,elements,tail,initialValue,cp,baseDomain);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
		for (int i= 0; i < keys.length; i++) {
			long currentKey= keys[i];
			if (currentKey < 0) {
				stream.writeObject(SymbolNames.retrieveSymbolName(-currentKey));
			}
		}
	}
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		for (int i= 0; i < keys.length; i++) {
			long currentKey= keys[i];
			if (currentKey < 0) {
				SymbolName symbolName= (SymbolName)stream.readObject();
				keys[i]= - SymbolNames.insertSymbolName(symbolName.identifier);
			}
		}
	}
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, boolean encodeWorlds, CharsetEncoder encoder) {
		StringBuilder buffer= new StringBuilder("{");
		boolean writeComma= false;
		for (int i= 0; i < elements.length; i++) {
			Term value= elements[i];
			value= value.dereferenceValue(cp);
			if (!provideStrictSyntax || !value.thisIsNoValue()) {
				if (writeComma) {
					buffer.append(",");
				} else {
					writeComma= true;
				};
				long name= keys[i];
				buffer.append(
					(name < 0 ?
						SymbolNames.retrieveSymbolName(-name).toSafeString(encoder) :
						String.format("%d",name) ) +
					":");
				if (value != null) {
					buffer.append(value.toString(cp,true,provideStrictSyntax,encodeWorlds,encoder));
				} else {
					buffer.append(PrologNoValue.namePrologNoValue);
				}
			}
		};
		Term extraTail= tail.dereferenceValue(cp);
		if (extraTail.thisIsFreeVariable()) {
			buffer.append("|_}");
		} else if (extraTail.thisIsEmptySet()) {
			buffer.append("}");
		} else {
			buffer.append("|");
			buffer.append(extraTail.toString(cp,true,provideStrictSyntax,encodeWorlds,encoder));
			buffer.append("}");
		};
		return buffer.toString();
	}
}
