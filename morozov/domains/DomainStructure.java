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

public class DomainStructure extends MultiArgumentDomainItem {
	//
	protected long functor;
	//
	private static final long serialVersionUID= 0xCAAF0336FD2A307CL; // -3841848422413225860L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.domains","DomainStructure");
	// }
	//
	public DomainStructure(long name, String[] entries) {
		super(entries);
		functor= name;
	}
	//
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		// initiateDomainItemsIfNecessary();
		// t= t.dereferenceValue(cp);
		t= t.dereferenceValue(cp);
		if (ignoreFreeVariables && t.thisIsFreeVariable()) {
			return true;
		} else {
			try {
				long name= t.getStructureFunctor(cp);
				if (name != functor) {
					return false;
				} else {
					Term[] arguments= t.getStructureArguments(cp);
					if (domainTableEntries.length != arguments.length) {
						return false;
					} else {
						for (int i= 0; i < domainTableEntries.length; i++) {
							if (!arguments[i].isCoveredByDomain(domainItems[i],cp,ignoreFreeVariables)) {
								return false;
							}
						};
						return true;
					}
				}
			} catch (TermIsNotAStructure e) {
				return false;
			}
		}
	}
	public Term checkAndOptimizeTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		// initiateDomainItemsIfNecessary();
		// t= t.dereferenceValue(cp);
		try {
			long name= t.getStructureFunctor(cp);
			if (name != functor) {
				throw new DomainAlternativeDoesNotCoverTerm(t.getPosition());
			} else {
				Term[] arguments1= t.getStructureArguments(cp);
				if (domainTableEntries.length != arguments1.length) {
					throw new DomainAlternativeDoesNotCoverTerm(t.getPosition());
				} else {
					Term[] arguments2= new Term[arguments1.length];
					for (int i= 0; i < domainTableEntries.length; i++) {
						arguments2[i]= domainItems[i].checkAndOptimizeTerm(arguments1[i],cp);
					};
					return new PrologStructure(name,arguments2);
				}
			}
		} catch (TermIsNotAStructure e) {
			throw new DomainAlternativeDoesNotCoverTerm(t.getPosition());
		}
	}
	public Term checkTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		// initiateDomainItemsIfNecessary();
		// t= t.dereferenceValue(cp);
		try {
			long name= t.getStructureFunctor(cp);
			if (name != functor) {
				throw new DomainAlternativeDoesNotCoverTerm(t.getPosition());
			} else {
				Term[] arguments1= t.getStructureArguments(cp);
				if (domainTableEntries.length != arguments1.length) {
					throw new DomainAlternativeDoesNotCoverTerm(t.getPosition());
				} else {
					Term[] arguments2= new Term[arguments1.length];
					for (int i= 0; i < domainTableEntries.length; i++) {
						arguments2[i]= domainItems[i].checkTerm(arguments1[i],cp);
					};
					return new PrologStructure(name,arguments2);
				}
			}
		} catch (TermIsNotAStructure e) {
			throw new DomainAlternativeDoesNotCoverTerm(t.getPosition());
		}
	}
	//
	public boolean isEqualTo(DomainAlternative a, HashSet<PrologDomainPair> stack) {
		// initiateDomainItemsIfNecessary();
		return a.isEqualToStructure(functor,domainItems,stack);
	}
	public boolean isEqualToStructure(long name, PrologDomain[] domains, HashSet<PrologDomainPair> stack) {
		// initiateDomainItemsIfNecessary();
		if (name==functor && domainItems.length==domains.length) {
			try {
				for (int n=0; n < domains.length; n++) {
					domainItems[n].isEqualTo(domains[n],stack);
				};
				return true;
			} catch (PrologDomainsAreNotEqual e) {
				return false;
			}
		} else {
			return false;
		}
	}
	public boolean coversAlternative(DomainAlternative a, PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) {
		return false;
	}
	// Converting Term to String
	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
		stream.writeObject(SymbolNames.retrieveSymbolName(functor));
	}
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		SymbolName symbolName= (SymbolName)stream.readObject();
		functor= SymbolNames.insertSymbolName(symbolName.identifier);
	}
	//
	protected String getMultiArgumentDomainTag() {
		return PrologDomainName.tagDomainAlternative_Structure;
	}
	//
	public String toString(CharsetEncoder encoder) {
		// initiateDomainItemsIfNecessary();
		StringBuffer buffer= new StringBuffer();
		buffer.append(getMultiArgumentDomainTag());
		buffer.append("(\'");
		String functorText= SymbolNames.retrieveSymbolName(functor).toRawString(encoder);
		buffer.append(functorText);
		buffer.append("\',[");
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
