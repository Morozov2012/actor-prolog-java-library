// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.domains;

import morozov.terms.*;

import target.*;

import java.math.BigInteger;

public class ActorPrologDomainIntegerConstant extends ActorPrologDomainAlternative {
	//
	protected BigInteger value;
	protected boolean radixIsUsed= false;
	protected BigInteger radix;
	//
	public ActorPrologDomainIntegerConstant(BigInteger v, int p) {
		super(p);
		value= v;
	}
	public ActorPrologDomainIntegerConstant(BigInteger v, BigInteger r, int p) {
		super(p);
		value= v;
		radixIsUsed= true;
		radix= r;
	}
	//
	public BigInteger getValue() {
		return value;
	}
	//
	@Override
	public boolean equals(ActorPrologDomainAlternative givenItem) {
		if (givenItem instanceof ActorPrologDomainIntegerConstant) {
			ActorPrologDomainIntegerConstant givenInstance= (ActorPrologDomainIntegerConstant)givenItem;
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
	public Term toValueTerm() {
		Term termValue= new PrologInteger(value);
		if (radixIsUsed) {
			Term[] termArray= new Term[2];
			termArray[0]= new PrologInteger(radix);
			termArray[1]= termValue;
			termValue= new PrologStructure(SymbolCodes.symbolCode_E_radix,termArray);
		};
		return termValue;
	}
	@Override
	public Term toActorPrologTerm() {
		Term[] internalArray= new Term[1];
		internalArray[0]= toValueTerm();
		return new PrologStructure(SymbolCodes.symbolCode_E_integer_constant,internalArray);
	}
}
