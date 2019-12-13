// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.domains;

import target.*;

import morozov.terms.*;

import java.math.BigInteger;

public class ActorPrologDomainRealConstant extends ActorPrologDomainAlternative {
	//
	protected double value;
	protected boolean radixIsUsed= false;
	protected BigInteger radix;
	//
	public ActorPrologDomainRealConstant(double v, int p) {
		super(p);
		value= v;
	}
	public ActorPrologDomainRealConstant(double v, BigInteger r, int p) {
		super(p);
		value= v;
		radixIsUsed= true;
		radix= r;
	}
	//
	public double getValue() {
		return value;
	}
	//
	@Override
	public boolean equals(ActorPrologDomainAlternative givenItem) {
		if (givenItem instanceof ActorPrologDomainRealConstant) {
			ActorPrologDomainRealConstant givenInstance= (ActorPrologDomainRealConstant)givenItem;
			if (Double.compare(getValue(),givenInstance.getValue())==0) {
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
		Term termValue= new PrologReal(value);
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
		return new PrologStructure(SymbolCodes.symbolCode_E_real_constant,internalArray);
	}
}
