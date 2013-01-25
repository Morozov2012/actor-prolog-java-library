// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import morozov.system.*;

public class TokenReal extends PrologToken {
	protected double value;
	public TokenReal(double n, int position) {
		super(position);
		value= n;
	}
	public PrologTokenType getType() {
		return PrologTokenType.REAL;
	}
	public double getRealValue() {
		return value;
	}
	public String toString() {
		return FormatOutput.realToString(value);
	}
}
