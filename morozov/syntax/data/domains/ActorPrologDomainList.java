// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.domains;

import target.*;

import morozov.terms.*;

public class ActorPrologDomainList extends ActorPrologDomainAlternative {
	//
	protected ActorPrologArgumentDomain argument;
	//
	public ActorPrologDomainList(ActorPrologArgumentDomain a, int p) {
		super(p);
		argument= a;
	}
	//
	public ActorPrologArgumentDomain getArgument() {
		return argument;
	}
	//
	@Override
	public boolean equals(ActorPrologDomainAlternative givenItem) {
		if (givenItem instanceof ActorPrologDomainList) {
			ActorPrologDomainList givenInstance= (ActorPrologDomainList)givenItem;
			return getArgument().equals(givenInstance.getArgument());
		} else {
			return false;
		}
	}
	//
	@Override
	public Term toActorPrologTerm() {
		Term[] auxiliaryArray= new Term[2];
		auxiliaryArray[0]= argument.toActorPrologTerm();
		auxiliaryArray[1]= new PrologInteger(argument.getPosition());
		Term item= new PrologStructure(SymbolCodes.symbolCode_E_ad,auxiliaryArray);
		Term[] internalArray= new Term[1];
		internalArray[0]= item;
		return new PrologStructure(SymbolCodes.symbolCode_E_list,internalArray);
	}
}
