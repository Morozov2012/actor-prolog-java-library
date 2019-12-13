// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.system.converters.*;
import morozov.system.converters.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.math.BigInteger;

public enum UnaryOperation {
	MINUS {
		@Override
		public Term eval(BigInteger n1) {
			return new PrologInteger(n1.negate());
		}
		@Override
		public Term eval(double n1) {
			return new PrologReal(-n1);
		}
	},
	INC {
		@Override
		public Term eval(BigInteger n1) {
			return new PrologInteger(n1.add(BigInteger.ONE));
		}
		@Override
		public Term eval(double n1) {
			return new PrologReal(++n1);
		}
	},
	DEC {
		@Override
		public Term eval(BigInteger n1) {
			return new PrologInteger(n1.subtract(BigInteger.ONE));
		}
		@Override
		public Term eval(double n1) {
			return new PrologReal(--n1);
		}
	},
	RANDOM {
		@Override
		public Term eval(BigInteger n1) {
			int i= n1.intValue();
			return new PrologInteger(RandomNumberGenerator.generator.nextInt(i));
		}
		@Override
		public Term eval(double n1) {
			return new PrologInteger(RandomNumberGenerator.generator.nextInt((int)(StrictMath.round(n1))));
		}
	},
	ABS {
		@Override
		public Term eval(BigInteger n1) {
			return new PrologInteger(n1.abs());
		}
		@Override
		public Term eval(double n1) {
			return new PrologReal(StrictMath.abs(n1));
		}
	},
	ROUND {
		@Override
		public Term eval(BigInteger n1) {
			return new PrologInteger(n1);
		}
		@Override
		public Term eval(double n1) {
			return new PrologInteger((long)(StrictMath.round(n1)));
		}
	},
	TRUNC {
		@Override
		public Term eval(BigInteger n1) {
			return new PrologInteger(n1);
		}
		@Override
		public Term eval(double n1) {
			return new PrologInteger((long)n1);
		}
	},
	SQRT {
		@Override
		public Term eval(BigInteger n1) {
			return new PrologReal(StrictMath.sqrt(n1.doubleValue()));
		}
		@Override
		public Term eval(double n1) {
			return new PrologReal(StrictMath.sqrt(n1));
		}
	},
	LN {
		@Override
		public Term eval(BigInteger n1) {
			return new PrologReal(StrictMath.log(n1.doubleValue()));
		}
		@Override
		public Term eval(double n1) {
			return new PrologReal(StrictMath.log(n1));
		}
	},
	LOG10 {
		@Override
		public Term eval(BigInteger n1) {
			return new PrologReal(StrictMath.log10(n1.doubleValue()));
		}
		@Override
		public Term eval(double n1) {
			return new PrologReal(StrictMath.log10(n1));
		}
	},
	EXP {
		@Override
		public Term eval(BigInteger n1) {
			return new PrologReal(StrictMath.exp(n1.doubleValue()));
		}
		@Override
		public Term eval(double n1) {
			return new PrologReal(StrictMath.exp(n1));
		}
	},
	SIN {
		@Override
		public Term eval(BigInteger n1) {
			return new PrologReal(StrictMath.sin(n1.doubleValue()));
		}
		@Override
		public Term eval(double n1) {
			return new PrologReal(StrictMath.sin(n1));
		}
	},
	COS {
		@Override
		public Term eval(BigInteger n1) {
			return new PrologReal(StrictMath.cos(n1.doubleValue()));
		}
		@Override
		public Term eval(double n1) {
			return new PrologReal(StrictMath.cos(n1));
		}
	},
	TAN {
		@Override
		public Term eval(BigInteger n1) {
			return new PrologReal(StrictMath.tan(n1.doubleValue()));
		}
		@Override
		public Term eval(double n1) {
			return new PrologReal(StrictMath.tan(n1));
		}
	},
	ARCSIN {
		@Override
		public Term eval(BigInteger n1) {
			return new PrologReal(StrictMath.asin(n1.doubleValue()));
		}
		@Override
		public Term eval(double n1) {
			return new PrologReal(StrictMath.asin(n1));
		}
	},
	ARCCOS {
		@Override
		public Term eval(BigInteger n1) {
			return new PrologReal(StrictMath.acos(n1.doubleValue()));
		}
		@Override
		public Term eval(double n1) {
			return new PrologReal(StrictMath.acos(n1));
		}
	},
	ARCTAN {
		@Override
		public Term eval(BigInteger n1) {
			return new PrologReal(StrictMath.atan(n1.doubleValue()));
		}
		@Override
		public Term eval(double n1) {
			return new PrologReal(StrictMath.atan(n1));
		}
	},
	SIGNUM {
		@Override
		public Term eval(BigInteger n1) {
			return new PrologInteger(n1.signum());
		}
		@Override
		public Term eval(double n1) {
			return new PrologInteger((int)StrictMath.signum(n1));
		}
	},
	BITNOT {
		@Override
		public Term eval(BigInteger n1) {
			return new PrologInteger(n1.not());
		}
		@Override
		public Term eval(double n1) {
			try {
				BigInteger value= GeneralConverters.doubleToBigInteger(n1);
				return new PrologInteger(value.not());
			} catch (TermIsNotAReal e) {
				throw new WrongArgumentIsNotNumerical(new PrologReal(n1));
			}
		}
	};
	abstract public Term eval(BigInteger n1);
	abstract public Term eval(double n1);
}
