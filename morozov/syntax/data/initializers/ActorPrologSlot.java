// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.initializers;

import target.*;

import morozov.terms.*;

public class ActorPrologSlot extends ActorPrologInitializer {
	//
	protected long nameCode;
	//
	public ActorPrologSlot(long c, int p) {
		super(p);
		nameCode= c;
	}
	//
	public long getNameCode() {
		return nameCode;
	}
	//
	@Override
	public Term toActorPrologTerm() {
		Term[] internalArray= new Term[1];
		internalArray[0]= new PrologSymbol(nameCode);
		return new PrologStructure(SymbolCodes.symbolCode_E_slot,internalArray);
	}
}
