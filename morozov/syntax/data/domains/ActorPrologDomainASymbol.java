// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.domains;

import target.*;

import morozov.terms.*;

public class ActorPrologDomainASymbol extends ActorPrologDomainName {
	//
	protected static String nameSYMBOL= "SYMBOL";
	protected static Term termASymbol= new PrologSymbol(SymbolCodes.symbolCode_E_a_symbol);
	//
	public ActorPrologDomainASymbol(int p) {
		super(nameSYMBOL,p);
	}
	//
	public static String getStaticName() {
		return nameSYMBOL;
	}
	//
	@Override
	public Term toActorPrologTerm() {
		return termASymbol;
	}
}
