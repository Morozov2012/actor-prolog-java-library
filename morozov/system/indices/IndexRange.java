// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.indices;

import morozov.system.indices.errors.*;

import java.math.BigInteger;

public class IndexRange {
	//
	protected BigInteger leftBound;
	protected BigInteger rightBound;
	protected BigInteger center;
	//
	public IndexRange(BigInteger left, BigInteger right) {
		leftBound= left;
		rightBound= right;
		center= left.add(right).divide(BigInteger.valueOf(2));
	}
	//
	public BigInteger getLeftBound() {
		return leftBound;
	}
	public BigInteger getRightBound() {
		return rightBound;
	}
	public BigInteger getCenter() {
		return center;
	}
	//
	public boolean includesValue(BigInteger value) {
		if (value.compareTo(leftBound) >= 0 && value.compareTo(rightBound) <= 0) {
			return true;
		} else {
			return false;
		}
	}
	public void checkIndexValue(BigInteger value) {
		if (value.compareTo(leftBound) < 0) {
			throw new ArrayIndexIsTooSmall(value,leftBound);
		} else if (value.compareTo(rightBound) > 0) {
			throw new ArrayIndexIsTooBig(value,rightBound);
		}
	}
	public BigInteger computeRadius() {
		BigInteger radius1= rightBound.subtract(center);
		BigInteger radius2= center.subtract(leftBound);
		return radius1.max(radius2);
	}
	public BigInteger reflectValue(BigInteger value) {
		return center.add(center).subtract(value);
	}
}
