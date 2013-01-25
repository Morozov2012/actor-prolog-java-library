// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.terms.*;

public enum NullaryArithmeticOperation {
	RANDOM {
		Term eval() {
			return new PrologReal(StrictMath.random());
		}
	},
	PI {
		Term eval() {
			return new PrologReal(StrictMath.PI);
		}
	};
	abstract Term eval();
}
