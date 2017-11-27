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
	protected BigDecimal decimalValue;
	protected double realValue;
	protected boolean hasRealValue= false;
	protected TimeUnits units;
	//
	// public static final BigDecimal oneNanoBig= BigDecimal.valueOf(1_000_000_000);
	//
	public TimeInterval(BigDecimal value) {
		hasRealValue= false;
		decimalValue= value;
		units= TimeUnits.SECONDS;
	}
	public TimeInterval(double value) {
		hasRealValue= true;
		realValue= value;
		units= TimeUnits.SECONDS;
	}
	public TimeInterval(Term a, TimeUnits u, ChoisePoint iX) {
		units= u;
		try {
			BigDecimal value= new BigDecimal(a.getIntegerValue(iX));
			hasRealValue= false;
			decimalValue= value;
		} catch (TermIsNotAnInteger b1) {
			try {
				double value= a.getRealValue(iX);
				hasRealValue= true;
				realValue= value;
			} catch (TermIsNotAReal b2) {
				throw new WrongArgumentIsNotTimeInterval(a);
			}
		}
	}
	public TimeInterval(boolean r, TimeUnits u) {
		hasRealValue= r;
		units= u;
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
					if (functor == SymbolCodes.symbolCode_E_seconds) {
						return new TimeInterval(arguments[0],TimeUnits.SECONDS,iX);
					} else if (functor == SymbolCodes.symbolCode_E_milliseconds) {
						return new TimeInterval(arguments[0],TimeUnits.MILLISECONDS,iX);
					} else if (functor == SymbolCodes.symbolCode_E_microseconds) {
						return new TimeInterval(arguments[0],TimeUnits.MICROSECONDS,iX);
					} else if (functor == SymbolCodes.symbolCode_E_nanoseconds) {
						return new TimeInterval(arguments[0],TimeUnits.NANOSECONDS,iX);
					} else if (functor == SymbolCodes.symbolCode_E_picoseconds) {
						return new TimeInterval(arguments[0],TimeUnits.PICOSECONDS,iX);
					} else if (functor == SymbolCodes.symbolCode_E_femtoseconds) {
						return new TimeInterval(arguments[0],TimeUnits.FEMTOSECONDS,iX);
					} else if (functor == SymbolCodes.symbolCode_E_minutes) {
						return new TimeInterval(arguments[0],TimeUnits.MINUTES,iX);
					} else if (functor == SymbolCodes.symbolCode_E_hours) {
						return new TimeInterval(arguments[0],TimeUnits.HOURS,iX);
					} else if (functor == SymbolCodes.symbolCode_E_days) {
						return new TimeInterval(arguments[0],TimeUnits.DAYS,iX);
					} else if (functor == SymbolCodes.symbolCode_E_weeks) {
						return new TimeInterval(arguments[0],TimeUnits.WEEKS,iX);
					} else if (functor == SymbolCodes.symbolCode_E_months) {
						return new TimeInterval(arguments[0],TimeUnits.MONTHS,iX);
					} else if (functor == SymbolCodes.symbolCode_E_years) {
						return new TimeInterval(arguments[0],TimeUnits.YEARS,iX);
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
			// return PrologInteger.toLong(realValue * Converters.oneMillionDouble);
			return units.toNanosecondsLong(realValue);
		} else {
			// return PrologInteger.toLong(decimalValue.multiply(Converters.oneNanoBig));
			return units.toNanosecondsLong(decimalValue);
		}
	}
	//
	public long toMillisecondsLong() {
		if (hasRealValue) {
			// return PrologInteger.toLong(realValue*Converters.oneMilliDouble);
			return units.toMillisecondsLong(realValue);
		} else {
			// return PrologInteger.toLong(decimalValue.multiply(Converters.oneMilliBig));
			return units.toMillisecondsLong(decimalValue);
		}
	}
	//
	public int toMillisecondsInteger() {
		if (hasRealValue) {
			// return PrologInteger.toInteger(realValue*Converters.oneMilliDouble);
			return units.toMillisecondsInteger(realValue);
		} else {
			// return PrologInteger.toInteger(decimalValue.multiply(Converters.oneMilliBig));
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
