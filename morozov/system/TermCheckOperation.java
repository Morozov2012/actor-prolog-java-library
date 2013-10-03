// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.classes.*;
import morozov.run.*;
import morozov.system.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.math.BigInteger;

public enum TermCheckOperation {
	FREE {
		public boolean eval(ChoisePoint iX, Term value) {
			return value.dereferenceValue(iX).thisIsFreeVariable();
		}
	},
	BOUND {
		public boolean eval(ChoisePoint iX, Term value) {
			return !value.dereferenceValue(iX).thisIsFreeVariable();
		}
	},
	SYMBOL {
		public boolean eval(ChoisePoint iX, Term value) {
			return (value instanceof PrologSymbol);
		}
	},
	STRING {
		public boolean eval(ChoisePoint iX, Term value) {
			return (value instanceof PrologString);
		}
	},
	INTEGER {
		public boolean eval(ChoisePoint iX, Term value) {
			return (value instanceof PrologInteger);
		}
	},
	REAL {
		public boolean eval(ChoisePoint iX, Term value) {
			return (value instanceof PrologReal);
		}
	},
	NUMERICAL {
		public boolean eval(ChoisePoint iX, Term value) {
			return
				(value instanceof PrologInteger) ||
				(value instanceof PrologReal);
		}
	},
	CLASS_INSTANCE {
		public boolean eval(ChoisePoint iX, Term value) {
			return
				(value instanceof AbstractWorld);
		}
	},
	INTERNAL_WORLD {
		public boolean eval(ChoisePoint iX, Term value, ActiveWorld currentProcess) {
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
		public boolean eval(ChoisePoint iX, Term value) {
			return false;
		}
	},
	EXTERNAL_WORLD {
		public boolean eval(ChoisePoint iX, Term value, ActiveWorld currentProcess) {
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
		public boolean eval(ChoisePoint iX, Term value) {
			return false;
		}
	},
	EVEN {
		public boolean eval(ChoisePoint iX, Term value) {
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
		public boolean eval(ChoisePoint iX, Term value) {
			try {
				BigInteger n= value.getIntegerValue(iX);
				// return (n % 2) != 0;
				return ( !(n.remainder(BigInteger.valueOf(2))).equals(BigInteger.ZERO) );
			} catch (TermIsNotAnInteger b1) {
				throw new WrongArgumentIsNotAnInteger(value);
			}
		}
	};
	abstract public boolean eval(ChoisePoint iX, Term value);
	// abstract public boolean eval(ChoisePoint iX, Term value, ActiveWorld currentProcess);
	public boolean eval(ChoisePoint iX, Term value, ActiveWorld currentProcess) {
		return eval(iX,value);
	}
	public void checkTerm(Term value, ChoisePoint iX, ActiveWorld currentProcess) throws Backtracking {
		if (!eval(iX,value.dereferenceValue(iX),currentProcess)) {
			throw Backtracking.instance;
		}
	}
}
