// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.domains;

import target.*;

import morozov.terms.*;

public class ActorPrologDomainRealRange extends ActorPrologDomainAlternative {
	//
	protected ActorPrologDomainRealConstant leftBound;
	protected ActorPrologDomainRealConstant rightBound;
	//
	public ActorPrologDomainRealRange(ActorPrologDomainRealConstant left, ActorPrologDomainRealConstant right, int p) {
		super(p);
		leftBound= left;
		rightBound= right;
	}
	//
	public ActorPrologDomainRealConstant getLeftBound() {
		return leftBound;
	}
	public ActorPrologDomainRealConstant getRightBound() {
		return rightBound;
	}
	//
	@Override
	public boolean equals(ActorPrologDomainAlternative givenItem) {
		if (givenItem instanceof ActorPrologDomainRealRange) {
			ActorPrologDomainRealRange givenInstance= (ActorPrologDomainRealRange)givenItem;
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
		return new PrologStructure(SymbolCodes.symbolCode_E_real_range,internalArray);
	}
}
