// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.terms.*;

public class TokenBinary extends PrologToken {
	//
	protected byte[] value;
	//
	///////////////////////////////////////////////////////////////
	//
	public TokenBinary(byte[] array, int position) {
		super(position);
		value= array;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public PrologTokenType getType() {
		return PrologTokenType.BINARY;
	}
	public byte[] getBinaryValue() {
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		Term[] arguments= new Term[]{new PrologBinary(value)};
		return new PrologStructure(SymbolCodes.symbolCode_E_binary,arguments);
	}
	public String toString() {
		return PrologBinary.binaryToString(value);
	}
}
