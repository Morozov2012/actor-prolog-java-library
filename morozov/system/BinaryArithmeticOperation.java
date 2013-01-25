// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.terms.*;

import java.math.BigInteger;

public enum BinaryArithmeticOperation {
	PLUS {
		Term eval(BigInteger n1, BigInteger n2) {
			return new PrologInteger(n1.add(n2));
		}
		Term eval(double n1, double n2) {
			return new PrologReal(n1 + n2);
		}
		Term eval(String n1, String n2) {
			return new PrologString(n1 + n2);
		}
		Term eval(BigInteger n1, String n2) {
			int i1= n1.intValue();
			return new PrologString(String.format("%c%s",i1,n2));
		}
		Term eval(String n1, BigInteger n2) {
			int i2= n2.intValue();
			return new PrologString(String.format("%s%c",n1,i2));
		}
		Term evalDate(BigInteger n1, BigInteger n2) {
			return Converters.integerToDate(n1.add(n2));
		}
		Term evalTime(BigInteger n1, BigInteger n2) {
			return Converters.integerToTime(n1.add(n2));
		}
		Term evalDays(BigInteger n1, BigInteger n2) {
			throw new IllegalTypesOfArgumentsInBinaryArithmeticOperation();
		}
	},
	MINUS {
		Term eval(BigInteger n1, BigInteger n2) {
			return new PrologInteger(n1.subtract(n2));
		}
		Term eval(double n1, double n2) {
			return new PrologReal(n1 - n2);
		}
		Term eval(String n1, String n2) {
			throw new IllegalTypesOfArgumentsInBinaryArithmeticOperation();
		}
		Term eval(BigInteger n1, String n2) {
			throw new IllegalTypesOfArgumentsInBinaryArithmeticOperation();
		}
		Term eval(String n1, BigInteger n2) {
			throw new IllegalTypesOfArgumentsInBinaryArithmeticOperation();
		}
		Term evalDate(BigInteger n1, BigInteger n2) {
			throw new IllegalTypesOfArgumentsInBinaryArithmeticOperation();
		}
		Term evalTime(BigInteger n1, BigInteger n2) {
			throw new IllegalTypesOfArgumentsInBinaryArithmeticOperation();
		}
		Term evalDays(BigInteger n1, BigInteger n2) {
			return Converters.integerToDays(n1.subtract(n2));
		}
	},
	MULT {
		Term eval(BigInteger n1, BigInteger n2) {
			return new PrologInteger(n1.multiply(n2));
		}
		Term eval(double n1, double n2) {
			return new PrologReal(n1 * n2);
		}
		Term eval(String n1, String n2) {
			throw new IllegalTypesOfArgumentsInBinaryArithmeticOperation();
		}
		Term eval(BigInteger n1, String n2) {
			throw new IllegalTypesOfArgumentsInBinaryArithmeticOperation();
		}
		Term eval(String n1, BigInteger n2) {
			throw new IllegalTypesOfArgumentsInBinaryArithmeticOperation();
		}
		Term evalDate(BigInteger n1, BigInteger n2) {
			throw new IllegalTypesOfArgumentsInBinaryArithmeticOperation();
		}
		Term evalTime(BigInteger n1, BigInteger n2) {
			throw new IllegalTypesOfArgumentsInBinaryArithmeticOperation();
		}
		Term evalDays(BigInteger n1, BigInteger n2) {
			throw new IllegalTypesOfArgumentsInBinaryArithmeticOperation();
		}
	},
	SLASH {
		Term eval(BigInteger n1, BigInteger n2) {
			return new PrologReal(n1.doubleValue() / n2.doubleValue());
		}
		Term eval(double n1, double n2) {
			return new PrologReal(n1 / n2);
		}
		Term eval(String n1, String n2) {
			throw new IllegalTypesOfArgumentsInBinaryArithmeticOperation();
		}
		Term eval(BigInteger n1, String n2) {
			throw new IllegalTypesOfArgumentsInBinaryArithmeticOperation();
		}
		Term eval(String n1, BigInteger n2) {
			throw new IllegalTypesOfArgumentsInBinaryArithmeticOperation();
		}
		Term evalDate(BigInteger n1, BigInteger n2) {
			throw new IllegalTypesOfArgumentsInBinaryArithmeticOperation();
		}
		Term evalTime(BigInteger n1, BigInteger n2) {
			throw new IllegalTypesOfArgumentsInBinaryArithmeticOperation();
		}
		Term evalDays(BigInteger n1, BigInteger n2) {
			throw new IllegalTypesOfArgumentsInBinaryArithmeticOperation();
		}
	},
	DIV {
		Term eval(BigInteger n1, BigInteger n2) {
			return new PrologInteger(n1.divide(n2));
		}
		Term eval(double n1, double n2) {
			return new PrologReal(StrictMath.rint(n1 / n2));
		}
		Term eval(String n1, String n2) {
			throw new IllegalTypesOfArgumentsInBinaryArithmeticOperation();
		}
		Term eval(BigInteger n1, String n2) {
			throw new IllegalTypesOfArgumentsInBinaryArithmeticOperation();
		}
		Term eval(String n1, BigInteger n2) {
			throw new IllegalTypesOfArgumentsInBinaryArithmeticOperation();
		}
		Term evalDate(BigInteger n1, BigInteger n2) {
			throw new IllegalTypesOfArgumentsInBinaryArithmeticOperation();
		}
		Term evalTime(BigInteger n1, BigInteger n2) {
			throw new IllegalTypesOfArgumentsInBinaryArithmeticOperation();
		}
		Term evalDays(BigInteger n1, BigInteger n2) {
			throw new IllegalTypesOfArgumentsInBinaryArithmeticOperation();
		}
	},
	MOD {
		Term eval(BigInteger n1, BigInteger n2) {
			return new PrologInteger(n1.remainder(n2));
		}
		Term eval(double n1, double n2) {
			return new PrologReal(StrictMath.IEEEremainder(n1,n2));
		}
		Term eval(String n1, String n2) {
			throw new IllegalTypesOfArgumentsInBinaryArithmeticOperation();
		}
		Term eval(BigInteger n1, String n2) {
			throw new IllegalTypesOfArgumentsInBinaryArithmeticOperation();
		}
		Term eval(String n1, BigInteger n2) {
			throw new IllegalTypesOfArgumentsInBinaryArithmeticOperation();
		}
		Term evalDate(BigInteger n1, BigInteger n2) {
			throw new IllegalTypesOfArgumentsInBinaryArithmeticOperation();
		}
		Term evalTime(BigInteger n1, BigInteger n2) {
			throw new IllegalTypesOfArgumentsInBinaryArithmeticOperation();
		}
		Term evalDays(BigInteger n1, BigInteger n2) {
			throw new IllegalTypesOfArgumentsInBinaryArithmeticOperation();
		}
	};
	abstract Term eval(BigInteger n1, BigInteger n2);
	abstract Term eval(double n1, double n2);
	abstract Term eval(String n1, String n2);
	abstract Term eval(BigInteger n1, String n2);
	abstract Term eval(String n1, BigInteger n2);
	abstract Term evalDate(BigInteger n1, BigInteger n2);
	abstract Term evalTime(BigInteger n1, BigInteger n2);
	abstract Term evalDays(BigInteger n1, BigInteger n2);
}
