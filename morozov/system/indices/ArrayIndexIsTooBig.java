// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.indices;

import java.math.BigInteger;

public class ArrayIndexIsTooBig extends RuntimeException {
	public BigInteger indexValue;
	public BigInteger indexBound;
	//
	public ArrayIndexIsTooBig(BigInteger value, BigInteger bound) {
		indexValue= value;
		indexBound= bound;
	}
	//
	public String toString() {
		return this.getClass().toString() + "(" + indexValue.toString() + ">" + indexBound.toString() + ")";
	}
}
