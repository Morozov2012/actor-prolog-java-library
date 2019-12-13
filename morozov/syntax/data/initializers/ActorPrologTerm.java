// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.initializers;

import target.*;

import morozov.terms.*;

public class ActorPrologTerm extends ActorPrologInitializer {
	//
	protected Term value;
	//
	public ActorPrologTerm(Term v, int p) {
		super(p);
		value= v;
	}
	//
	public Term getValue() {
		return value;
	}
	//
	@Override
	public Term toActorPrologTerm() {
		Term[] internalArray= new Term[1];
		internalArray[0]= value;
		return new PrologStructure(SymbolCodes.symbolCode_E_term,internalArray);
	}
}
