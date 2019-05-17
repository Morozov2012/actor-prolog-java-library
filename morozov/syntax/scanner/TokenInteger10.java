// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.run.*;
import morozov.syntax.scanner.errors.*;
import morozov.syntax.scanner.interfaces.*;
import morozov.system.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.math.BigInteger;

public class TokenInteger10 extends PrologToken {
	//
	protected BigInteger value;
	protected boolean isExtendedNumber;
	//
	///////////////////////////////////////////////////////////////
	//
	public TokenInteger10(BigInteger n, boolean isExtended, int position) {
		super(position);
		value= n;
		isExtendedNumber= isExtended;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public PrologTokenType getType() {
		return PrologTokenType.INTEGER;
	}
	public BigInteger getIntegerValue(LexicalScannerMasterInterface master, ChoisePoint iX) throws LexicalScannerError {
		return value;
	}
	public BigInteger getIntegerValueOrBacktrack() throws Backtracking {
		return value;
	}
	public BigInteger getIntegerValueOrTermIsNotAnInteger() throws TermIsNotAnInteger {
		return value;
	}
	public boolean isExtendedNumber(LexicalScannerMasterInterface master, ChoisePoint iX) {
		return isExtendedNumber;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toActorPrologTerm() {
		Term[] arguments= new Term[]{new PrologInteger(value)};
		return new PrologStructure(SymbolCodes.symbolCode_E_integer_10,arguments);
	}
	public String toString() {
		return FormatOutput.integerToString(value);
	}
}
