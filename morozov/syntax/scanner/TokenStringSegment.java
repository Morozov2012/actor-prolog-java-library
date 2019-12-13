// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.run.*;
import morozov.syntax.scanner.errors.*;
import morozov.syntax.scanner.interfaces.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class TokenStringSegment extends PrologToken {
	//
	protected String value;
	//
	///////////////////////////////////////////////////////////////
	//
	public TokenStringSegment(String s, int position) {
		super(position);
		value= s;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public PrologTokenType getType() {
		return PrologTokenType.STRING_SEGMENT;
	}
	@Override
	public String getStringValue(LexicalScannerMasterInterface master, ChoisePoint iX) throws LexicalScannerError {
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean correspondsToActorPrologTerm(Term argument, ChoisePoint iX) {
		try {
			long functor= argument.getStructureFunctor(iX);
			if (functor != SymbolCodes.symbolCode_E_string_segment) {
				return false;
			};
			Term[] list= argument.getStructureArguments(iX);
			if (list.length != 1) {
				return false;
			};
			argument= list[0];
			String v= argument.getStringValue(iX);
			return value.equals(v);
		} catch (TermIsNotAStructure e) {
			return false;
		} catch (TermIsNotAString e) {
			return false;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term toActorPrologTerm() {
		Term[] arguments= new Term[]{new PrologString(value)};
		return new PrologStructure(SymbolCodes.symbolCode_E_string_segment,arguments);
	}
	@Override
	public String toString() {
		return value;
	}
}
