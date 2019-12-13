// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.domains;

import target.*;

import morozov.terms.*;

public class ActorPrologDomainAReal extends ActorPrologDomainName {
	//
	protected static String nameREAL= "REAL";
	protected static Term termAReal= new PrologSymbol(SymbolCodes.symbolCode_E_a_real);
	//
	public ActorPrologDomainAReal(int p) {
		super(nameREAL,p);
	}
	//
	public static String getStaticName() {
		return nameREAL;
	}
	//
	@Override
	public Term toActorPrologTerm() {
		return termAReal;
	}
}
