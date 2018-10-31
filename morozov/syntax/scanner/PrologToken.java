// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.syntax.scanner.errors.*;
import morozov.terms.*;

import java.math.BigInteger;

public abstract class PrologToken {
	//
	public int position;
	//
	///////////////////////////////////////////////////////////////
	//
	public PrologToken(int p) {
		position= p;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int getPosition() {
		return position;
	}
	abstract public PrologTokenType getType();
	public BigInteger getIntegerValue() {
		throw new WrongTokenIsNotAnInteger();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public double getRealValue() {
		throw new WrongTokenIsNotAReal();
	}
	public int getSymbolCode() {
		throw new WrongTokenIsNotASymbol();
	}
	public boolean isInQuotes() {
		throw new WrongTokenIsNotASymbol();
	}
	public String getStringValue() {
		throw new WrongTokenIsNotAString();
	}
	public byte[] getBinaryValue() {
		throw new WrongTokenIsNotABinary();
	}
	public String getVariableName() {
		throw new WrongTokenIsNotAVariable();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term toTerm();
	//
	public Term toTermTP() {
		Term[] arguments= new Term[2];
		arguments[0]= toTerm();
		arguments[1]= new PrologInteger(position);
		return new PrologStructure(SymbolCodes.symbolCode_E_t,arguments);
	}
}
