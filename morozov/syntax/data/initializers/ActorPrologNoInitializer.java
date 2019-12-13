// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.initializers;

import target.*;

import morozov.terms.*;

public class ActorPrologNoInitializer extends ActorPrologInitializer {
	//
	protected static Term termNoInitializer= new PrologSymbol(SymbolCodes.symbolCode_E_no_initializer);
	//
	public ActorPrologNoInitializer(int p) {
		super(p);
	}
	//
	public static Term getTermNoInitializer() {
		return termNoInitializer;
	}
	//
	@Override
	public Term toActorPrologTerm() {
		return termNoInitializer;
	}
}
