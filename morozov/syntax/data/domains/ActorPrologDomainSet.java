// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.domains;

import target.*;

import morozov.syntax.data.*;
import morozov.terms.*;

public class ActorPrologDomainSet extends ActorPrologDomainAlternative {
	//
	protected LabeledDomain[] pairs;
	//
	public ActorPrologDomainSet(LabeledDomain[] v, int p) {
		super(p);
		pairs= v;
	}
	//
	public LabeledDomain[] getPairs() {
		return pairs;
	}
	//
	@Override
	public boolean equals(ActorPrologDomainAlternative givenItem) {
		if (givenItem instanceof ActorPrologDomainSet) {
			ActorPrologDomainSet givenInstance= (ActorPrologDomainSet)givenItem;
			LabeledDomain[] array1= getPairs();
			LabeledDomain[] array2= givenInstance.getPairs();
			if (array1.length!=array2.length) {
				return false;
			};
			Loop1: for (int k=0; k < array1.length; k++) {
				LabeledDomain pair1= array1[k];
				long keyCode1= pair1.getKeyCode();
				Loop2: for (int m=0; m < array2.length; m++) {
					LabeledDomain pair2= array2[m];
					long keyCode2= pair2.getKeyCode();
					if (keyCode1==keyCode2) {
						if (!pair1.getDomain().equals(pair2.getDomain())) {
							return false;
						};
						continue Loop1;
					}
				};
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
		Term list= PrologEmptyList.instance;
		for (int k=pairs.length-1; k >= 0; k--) {
			LabeledDomain pair= pairs[k];
			Term[] auxiliaryArray= new Term[3];
			auxiliaryArray[0]= pair.getKeyTerm();
			auxiliaryArray[1]= pair.getDomain().toActorPrologTerm();
			auxiliaryArray[2]= new PrologInteger(pair.getPosition());
			Term item= new PrologStructure(SymbolCodes.symbolCode_E_pair,auxiliaryArray);
			list= new PrologList(item,list);
		};
		Term[] internalArray= new Term[1];
		internalArray[0]= list;
		return new PrologStructure(SymbolCodes.symbolCode_E_set,internalArray);
	}
}
