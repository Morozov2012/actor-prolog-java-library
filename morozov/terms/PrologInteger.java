// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import target.*;

import morozov.system.*;

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
			throw new Backtracking();
	}
	public void isInteger(BigInteger v, ChoisePoint cp) throws Backtracking {
		if (!value.equals(v))
			throw new Backtracking();
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
	// Converting Term to String
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, CharsetEncoder encoder) {
		return FormatOutput.integerToString(value);
	}
	// public String toString() {
	//	return FormatOutput.integerToString(value);
	// }
}
