// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.domains;

import target.*;

import morozov.terms.*;

public class ActorPrologDomainAnySet extends ActorPrologDomainAlternative {
	//
	protected static Term termAnySet= new PrologSymbol(SymbolCodes.symbolCode_E_any_set);
	//
	public ActorPrologDomainAnySet(int p) {
		super(p);
	}
	//
	@Override
	public Term toActorPrologTerm() {
		return termAnySet;
	}
}
