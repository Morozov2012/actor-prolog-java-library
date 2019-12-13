// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.run.*;
import morozov.system.converters.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;
import morozov.worlds.*;
import morozov.worlds.remote.*;

import java.math.BigInteger;

public enum TermCheckOperation {
	//
	FREE {
		@Override
		public boolean eval(ChoisePoint iX, Term value) {
			return value.dereferenceValue(iX).thisIsFreeVariable();
		}
	},
	BOUND {
		@Override
		public boolean eval(ChoisePoint iX, Term value) {
			return !value.dereferenceValue(iX).thisIsFreeVariable();
		}
	},
	SYMBOL {
		@Override
		public boolean eval(ChoisePoint iX, Term value) {
			return (value instanceof PrologSymbol);
		}
	},
	STRING {
		@Override
		public boolean eval(ChoisePoint iX, Term value) {
			return (value instanceof PrologString);
		}
	},
	BINARY {
		@Override
		public boolean eval(ChoisePoint iX, Term value) {
			return (value instanceof PrologBinary);
		}
	},
	INTEGER {
		@Override
		public boolean eval(ChoisePoint iX, Term value) {
			return (value instanceof PrologInteger);
		}
	},
	REAL {
		@Override
		public boolean eval(ChoisePoint iX, Term value) {
			return (value instanceof PrologReal);
		}
	},
	NUMERICAL {
		@Override
		public boolean eval(ChoisePoint iX, Term value) {
			return
				(value instanceof PrologInteger) ||
				(value instanceof PrologReal);
		}
	},
	CLASS_INSTANCE {
		@Override
		public boolean eval(ChoisePoint iX, Term value) {
			return
				(value instanceof AbstractWorld);
		}
	},
	INTERNAL_WORLD {
		@Override
		public boolean eval(ChoisePoint iX, Term value, ActiveWorld currentProcess) {
			if (value instanceof AbstractProcess) {
				AbstractProcess process= (AbstractProcess)value;
				if (currentProcess==process) {
					return true;
				} else {
					return false;
				}
			} else if (value instanceof AbstractInternalWorld) {
				AbstractInternalWorld world= (AbstractInternalWorld)value;
				if (currentProcess==world.currentProcess) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		@Override
		public boolean eval(ChoisePoint iX, Term value) {
			return false;
		}
	},
	EXTERNAL_WORLD {
		@Override
		public boolean eval(ChoisePoint iX, Term value, ActiveWorld currentProcess) {
			if (value instanceof AbstractProcess) {
				AbstractProcess process= (AbstractProcess)value;
				if (currentProcess==process) {
					return false;
				} else {
					return true;
				}
			} else if (value instanceof AbstractInternalWorld) {
				AbstractInternalWorld world= (AbstractInternalWorld)value;
				if (currentProcess==world.currentProcess) {
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		}
		@Override
		public boolean eval(ChoisePoint iX, Term value) {
			return false;
		}
	},
	REMOTE_WORLD {
		@Override
		public boolean eval(ChoisePoint iX, Term value, ActiveWorld currentProcess) {
			if (value instanceof ForeignWorldWrapper) {
				return true;
			} else {
				return false;
			}
		}
		@Override
		public boolean eval(ChoisePoint iX, Term value) {
			if (value instanceof ForeignWorldWrapper) {
				return true;
			} else {
				return false;
			}
		}
	},
	EVEN {
		@Override
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
		@Override
		public boolean eval(ChoisePoint iX, Term value) {
			try {
				BigInteger n= value.getIntegerValue(iX);
				// return (n % 2) != 0;
				return ( !(n.remainder(BigInteger.valueOf(2))).equals(BigInteger.ZERO) );
			} catch (TermIsNotAnInteger b1) {
				throw new WrongArgumentIsNotAnInteger(value);
			}
		}
	},
	NAN {
		@Override
		public boolean eval(ChoisePoint iX, Term value) {
			try {
				double r= value.getRealValue(iX);
				return Double.isNaN(r) || Double.isInfinite(r);
			} catch (TermIsNotAReal b1) {
				return false;
			}
		}
	},
	INFINITE {
		@Override
		public boolean eval(ChoisePoint iX, Term value) {
			try {
				double r= value.getRealValue(iX);
				return Double.isInfinite(r);
			} catch (TermIsNotAReal b1) {
				return false;
			}
		}
	},
	FINITE {
		@Override
		public boolean eval(ChoisePoint iX, Term value) {
			try {
				double r= value.getRealValue(iX);
				return !(Double.isInfinite(r)) && !(Double.isNaN(r));
			} catch (TermIsNotAReal b1) {
				return false;
			}
		}
	};
	//
	abstract public boolean eval(ChoisePoint iX, Term value);
	//
	public boolean eval(ChoisePoint iX, Term value, ActiveWorld currentProcess) {
		return eval(iX,value);
	}
	public void checkTerm(Term value, ChoisePoint iX, ActiveWorld currentProcess) throws Backtracking {
		if (!eval(iX,value.dereferenceValue(iX),currentProcess)) {
			throw Backtracking.instance;
		}
	}
}
