// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.domains;

import target.*;

import morozov.terms.*;

public class ActorPrologDomainABinary extends ActorPrologDomainName {
	//
	protected static String nameBINARY= "BINARY";
	protected static Term termABinary= new PrologSymbol(SymbolCodes.symbolCode_E_a_binary);
	//
	public ActorPrologDomainABinary(int p) {
		super(nameBINARY,p);
	}
	//
	public static String getStaticName() {
		return nameBINARY;
	}
	//
	@Override
	public Term toActorPrologTerm() {
		return termABinary;
	}
}
