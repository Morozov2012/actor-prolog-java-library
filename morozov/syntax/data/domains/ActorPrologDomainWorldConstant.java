// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.domains;

import target.*;

import morozov.terms.*;

public class ActorPrologDomainWorldConstant extends ActorPrologArgumentDomain {
	//
	protected long value;
	//
	public ActorPrologDomainWorldConstant(long v, int p) {
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
		if (givenItem instanceof ActorPrologDomainWorldConstant) {
			ActorPrologDomainWorldConstant givenInstance= (ActorPrologDomainWorldConstant)givenItem;
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
		return new PrologStructure(SymbolCodes.symbolCode_E_world,internalArray);
	}
}
