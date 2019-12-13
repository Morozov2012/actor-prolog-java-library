// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.converters;

import target.*;

import morozov.system.*;
import morozov.system.converters.errors.*;
import morozov.terms.*;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TimeUnitsConverters {
	//
	public static final double oneThousandDouble	= 1000.0;
	public static final double oneMillionDouble	= 1_000_000.0;
	public static final BigDecimal oneThousandBig	= BigDecimal.valueOf(1000);
	public static final BigDecimal oneMillionBig	= BigDecimal.valueOf(1_000_000);
	public static final BigDecimal oneMicroBig	= BigDecimal.valueOf(1_000_000);
	public static final BigDecimal oneNanoBig	= BigDecimal.valueOf(1_000_000_000);
	public static final BigDecimal onePicoBig	= BigDecimal.valueOf(1_000_000_000_000L);
	public static final BigDecimal oneFemtoBig	= BigDecimal.valueOf(1_000_000_000_000_000L);
	//
	public static final long oneDayLengthInSecondsLong				= 24 * 60 * 60; // 86_400
	public static final long oneDayLengthInMillisecondsLong			= oneDayLengthInSecondsLong * 1000; // 86_400_000
	public static final long oneWeekLengthInSecondsLong				= 7 * 24 * 60 * 60; // 604_800
	public static final double oneYearLengthInSecondsDouble			= 31556925.9747;
	public static final double oneMonthLengthInSecondsDouble			= 31556925.9747 / 12;
	//
	public static final BigDecimal oneMinuteInSecondsBig			= BigDecimal.valueOf(60);
	public static final BigDecimal oneHourInSecondsBig			= BigDecimal.valueOf(60*60); // 3_600
	public static final BigInteger oneDayLengthInMillisecondsBigInteger		= BigInteger.valueOf(oneDayLengthInMillisecondsLong);
	public static final BigDecimal oneDayLengthInSecondsBigDecimal		= BigDecimal.valueOf(oneDayLengthInSecondsLong);
	public static final BigDecimal oneWeekLengthInSecondsBigDecimal		= BigDecimal.valueOf(oneWeekLengthInSecondsLong);
	public static final BigDecimal oneYearLengthInSecondsBigInteger		= new BigDecimal(oneYearLengthInSecondsDouble);
	public static final BigDecimal oneMonthLengthInSecondsBigInteger	= new BigDecimal(oneMonthLengthInSecondsDouble);
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(TimeUnits unit, Term argument) {
		switch (unit) {
		case SECONDS:
			return new PrologStructure(SymbolCodes.symbolCode_E_seconds,new Term[]{argument});
		case MILLISECONDS:
			return new PrologStructure(SymbolCodes.symbolCode_E_milliseconds,new Term[]{argument});
		case MICROSECONDS:
			return new PrologStructure(SymbolCodes.symbolCode_E_microseconds,new Term[]{argument});
		case NANOSECONDS:
			return new PrologStructure(SymbolCodes.symbolCode_E_nanoseconds,new Term[]{argument});
		case PICOSECONDS:
			return new PrologStructure(SymbolCodes.symbolCode_E_picoseconds,new Term[]{argument});
		case FEMTOSECONDS:
			return new PrologStructure(SymbolCodes.symbolCode_E_femtoseconds,new Term[]{argument});
		case MINUTES:
			return new PrologStructure(SymbolCodes.symbolCode_E_minutes,new Term[]{argument});
		case HOURS:
			return new PrologStructure(SymbolCodes.symbolCode_E_hours,new Term[]{argument});
		case DAYS:
			return new PrologStructure(SymbolCodes.symbolCode_E_days,new Term[]{argument});
		case WEEKS:
			return new PrologStructure(SymbolCodes.symbolCode_E_weeks,new Term[]{argument});
		case MONTHS:
			return new PrologStructure(SymbolCodes.symbolCode_E_months,new Term[]{argument});
		case YEARS:
			return new PrologStructure(SymbolCodes.symbolCode_E_years,new Term[]{argument});
		default:
			throw new UnknownTimeUnits(unit);
		}
	}
}
