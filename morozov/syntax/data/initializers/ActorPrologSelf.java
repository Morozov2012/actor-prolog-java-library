// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.initializers;

import target.*;

import morozov.terms.*;

public class ActorPrologSelf extends ActorPrologSlot {
	//
	protected static long staticNameCode= SymbolCodes.symbolCode_E_self;
	protected static Term termSelf= new PrologSymbol(SymbolCodes.symbolCode_E_self);
	//
	public ActorPrologSelf(int p) {
		super(staticNameCode,p);
	}
	//
	@Override
	public Term toActorPrologTerm() {
		return termSelf;
	}
}
