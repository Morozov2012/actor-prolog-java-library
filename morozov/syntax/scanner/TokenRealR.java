// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.math.BigInteger;

public class TokenRealR extends TokenReal10 {
	//
	protected BigInteger radix;
	//
	///////////////////////////////////////////////////////////////
	//
	public TokenRealR(BigInteger r, double n, boolean isExtended, int position) {
		super(n,isExtended,position);
		radix= r;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public BigInteger getRadix() {
		return radix;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean correspondsToActorPrologTerm(Term argument, ChoisePoint iX) {
		try {
			long functor= argument.getStructureFunctor(iX);
			if (functor != SymbolCodes.symbolCode_E_real_R) {
				return false;
			};
			Term[] list= argument.getStructureArguments(iX);
			if (list.length != 2) {
				return false;
			};
			argument= list[0];
			BigInteger r= argument.getIntegerValue(iX);
			if (!radix.equals(r)) {
				return false;
			};
			argument= list[1];
			double v= argument.getRealValue(iX);
			return Arithmetic.realsAreEqual(value,v);
		} catch (TermIsNotAStructure e) {
			return false;
		} catch (TermIsNotAnInteger e) {
			return false;
		} catch (TermIsNotAReal e) {
			return false;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term toActorPrologTerm() {
		Term[] arguments= new Term[]{new PrologInteger(radix),new PrologReal(value)};
		return new PrologStructure(SymbolCodes.symbolCode_E_real_R,arguments);
	}
}
