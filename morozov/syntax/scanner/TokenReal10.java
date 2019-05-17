// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.run.*;
import morozov.syntax.scanner.errors.*;
import morozov.syntax.scanner.interfaces.*;
import morozov.system.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class TokenReal10 extends PrologToken {
	//
	protected double value;
	protected boolean isExtendedNumber;
	//
	///////////////////////////////////////////////////////////////
	//
	public TokenReal10(double n, boolean isExtended, int position) {
		super(position);
		value= n;
		isExtendedNumber= isExtended;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public PrologTokenType getType() {
		return PrologTokenType.REAL;
	}
	public double getRealValue(LexicalScannerMasterInterface master, ChoisePoint iX) throws LexicalScannerError {
		return value;
	}
	public double getRealValueOrBacktrack() throws Backtracking {
		return value;
	}
	public double getRealValueOrTermIsNotAReal() throws TermIsNotAReal {
		return value;
	}
	public boolean isExtendedNumber(LexicalScannerMasterInterface master, ChoisePoint iX) {
		return isExtendedNumber;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toActorPrologTerm() {
		Term[] arguments= new Term[]{new PrologReal(value)};
		return new PrologStructure(SymbolCodes.symbolCode_E_real_10,arguments);
	}
	public String toString() {
		return FormatOutput.realToString(value);
	}
}
