// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class TokenKeyword extends TokenSymbol {
	//
	public TokenKeyword(int c, int position) {
		super(c,false,position);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public PrologTokenType getType() {
		return PrologTokenType.KEYWORD;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean correspondsToActorPrologTerm(Term argument, ChoisePoint iX) {
		try {
			long functor= argument.getStructureFunctor(iX);
			if (functor != SymbolCodes.symbolCode_E_keyword) {
				return false;
			};
			Term[] list= argument.getStructureArguments(iX);
			if (list.length != 1) {
				return false;
			};
			argument= list[0];
			long v= argument.getSymbolValue(iX);
			return (value==v);
		} catch (TermIsNotAStructure e) {
			return false;
		} catch (TermIsNotASymbol e) {
			return false;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term toActorPrologTerm() {
		Term[] arguments= new Term[]{new PrologSymbol(value)};
		return new PrologStructure(SymbolCodes.symbolCode_E_keyword,arguments);
	}
}
