// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.run.*;
import morozov.system.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.io.Serializable;
import java.math.BigInteger;

public class NumericalValue implements Serializable {
	//
	protected boolean useDoubleValue= false;
	protected BigInteger integerValue;
	protected double doubleValue;
	//
	public NumericalValue(BigInteger v) {
		useDoubleValue= false;
		integerValue= v;
	}
	public NumericalValue(double v) {
		useDoubleValue= true;
		doubleValue= v;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static NumericalValue argumentToNumericalValue(Term value, ChoisePoint iX) {
		try {
			BigInteger integerValue= value.getIntegerValue(iX);
			return new NumericalValue(integerValue);
		} catch (TermIsNotAnInteger e1) {
			try {
				double doubleValue= value.getRealValue(iX);
				return new NumericalValue(doubleValue);
			} catch (TermIsNotAReal e2) {
				throw new WrongArgumentIsNotNumerical(value);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		if (useDoubleValue) {
			return new PrologReal(doubleValue);
		} else {
			return new PrologInteger(integerValue);
		}
	}
	//
	public long toLong(long coefficient) {
		if (useDoubleValue) {
			return PrologInteger.toLong(doubleValue * coefficient);
		} else {
			return PrologInteger.toLong(integerValue.multiply(BigInteger.valueOf(coefficient)));
		}
	}
	//
	public double toDouble() {
		if (useDoubleValue) {
			return doubleValue;
		} else {
			return integerValue.doubleValue();
		}
	}
	//
	public String toString() {
		if (useDoubleValue) {
			return Double.toString(doubleValue);
		} else {
			return integerValue.toString();
		}
	}
}
