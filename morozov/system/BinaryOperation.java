// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.system.errors.*;
import morozov.terms.*;

import java.math.BigInteger;

public enum BinaryOperation {
	PLUS {
		public Term eval(BigInteger n1, BigInteger n2) {
			return new PrologInteger(n1.add(n2));
		}
		public Term eval(double n1, double n2) {
			return new PrologReal(n1 + n2);
		}
		public Term eval(String n1, String n2) {
			return new PrologString(n1 + n2);
		}
		public Term eval(BigInteger n1, String n2) {
			int i1= n1.intValue();
			return new PrologString(String.format("%c%s",i1,n2));
		}
		public Term eval(String n1, BigInteger n2) {
			int i2= n2.intValue();
			return new PrologString(String.format("%s%c",n1,i2));
		}
		public Term evalDate(BigInteger n1, BigInteger n2) {
			return Converters.millisecondsToDate(n1.add(n2));
		}
		public Term evalTime(BigInteger n1, BigInteger n2) {
			return Converters.millisecondsToTime(n1.add(n2));
		}
		public Term evalDays(BigInteger n1, BigInteger n2) {
			return new PrologInteger((n1.add(n2)).divide(TimeUnits.oneDayLengthInMillisecondsBigInteger));
		}
	},
	MINUS {
		public Term eval(BigInteger n1, BigInteger n2) {
			return new PrologInteger(n1.subtract(n2));
		}
		public Term eval(double n1, double n2) {
			return new PrologReal(n1 - n2);
		}
		public Term evalDate(BigInteger n1, BigInteger n2) {
			return Converters.millisecondsToDate(n1.subtract(n2));
		}
		public Term evalTime(BigInteger n1, BigInteger n2) {
			return Converters.millisecondsToTime(n1.subtract(n2));
		}
		public Term evalDays(BigInteger n1, BigInteger n2) {
			return new PrologInteger((n1.subtract(n2)).divide(TimeUnits.oneDayLengthInMillisecondsBigInteger));
		}
	},
	MULT {
		public Term eval(BigInteger n1, BigInteger n2) {
			return new PrologInteger(n1.multiply(n2));
		}
		public Term eval(double n1, double n2) {
			return new PrologReal(n1 * n2);
		}
	},
	SLASH {
		public Term eval(BigInteger n1, BigInteger n2) {
			return new PrologReal(n1.doubleValue() / n2.doubleValue());
		}
		public Term eval(double n1, double n2) {
			return new PrologReal(n1 / n2);
		}
	},
	DIV {
		public Term eval(BigInteger n1, BigInteger n2) {
			return new PrologInteger(n1.divide(n2));
		}
		public Term eval(double n1, double n2) {
			return new PrologReal(StrictMath.rint(n1 / n2));
		}
	},
	MOD {
		public Term eval(BigInteger n1, BigInteger n2) {
			return new PrologInteger(n1.remainder(n2));
		}
		public Term eval(double n1, double n2) {
			return new PrologReal(StrictMath.IEEEremainder(n1,n2));
		}
	},
	POWER {
		public Term eval(BigInteger n1, BigInteger n2) {
			return new PrologInteger(n1.pow(PrologInteger.toInteger(n2)));
		}
		public Term eval(double n1, double n2) {
			return new PrologReal(StrictMath.pow(n1,n2));
		}
	},
	HYPOT {
		public Term eval(BigInteger n1, BigInteger n2) {
			return new PrologReal(StrictMath.hypot(n1.doubleValue(),n2.doubleValue()));
		}
		public Term eval(double n1, double n2) {
			return new PrologReal(StrictMath.hypot(n1,n2));
		}
	},
	BITAND {
		public Term eval(BigInteger n1, BigInteger n2) {
			return new PrologInteger(n1.and(n2));
		}
	},
	BITOR {
		public Term eval(BigInteger n1, BigInteger n2) {
			return new PrologInteger(n1.or(n2));
		}
	},
	BITXOR {
		public Term eval(BigInteger n1, BigInteger n2) {
			return new PrologInteger(n1.xor(n2));
		}
	},
	BITRIGHT {
		public Term eval(BigInteger n1, BigInteger n2) {
			return new PrologInteger(n1.shiftRight(PrologInteger.toInteger(n2)));
		}
	},
	BITLEFT {
		public Term eval(BigInteger n1, BigInteger n2) {
			return new PrologInteger(n1.shiftLeft(PrologInteger.toInteger(n2)));
		}
	};
	abstract public Term eval(BigInteger n1, BigInteger n2);
	public Term eval(double n1, double n2) {
		throw new IllegalTypesOfArgumentsInBinaryOperation();
	}
	public Term eval(String n1, String n2) {
		throw new IllegalTypesOfArgumentsInBinaryOperation();
	}
	public Term eval(BigInteger n1, String n2) {
		throw new IllegalTypesOfArgumentsInBinaryOperation();
	}
	public Term eval(String n1, BigInteger n2) {
		throw new IllegalTypesOfArgumentsInBinaryOperation();
	}
	public Term evalDate(BigInteger n1, BigInteger n2) {
		throw new IllegalTypesOfArgumentsInBinaryOperation();
	}
	public Term evalTime(BigInteger n1, BigInteger n2) {
		throw new IllegalTypesOfArgumentsInBinaryOperation();
	}
	public Term evalDays(BigInteger n1, BigInteger n2) {
		throw new IllegalTypesOfArgumentsInBinaryOperation();
	}
}
