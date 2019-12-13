// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.domains;

import target.*;

import morozov.terms.*;

public class ActorPrologDomainAString extends ActorPrologDomainName {
	//
	protected static String nameSTRING= "STRING";
	protected static Term termAString= new PrologSymbol(SymbolCodes.symbolCode_E_a_string);
	//
	public ActorPrologDomainAString(int p) {
		super(nameSTRING,p);
	}
	//
	public static String getStaticName() {
		return nameSTRING;
	}
	//
	@Override
	public Term toActorPrologTerm() {
		return termAString;
	}
}
