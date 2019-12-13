// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.domains;

import target.*;

import morozov.terms.*;

public class ActorPrologDomainAnInteger extends ActorPrologDomainName {
	//
	protected static String nameINTEGER= "INTEGER";
	protected static Term termAnInteger= new PrologSymbol(SymbolCodes.symbolCode_E_an_integer);
	//
	public ActorPrologDomainAnInteger(int p) {
		super(nameINTEGER,p);
	}
	//
	public static String getStaticName() {
		return nameINTEGER;
	}
	//
	@Override
	public Term toActorPrologTerm() {
		return termAnInteger;
	}
}
