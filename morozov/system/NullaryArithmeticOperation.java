// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.terms.*;

public enum NullaryArithmeticOperation {
	NOT_A_NUMBER {
		public Term eval() {
			return new PrologReal(Double.NaN);
		}
	},
	POSITIVE_INFINITY {
		public Term eval() {
			return new PrologReal(Double.POSITIVE_INFINITY);
		}
	},
	NEGATIVE_INFINITY {
		public Term eval() {
			return new PrologReal(Double.NEGATIVE_INFINITY);
		}
	},
	RANDOM {
		public Term eval() {
			return new PrologReal(StrictMath.random());
		}
	},
	PI {
		public Term eval() {
			return new PrologReal(StrictMath.PI);
		}
	};
	abstract public Term eval();
}
