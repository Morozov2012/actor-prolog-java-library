// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.domains;

import target.*;

import morozov.terms.*;

public class ActorPrologDomainNone extends ActorPrologArgumentDomain {
	//
	protected static Term termNoDomain= new PrologSymbol(SymbolCodes.symbolCode_E_no_domain);
	//
	public ActorPrologDomainNone(int p) {
		super(p);
	}
	//
	@Override
	public Term toActorPrologTerm() {
		return termNoDomain;
	}
}
