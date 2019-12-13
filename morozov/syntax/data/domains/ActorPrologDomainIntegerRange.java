// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.domains;

import target.*;

import morozov.terms.*;

public class ActorPrologDomainIntegerRange extends ActorPrologDomainAlternative {
	//
	protected ActorPrologDomainIntegerConstant leftBound;
	protected ActorPrologDomainIntegerConstant rightBound;
	//
	public ActorPrologDomainIntegerRange(ActorPrologDomainIntegerConstant left, ActorPrologDomainIntegerConstant right, int p) {
		super(p);
		leftBound= left;
		rightBound= right;
	}
	//
	public ActorPrologDomainIntegerConstant getLeftBound() {
		return leftBound;
	}
	public ActorPrologDomainIntegerConstant getRightBound() {
		return rightBound;
	}
	//
	@Override
	public boolean equals(ActorPrologDomainAlternative givenItem) {
		if (givenItem instanceof ActorPrologDomainIntegerRange) {
			ActorPrologDomainIntegerRange givenInstance= (ActorPrologDomainIntegerRange)givenItem;
			if (!getLeftBound().equals(givenInstance.getLeftBound())) {
				return false;
			};
			if (!getRightBound().equals(givenInstance.getRightBound())) {
				return false;
			};
			return true;
		} else {
			return false;
		}
	}
	//
	@Override
	public Term toActorPrologTerm() {
		Term[] internalArray= new Term[2];
		internalArray[0]= leftBound.toValueTerm();
		internalArray[1]= rightBound.toValueTerm();
		return new PrologStructure(SymbolCodes.symbolCode_E_integer_range,internalArray);
	}
}
