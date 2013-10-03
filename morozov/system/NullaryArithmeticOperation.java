// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.terms.*;

public enum NullaryArithmeticOperation {
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
	public abstract Term eval();
}
