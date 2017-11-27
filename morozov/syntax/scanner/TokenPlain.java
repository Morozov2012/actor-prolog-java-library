// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.syntax.scanner.signals.*;
import morozov.terms.*;

public class TokenPlain extends PrologToken {
	//
	protected PrologTokenType type;
	//
	///////////////////////////////////////////////////////////////
	//
	public TokenPlain(PrologTokenType t, int p) {
		super(p);
		type= t;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public PrologTokenType getType() {
		return type;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		try {
			return type.toTerm();
		} catch (TokenIsCompound e) {
			Term[] arguments= new Term[]{new PrologString(type.toString())};
			return new PrologStructure(SymbolCodes.symbolCode_E_string,arguments);
		}
	}
	public String toString() {
		try {
			return type.toText();
		} catch (TokenIsCompound e) {
			return type.toString();
		}
	}
}
