// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.domains;

import target.*;

import morozov.terms.*;

public class ActorPrologDomainStructure extends ActorPrologDomainAlternative {
	//
	protected long functor;
	protected ActorPrologArgumentDomain[] arguments;
	//
	public ActorPrologDomainStructure(long f, ActorPrologArgumentDomain[] a, int p) {
		super(p);
		functor= f;
		arguments= a;
	}
	//
	public long getFunctor() {
		return functor;
	}
	public ActorPrologArgumentDomain[] getArguments() {
		return arguments;
	}
	//
	@Override
	public boolean equals(ActorPrologDomainAlternative givenItem) {
		if (givenItem instanceof ActorPrologDomainStructure) {
			ActorPrologDomainStructure givenInstance= (ActorPrologDomainStructure)givenItem;
			if (Long.compare(getFunctor(),givenInstance.getFunctor())!=0) {
				return false;
			};
			ActorPrologArgumentDomain[] array1= getArguments();
			ActorPrologArgumentDomain[] array2= givenInstance.getArguments();
			if (array1.length!=array2.length) {
				return false;
			};
			for (int k=0; k < array1.length; k++) {
				if (!array1[k].equals(array2[k])) {
					return false;
				}
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
		for (int k=arguments.length-1; k >= 0; k--) {
			ActorPrologArgumentDomain argument= arguments[k];
			Term[] auxiliaryArray= new Term[2];
			auxiliaryArray[0]= argument.toActorPrologTerm();
			auxiliaryArray[1]= new PrologInteger(argument.getPosition());
			Term item= new PrologStructure(SymbolCodes.symbolCode_E_ad,auxiliaryArray);
			list= new PrologList(item,list);
		};
		Term[] internalArray= new Term[2];
		internalArray[0]= new PrologSymbol(functor);
		internalArray[1]= list;
		return new PrologStructure(SymbolCodes.symbolCode_E_structure,internalArray);
	}
}
