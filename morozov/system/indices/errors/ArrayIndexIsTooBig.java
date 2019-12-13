// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.indices.errors;

import java.math.BigInteger;

public class ArrayIndexIsTooBig extends RuntimeException {
	//
	protected BigInteger indexValue;
	protected BigInteger indexBound;
	//
	public ArrayIndexIsTooBig(BigInteger value, BigInteger bound) {
		indexValue= value;
		indexBound= bound;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(" + indexValue.toString() + ">" + indexBound.toString() + ")";
	}
}
