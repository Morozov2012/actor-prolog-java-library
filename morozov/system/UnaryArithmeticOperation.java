// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.terms.*;

import java.math.BigInteger;
import java.util.Random;
import java.security.SecureRandom;

class RandomNumberGenerator {
	static final Random generator= new SecureRandom();
}

public enum UnaryArithmeticOperation {
	MINUS {
		Term eval(BigInteger n1) {
			return new PrologInteger(n1.negate());
		}
		Term eval(double n1) {
			return new PrologReal(-n1);
		}
	},
	INC {
		Term eval(BigInteger n1) {
			return new PrologInteger(n1.add(BigInteger.ONE));
		}
		Term eval(double n1) {
			return new PrologReal(++n1);
		}
	},
	DEC {
		Term eval(BigInteger n1) {
			return new PrologInteger(n1.subtract(BigInteger.ONE));
		}
		Term eval(double n1) {
			return new PrologReal(--n1);
		}
	},
	RANDOM {
		Term eval(BigInteger n1) {
			int i= n1.intValue();
			return new PrologInteger(RandomNumberGenerator.generator.nextInt(i));
		}
		Term eval(double n1) {
			return new PrologInteger(RandomNumberGenerator.generator.nextInt((int)(StrictMath.round(n1))));
		}
	},
	ABS {
		Term eval(BigInteger n1) {
			return new PrologInteger(n1.abs());
		}
		Term eval(double n1) {
			return new PrologReal(StrictMath.abs(n1));
		}
	},
	ROUND {
		Term eval(BigInteger n1) {
			return new PrologInteger(n1);
		}
		Term eval(double n1) {
			return new PrologInteger((long)(StrictMath.round(n1)));
		}
	},
	TRUNC {
		Term eval(BigInteger n1) {
			return new PrologInteger(n1);
		}
		Term eval(double n1) {
			return new PrologInteger((long)n1);
		}
	},
	SQRT {
		Term eval(BigInteger n1) {
			return new PrologReal(StrictMath.sqrt(n1.doubleValue()));
		}
		Term eval(double n1) {
			return new PrologReal(StrictMath.sqrt(n1));
		}
	},
	LN {
		Term eval(BigInteger n1) {
			return new PrologReal(StrictMath.log(n1.doubleValue()));
		}
		Term eval(double n1) {
			return new PrologReal(StrictMath.log(n1));
		}
	},
	LOG {
		Term eval(BigInteger n1) {
			return new PrologReal(StrictMath.log10(n1.doubleValue()));
		}
		Term eval(double n1) {
			return new PrologReal(StrictMath.log10(n1));
		}
	},
	EXP {
		Term eval(BigInteger n1) {
			return new PrologReal(StrictMath.exp(n1.doubleValue()));
		}
		Term eval(double n1) {
			return new PrologReal(StrictMath.exp(n1));
		}
	},
	SIN {
		Term eval(BigInteger n1) {
			return new PrologReal(StrictMath.sin(n1.doubleValue()));
		}
		Term eval(double n1) {
			return new PrologReal(StrictMath.sin(n1));
		}
	},
	COS {
		Term eval(BigInteger n1) {
			return new PrologReal(StrictMath.cos(n1.doubleValue()));
		}
		Term eval(double n1) {
			return new PrologReal(StrictMath.cos(n1));
		}
	},
	TAN {
		Term eval(BigInteger n1) {
			return new PrologReal(StrictMath.tan(n1.doubleValue()));
		}
		Term eval(double n1) {
			return new PrologReal(StrictMath.tan(n1));
		}
	},
	ARCTAN {
		Term eval(BigInteger n1) {
			return new PrologReal(StrictMath.atan(n1.doubleValue()));
		}
		Term eval(double n1) {
			return new PrologReal(StrictMath.atan(n1));
		}
	};
	abstract Term eval(BigInteger n1);
	abstract Term eval(double n1);
}
