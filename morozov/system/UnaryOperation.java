// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.system.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.math.BigInteger;
import java.util.Random;
import java.security.SecureRandom;

class RandomNumberGenerator {
	static final Random generator= new SecureRandom();
}

public enum UnaryOperation {
	MINUS {
		public Term eval(BigInteger n1) {
			return new PrologInteger(n1.negate());
		}
		public Term eval(double n1) {
			return new PrologReal(-n1);
		}
	},
	INC {
		public Term eval(BigInteger n1) {
			return new PrologInteger(n1.add(BigInteger.ONE));
		}
		public Term eval(double n1) {
			return new PrologReal(++n1);
		}
	},
	DEC {
		public Term eval(BigInteger n1) {
			return new PrologInteger(n1.subtract(BigInteger.ONE));
		}
		public Term eval(double n1) {
			return new PrologReal(--n1);
		}
	},
	RANDOM {
		public Term eval(BigInteger n1) {
			int i= n1.intValue();
			return new PrologInteger(RandomNumberGenerator.generator.nextInt(i));
		}
		public Term eval(double n1) {
			return new PrologInteger(RandomNumberGenerator.generator.nextInt((int)(StrictMath.round(n1))));
		}
	},
	ABS {
		public Term eval(BigInteger n1) {
			return new PrologInteger(n1.abs());
		}
		public Term eval(double n1) {
			return new PrologReal(StrictMath.abs(n1));
		}
	},
	ROUND {
		public Term eval(BigInteger n1) {
			return new PrologInteger(n1);
		}
		public Term eval(double n1) {
			return new PrologInteger((long)(StrictMath.round(n1)));
		}
	},
	TRUNC {
		public Term eval(BigInteger n1) {
			return new PrologInteger(n1);
		}
		public Term eval(double n1) {
			return new PrologInteger((long)n1);
		}
	},
	SQRT {
		public Term eval(BigInteger n1) {
			return new PrologReal(StrictMath.sqrt(n1.doubleValue()));
		}
		public Term eval(double n1) {
			return new PrologReal(StrictMath.sqrt(n1));
		}
	},
	LN {
		public Term eval(BigInteger n1) {
			return new PrologReal(StrictMath.log(n1.doubleValue()));
		}
		public Term eval(double n1) {
			return new PrologReal(StrictMath.log(n1));
		}
	},
	LOG10 {
		public Term eval(BigInteger n1) {
			return new PrologReal(StrictMath.log10(n1.doubleValue()));
		}
		public Term eval(double n1) {
			return new PrologReal(StrictMath.log10(n1));
		}
	},
	EXP {
		public Term eval(BigInteger n1) {
			return new PrologReal(StrictMath.exp(n1.doubleValue()));
		}
		public Term eval(double n1) {
			return new PrologReal(StrictMath.exp(n1));
		}
	},
	SIN {
		public Term eval(BigInteger n1) {
			return new PrologReal(StrictMath.sin(n1.doubleValue()));
		}
		public Term eval(double n1) {
			return new PrologReal(StrictMath.sin(n1));
		}
	},
	COS {
		public Term eval(BigInteger n1) {
			return new PrologReal(StrictMath.cos(n1.doubleValue()));
		}
		public Term eval(double n1) {
			return new PrologReal(StrictMath.cos(n1));
		}
	},
	TAN {
		public Term eval(BigInteger n1) {
			return new PrologReal(StrictMath.tan(n1.doubleValue()));
		}
		public Term eval(double n1) {
			return new PrologReal(StrictMath.tan(n1));
		}
	},
	ARCTAN {
		public Term eval(BigInteger n1) {
			return new PrologReal(StrictMath.atan(n1.doubleValue()));
		}
		public Term eval(double n1) {
			return new PrologReal(StrictMath.atan(n1));
		}
	},
	SIGNUM {
		public Term eval(BigInteger n1) {
			return new PrologInteger(n1.signum());
		}
		public Term eval(double n1) {
			return new PrologInteger((int)StrictMath.signum(n1));
		}
	},
	BITNOT {
		public Term eval(BigInteger n1) {
			return new PrologInteger(n1.not());
		}
		public Term eval(double n1) {
			try {
				BigInteger value= Converters.doubleToBigInteger(n1);
				return new PrologInteger(value.not());
			} catch (TermIsNotAReal e) {
				throw new WrongArgumentIsNotNumerical(new PrologReal(n1));
			}
		}
	};
	public abstract Term eval(BigInteger n1);
	public abstract Term eval(double n1);
}
