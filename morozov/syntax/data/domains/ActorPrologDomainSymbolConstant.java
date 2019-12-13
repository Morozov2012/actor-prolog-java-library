// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.domains;

import target.*;

import morozov.terms.*;

public class ActorPrologDomainSymbolConstant extends ActorPrologDomainAlternative {
	//
	protected long value;
	//
	public ActorPrologDomainSymbolConstant(long v, int p) {
		super(p);
		value= v;
	}
	//
	public long getValue() {
		return value;
	}
	//
	@Override
	public boolean equals(ActorPrologDomainAlternative givenItem) {
		if (givenItem instanceof ActorPrologDomainSymbolConstant) {
			ActorPrologDomainSymbolConstant givenInstance= (ActorPrologDomainSymbolConstant)givenItem;
			if (Long.compare(getValue(),givenInstance.getValue())==0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	//
	@Override
	public Term toActorPrologTerm() {
		Term[] internalArray= new Term[1];
		internalArray[0]= new PrologSymbol(value);
		return new PrologStructure(SymbolCodes.symbolCode_E_symbol_constant,internalArray);
	}
}
