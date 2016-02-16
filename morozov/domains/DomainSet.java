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
import java.util.HashSet;

public class DomainSet extends MonoArgumentDomainItem {
	protected long key;
	//
	public DomainSet(long k, String entry) {
		super(entry);
		key= k;
	}
	//
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		// initiateDomainItemIfNecessary();
		// t= t.dereferenceValue(cp);
		t= t.dereferenceValue(cp);
		if (ignoreFreeVariables && t.thisIsFreeVariable()) {
			return true;
		} else {
			return t.isCoveredBySetDomain(key,domainItem,baseDomain,cp,ignoreFreeVariables);
		}
	}
	public boolean coversOptimizedSet(long[] keysOfTerms, Term[] elements, Term tail, PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		// initiateDomainItemIfNecessary();
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
					DomainAlternative[] alternatives= baseDomain.alternatives;
					for (int i=0; i < alternatives.length; i++) {
						DomainAlternative alternative= alternatives[i];
						try {
							PrologDomain currentDomain= alternative.getPairDomain(currentKey);
							if (!value.isCoveredByDomain(currentDomain,cp,ignoreFreeVariables)) {
								return false;
							} else {
								elementIsFound= true;
								break;
							}
						} catch (TermIsNotPairDomainAlternative e) {
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
		throw new DomainAlternativeDoesNotCoverTerm(t.getPosition());
	}
	public Term checkTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		// initiateDomainItemIfNecessary();
		return t.checkSetTerm(key,domainItem,t,cp,baseDomain);
	}
	public Term checkOptimizedSet(long[] keysOfTerms, Term[] elements, Term tail, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		// initiateDomainItemIfNecessary();
		return checkAndCollectSetElements(initialValue,cp,baseDomain);
	}
	public PrologDomain getPairDomain(long keyOfPair) throws TermIsNotPairDomainAlternative {
		if (keyOfPair==key) {
			// initiateDomainItemIfNecessary();
			return domainItem;
		} else {
			throw TermIsNotPairDomainAlternative.instance;
		}
	}
	//
	public boolean isEqualTo(DomainAlternative a, HashSet<PrologDomainPair> stack) {
		// initiateDomainItemIfNecessary();
		return a.isEqualToSet(key,domainItem,stack);
	}
	public boolean isEqualToSet(long targetKey, PrologDomain domain, HashSet<PrologDomainPair> stack) {
		// initiateDomainItemIfNecessary();
		if (targetKey==key) {
			try {
				domainItem.isEqualTo(domain,stack);
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
	public boolean isCoveredByOptimizedSet(long[] listOfNamesAndCodes, PrologDomain[] domains, HashSet<PrologDomainPair> stack) {
		// initiateDomainItemIfNecessary();
		try {
			boolean isFound= false;
			for (int k=0; k < listOfNamesAndCodes.length; k++) {
				if (key==listOfNamesAndCodes[k]) {
					domainItem.isCoveredByDomain(domains[k],stack);
					isFound= true;
					break;
				}
			};
			return isFound;
		} catch (PrologDomainsAreNotEqual e) {
			return false;
		}
	}
	public boolean coversAlternative(DomainAlternative a, PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) {
		return a.isCoveredBySet(ownerDomain);
	}
	// Converting Term to String
	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
		if (key < 0) {
			stream.writeObject(SymbolNames.retrieveSymbolName(-key));
		}
	}
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		if (key < 0) {
			SymbolName symbolName= (SymbolName)stream.readObject();
			key= - SymbolNames.insertSymbolName(symbolName.identifier);
		}
	}
	//
	protected String getMonoArgumentDomainTag() {
		return PrologDomainName.tagDomainAlternative_Set;
	}
	//
	public String toString(CharsetEncoder encoder) {
		// initiateDomainItemIfNecessary();
		StringBuffer buffer= new StringBuffer();
		buffer.append(getMonoArgumentDomainTag());
		buffer.append("(");
		buffer.append(Long.toString(key));
		buffer.append(",\"");
		buffer.append(FormatOutput.encodeString(domainTableEntry,false,encoder));
		buffer.append("\")");
		return buffer.toString();
	}
}
