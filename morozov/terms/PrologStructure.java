// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.terms.errors.*;
import morozov.terms.signals.*;
import morozov.worlds.*;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.math.BigInteger;

public class PrologStructure extends Term {
	//
	protected long functor;
	protected Term[] arguments;
	//
	private static final long serialVersionUID= 0xDB99B241209C67E0L; // -2622869315176863776L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.terms","PrologStructure");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public PrologStructure(long aFunctor, Term[] aContents) {
		functor= aFunctor;
		arguments= aContents;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public int hashCode() {
		int sum= PrologSymbol.calculateHashCode(functor);
		for (int i= 0; i < arguments.length; i++) {
			sum+= arguments[i].hashCode();
		};
		return sum;
	}
	@Override
	public boolean equals(Object o2) {
		if (o2 instanceof Term) {
			return ((Term)o2).isEqualToStructure(functor,arguments);
		} else {
			return false;
		}
	}
	@Override
	public int compare(Object o2) {
		if (o2 instanceof Term) {
			return -((Term)o2).compareWithStructure(functor,arguments);
		} else {
			return 1;
		}
	}
	@Override
	public boolean isEqualToStructure(long f2, Term[] a2) {
		if (functor==f2) {
			return TermComparator.areEqualTermArrays(arguments,a2);
		} else {
			return false;
		}
	}
	@Override
	public int compareWithStructure(long f2, Term[] a2) {
		if (functor==f2) {
			return TermComparator.compareTwoTermArrays(arguments,a2);
		} else {
			return Long.compare(functor,f2);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term[] isStructure(long aFunctor, int aArity, ChoisePoint cp) throws Backtracking {
		if ( functor==aFunctor && arguments.length==aArity ) {
			return arguments;
		};
		throw Backtracking.instance;
	}
	@Override
	public long getStructureFunctor(ChoisePoint cp) throws TermIsNotAStructure {
		return functor;
	}
	@Override
	public Term[] getStructureArguments(ChoisePoint cp) throws TermIsNotAStructure {
		return arguments;
	}
	@Override
	public void unifyWithStructure(long aFunctor, Term[] values, Term structure, ChoisePoint cp) throws Backtracking {
		if (functor==aFunctor && arguments.length==values.length) {
			for (int i= 0; i < arguments.length; i++) {
				arguments[i].unifyWith(values[i],cp);
			}
		} else {
			throw Backtracking.instance;
		}
	}
	@Override
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.unifyWithStructure(functor,arguments,this,cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void registerVariables(ActiveWorld process, boolean isSuspending, boolean isProtecting) {
		for (int i= 0; i < arguments.length; i++) {
			arguments[i].registerVariables(process,isSuspending,isProtecting);
		}
	}
	@Override
	public void registerTargetWorlds(HashSet<AbstractWorld> worlds, ChoisePoint cp) {
		for (int i= 0; i < arguments.length; i++) {
			arguments[i].registerTargetWorlds(worlds,cp);
		}
	}
	@Override
	public PrologStructure circumscribe() {
		Term[] aContents= new Term[arguments.length];
		for (int i= 0; i < arguments.length; i++) {
			aContents[i]= arguments[i].circumscribe();
		};
		return new PrologStructure(functor,aContents);
	}
	@Override
	public PrologStructure copyValue(ChoisePoint cp, TermCircumscribingMode mode) {
		Term[] aContents= new Term[arguments.length];
		for (int i= 0; i < arguments.length; i++) {
			aContents[i]= arguments[i].copyValue(cp,mode);
		};
		return new PrologStructure(functor,aContents);
	}
	@Override
	public PrologStructure copyGroundValue(ChoisePoint cp) throws TermIsUnboundVariable {
		Term[] aContents= new Term[arguments.length];
		for (int i= 0; i < arguments.length; i++) {
			aContents[i]= arguments[i].copyGroundValue(cp);
		};
		return new PrologStructure(functor,aContents);
	}
	@Override
	public PrologStructure substituteWorlds(HashMap<AbstractWorld,Term> map, ChoisePoint cp) {
		Term[] aContents= new Term[arguments.length];
		for (int i= 0; i < arguments.length; i++) {
			aContents[i]= arguments[i].substituteWorlds(map,cp);
		};
		return new PrologStructure(functor,aContents);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void compareWithTerm(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= GeneralConverters.argumentsToDateInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
			a.compareDateWith(timeInMillis,iX,op);
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
				a.compareLongWith(timeInMillis,iX,op);
			} else if (arguments.length==4) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				a.compareLongWith(timeInMillis,iX,op);
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	@Override
	public void compareWithBigInteger(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= GeneralConverters.argumentsToDateInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
			if (!op.eval(BigInteger.valueOf(timeInMillis),a.multiply(TimeUnitsConverters.oneDayLengthInMillisecondsBigInteger))) {
				throw Backtracking.instance;
			}
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
				if (!op.eval(BigInteger.valueOf(timeInMillis),a)) {
					throw Backtracking.instance;
				}
			} else if (arguments.length==4) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
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
	@Override
	public void compareWithLong(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= GeneralConverters.argumentsToDateInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
			if (!op.eval(BigInteger.valueOf(timeInMillis),BigInteger.valueOf(a).multiply(TimeUnitsConverters.oneDayLengthInMillisecondsBigInteger))) {
				throw Backtracking.instance;
			}
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
				if (!op.eval(BigInteger.valueOf(timeInMillis),BigInteger.valueOf(a))) {
					throw Backtracking.instance;
				}
			} else if (arguments.length==4) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
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
	@Override
	public void compareWithDouble(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= GeneralConverters.argumentsToDateInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
			if (!op.eval(BigInteger.valueOf(timeInMillis),GeneralConverters.doubleValueToBigInteger(a).multiply(TimeUnitsConverters.oneDayLengthInMillisecondsBigInteger))) {
				throw Backtracking.instance;
			}
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
				if (!op.eval(BigInteger.valueOf(timeInMillis),GeneralConverters.doubleValueToBigInteger(a))) {
					throw Backtracking.instance;
				}
			} else if (arguments.length==4) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				if (!op.eval(BigInteger.valueOf(timeInMillis),GeneralConverters.doubleValueToBigInteger(a))) {
					throw Backtracking.instance;
				}
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	@Override
	public void compareWithDate(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= GeneralConverters.argumentsToDateInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
			if (!op.eval(BigInteger.valueOf(timeInMillis),BigInteger.valueOf(a))) {
				throw Backtracking.instance;
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	@Override
	public void compareTermWith(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= GeneralConverters.argumentsToDateInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
			a.compareWithDate(timeInMillis,iX,op);
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
				a.compareWithLong(timeInMillis,iX,op);
			} else if (arguments.length==4) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				a.compareWithLong(timeInMillis,iX,op);
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	@Override
	public void compareBigIntegerWith(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= GeneralConverters.argumentsToDateInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
			if (!op.eval(a.multiply(TimeUnitsConverters.oneDayLengthInMillisecondsBigInteger),BigInteger.valueOf(timeInMillis))) {
				throw Backtracking.instance;
			}
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
				if (!op.eval(a,BigInteger.valueOf(timeInMillis))) {
					throw Backtracking.instance;
				}
			} else if (arguments.length==4) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
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
	@Override
	public void compareLongWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= GeneralConverters.argumentsToDateInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
			if (!op.eval(BigInteger.valueOf(a).multiply(TimeUnitsConverters.oneDayLengthInMillisecondsBigInteger),BigInteger.valueOf(timeInMillis))) {
				throw Backtracking.instance;
			}
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
				if (!op.eval(BigInteger.valueOf(a),BigInteger.valueOf(timeInMillis))) {
					throw Backtracking.instance;
				}
			} else if (arguments.length==4) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
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
	@Override
	public void compareDoubleWith(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= GeneralConverters.argumentsToDateInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
			if (!op.eval(GeneralConverters.doubleValueToBigInteger(a).multiply(TimeUnitsConverters.oneDayLengthInMillisecondsBigInteger),BigInteger.valueOf(timeInMillis))) {
				throw Backtracking.instance;
			}
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
				if (!op.eval(GeneralConverters.doubleValueToBigInteger(a),BigInteger.valueOf(timeInMillis))) {
					throw Backtracking.instance;
				}
			} else if (arguments.length==4) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				if (!op.eval(GeneralConverters.doubleValueToBigInteger(a),BigInteger.valueOf(timeInMillis))) {
					throw Backtracking.instance;
				}
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	@Override
	public void compareDateWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= GeneralConverters.argumentsToDateInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
			if (!op.eval(BigInteger.valueOf(a),BigInteger.valueOf(timeInMillis))) {
				throw Backtracking.instance;
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	@Override
	public void compareListWith(Term aHead, Term aTail, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= GeneralConverters.argumentsToDateInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
			aHead.compareWithDate(timeInMillis,iX,op);
			aTail.compareWithDate(timeInMillis,iX,op);
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
				aHead.compareWithLong(timeInMillis,iX,op);
				aTail.compareWithLong(timeInMillis,iX,op);
			} else if (arguments.length==4) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				aHead.compareWithLong(timeInMillis,iX,op);
				aTail.compareWithLong(timeInMillis,iX,op);
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term reactWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= GeneralConverters.argumentsToDateInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
			return a.reactDateWith(timeInMillis,iX,op);
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
				return a.reactTimeWith(timeInMillis,iX,op);
			} else if (arguments.length==4) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				return a.reactTimeWith(timeInMillis,iX,op);
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	@Override
	public Term reactWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= GeneralConverters.argumentsToDateInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
			return op.evalDate(BigInteger.valueOf(timeInMillis),a.multiply(TimeUnitsConverters.oneDayLengthInMillisecondsBigInteger));
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
				return op.evalTime(BigInteger.valueOf(timeInMillis),a);
			} else if (arguments.length==4) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				return op.evalTime(BigInteger.valueOf(timeInMillis),a);
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	@Override
	public Term reactWithLong(long a, ChoisePoint iX, BinaryOperation op) {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= GeneralConverters.argumentsToDateInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
			return op.evalDate(BigInteger.valueOf(timeInMillis),BigInteger.valueOf(a).multiply(TimeUnitsConverters.oneDayLengthInMillisecondsBigInteger));
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
				return op.evalTime(BigInteger.valueOf(timeInMillis),BigInteger.valueOf(a));
			} else if (arguments.length==4) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				return op.evalTime(BigInteger.valueOf(timeInMillis),BigInteger.valueOf(a));
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	@Override
	public Term reactWithDouble(double a, ChoisePoint iX, BinaryOperation op) {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= GeneralConverters.argumentsToDateInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
			return op.evalDate(BigInteger.valueOf(timeInMillis),GeneralConverters.doubleValueToBigInteger(a).multiply(TimeUnitsConverters.oneDayLengthInMillisecondsBigInteger));
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
				return op.evalTime(BigInteger.valueOf(timeInMillis),GeneralConverters.doubleValueToBigInteger(a));
			} else if (arguments.length==4) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				return op.evalTime(BigInteger.valueOf(timeInMillis),GeneralConverters.doubleValueToBigInteger(a));
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	@Override
	public Term reactWithDate(long a, ChoisePoint iX, BinaryOperation op) {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= GeneralConverters.argumentsToDateInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
			return op.evalDays(BigInteger.valueOf(timeInMillis),BigInteger.valueOf(a));
		} else {
			throw new WrongArgumentIsNotADate(this);
		}
	}
	@Override
	public Term reactWithTime(long a, ChoisePoint iX, BinaryOperation op) {
		if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
				return op.eval(BigInteger.valueOf(timeInMillis),BigInteger.valueOf(a));
			} else if (arguments.length==4) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				return op.eval(BigInteger.valueOf(timeInMillis),BigInteger.valueOf(a));
			} else {
				throw new WrongArgumentIsNotATime(this);
			}
		} else {
			throw new WrongArgumentIsNotATime(this);
		}
	}
	@Override
	public Term reactBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= GeneralConverters.argumentsToDateInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
			return op.evalDate(a.multiply(TimeUnitsConverters.oneDayLengthInMillisecondsBigInteger),BigInteger.valueOf(timeInMillis));
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
				return op.evalTime(a,BigInteger.valueOf(timeInMillis));
			} else if (arguments.length==4) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				return op.evalTime(a,BigInteger.valueOf(timeInMillis));
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	@Override
	public Term reactLongWith(long a, ChoisePoint iX, BinaryOperation op) {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= GeneralConverters.argumentsToDateInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
			return op.evalDate(BigInteger.valueOf(a).multiply(TimeUnitsConverters.oneDayLengthInMillisecondsBigInteger),BigInteger.valueOf(timeInMillis));
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
				return op.evalTime(BigInteger.valueOf(a),BigInteger.valueOf(timeInMillis));
			} else if (arguments.length==4) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				return op.evalTime(BigInteger.valueOf(a),BigInteger.valueOf(timeInMillis));
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	@Override
	public Term reactDoubleWith(double a, ChoisePoint iX, BinaryOperation op) {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= GeneralConverters.argumentsToDateInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
			return op.evalDate(GeneralConverters.doubleValueToBigInteger(a).multiply(TimeUnitsConverters.oneDayLengthInMillisecondsBigInteger),BigInteger.valueOf(timeInMillis));
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
				return op.evalTime(GeneralConverters.doubleValueToBigInteger(a),BigInteger.valueOf(timeInMillis));
			} else if (arguments.length==4) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				return op.evalTime(GeneralConverters.doubleValueToBigInteger(a),BigInteger.valueOf(timeInMillis));
			} else {
				throw new OperationIsNotDefinedForTheArgument(this);
			}
		} else {
			throw new OperationIsNotDefinedForTheArgument(this);
		}
	}
	@Override
	public Term reactDateWith(long a, ChoisePoint iX, BinaryOperation op) {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= GeneralConverters.argumentsToDateInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
			return op.evalDays(BigInteger.valueOf(a),BigInteger.valueOf(timeInMillis));
		} else {
			throw new WrongArgumentIsNotADate(this);
		}
	}
	@Override
	public Term reactTimeWith(long a, ChoisePoint iX, BinaryOperation op) {
		if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
				return op.eval(BigInteger.valueOf(a),BigInteger.valueOf(timeInMillis));
			} else if (arguments.length==4) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
				return op.eval(BigInteger.valueOf(a),BigInteger.valueOf(timeInMillis));
			} else {
				throw new WrongArgumentIsNotATime(this);
			}
		} else {
			throw new WrongArgumentIsNotATime(this);
		}
	}
	@Override
	public Term reactListWith(Term aHead, Term aTail, ChoisePoint iX, BinaryOperation op) {
		if (functor==SymbolCodes.symbolCode_E_date && arguments.length==3) {
			long timeInMillis= GeneralConverters.argumentsToDateInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
			return new PrologList(
				aHead.reactWithDate(timeInMillis,iX,op),
				aTail.reactWithDate(timeInMillis,iX,op));
		} else if (functor==SymbolCodes.symbolCode_E_time) {
			if (arguments.length==3) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],this,iX);
				return new PrologList(
					aHead.reactWithTime(timeInMillis,iX,op),
					aTail.reactWithTime(timeInMillis,iX,op));
			} else if (arguments.length==4) {
				long timeInMillis= GeneralConverters.argumentsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],arguments[3],this,iX);
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
	//
	///////////////////////////////////////////////////////////////
	//
	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
		stream.writeObject(SymbolNames.retrieveSymbolName(functor));
	}
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		SymbolName symbolName= (SymbolName)stream.readObject();
		functor= SymbolNames.insertSymbolName(symbolName.identifier);
	}
	@Override
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, boolean encodeWorlds, CharsetEncoder encoder) {
		StringBuilder buffer= new StringBuilder();
		buffer.append(SymbolNames.retrieveSymbolName(functor).toSafeString(encoder));
		buffer.append("(");
		boolean isFirst= true;
		for (int i= 0; i < arguments.length; i++) {
			if (!isFirst) {
				buffer.append(",");
			};
			buffer.append(arguments[i].toString(cp,true,provideStrictSyntax,encodeWorlds,encoder));
			isFirst= false;
		};
		buffer.append(")");
		return buffer.toString();
	}
}
