// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.terms.errors.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.math.BigInteger;
import java.math.BigDecimal;

public class PrologInteger extends Term {
	private BigInteger value;
	public PrologInteger(long v) {
		value= BigInteger.valueOf(v);
	}
	public PrologInteger(BigInteger v) {
		value= v;
	}
	public int hashCode() {
		return value.intValue();
	}
	public void isInteger(int v, ChoisePoint cp) throws Backtracking {
		if (!value.equals(BigInteger.valueOf(v)))
			throw Backtracking.instance;
	}
	public void isInteger(BigInteger v, ChoisePoint cp) throws Backtracking {
		if (!value.equals(v))
			throw Backtracking.instance;
	}
	public BigInteger getIntegerValue(ChoisePoint cp) throws TermIsNotAnInteger {
		return value;
	}
	public int getSmallIntegerValue(ChoisePoint cp) throws TermIsNotAnInteger {
		return toInteger(value);
	}
	public long getLongIntegerValue(ChoisePoint cp) throws TermIsNotAnInteger {
		return toLong(value);
	}
	//
	public static int toInteger(BigInteger value) {
		if (DefaultOptions.integerOverflowCheck) {
			if (value.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
				throw new IntegerValueIsTooBig();
			} else if (value.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) < 0) {
				throw new IntegerValueIsTooSmall();
			} else {
				return value.intValue();
			}
		} else {
			return value.intValue();
		}
	}
	public static int toInteger(BigDecimal value) {
		if (DefaultOptions.integerOverflowCheck) {
			if (value.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) > 0) {
				throw new IntegerValueIsTooBig();
			} else if (value.compareTo(BigDecimal.valueOf(Integer.MIN_VALUE)) < 0) {
				throw new IntegerValueIsTooSmall();
			} else {
				return value.intValue();
			}
		} else {
			return value.intValue();
		}
	}
	public static int toInteger(long value) {
		if (DefaultOptions.integerOverflowCheck) {
			if (value > Integer.MAX_VALUE) {
				throw new IntegerValueIsTooBig();
			} else if (value < Integer.MIN_VALUE) {
				throw new IntegerValueIsTooSmall();
			} else {
				return (int)value;
			}
		} else {
			return (int)value;
		}
	}
	public static char toCharacter(BigInteger value) {
		if (DefaultOptions.integerOverflowCheck) {
			if (value.compareTo(BigInteger.valueOf(Character.MAX_VALUE)) > 0) {
				throw new IntegerValueIsTooBig();
			} else if (value.compareTo(BigInteger.valueOf(Character.MIN_VALUE)) < 0) {
				throw new IntegerValueIsTooSmall();
			} else {
				return (char)value.intValue();
			}
		} else {
			return (char)value.intValue();
		}
	}
	public static long toLong(BigInteger value) {
		if (DefaultOptions.integerOverflowCheck) {
			if (value.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) {
				throw new IntegerValueIsTooBig();
			} else if (value.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0) {
				throw new IntegerValueIsTooSmall();
			} else {
				return value.longValue();
			}
		} else {
			return value.longValue();
		}
	}
	public static long toLong(BigDecimal value) {
		if (DefaultOptions.integerOverflowCheck) {
			if (value.compareTo(BigDecimal.valueOf(Long.MAX_VALUE)) > 0) {
				throw new IntegerValueIsTooBig();
			} else if (value.compareTo(BigDecimal.valueOf(Long.MIN_VALUE)) < 0) {
				throw new IntegerValueIsTooSmall();
			} else {
				return value.longValue();
			}
		} else {
			return value.longValue();
		}
	}
	public static boolean isSmallInteger(BigInteger value) {
		if (value.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
			return false;
		} else if (value.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) < 0) {
			return false;
		} else {
			return true;
		}
	}
	//
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.isInteger(value,cp);
	}
	// Comparison operations
	public void compareWithTerm(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		a.compareBigIntegerWith(value,iX,op);
	}
	public void compareWithBigInteger(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (!op.eval(value,a)) {
			throw Backtracking.instance;
		}
	}
	public void compareWithLong(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (!op.eval(value,BigInteger.valueOf(a))) {
			throw Backtracking.instance;
		}
	}
	public void compareWithDouble(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (!op.eval(value.doubleValue(),a)) {
			throw Backtracking.instance;
		}
	}
	public void compareWithDate(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (!op.eval(value.multiply(Converters.oneDayLengthBigInteger),BigInteger.valueOf(a))) {
			throw Backtracking.instance;
		}
	}
	public void compareTermWith(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		a.compareWithBigInteger(value,iX,op);
	}
	public void compareBigIntegerWith(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (!op.eval(a,value)) {
			throw Backtracking.instance;
		}
	}
	public void compareLongWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (!op.eval(BigInteger.valueOf(a),value)) {
			throw Backtracking.instance;
		}
	}
	public void compareDoubleWith(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (!op.eval(a,value.doubleValue())) {
			throw Backtracking.instance;
		}
	}
	public void compareDateWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (!op.eval(BigInteger.valueOf(a),value.multiply(Converters.oneDayLengthBigInteger))) {
			throw Backtracking.instance;
		}
	}
	public void compareListWith(Term aHead, Term aTail, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		aHead.compareWithBigInteger(value,iX,op);
		aTail.compareWithBigInteger(value,iX,op);
	}
	// Arithmetic operations
	public Term reactWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		return a.reactBigIntegerWith(value,iX,op);
	}
	public Term reactWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(value,a);
	}
	public Term reactWithLong(long a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(value,BigInteger.valueOf(a));
	}
	public Term reactWithDouble(double a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(value.doubleValue(),a);
	}
	public Term reactWithString(String a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(value,a);
	}
	public Term reactWithDate(long a, ChoisePoint iX, BinaryOperation op) {
		return op.evalDate(value.multiply(Converters.oneDayLengthBigInteger),BigInteger.valueOf(a));
	}
	public Term reactWithTime(long a, ChoisePoint iX, BinaryOperation op) {
		return op.evalTime(value,BigInteger.valueOf(a));
	}
	public Term reactBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(a,value);
	}
	public Term reactLongWith(long a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(BigInteger.valueOf(a),value);
	}
	public Term reactDoubleWith(double a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(a,value.doubleValue());
	}
	public Term reactStringWith(String a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(a,value);
	}
	public Term reactDateWith(long a, ChoisePoint iX, BinaryOperation op) {
		return op.evalDate(BigInteger.valueOf(a),value.multiply(Converters.oneDayLengthBigInteger));
	}
	public Term reactTimeWith(long a, ChoisePoint iX, BinaryOperation op) {
		return op.evalTime(BigInteger.valueOf(a),value);
	}
	public Term reactListWith(Term aHead, Term aTail, ChoisePoint iX, BinaryOperation op) {
		return new PrologList(
			aHead.reactWithBigInteger(value,iX,op),
			aTail.reactWithBigInteger(value,iX,op));
	}
	// Bitwise operations
	public Term blitWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		return a.blitBigIntegerWith(value,iX,op);
	}
	public Term blitWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(value,a);
	}
	public Term blitWithLong(long a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(value,BigInteger.valueOf(a));
	}
	public Term blitWithDouble(double a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(value,Converters.doubleValueToBigInteger(a));
	}
	public Term blitBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(a,value);
	}
	public Term blitLongWith(long a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(BigInteger.valueOf(a),value);
	}
	public Term blitDoubleWith(double a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(Converters.doubleValueToBigInteger(a),value);
	}
	public Term blitListWith(Term aHead, Term aTail, ChoisePoint iX, BinaryOperation op) {
		return new PrologList(
			aHead.blitWithBigInteger(value,iX,op),
			aTail.blitWithBigInteger(value,iX,op));
	}
	// Unary operations
	public Term evaluate(ChoisePoint iX, UnaryOperation op) {
		return op.eval(value);
	}
	// Converting Term to String
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, CharsetEncoder encoder) {
		return FormatOutput.integerToString(value);
	}
	// public String toString() {
	//	return FormatOutput.integerToString(value);
	// }
}
