// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.terms.*;

public class DomainStructure extends MultiArgumentDomainItem {
	protected long functor;
	public DomainStructure(long name, String[] entries) {
		super(entries);
		functor= name;
	}
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		initiateDomainItemsIfNecessary();
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
		initiateDomainItemsIfNecessary();
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
		initiateDomainItemsIfNecessary();
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
}
