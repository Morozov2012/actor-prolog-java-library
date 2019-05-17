// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.run.*;
import morozov.syntax.scanner.errors.*;
import morozov.syntax.scanner.interfaces.*;
import morozov.terms.*;

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
	public PrologTokenType getType() {
		return PrologTokenType.VARIABLE;
	}
	public String getVariableName(LexicalScannerMasterInterface master, ChoisePoint iX) throws LexicalScannerError {
		return name;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toActorPrologTerm() {
		Term[] arguments= new Term[]{new PrologString(name)};
		return new PrologStructure(SymbolCodes.symbolCode_E_vn,arguments);
	}
	public String toString() {
		return name;
	}
}
