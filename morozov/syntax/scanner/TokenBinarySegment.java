// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.run.*;
import morozov.syntax.scanner.errors.*;
import morozov.syntax.scanner.interfaces.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class TokenBinarySegment extends PrologToken {
	//
	protected byte[] value;
	//
	///////////////////////////////////////////////////////////////
	//
	public TokenBinarySegment(byte[] array, int position) {
		super(position);
		value= array;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public PrologTokenType getType() {
		return PrologTokenType.BINARY_SEGMENT;
	}
	@Override
	public byte[] getBinaryValue(LexicalScannerMasterInterface master, ChoisePoint iX) throws LexicalScannerError {
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean correspondsToActorPrologTerm(Term argument, ChoisePoint iX) {
		try {
			byte[] v= argument.getBinaryValue(iX);
			return PrologBinary.twoBinariesAreEqual(value,v);
		} catch (TermIsNotABinary e) {
			return false;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term toActorPrologTerm() {
		Term[] arguments= new Term[]{new PrologBinary(value)};
		return new PrologStructure(SymbolCodes.symbolCode_E_binary,arguments);
	}
	@Override
	public String toString() {
		return PrologBinary.binaryToString(value);
	}
}
