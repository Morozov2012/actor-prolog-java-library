// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.system.converters.*;
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
		public Term eval(byte[] n1, byte[] n2) {
			int length1= n1.length;
			int length2= n2.length;
			int totalLength= length1 + length2;
			byte[] array= new byte[totalLength];
			for (int k=0; k < length1; k++) {
				array[k]= n1[k];
			};
			for (int k=0; k < length2; k++) {
				array[k+length1]= n2[k];
			};
			return new PrologBinary(array);
		}
		public Term eval(BigInteger n1, byte[] n2) {
			int i1= n1.intValue();
			byte[] newArray= new byte[n2.length+1];
			newArray[0]= (byte)i1;
			for (int k=0; k < n2.length; k++) {
				newArray[k+1]= n2[k];
			};
			return new PrologBinary(newArray);
		}
		public Term eval(byte[] n1, BigInteger n2) {
			int i2= n2.intValue();
			byte[] newArray= new byte[n1.length+1];
			for (int k=0; k < n1.length; k++) {
				newArray[k]= n1[k];
			};
			newArray[n1.length]= (byte)i2;
			return new PrologBinary(newArray);
		}
		public Term evalDate(BigInteger n1, BigInteger n2) {
			return GeneralConverters.millisecondsToDate(n1.add(n2));
		}
		public Term evalTime(BigInteger n1, BigInteger n2) {
			return GeneralConverters.millisecondsToTime(n1.add(n2));
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
			return GeneralConverters.millisecondsToDate(n1.subtract(n2));
		}
		public Term evalTime(BigInteger n1, BigInteger n2) {
			return GeneralConverters.millisecondsToTime(n1.subtract(n2));
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
			// return new PrologReal(StrictMath.rint(n1 / n2));
			double division= n1 / n2;
			if (division > 0) {
				division= StrictMath.floor(division);
			} else {
				division= StrictMath.ceil(division);
			};
			// return new PrologReal();
			return new PrologInteger(GeneralConverters.doubleValueToBigInteger(division));
		}
	},
	MOD {
		public Term eval(BigInteger n1, BigInteger n2) {
			return new PrologInteger(n1.remainder(n2));
		}
		public Term eval(double n1, double n2) {
			// return new PrologReal(StrictMath.IEEEremainder(n1,n2));
			return new PrologReal(n1 % n2);
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
	ARCTAN2 {
		public Term eval(BigInteger n1, BigInteger n2) {
			return new PrologReal(StrictMath.atan2(n1.doubleValue(),n2.doubleValue()));
		}
		public Term eval(double n1, double n2) {
			return new PrologReal(StrictMath.atan2(n1,n2));
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
	public Term eval(byte[] n1, byte[] n2) {
		throw new IllegalTypesOfArgumentsInBinaryOperation();
	}
	public Term eval(BigInteger n1, byte[] n2) {
		throw new IllegalTypesOfArgumentsInBinaryOperation();
	}
	public Term eval(byte[] n1, BigInteger n2) {
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
