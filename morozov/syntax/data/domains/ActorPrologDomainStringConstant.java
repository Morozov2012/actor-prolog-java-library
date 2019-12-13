// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.domains;

import target.*;

import morozov.terms.*;

public class ActorPrologDomainStringConstant extends ActorPrologDomainAlternative {
	//
	protected String value;
	//
	public ActorPrologDomainStringConstant(String v, int p) {
		super(p);
		value= v;
	}
	//
	public String getValue() {
		return value;
	}
	//
	@Override
	public boolean equals(ActorPrologDomainAlternative givenItem) {
		if (givenItem instanceof ActorPrologDomainStringConstant) {
			ActorPrologDomainStringConstant givenInstance= (ActorPrologDomainStringConstant)givenItem;
			if (getValue().equals(givenInstance.getValue())) {
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
		internalArray[0]= new PrologString(value);
		return new PrologStructure(SymbolCodes.symbolCode_E_string_constant,internalArray);
	}
}
