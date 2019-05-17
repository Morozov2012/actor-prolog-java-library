// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import target.*;

import morozov.domains.signals.*;
import morozov.run.*;
import morozov.system.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DomainOptimizedSet extends MultiArgumentDomainItem {
	//
	protected long[] keys;
	//
	private static final long serialVersionUID= 0x5E55398845853959L; // 6797402470030326105L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.domains","DomainOptimizedSet");
	// }
	//
	public DomainOptimizedSet(long[] keyList, String[] entries) {
		super(entries);
		keys= keyList;
	}
	//
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		// initiateDomainItemsIfNecessary();
		// t= t.dereferenceValue(cp);
		t= t.dereferenceValue(cp);
		if (ignoreFreeVariables && t.thisIsFreeVariable()) {
			return true;
		} else {
			HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
			Term setEnd= t.exploreSetPositiveElements(setPositiveMap,cp);
			setEnd= setEnd.dereferenceValue(cp);
			if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
				Set<Long> setOfKeys= setPositiveMap.keySet();
				Long[] keyArray= new Long[setOfKeys.size()];
				setOfKeys.toArray(keyArray);
				for (int n=0; n < keyArray.length; n++) {
					long currentKey= keyArray[n];
					// Term value= setPositiveMap.get(currentKey).retrieveSetElementValue(cp);
					Term value= setPositiveMap.get(currentKey);
					// value= value.dereferenceValue(cp);
					value= value.dereferenceValue(cp);
					if (ignoreFreeVariables && value.thisIsFreeVariable()) {
						// return true;
						continue;
					} else {
						boolean elementIsFound= false;
						for (int k=0; k < keys.length; k++) {
							if (keys[k] == currentKey) {
								if (!value.isCoveredByDomain(domainItems[k],cp,ignoreFreeVariables)) {
									return false;
								};
								elementIsFound= true;
								break;
							}
						};
						if (!elementIsFound) {
							return false;
						}
					}
				};
				return true;
			} else {
				if (ignoreFreeVariables && setEnd.thisIsFreeVariable()) {
					return true;
				} else {
					return false;
				}
			}
		}
	}
	public boolean coversOptimizedSet(long[] keysOfTerms, Term[] elements, Term tail, PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		// initiateDomainItemsIfNecessary();
		for (int n=0; n < keysOfTerms.length; n++) {
			long currentKey= keysOfTerms[n];
			try {
				Term value= elements[n].retrieveSetElementValue(cp);
				// value= value.dereferenceValue(cp);
				value= value.dereferenceValue(cp);
				if (ignoreFreeVariables && value.thisIsFreeVariable()) {
					// return true;
					continue;
				} else {
					boolean elementIsFound= false;
					for (int k=0; k < keys.length; k++) {
						if (keys[k] == currentKey) {
							if (!value.isCoveredByDomain(domainItems[k],cp,ignoreFreeVariables)) {
								return false;
							};
							elementIsFound= true;
							break;
						}
					};
					if (!elementIsFound) {
						return false;
					}
				}
			} catch (Backtracking b) {
			} catch (TermIsNotSetElement b) {
				return false;
			}
		};
		return tail.isCoveredByDomain(baseDomain,cp,ignoreFreeVariables);
	}
	public Term checkAndOptimizeTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		// initiateDomainItemsIfNecessary();
		Term[] arguments= checkCollectAndOptimizeSetElements(t,cp,baseDomain,true);
		return new PrologOptimizedSet(arguments,keys);
	}
	public Term checkTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		// initiateDomainItemsIfNecessary();
		Term[] arguments= checkCollectAndOptimizeSetElements(t,cp,baseDomain,false);
		Term set= PrologEmptySet.instance;
		for (int n=arguments.length-1; n >= 0; n--) {
			Term argument= arguments[n];
			if (argument != null) {
				set= new PrologSet(keys[n],arguments[n],set);
			} else {
				set= new ProhibitedSetElement(keys[n],set);
			}
		};
		return set;
	}
	public Term checkOptimizedSet(long[] keysOfTerms, Term[] elements, Term tail, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		// initiateDomainItemsIfNecessary();
		Term[] arguments= checkCollectAndOptimizeSetElements(initialValue,cp,baseDomain,true);
		return new PrologOptimizedSet(arguments,keys);
	}
	protected Term[] checkCollectAndOptimizeSetElements(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean optimizeTerms) throws DomainAlternativeDoesNotCoverTerm {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
		Term setEnd= t.exploreSetPositiveElements(setPositiveMap,cp);
		setEnd= setEnd.dereferenceValue(cp);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			Term[] arguments= new Term[keys.length];
			for (int n=0; n < keys.length; n++) {
				long currentKey= keys[n];
				if (setPositiveMap.containsKey(currentKey)) {
					Term currentElement= setPositiveMap.get(currentKey);
					if (optimizeTerms) {
						arguments[n]= domainItems[n].checkAndOptimizeTerm(currentElement,cp);
					} else {
						arguments[n]= domainItems[n].checkTerm(currentElement,cp);
					};
					setPositiveMap.remove(currentKey);
				} else {
					arguments[n]= null; // PrologNoValue.instance;
				}
			};
			if (setPositiveMap.isEmpty()) {
				return arguments;
			} else {
				throw new DomainAlternativeDoesNotCoverTerm(t.getPosition());
			}
		} else {
			long position1= t.getPosition();
			long position2= setEnd.getPosition();
			if (position1 < position2) {
				position1= position2;
			};
			throw new DomainAlternativeDoesNotCoverTerm(position1);
		}
	}
	public PrologDomain getPairDomain(long key) throws TermIsNotPairDomainAlternative {
		// initiateDomainItemsIfNecessary();
		int elementNumber= -1;
		for (int n=0; n < keys.length; n++) {
			if (key==keys[n]) {
				elementNumber= n;
				break;
			}
		};
		if (elementNumber >= 0) {
			return domainItems[elementNumber];
		} else {
			throw TermIsNotPairDomainAlternative.instance;
		}
	}
	//
	public boolean isEqualTo(DomainAlternative a, HashSet<PrologDomainPair> stack) {
		// initiateDomainItemsIfNecessary();
		return a.isEqualToOptimizedSet(keys,domainItems,stack);
	}
	public boolean isEqualToOptimizedSet(long[] listOfNamesAndCodes, PrologDomain[] domains, HashSet<PrologDomainPair> stack) {
		// initiateDomainItemsIfNecessary();
		if (listOfNamesAndCodes.length==keys.length && domains.length==domainItems.length) {
			try {
				for (int n=0; n < domains.length; n++) {
					if (listOfNamesAndCodes[n]==keys[n]) {
						domains[n].isEqualTo(domainItems[n],stack);
					} else {
						return false;
					}
				};
				return true;
			} catch (PrologDomainsAreNotEqual e) {
				return false;
			}
		} else {
			return false;
		}
	}
	public boolean isCoveredBySetAny() {
		return true;
	}
	public boolean coversAlternative(DomainAlternative a, PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) {
		// initiateDomainItemsIfNecessary();
		return a.isCoveredByOptimizedSet(keys,domainItems,stack);
	}
	public boolean isCoveredByOptimizedSet(long[] listOfNamesAndCodes, PrologDomain[] domains, HashSet<PrologDomainPair> stack) {
		// initiateDomainItemsIfNecessary();
		if (listOfNamesAndCodes.length >= keys.length && domains.length >= domainItems.length) {
			try {
				for (int n=0; n < keys.length; n++) {
					long currentKey= keys[n];
					boolean isFound= false;
					for (int k=0; k < listOfNamesAndCodes.length; k++) {
						if (currentKey==listOfNamesAndCodes[k]) {
							domainItems[n].isCoveredByDomain(domains[k],stack);
							isFound= true;
							break;
						}
					};
					if (!isFound) {
						return false;
					}
				};
				return true;
			} catch (PrologDomainsAreNotEqual e) {
				return false;
			}
		} else {
			return false;
		}
	}
	public boolean isCoveredBySet(PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) {
		// initiateDomainItemsIfNecessary();
		for (int n=0; n < keys.length; n++) {
			long currentKey= keys[n];
			PrologDomain currentDomain= domainItems[n];
			boolean isFound= false;
			for (int k= 0; k < ownerDomain.alternatives.length; k++) {
				ownerDomain.alternatives[k].isEqualToSet(currentKey,currentDomain,stack);
				isFound= true;
				break;
			};
			if (!isFound) {
				return false;
			}
		};
		return true;
	}
	// Converting Term to String
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
	//
	protected String getMultiArgumentDomainTag() {
		return PrologDomainName.tagDomainAlternative_OptimizedSet;
	}
	//
	public String toString(CharsetEncoder encoder) {
		// initiateDomainItemsIfNecessary();
		StringBuffer buffer= new StringBuffer();
		buffer.append(getMultiArgumentDomainTag());
		buffer.append("([");
		if (keys.length > 0) {
			buffer.append(Long.toString(keys[0]));
			for (int n=1; n < keys.length; n++) {
				buffer.append(",");
				buffer.append(Long.toString(keys[n]));
			}
		};
		buffer.append("],[");
		if (domainTableEntries.length > 0) {
			buffer.append("\"");
			buffer.append(FormatOutput.encodeString(domainTableEntries[0],false,encoder));
			for (int n=1; n < domainTableEntries.length; n++) {
				buffer.append("\",\"");
				buffer.append(FormatOutput.encodeString(domainTableEntries[n],false,encoder));
			};
			buffer.append("\"");
		};
		buffer.append("])");
		return buffer.toString();
	}
}
