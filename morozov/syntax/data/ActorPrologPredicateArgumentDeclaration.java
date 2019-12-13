// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import target.*;

import morozov.syntax.data.domains.*;
import morozov.terms.*;

public class ActorPrologPredicateArgumentDeclaration {
	//
	protected ActorPrologArgumentDomain domain;
	protected boolean hasAsterisk;
	//
	public ActorPrologPredicateArgumentDeclaration(
			ActorPrologArgumentDomain d,
			boolean hA) {
		domain= d;
		hasAsterisk= hA;
	}
	//
	public ActorPrologArgumentDomain getDomain() {
		return domain;
	}
	public boolean hasAsterisk() {
		return hasAsterisk;
	}
	public int getPosition() {
		return domain.getPosition();
	}
	//
	public Term toActorPrologTerm() {
		Term[] internalArray= new Term[2];
		internalArray[0]= domain.toActorPrologTerm();
		internalArray[1]= new PrologInteger(getPosition());
		if (hasAsterisk) {
			return new PrologStructure(SymbolCodes.symbolCode_E_asterisk,internalArray);
		} else {
			return new PrologStructure(SymbolCodes.symbolCode_E_item,internalArray);
		}
	}
}
