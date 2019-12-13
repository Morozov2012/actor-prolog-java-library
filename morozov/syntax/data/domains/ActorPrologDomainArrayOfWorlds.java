// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.domains;

import target.*;

import morozov.terms.*;

public class ActorPrologDomainArrayOfWorlds extends ActorPrologArgumentDomain {
	//
	protected long container;
	protected long element;
	//
	public ActorPrologDomainArrayOfWorlds(long c, long e, int p) {
		super(p);
		container= c;
		element= e;
	}
	//
	public long getContainer() {
		return container;
	}
	public long getElement() {
		return element;
	}
	//
	@Override
	public boolean equals(ActorPrologDomainAlternative givenItem) {
		if (givenItem instanceof ActorPrologDomainArrayOfWorlds) {
			ActorPrologDomainArrayOfWorlds givenInstance= (ActorPrologDomainArrayOfWorlds)givenItem;
			if (	getContainer()==givenInstance.getContainer() &&
				getElement()==givenInstance.getElement()) {
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
		Term[] internalArray= new Term[2];
		internalArray[0]= new PrologSymbol(container);
		internalArray[1]= new PrologSymbol(element);
		return new PrologStructure(SymbolCodes.symbolCode_E_array_of_worlds,internalArray);
	}
}
