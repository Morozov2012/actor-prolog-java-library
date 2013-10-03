// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import target.*;

import morozov.classes.*;
import morozov.run.*;
import morozov.system.*;
import morozov.terms.errors.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.math.BigInteger;

public class PrologStructure extends Term {
	private long functor;
	private Term[] arguments;
	public PrologStructure(long aFunctor, Term[] aContents) {
		functor= aFunctor;
		arguments= aContents;
	}
	public int hashCode() {
		int sum= (int)functor;
		for (int i= 0; i < arguments.length; i++) {
			sum+= arguments[i].hashCode();
		};
		return sum;
	}
	public Term[] isStructure(long aFunctor, int aArity, ChoisePoint cp) throws Backtracking {
		if (aFunctor==functor && aArity==arguments.length)
			return arguments;
		throw Backtracking.instance;
	}
	public long getStructureFunctor(ChoisePoint cp) throws TermIsNotAStructure {
		return functor;
	}
	public Term[] getStructureArguments(ChoisePoint cp) throws TermIsNotAStructure {
		return arguments;
	}
	public void unifyWithStructure(long aFunctor, Term[] values, Term structure, ChoisePoint cp) throws Backtracking {
		if (aFunctor==functor && values.length==arguments.length) {
			for (int i= 0; i < arguments.length; i++) {
				arguments[i].unifyWith(values[i],cp);
			}
		} else {
			throw Backtracking.instance;
		}
	}
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.unifyWithStructure(functor,arguments,this,cp);
	}
	public void registerVariables(ActiveWorld process, boolean isSuspending, boolean isProtecting) {
		for (int i= 0; i < arguments.length; i++) {
			arguments[i].registerVariables(process,isSuspending,isProtecting);
		}
	}
	public void registerTargetWorlds(HashSet<AbstractWorld> worlds, ChoisePoint cp) {
		for (int i= 0; i < arguments.length; i++) {
			arguments[i].registerTargetWorlds(worlds,cp);
		}
	}
	public PrologStructure circumscribe() {
		Term[] aContents= new Term[arguments.length];
		for (int i= 0; i < arguments.length; i++) {
			aContents[i]= arguments[i].circumscribe();
		};
		return new PrologStructure(functor,aContents);
	}
	public PrologStructure copyValue(ChoisePoint cp, TermCircumscribingMode mode) {
		Term[] aContents= new Term[arguments.length];
		for (int i= 0; i < arguments.length; i++) {
			aContents[i]= arguments[i].copyValue(cp,mode);
		};
		return new PrologStructure(functor,aContents);
	}
	public PrologStructure copyGroundValue(ChoisePoint cp) throws TermIsUnboundVariable {
		Term[] aContents= new Term[arguments.length];
		for (int i= 0; i < arguments.length; i++) {
			aContents[i]= arguments[i].copyGroundValue(cp);
		};
		return new PrologStructure(functor,aContents);
	}
	public PrologStructure substituteWorlds(HashMap<AbstractWorld,Term> map, ChoisePoint cp) {
		Term[] aContents= new Term[arguments.length];
		for (int i= 0; i < arguments.length; i++) {
			aContents[i]= arguments[i].substituteWorlds(map,cp);
		};
		return new PrologStructure(functor,aContents);
	}
	// Comparison operations
	public void compareWithTerm(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= Converters.argumentsToDate(arguments[0],arguments[1],arguments[2],this,iX);
			a.compareDateWith(timeInMillis,iX,op);
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],this,iX);
				a.compareLongWith(timeInMillis,iX,op);
			} else if (arguments.length==4) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				a.compareLongWith(timeInMillis,iX,op);
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	public void compareWithBigInteger(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= Converters.argumentsToDate(arguments[0],arguments[1],arguments[2],this,iX);
			if (!op.eval(BigInteger.valueOf(timeInMillis),a.multiply(Converters.oneDayLengthBigInteger))) {
				throw Backtracking.instance;
			}
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],this,iX);
				if (!op.eval(BigInteger.valueOf(timeInMillis),a)) {
					throw Backtracking.instance;
				}
			} else if (arguments.length==4) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				if (!op.eval(BigInteger.valueOf(timeInMillis),a)) {
					throw Backtracking.instance;
				}
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	public void compareWithLong(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= Converters.argumentsToDate(arguments[0],arguments[1],arguments[2],this,iX);
			if (!op.eval(BigInteger.valueOf(timeInMillis),BigInteger.valueOf(a).multiply(Converters.oneDayLengthBigInteger))) {
				throw Backtracking.instance;
			}
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],this,iX);
				if (!op.eval(BigInteger.valueOf(timeInMillis),BigInteger.valueOf(a))) {
					throw Backtracking.instance;
				}
			} else if (arguments.length==4) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				if (!op.eval(BigInteger.valueOf(timeInMillis),BigInteger.valueOf(a))) {
					throw Backtracking.instance;
				}
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	public void compareWithDouble(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= Converters.argumentsToDate(arguments[0],arguments[1],arguments[2],this,iX);
			if (!op.eval(BigInteger.valueOf(timeInMillis),Converters.doubleValueToBigInteger(a).multiply(Converters.oneDayLengthBigInteger))) {
				throw Backtracking.instance;
			}
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],this,iX);
				if (!op.eval(BigInteger.valueOf(timeInMillis),Converters.doubleValueToBigInteger(a))) {
					throw Backtracking.instance;
				}
			} else if (arguments.length==4) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				if (!op.eval(BigInteger.valueOf(timeInMillis),Converters.doubleValueToBigInteger(a))) {
					throw Backtracking.instance;
				}
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	public void compareWithDate(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= Converters.argumentsToDate(arguments[0],arguments[1],arguments[2],this,iX);
			if (!op.eval(BigInteger.valueOf(timeInMillis),BigInteger.valueOf(a))) {
				throw Backtracking.instance;
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	public void compareTermWith(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= Converters.argumentsToDate(arguments[0],arguments[1],arguments[2],this,iX);
			a.compareWithDate(timeInMillis,iX,op);
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],this,iX);
				a.compareWithLong(timeInMillis,iX,op);
			} else if (arguments.length==4) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				a.compareWithLong(timeInMillis,iX,op);
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	public void compareBigIntegerWith(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= Converters.argumentsToDate(arguments[0],arguments[1],arguments[2],this,iX);
			if (!op.eval(a.multiply(Converters.oneDayLengthBigInteger),BigInteger.valueOf(timeInMillis))) {
				throw Backtracking.instance;
			}
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],this,iX);
				if (!op.eval(a,BigInteger.valueOf(timeInMillis))) {
					throw Backtracking.instance;
				}
			} else if (arguments.length==4) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				if (!op.eval(a,BigInteger.valueOf(timeInMillis))) {
					throw Backtracking.instance;
				}
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	public void compareLongWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= Converters.argumentsToDate(arguments[0],arguments[1],arguments[2],this,iX);
			if (!op.eval(BigInteger.valueOf(a).multiply(Converters.oneDayLengthBigInteger),BigInteger.valueOf(timeInMillis))) {
				throw Backtracking.instance;
			}
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],this,iX);
				if (!op.eval(BigInteger.valueOf(a),BigInteger.valueOf(timeInMillis))) {
					throw Backtracking.instance;
				}
			} else if (arguments.length==4) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				if (!op.eval(BigInteger.valueOf(a),BigInteger.valueOf(timeInMillis))) {
					throw Backtracking.instance;
				}
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	public void compareDoubleWith(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= Converters.argumentsToDate(arguments[0],arguments[1],arguments[2],this,iX);
			if (!op.eval(Converters.doubleValueToBigInteger(a).multiply(Converters.oneDayLengthBigInteger),BigInteger.valueOf(timeInMillis))) {
				throw Backtracking.instance;
			}
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],this,iX);
				if (!op.eval(Converters.doubleValueToBigInteger(a),BigInteger.valueOf(timeInMillis))) {
					throw Backtracking.instance;
				}
			} else if (arguments.length==4) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				if (!op.eval(Converters.doubleValueToBigInteger(a),BigInteger.valueOf(timeInMillis))) {
					throw Backtracking.instance;
				}
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	public void compareDateWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= Converters.argumentsToDate(arguments[0],arguments[1],arguments[2],this,iX);
			if (!op.eval(BigInteger.valueOf(a),BigInteger.valueOf(timeInMillis))) {
				throw Backtracking.instance;
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	public void compareListWith(Term aHead, Term aTail, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= Converters.argumentsToDate(arguments[0],arguments[1],arguments[2],this,iX);
			aHead.compareWithDate(timeInMillis,iX,op);
			aTail.compareWithDate(timeInMillis,iX,op);
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],this,iX);
				aHead.compareWithLong(timeInMillis,iX,op);
				aTail.compareWithLong(timeInMillis,iX,op);
			} else if (arguments.length==4) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				aHead.compareWithLong(timeInMillis,iX,op);
				aTail.compareWithLong(timeInMillis,iX,op);
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	// Arithmetic operations
	public Term reactWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= Converters.argumentsToDate(arguments[0],arguments[1],arguments[2],this,iX);
			return a.reactDateWith(timeInMillis,iX,op);
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],this,iX);
				return a.reactTimeWith(timeInMillis,iX,op);
			} else if (arguments.length==4) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				return a.reactTimeWith(timeInMillis,iX,op);
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	public Term reactWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= Converters.argumentsToDate(arguments[0],arguments[1],arguments[2],this,iX);
			return op.evalDate(BigInteger.valueOf(timeInMillis),a.multiply(Converters.oneDayLengthBigInteger));
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],this,iX);
				return op.evalTime(BigInteger.valueOf(timeInMillis),a);
			} else if (arguments.length==4) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				return op.evalTime(BigInteger.valueOf(timeInMillis),a);
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	public Term reactWithLong(long a, ChoisePoint iX, BinaryOperation op) {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= Converters.argumentsToDate(arguments[0],arguments[1],arguments[2],this,iX);
			return op.evalDate(BigInteger.valueOf(timeInMillis),BigInteger.valueOf(a).multiply(Converters.oneDayLengthBigInteger));
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],this,iX);
				return op.evalTime(BigInteger.valueOf(timeInMillis),BigInteger.valueOf(a));
			} else if (arguments.length==4) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				return op.evalTime(BigInteger.valueOf(timeInMillis),BigInteger.valueOf(a));
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	public Term reactWithDouble(double a, ChoisePoint iX, BinaryOperation op) {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= Converters.argumentsToDate(arguments[0],arguments[1],arguments[2],this,iX);
			return op.evalDate(BigInteger.valueOf(timeInMillis),Converters.doubleValueToBigInteger(a).multiply(Converters.oneDayLengthBigInteger));
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],this,iX);
				return op.evalTime(BigInteger.valueOf(timeInMillis),Converters.doubleValueToBigInteger(a));
			} else if (arguments.length==4) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				return op.evalTime(BigInteger.valueOf(timeInMillis),Converters.doubleValueToBigInteger(a));
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	public Term reactWithDate(long a, ChoisePoint iX, BinaryOperation op) {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= Converters.argumentsToDate(arguments[0],arguments[1],arguments[2],this,iX);
			return op.evalDays(BigInteger.valueOf(timeInMillis),BigInteger.valueOf(a));
		} else {
			throw new WrongArgumentIsNotADate(this);
		}
	}
	public Term reactWithTime(long a, ChoisePoint iX, BinaryOperation op) {
		if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],this,iX);
				return op.eval(BigInteger.valueOf(timeInMillis),BigInteger.valueOf(a));
			} else if (arguments.length==4) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				return op.eval(BigInteger.valueOf(timeInMillis),BigInteger.valueOf(a));
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	public Term reactBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= Converters.argumentsToDate(arguments[0],arguments[1],arguments[2],this,iX);
			return op.evalDate(a.multiply(Converters.oneDayLengthBigInteger),BigInteger.valueOf(timeInMillis));
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],this,iX);
				return op.evalTime(a,BigInteger.valueOf(timeInMillis));
			} else if (arguments.length==4) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				return op.evalTime(a,BigInteger.valueOf(timeInMillis));
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	public Term reactLongWith(long a, ChoisePoint iX, BinaryOperation op) {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= Converters.argumentsToDate(arguments[0],arguments[1],arguments[2],this,iX);
			return op.evalDate(BigInteger.valueOf(a).multiply(Converters.oneDayLengthBigInteger),BigInteger.valueOf(timeInMillis));
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],this,iX);
				return op.evalTime(BigInteger.valueOf(a),BigInteger.valueOf(timeInMillis));
			} else if (arguments.length==4) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				return op.evalTime(BigInteger.valueOf(a),BigInteger.valueOf(timeInMillis));
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	public Term reactDoubleWith(double a, ChoisePoint iX, BinaryOperation op) {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= Converters.argumentsToDate(arguments[0],arguments[1],arguments[2],this,iX);
			return op.evalDate(Converters.doubleValueToBigInteger(a).multiply(Converters.oneDayLengthBigInteger),BigInteger.valueOf(timeInMillis));
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],this,iX);
				return op.evalTime(Converters.doubleValueToBigInteger(a),BigInteger.valueOf(timeInMillis));
			} else if (arguments.length==4) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				return op.evalTime(Converters.doubleValueToBigInteger(a),BigInteger.valueOf(timeInMillis));
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	public Term reactDateWith(long a, ChoisePoint iX, BinaryOperation op) {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= Converters.argumentsToDate(arguments[0],arguments[1],arguments[2],this,iX);
			return op.evalDays(BigInteger.valueOf(a),BigInteger.valueOf(timeInMillis));
		} else {
			throw new WrongArgumentIsNotADate(this);
		}
	}
	public Term reactTimeWith(long a, ChoisePoint iX, BinaryOperation op) {
		if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],this,iX);
				return op.eval(BigInteger.valueOf(a),BigInteger.valueOf(timeInMillis));
			} else if (arguments.length==4) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				return op.eval(BigInteger.valueOf(a),BigInteger.valueOf(timeInMillis));
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	public Term reactListWith(Term aHead, Term aTail, ChoisePoint iX, BinaryOperation op) {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= Converters.argumentsToDate(arguments[0],arguments[1],arguments[2],this,iX);
			return new PrologList(
				aHead.reactWithDate(timeInMillis,iX,op),
				aTail.reactWithDate(timeInMillis,iX,op));
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],this,iX);
				return new PrologList(
					aHead.reactWithTime(timeInMillis,iX,op),
					aTail.reactWithTime(timeInMillis,iX,op));
			} else if (arguments.length==4) {
				long timeInMillis= Converters.argumentsToTime(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				return new PrologList(
					aHead.reactWithTime(timeInMillis,iX,op),
					aTail.reactWithTime(timeInMillis,iX,op));
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	// Converting Term to String
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, CharsetEncoder encoder) {
		StringBuilder buffer= new StringBuilder();
		buffer.append(SymbolNames.retrieveSymbolName(functor).toString(encoder));
		buffer.append("(");
		boolean isFirst= true;
		for (int i= 0; i < arguments.length; i++) {
			if (!isFirst) {
				buffer.append(",");
			};
			buffer.append(arguments[i].toString(cp,true,provideStrictSyntax,encoder));
			isFirst= false;
		};
		buffer.append(")");
		return buffer.toString();
	}
}
