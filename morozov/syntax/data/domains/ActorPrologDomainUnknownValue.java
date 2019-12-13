// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.domains;

import target.*;

import morozov.terms.*;

public class ActorPrologDomainUnknownValue extends ActorPrologArgumentDomain {
	//
	protected static Term termUnknownValue= new PrologSymbol(SymbolCodes.symbolCode_E_unknown_value);
	//
	public ActorPrologDomainUnknownValue(int p) {
		super(p);
	}
	//
	@Override
	public Term toActorPrologTerm() {
		return termUnknownValue;
	}
}
