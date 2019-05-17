// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.converters;

import morozov.run.*;
import morozov.system.*;
import morozov.system.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.math.BigInteger;

public class NumericalValueConverters {
	//
	protected static BigInteger bigIntegerNine= BigInteger.valueOf(9);
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
	public static Term toTerm(NumericalValue value) {
		if (value.useDoubleValue()) {
			return new PrologReal(value.getDoubleValue());
		} else {
			return new PrologInteger(value.getIntegerValue());
		}
	}
	//
	public static int toInteger(NumericalValue value) {
		if (value.useDoubleValue()) {
			return PrologInteger.toInteger(value.getDoubleValue());
		} else {
			return PrologInteger.toInteger(value.getIntegerValue());
		}
	}
	//
	public static long toLong(NumericalValue value) {
		if (value.useDoubleValue()) {
			return PrologInteger.toLong(value.getDoubleValue());
		} else {
			return PrologInteger.toLong(value.getIntegerValue());
		}
	}
	//
	public static long toLong(NumericalValue value, long coefficient) {
		if (value.useDoubleValue()) {
			return PrologInteger.toLong(value.getDoubleValue() * coefficient);
		} else {
			return PrologInteger.toLong(value.getIntegerValue().multiply(BigInteger.valueOf(coefficient)));
		}
	}
	//
	public static double toDouble(NumericalValue value) {
		if (value.useDoubleValue()) {
			return value.getDoubleValue();
		} else {
			return value.getIntegerValue().doubleValue();
		}
	}
	//
	public static float toFloat(NumericalValue value) {
		if (value.useDoubleValue()) {
			return (float)value.getDoubleValue();
		} else {
			return value.getIntegerValue().floatValue();
		}
	}
	//
	public static String toString(NumericalValue value) {
		if (value.useDoubleValue()) {
			return Double.toString(value.getDoubleValue());
		} else {
			return value.getIntegerValue().toString();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static String applyRadix(int radix, int fractionPartLength, NumericalValue number) {
		if (number.useDoubleValue()) {
//=====================================================================
double sourceValue= number.getDoubleValue();
double absoluteValue;
boolean isNegative= false;
if (sourceValue < 0) {
	isNegative= true;
	absoluteValue= -sourceValue;
} else {
	absoluteValue= sourceValue;
};
int exponent= 0;
while (true) {
	if (absoluteValue > radix) {
		absoluteValue= absoluteValue / radix;
		exponent++;
	} else if (absoluteValue < 1 && absoluteValue > 0) {
		absoluteValue= absoluteValue * radix;
		exponent--;
	} else {
		break;
	}
};
double normalizedValue= absoluteValue;
StringBuffer buffer= new StringBuffer();
int counter= 0;
while (true) {
	if (normalizedValue < 1) {
		break;
	} else {
		int remainder= (int)(normalizedValue % radix);
		normalizedValue= normalizedValue / radix;
		String c= toExtendedNumber(remainder);
		buffer.insert(0,c);
	};
	counter++;
};
if (counter <= 0) {
	buffer.insert(0,"0");
};
buffer.append(".");
normalizedValue= absoluteValue;
counter= 0;
while (fractionPartLength > 0) {
	if (normalizedValue < 1) {
		break;
	} else {
		int v= ((int)(normalizedValue*radix))-((int)normalizedValue*radix);
		String c= toExtendedNumber(v);
		normalizedValue= (normalizedValue % radix) * radix;
		buffer.append(c);
		fractionPartLength--;
	};
	counter++;
};
if (counter <= 0) {
	buffer.append("0");
};
int lastZeroIndex= -1;
for (int k=buffer.length()-1; k >= 0; k--) {
	if (buffer.charAt(k)=='0') {
		if (k > 0 && buffer.charAt(k-1)=='.') {
			break;
		} else {
			lastZeroIndex= k;
		}
	} else {
		break;
	}
};
if (lastZeroIndex > 0) {
	buffer.delete(lastZeroIndex,buffer.length());
};
buffer.insert(0,"#");
buffer.insert(0,radix);
if (isNegative) {
	buffer.insert(0,"-");
};
buffer.append("#");
if (exponent != 0) {
	buffer.append("E");
	buffer.append(exponent);
};
return buffer.toString();
//=====================================================================
		} else {
//=====================================================================
BigInteger bigRadix= BigInteger.valueOf(radix);
BigInteger sourceValue= number.getIntegerValue();
BigInteger absoluteValue;
boolean isNegative= false;
if (sourceValue.signum() < 0) {
	isNegative= true;
	absoluteValue= sourceValue.negate();
} else {
	absoluteValue= sourceValue.abs();
};
int exponent= 0;
while (absoluteValue.compareTo(BigInteger.ZERO) > 0) {
	if (absoluteValue.remainder(bigRadix).equals(BigInteger.ZERO)) {
		absoluteValue= absoluteValue.divide(bigRadix);
		exponent++;
	} else {
		break;
	}
};
StringBuffer buffer= new StringBuffer();
int counter= 0;
while (true) {
	if (absoluteValue.equals(BigInteger.ZERO)) {
		break;
	} else {
		BigInteger remainder= absoluteValue.remainder(bigRadix);
		absoluteValue= absoluteValue.divide(bigRadix);
		String c= toExtendedNumber(remainder);
		buffer.insert(0,c);
	};
	counter++;
};
if (counter <= 0) {
	buffer.insert(0,"0");
};
buffer.insert(0,"#");
buffer.insert(0,radix);
if (isNegative) {
	buffer.insert(0,"-");
};
buffer.append("#");
if (exponent > 0) {
	buffer.append("E");
	buffer.append(exponent);
};
return buffer.toString();
//=====================================================================
		}
	}
	//
	protected static String toExtendedNumber(int number) {
		return Integer.toString(number,Character.MAX_RADIX).toUpperCase();
	}
	protected static String toExtendedNumber(BigInteger number) {
		return number.toString(Character.MAX_RADIX).toUpperCase();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static String normalizeNumber(int fractionPartLength, NumericalValue number) {
		if (number.useDoubleValue()) {
//=====================================================================
int radix= 10;
double sourceValue= number.getDoubleValue();
double absoluteValue;
boolean isNegative= false;
if (sourceValue < 0) {
	isNegative= true;
	absoluteValue= -sourceValue;
} else {
	absoluteValue= sourceValue;
};
int exponent= 0;
while (true) {
	if (absoluteValue > radix) {
		absoluteValue= absoluteValue / radix;
		exponent++;
	} else if (absoluteValue < 1 && absoluteValue > 0) {
		absoluteValue= absoluteValue * radix;
		exponent--;
	} else {
		break;
	}
};
double normalizedValue= absoluteValue;
StringBuffer buffer= new StringBuffer();
int counter= 0;
while (true) {
	if (normalizedValue < 1) {
		break;
	} else {
		int remainder= (int)(normalizedValue % radix);
		normalizedValue= normalizedValue / radix;
		String c= toExtendedNumber(remainder);
		buffer.insert(0,c);
	};
	counter++;
};
if (counter <= 0) {
	buffer.insert(0,"0");
};
buffer.append(".");
normalizedValue= absoluteValue;
counter= 0;
while (fractionPartLength > 0) {
	if (normalizedValue < 1) {
		break;
	} else {
		int v= ((int)(normalizedValue*radix))-((int)normalizedValue*radix);
		String c= toExtendedNumber(v);
		normalizedValue= (normalizedValue % radix) * radix;
		buffer.append(c);
		fractionPartLength--;
	};
	counter++;
};
if (counter <= 0) {
	buffer.append("0");
};
int lastZeroIndex= -1;
for (int k=buffer.length()-1; k >= 0; k--) {
	if (buffer.charAt(k)=='0') {
		if (k > 0 && buffer.charAt(k-1)=='.') {
			break;
		} else {
			lastZeroIndex= k;
		}
	} else {
		break;
	}
};
if (lastZeroIndex > 0) {
	buffer.delete(lastZeroIndex,buffer.length());
};
if (isNegative) {
	buffer.insert(0,"-");
};
if (exponent != 0) {
	buffer.append("E");
	buffer.append(exponent);
};
return buffer.toString();
//=====================================================================
		} else {
//=====================================================================
			return number.getIntegerValue().toString();
//=====================================================================
		}
	}
}
