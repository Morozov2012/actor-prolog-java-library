// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.indices;

import target.*;

import morozov.system.indices.errors.*;

import java.math.BigInteger;

public class IndexRange {
	public BigInteger leftBound;
	public BigInteger rightBound;
	public BigInteger center;
	public IndexRange(BigInteger left, BigInteger right) {
		if (left.compareTo(right) > 0) {
			if (DefaultOptions.integerOverflowCheck) {
				throw new IllegalIndexRange(left,right);
			} else {
				BigInteger temporaryValue= left;
				left= right;
				right= temporaryValue;
			}
		};
		leftBound= left;
		rightBound= right;
		center= left.add(right).divide(BigInteger.valueOf(2));
	}
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
			// if (DefaultOptions.integerOverflowCheck) {
			//	throw new ArrayIndexIsTooSmall(value,leftBound);
			// } else {
			//	// y(n)= rem(x(n)-right,right-left+1)+right;
			//	BigInteger range= rightBound.subtract(leftBound).add(BigInteger.ONE);
			//	BigInteger[] dar= value.subtract(rightBound).divideAndRemainder(range);
			//	value= dar[1].add(rightBound);
			// }
		} else if (value.compareTo(rightBound) > 0) {
			throw new ArrayIndexIsTooBig(value,rightBound);
			// if (DefaultOptions.integerOverflowCheck) {
			//	throw new ArrayIndexIsTooBig(value,rightBound);
			// } else {
			//	// y(n)= rem(x(n)-left,right-left+1)+left;
			//	BigInteger range= rightBound.subtract(leftBound).add(BigInteger.ONE);
			//	BigInteger[] dar= value.subtract(leftBound).divideAndRemainder(range);
			//	value= dar[1].add(leftBound);
			// }
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
