// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import morozov.syntax.scanner.errors.*;

import java.math.BigInteger;

public abstract class PrologToken {
	public int position;
	public PrologToken(int p) {
		position= p;
	}
	public int getPosition() {
		return position;
	}
	public abstract PrologTokenType getType();
	public BigInteger getIntegerValue() {
		throw new WrongTokenIsNotAnInteger();
	}
	public double getRealValue() {
		throw new WrongTokenIsNotAReal();
	}
	public int getSymbolCode() {
		throw new WrongTokenIsNotASymbol();
	}
	public boolean isIncludedIntoApostrophes() {
		throw new WrongTokenIsNotASymbol();
	}
	public String getStringValue() {
		throw new WrongTokenIsNotAString();
	}
	public String getVariableName() {
		throw new WrongTokenIsNotAVariable();
	}
}
