// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.math.BigInteger;

public class TokenCharacter extends TokenInteger10 {
	//
	public TokenCharacter(int code, int position) {
		super(BigInteger.valueOf(code),false,position);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean correspondsToActorPrologTerm(Term argument, ChoisePoint iX) {
		try {
			long functor= argument.getStructureFunctor(iX);
			if (functor != SymbolCodes.symbolCode_E_character) {
				return false;
			};
			Term[] list= argument.getStructureArguments(iX);
			if (list.length != 1) {
				return false;
			};
			argument= list[0];
			BigInteger v= argument.getIntegerValue(iX);
			return value.equals(v);
		} catch (TermIsNotAStructure e) {
			return false;
		} catch (TermIsNotAnInteger e) {
			return false;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term toActorPrologTerm() {
		Term[] arguments= new Term[]{new PrologInteger(value)};
		return new PrologStructure(SymbolCodes.symbolCode_E_character,arguments);
	}
}
