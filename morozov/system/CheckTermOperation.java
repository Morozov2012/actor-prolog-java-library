// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.classes.*;
import morozov.terms.*;

import java.math.BigInteger;

public enum CheckTermOperation {
	FREE {
		boolean eval(ChoisePoint iX, Term value) {
			return value.dereferenceValue(iX).thisIsFreeVariable();
		}
	},
	BOUND {
		boolean eval(ChoisePoint iX, Term value) {
			return !value.dereferenceValue(iX).thisIsFreeVariable();
		}
	},
	SYMBOL {
		boolean eval(ChoisePoint iX, Term value) {
			return (value instanceof PrologSymbol);
		}
	},
	STRING {
		boolean eval(ChoisePoint iX, Term value) {
			return (value instanceof PrologString);
		}
	},
	INTEGER {
		boolean eval(ChoisePoint iX, Term value) {
			return (value instanceof PrologInteger);
		}
	},
	REAL {
		boolean eval(ChoisePoint iX, Term value) {
			return (value instanceof PrologReal);
		}
	},
	NUMERICAL {
		boolean eval(ChoisePoint iX, Term value) {
			return
				(value instanceof PrologInteger) ||
				(value instanceof PrologReal);
		}
	},
	CLASS_INSTANCE {
		boolean eval(ChoisePoint iX, Term value) {
			return
				(value instanceof AbstractWorld);
		}
	},
	INTERNAL_WORLD {
		boolean eval(ChoisePoint iX, Term value, ActiveWorld currentProcess) {
			if (value instanceof AbstractWorld) {
				AbstractWorld world= (AbstractWorld)value;
				if (currentProcess==world.currentProcess) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		boolean eval(ChoisePoint iX, Term value) {
			return false;
		}
	},
	EXTERNAL_WORLD {
		boolean eval(ChoisePoint iX, Term value, ActiveWorld currentProcess) {
			if (value instanceof AbstractWorld) {
				AbstractWorld world= (AbstractWorld)value;
				if (currentProcess==world.currentProcess) {
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		}
		boolean eval(ChoisePoint iX, Term value) {
			return false;
		}
	},
	EVEN {
		boolean eval(ChoisePoint iX, Term value) {
			try {
				BigInteger n= value.getIntegerValue(iX);
				// return (n % 2) == 0;
				return (n.remainder(BigInteger.valueOf(2))).equals(BigInteger.ZERO);
			} catch (TermIsNotAnInteger b1) {
				throw new WrongArgumentIsNotAnInteger(value);
			}
		}
	},
	ODD {
		boolean eval(ChoisePoint iX, Term value) {
			try {
				BigInteger n= value.getIntegerValue(iX);
				// return (n % 2) != 0;
				return ( !(n.remainder(BigInteger.valueOf(2))).equals(BigInteger.ZERO) );
			} catch (TermIsNotAnInteger b1) {
				throw new WrongArgumentIsNotAnInteger(value);
			}
		}
	};
	abstract boolean eval(ChoisePoint iX, Term value);
	// abstract boolean eval(ChoisePoint iX, Term value, ActiveWorld currentProcess);
	boolean eval(ChoisePoint iX, Term value, ActiveWorld currentProcess) {
		return eval(iX,value);
	}
}
