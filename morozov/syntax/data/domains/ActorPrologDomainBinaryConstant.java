// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.domains;

import target.*;

import morozov.terms.*;

import java.util.Arrays;

public class ActorPrologDomainBinaryConstant extends ActorPrologDomainAlternative {
	//
	protected byte[] value;
	//
	public ActorPrologDomainBinaryConstant(byte[] v, int p) {
		super(p);
		value= v;
	}
	//
	public byte[] getValue() {
		return value;
	}
	//
	@Override
	public boolean equals(ActorPrologDomainAlternative givenItem) {
		if (givenItem instanceof ActorPrologDomainBinaryConstant) {
			ActorPrologDomainBinaryConstant givenInstance= (ActorPrologDomainBinaryConstant)givenItem;
			if (Arrays.equals(getValue(),givenInstance.getValue())) {
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
		internalArray[0]= new PrologBinary(value);
		return new PrologStructure(SymbolCodes.symbolCode_E_binary_constant,internalArray);
	}
}
