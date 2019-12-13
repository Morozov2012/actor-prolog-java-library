// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.domains;

import target.*;

import morozov.syntax.data.*;
import morozov.terms.*;

public class ActorPrologDomainEmptySet extends ActorPrologDomainSet {
	//
	protected static LabeledDomain[] noLabeledDomains= new LabeledDomain[0];
	protected static Term termEmptySet= new PrologSymbol(SymbolCodes.symbolCode_E_empty_set);
	//
	public ActorPrologDomainEmptySet(int p) {
		super(noLabeledDomains,p);
	}
	//
	@Override
	public Term toActorPrologTerm() {
		return termEmptySet;
	}
}
