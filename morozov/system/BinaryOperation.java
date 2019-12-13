// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.system.converters.*;
import morozov.system.errors.*;
import morozov.terms.*;

import java.math.BigInteger;

public enum BinaryOperation {
	PLUS {
		@Override
		public Term eval(BigInteger n1, BigInteger n2) {
			return new PrologInteger(n1.add(n2));
		}
		@Override
		public Term eval(double n1, double n2) {
			return new PrologReal(n1 + n2);
		}
		@Override
		public Term eval(String n1, String n2) {
			return new PrologString(n1 + n2);
		}
		@Override
		public Term eval(BigInteger n1, String n2) {
			int i1= n1.intValue();
			return new PrologString(String.format("%c%s",i1,n2));
		}
		@Override
		public Term eval(String n1, BigInteger n2) {
			int i2= n2.intValue();
			return new PrologString(String.format("%s%c",n1,i2));
		}
		@Override
		public Term eval(byte[] n1, byte[] n2) {
			int length1= n1.length;
			int length2= n2.length;
			int totalLength= length1 + length2;
			byte[] array= new byte[totalLength];
			System.arraycopy(n1,0,array,0,length1);
			System.arraycopy(n2,0,array,length1,length2);
			return new PrologBinary(array);
		}
		@Override
		public Term eval(BigInteger n1, byte[] n2) {
			int i1= n1.intValue();
			byte[] newArray= new byte[n2.length+1];
			newArray[0]= (byte)i1;
			System.arraycopy(n2,0,newArray,1,n2.length);
			return new PrologBinary(newArray);
		}
		@Override
		public Term eval(byte[] n1, BigInteger n2) {
			int i2= n2.intValue();
			byte[] newArray= new byte[n1.length+1];
			System.arraycopy(n1,0,newArray,0,n1.length);
			newArray[n1.length]= (byte)i2;
			return new PrologBinary(newArray);
		}
		@Override
		public Term evalDate(BigInteger n1, BigInteger n2) {
			return GeneralConverters.millisecondsToDate(n1.add(n2));
		}
		@Override
		public Term evalTime(BigInteger n1, BigInteger n2) {
			return GeneralConverters.millisecondsToTime(n1.add(n2));
		}
		@Override
		public Term evalDays(BigInteger n1, BigInteger n2) {
			return new PrologInteger((n1.add(n2)).divide(TimeUnitsConverters.oneDayLengthInMillisecondsBigInteger));
		}
	},
	MINUS {
		@Override
		public Term eval(BigInteger n1, BigInteger n2) {
			return new PrologInteger(n1.subtract(n2));
		}
		@Override
		public Term eval(double n1, double n2) {
			return new PrologReal(n1 - n2);
		}
		@Override
		public Term evalDate(BigInteger n1, BigInteger n2) {
			return GeneralConverters.millisecondsToDate(n1.subtract(n2));
		}
		@Override
		public Term evalTime(BigInteger n1, BigInteger n2) {
			return GeneralConverters.millisecondsToTime(n1.subtract(n2));
		}
		@Override
		public Term evalDays(BigInteger n1, BigInteger n2) {
			return new PrologInteger((n1.subtract(n2)).divide(TimeUnitsConverters.oneDayLengthInMillisecondsBigInteger));
		}
	},
	MULT {
		@Override
		public Term eval(BigInteger n1, BigInteger n2) {
			return new PrologInteger(n1.multiply(n2));
		}
		@Override
		public Term eval(double n1, double n2) {
			return new PrologReal(n1 * n2);
		}
	},
	SLASH {
		@Override
		public Term eval(BigInteger n1, BigInteger n2) {
			return new PrologReal(n1.doubleValue() / n2.doubleValue());
		}
		@Override
		public Term eval(double n1, double n2) {
			return new PrologReal(n1 / n2);
		}
	},
	DIV {
		@Override
		public Term eval(BigInteger n1, BigInteger n2) {
			return new PrologInteger(n1.divide(n2));
		}
		@Override
		public Term eval(double n1, double n2) {
			double division= n1 / n2;
			if (division > 0) {
				division= StrictMath.floor(division);
			} else {
				division= StrictMath.ceil(division);
			};
			return new PrologInteger(GeneralConverters.doubleValueToBigInteger(division));
		}
	},
	MOD {
		@Override
		public Term eval(BigInteger n1, BigInteger n2) {
			return new PrologInteger(n1.remainder(n2));
		}
		@Override
		public Term eval(double n1, double n2) {
				return new PrologReal(n1 % n2);
		}
	},
	POWER {
		@Override
		public Term eval(BigInteger n1, BigInteger n2) {
			return new PrologInteger(n1.pow(Arithmetic.toInteger(n2)));
		}
		@Override
		public Term eval(double n1, double n2) {
			return new PrologReal(StrictMath.pow(n1,n2));
		}
	},
	HYPOT {
		@Override
		public Term eval(BigInteger n1, BigInteger n2) {
			return new PrologReal(StrictMath.hypot(n1.doubleValue(),n2.doubleValue()));
		}
		@Override
		public Term eval(double n1, double n2) {
			return new PrologReal(StrictMath.hypot(n1,n2));
		}
	},
	ARCTAN2 {
		@Override
		public Term eval(BigInteger n1, BigInteger n2) {
			return new PrologReal(StrictMath.atan2(n1.doubleValue(),n2.doubleValue()));
		}
		@Override
		public Term eval(double n1, double n2) {
			return new PrologReal(StrictMath.atan2(n1,n2));
		}
	},
	BITAND {
		@Override
		public Term eval(BigInteger n1, BigInteger n2) {
			return new PrologInteger(n1.and(n2));
		}
	},
	BITOR {
		@Override
		public Term eval(BigInteger n1, BigInteger n2) {
			return new PrologInteger(n1.or(n2));
		}
	},
	BITXOR {
		@Override
		public Term eval(BigInteger n1, BigInteger n2) {
			return new PrologInteger(n1.xor(n2));
		}
	},
	BITRIGHT {
		@Override
		public Term eval(BigInteger n1, BigInteger n2) {
			return new PrologInteger(n1.shiftRight(Arithmetic.toInteger(n2)));
		}
	},
	BITLEFT {
		@Override
		public Term eval(BigInteger n1, BigInteger n2) {
			return new PrologInteger(n1.shiftLeft(Arithmetic.toInteger(n2)));
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
