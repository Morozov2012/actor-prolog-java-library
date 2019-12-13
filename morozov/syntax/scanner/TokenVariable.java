// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.run.*;
import morozov.syntax.scanner.errors.*;
import morozov.syntax.scanner.interfaces.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class TokenVariable extends PrologToken {
	//
	protected String name;
	//
	///////////////////////////////////////////////////////////////
	//
	public TokenVariable(String s, int position) {
		super(position);
		name= s;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public PrologTokenType getType() {
		return PrologTokenType.VARIABLE;
	}
	@Override
	public String getVariableName(LexicalScannerMasterInterface master, ChoisePoint iX) throws LexicalScannerError {
		return name;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean correspondsToActorPrologTerm(Term argument, ChoisePoint iX) {
		try {
			long functor= argument.getStructureFunctor(iX);
			if (functor != SymbolCodes.symbolCode_E_vn) {
				return false;
			};
			Term[] list= argument.getStructureArguments(iX);
			if (list.length != 1) {
				return false;
			};
			argument= list[0];
			String v= argument.getStringValue(iX);
			return name.equals(v);
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
		Term[] arguments= new Term[]{new PrologString(name)};
		return new PrologStructure(SymbolCodes.symbolCode_E_vn,arguments);
	}
	@Override
	public String toString() {
		return name;
	}
}
