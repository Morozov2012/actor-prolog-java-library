// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.indices.errors;

import java.math.BigInteger;

public class IllegalIndexRange extends RuntimeException {
	public BigInteger leftBound;
	public BigInteger rightBound;
	//
	public IllegalIndexRange(BigInteger left, BigInteger right) {
		leftBound= left;
		rightBound= right;
	}
	//
	public String toString() {
		return this.getClass().toString() + "(" + leftBound.toString() + ">" + rightBound.toString() + ")";
	}
}
