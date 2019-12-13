// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.converters;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.math.MathContext;
import java.math.BigDecimal;

public class TimeIntervalConverters {
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
				value= value.divide(TimeUnitsConverters.oneThousandBig,MathContext.DECIMAL128);
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
	public static Term toTerm(TimeInterval u) {
		if (u.hasRealValue()) {
			return TimeUnitsConverters.toTerm(u.getUnits(),new PrologReal(u.getRealValue()));
		} else {
			return TimeUnitsConverters.toTerm(u.getUnits(),new PrologInteger(u.getDecimalValue().toBigIntegerExact()));
		}
	}
}
