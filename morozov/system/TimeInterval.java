// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system;

import target.*;

import morozov.run.*;
import morozov.system.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.math.MathContext;
import java.math.BigDecimal;

public class TimeInterval {
	//
	protected boolean hasRealValue= false;
	protected double realValue;
	protected BigDecimal decimalValue;
	protected TimeUnits units;
	//
	public TimeInterval(double value) {
		hasRealValue= true;
		realValue= value;
		units= TimeUnits.SECONDS;
	}
	public TimeInterval(double value, TimeUnits u) {
		hasRealValue= true;
		realValue= value;
		units= u;
	}
	public TimeInterval(BigDecimal value) {
		hasRealValue= false;
		decimalValue= value;
		units= TimeUnits.SECONDS;
	}
	public TimeInterval(BigDecimal value, TimeUnits u) {
		hasRealValue= false;
		decimalValue= value;
		units= u;
	}
	public TimeInterval(boolean hasRV, double real, BigDecimal decimal, TimeUnits u) {
		hasRealValue= hasRV;
		realValue= real;
		decimalValue= decimal;
		units= u;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean hasRealValue() {
		return hasRealValue;
	}
	public double getRealValue() {
		return realValue;
	}
	public BigDecimal getDecimalValue() {
		return decimalValue;
	}
	public TimeUnits getUnits() {
		return units;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static TimeInterval argumentSecondsToTimeInterval(Term a, ChoisePoint iX) {
		return argumentToTimeInterval(a,false,iX);
	}
	//
	public static TimeInterval argumentMillisecondsToTimeInterval(Term a, ChoisePoint iX) {
		return argumentToTimeInterval(a,true,iX);
	}
	//
	public static TimeInterval argumentToTimeInterval(Term a, boolean acceptMilliseconds, ChoisePoint iX) {
		try {
			BigDecimal value= new BigDecimal(a.getIntegerValue(iX));
			if (acceptMilliseconds) {
				value= value.divide(TimeUnits.oneThousandBig,MathContext.DECIMAL128);
			};
			return new TimeInterval(value);
		} catch (TermIsNotAnInteger b1) {
			try {
				double value= a.getRealValue(iX);
				if (acceptMilliseconds) {
					value= value / 1000.0;
				};
				return new TimeInterval(value);
			} catch (TermIsNotAReal b2) {
				try {
					long functor= a.getStructureFunctor(iX);
					Term[] arguments= a.getStructureArguments(iX);
					if (arguments.length > 1 || arguments.length < 1) {
						throw new WrongArgumentIsNotTimeInterval(a);
					};
					Term firstArgument= arguments[0];
					if (functor == SymbolCodes.symbolCode_E_seconds) {
						return argumentToTimeInterval(firstArgument,TimeUnits.SECONDS,iX);
					} else if (functor == SymbolCodes.symbolCode_E_milliseconds) {
						return argumentToTimeInterval(firstArgument,TimeUnits.MILLISECONDS,iX);
					} else if (functor == SymbolCodes.symbolCode_E_microseconds) {
						return argumentToTimeInterval(firstArgument,TimeUnits.MICROSECONDS,iX);
					} else if (functor == SymbolCodes.symbolCode_E_nanoseconds) {
						return argumentToTimeInterval(firstArgument,TimeUnits.NANOSECONDS,iX);
					} else if (functor == SymbolCodes.symbolCode_E_picoseconds) {
						return argumentToTimeInterval(firstArgument,TimeUnits.PICOSECONDS,iX);
					} else if (functor == SymbolCodes.symbolCode_E_femtoseconds) {
						return argumentToTimeInterval(firstArgument,TimeUnits.FEMTOSECONDS,iX);
					} else if (functor == SymbolCodes.symbolCode_E_minutes) {
						return argumentToTimeInterval(firstArgument,TimeUnits.MINUTES,iX);
					} else if (functor == SymbolCodes.symbolCode_E_hours) {
						return argumentToTimeInterval(firstArgument,TimeUnits.HOURS,iX);
					} else if (functor == SymbolCodes.symbolCode_E_days) {
						return argumentToTimeInterval(firstArgument,TimeUnits.DAYS,iX);
					} else if (functor == SymbolCodes.symbolCode_E_weeks) {
						return argumentToTimeInterval(firstArgument,TimeUnits.WEEKS,iX);
					} else if (functor == SymbolCodes.symbolCode_E_months) {
						return argumentToTimeInterval(firstArgument,TimeUnits.MONTHS,iX);
					} else if (functor == SymbolCodes.symbolCode_E_years) {
						return argumentToTimeInterval(firstArgument,TimeUnits.YEARS,iX);
					} else {
						throw new WrongArgumentIsNotTimeInterval(a);
					}
				} catch (TermIsNotAStructure b3) {
					throw new WrongArgumentIsNotTimeInterval(a);
				}
			}
		}
	}
	//
	public static TimeInterval argumentToTimeInterval(Term argument, TimeUnits u, ChoisePoint iX) {
		try {
			BigDecimal value= new BigDecimal(argument.getIntegerValue(iX));
			return new TimeInterval(value,u);
		} catch (TermIsNotAnInteger b1) {
			try {
				double value= argument.getRealValue(iX);
				return new TimeInterval(value,u);
			} catch (TermIsNotAReal b2) {
				throw new WrongArgumentIsNotTimeInterval(argument);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public BigDecimal toNanosecondsBigDecimal() {
		if (hasRealValue) {
			return units.toNanosecondsBigDecimal(realValue);
		} else {
			return units.toNanosecondsBigDecimal(decimalValue);
		}
	}
	//
	public long toNanosecondsLong() {
		if (hasRealValue) {
			// return PrologInteger.toLong(realValue * GeneralConverters.oneMillionDouble);
			return units.toNanosecondsLong(realValue);
		} else {
			// return PrologInteger.toLong(decimalValue.multiply(GeneralConverters.oneNanoBig));
			return units.toNanosecondsLong(decimalValue);
		}
	}
	//
	public long toMillisecondsLong() {
		if (hasRealValue) {
			// return PrologInteger.toLong(realValue*Converters.oneMilliDouble);
			return units.toMillisecondsLong(realValue);
		} else {
			// return PrologInteger.toLong(decimalValue.multiply(GeneralConverters.oneMilliBig));
			return units.toMillisecondsLong(decimalValue);
		}
	}
	//
	public int toMillisecondsInteger() {
		if (hasRealValue) {
			// return PrologInteger.toInteger(realValue*Converters.oneMilliDouble);
			return units.toMillisecondsInteger(realValue);
		} else {
			// return PrologInteger.toInteger(decimalValue.multiply(GeneralConverters.oneMilliBig));
			return units.toMillisecondsInteger(decimalValue);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		if (hasRealValue) {
			return units.toTerm(new PrologReal(realValue));
		} else {
			return units.toTerm(new PrologInteger(decimalValue.toBigIntegerExact()));
		}
	}
}
