// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.system.*;
import morozov.terms.*;

public class TokenReal extends PrologToken {
	//
	protected double value;
	//
	///////////////////////////////////////////////////////////////
	//
	public TokenReal(double n, int position) {
		super(position);
		value= n;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public PrologTokenType getType() {
		return PrologTokenType.REAL;
	}
	public double getRealValue() {
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		Term[] arguments= new Term[]{new PrologReal(value)};
		return new PrologStructure(SymbolCodes.symbolCode_E_real,arguments);
	}
	public String toString() {
		return FormatOutput.realToString(value);
	}
}
