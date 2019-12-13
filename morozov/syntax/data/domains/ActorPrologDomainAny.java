// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.domains;

import target.*;

import morozov.terms.*;

public class ActorPrologDomainAny extends ActorPrologDomainName {
	//
	protected static String anonymousName= "_";
	protected static Term termAny= new PrologSymbol(SymbolCodes.symbolCode_E_any);
	//
	public ActorPrologDomainAny(int p) {
		super(anonymousName,p);
	}
	//
	public static String getStaticName() {
		return anonymousName;
	}
	//
	@Override
	public Term toActorPrologTerm() {
		return termAny;
	}
}
