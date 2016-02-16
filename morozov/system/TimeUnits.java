// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system;

import target.*;

import morozov.terms.*;

import java.math.MathContext;
import java.math.BigDecimal;
import java.math.BigInteger;

public enum TimeUnits {
	//
	SECONDS {
		public BigDecimal toNanosecondsBigDecimal(BigDecimal decimalValue) {
			return decimalValue.multiply(oneNanoBig);
		}
		public BigDecimal toNanosecondsBigDecimal(double realValue) {
			return new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneNanoBig);
		}
		public long toNanosecondsLong(BigDecimal decimalValue) {
			return PrologInteger.toLong(decimalValue.multiply(oneNanoBig));
		}
		public long toNanosecondsLong(double realValue) {
			return PrologInteger.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneNanoBig));
		}
		public long toMillisecondsLong(BigDecimal decimalValue) {
			return PrologInteger.toLong(decimalValue.multiply(oneThousandBig));
		}
		public long toMillisecondsLong(double realValue) {
			return PrologInteger.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneThousandBig));
		}
		public int toMillisecondsInteger(BigDecimal decimalValue) {
			return PrologInteger.toInteger(decimalValue.multiply(oneThousandBig));
		}
		public int toMillisecondsInteger(double realValue) {
			return PrologInteger.toInteger(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneThousandBig));
		}
		public Term toTerm(Term argument) {
			return new PrologStructure(SymbolCodes.symbolCode_E_seconds,new Term[]{argument});
		}
	},
	MILLISECONDS {
		public BigDecimal toNanosecondsBigDecimal(BigDecimal decimalValue) {
			return decimalValue.multiply(oneMillionBig);
		}
		public BigDecimal toNanosecondsBigDecimal(double realValue) {
			return new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneMillionBig);
		}
		public long toNanosecondsLong(BigDecimal decimalValue) {
			return PrologInteger.toLong(decimalValue.multiply(oneMillionBig));
		}
		public long toNanosecondsLong(double realValue) {
			return PrologInteger.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneMillionBig));
		}
		public long toMillisecondsLong(BigDecimal decimalValue) {
			return PrologInteger.toLong(decimalValue);
		}
		public long toMillisecondsLong(double realValue) {
			return PrologInteger.toLong(realValue);
		}
		public int toMillisecondsInteger(BigDecimal decimalValue) {
			return PrologInteger.toInteger(decimalValue);
		}
		public int toMillisecondsInteger(double realValue) {
			return PrologInteger.toInteger(realValue);
		}
		public Term toTerm(Term argument) {
			return new PrologStructure(SymbolCodes.symbolCode_E_milliseconds,new Term[]{argument});
		}
	},
	MICROSECONDS {
		public BigDecimal toNanosecondsBigDecimal(BigDecimal decimalValue) {
			return decimalValue.multiply(oneThousandBig);
		}
		public BigDecimal toNanosecondsBigDecimal(double realValue) {
			return new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneThousandBig);
		}
		public long toNanosecondsLong(BigDecimal decimalValue) {
			return PrologInteger.toLong(decimalValue.multiply(oneThousandBig));
		}
		public long toNanosecondsLong(double realValue) {
			return PrologInteger.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneThousandBig));
		}
		public long toMillisecondsLong(BigDecimal decimalValue) {
			return PrologInteger.toLong(decimalValue.divide(oneThousandBig,MathContext.DECIMAL128));
		}
		public long toMillisecondsLong(double realValue) {
			return PrologInteger.toLong(realValue / 1000.0);
		}
		public int toMillisecondsInteger(BigDecimal decimalValue) {
			return PrologInteger.toInteger(decimalValue.divide(oneThousandBig,MathContext.DECIMAL128));
		}
		public int toMillisecondsInteger(double realValue) {
			return PrologInteger.toInteger(realValue / 1000.0);
		}
		public Term toTerm(Term argument) {
			return new PrologStructure(SymbolCodes.symbolCode_E_microseconds,new Term[]{argument});
		}
	},
	NANOSECONDS {
		public BigDecimal toNanosecondsBigDecimal(BigDecimal decimalValue) {
			return decimalValue;
		}
		public BigDecimal toNanosecondsBigDecimal(double realValue) {
			return new BigDecimal(realValue,MathContext.DECIMAL128);
		}
		public long toNanosecondsLong(BigDecimal decimalValue) {
			return PrologInteger.toLong(decimalValue);
		}
		public long toNanosecondsLong(double realValue) {
			return PrologInteger.toLong(realValue);
		}
		public long toMillisecondsLong(BigDecimal decimalValue) {
			return PrologInteger.toLong(decimalValue.divide(oneMillionBig,MathContext.DECIMAL128));
		}
		public long toMillisecondsLong(double realValue) {
			return PrologInteger.toLong(realValue / 1_000_000.0);
		}
		public int toMillisecondsInteger(BigDecimal decimalValue) {
			return PrologInteger.toInteger(decimalValue.divide(oneMillionBig,MathContext.DECIMAL128));
		}
		public int toMillisecondsInteger(double realValue) {
			return PrologInteger.toInteger(realValue / 1_000_000.0);
		}
		public Term toTerm(Term argument) {
			return new PrologStructure(SymbolCodes.symbolCode_E_nanoseconds,new Term[]{argument});
		}
	},
	PICOSECONDS {
		public BigDecimal toNanosecondsBigDecimal(BigDecimal decimalValue) {
			return decimalValue.divide(oneThousandBig,MathContext.DECIMAL128);
		}
		public BigDecimal toNanosecondsBigDecimal(double realValue) {
			return new BigDecimal(realValue / 1000,MathContext.DECIMAL128);
		}
		public long toNanosecondsLong(BigDecimal decimalValue) {
			return PrologInteger.toLong(decimalValue.divide(oneThousandBig,MathContext.DECIMAL128));
		}
		public long toNanosecondsLong(double realValue) {
			return PrologInteger.toLong(realValue / 1000.0);
		}
		public long toMillisecondsLong(BigDecimal decimalValue) {
			return PrologInteger.toLong(decimalValue.divide(oneNanoBig,MathContext.DECIMAL128));
		}
		public long toMillisecondsLong(double realValue) {
			return PrologInteger.toLong(realValue / 1_000_000_000.0);
		}
		public int toMillisecondsInteger(BigDecimal decimalValue) {
			return PrologInteger.toInteger(decimalValue.divide(oneNanoBig,MathContext.DECIMAL128));
		}
		public int toMillisecondsInteger(double realValue) {
			return PrologInteger.toInteger(realValue / 1_000_000_000.0);
		}
		public Term toTerm(Term argument) {
			return new PrologStructure(SymbolCodes.symbolCode_E_picoseconds,new Term[]{argument});
		}
	},
	FEMTOSECONDS {
		public BigDecimal toNanosecondsBigDecimal(BigDecimal decimalValue) {
			return decimalValue.divide(oneMillionBig,MathContext.DECIMAL128);
		}
		public BigDecimal toNanosecondsBigDecimal(double realValue) {
			return new BigDecimal(realValue / 1_000_000,MathContext.DECIMAL128);
		}
		public long toNanosecondsLong(BigDecimal decimalValue) {
			return PrologInteger.toLong(decimalValue.divide(oneMillionBig,MathContext.DECIMAL128));
		}
		public long toNanosecondsLong(double realValue) {
			return PrologInteger.toLong(realValue / 1_000_000.0);
		}
		public long toMillisecondsLong(BigDecimal decimalValue) {
			return PrologInteger.toLong(decimalValue.divide(onePicoBig,MathContext.DECIMAL128));
		}
		public long toMillisecondsLong(double realValue) {
			return PrologInteger.toLong(realValue / 1_000_000_000_000.0);
		}
		public int toMillisecondsInteger(BigDecimal decimalValue) {
			return PrologInteger.toInteger(decimalValue.divide(onePicoBig,MathContext.DECIMAL128));
		}
		public int toMillisecondsInteger(double realValue) {
			return PrologInteger.toInteger(realValue / 1_000_000_000_000.0);
		}
		public Term toTerm(Term argument) {
			return new PrologStructure(SymbolCodes.symbolCode_E_femtoseconds,new Term[]{argument});
		}
	},
	MINUTES {
		public BigDecimal toNanosecondsBigDecimal(BigDecimal decimalValue) {
			return decimalValue.multiply(oneMinuteInSecondsBig).multiply(oneNanoBig);
		}
		public BigDecimal toNanosecondsBigDecimal(double realValue) {
			return new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneMinuteInSecondsBig).multiply(oneNanoBig);
		}
		public long toNanosecondsLong(BigDecimal decimalValue) {
			return PrologInteger.toLong(decimalValue.multiply(oneMinuteInSecondsBig).multiply(oneNanoBig));
		}
		public long toNanosecondsLong(double realValue) {
			return PrologInteger.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneMinuteInSecondsBig).multiply(oneNanoBig));
		}
		public long toMillisecondsLong(BigDecimal decimalValue) {
			return PrologInteger.toLong(decimalValue.multiply(oneMinuteInSecondsBig).multiply(oneThousandBig));
		}
		public long toMillisecondsLong(double realValue) {
			return PrologInteger.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneMinuteInSecondsBig).multiply(oneThousandBig));
		}
		public int toMillisecondsInteger(BigDecimal decimalValue) {
			return PrologInteger.toInteger(decimalValue.multiply(oneMinuteInSecondsBig).multiply(oneThousandBig));
		}
		public int toMillisecondsInteger(double realValue) {
			return PrologInteger.toInteger(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneMinuteInSecondsBig).multiply(oneThousandBig));
		}
		public Term toTerm(Term argument) {
			return new PrologStructure(SymbolCodes.symbolCode_E_minutes,new Term[]{argument});
		}
	},
	HOURS {
		public BigDecimal toNanosecondsBigDecimal(BigDecimal decimalValue) {
			return decimalValue.multiply(oneHourInSecondsBig).multiply(oneNanoBig);
		}
		public BigDecimal toNanosecondsBigDecimal(double realValue) {
			return new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneHourInSecondsBig).multiply(oneNanoBig);
		}
		public long toNanosecondsLong(BigDecimal decimalValue) {
			return PrologInteger.toLong(decimalValue.multiply(oneHourInSecondsBig).multiply(oneNanoBig));
		}
		public long toNanosecondsLong(double realValue) {
			return PrologInteger.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneHourInSecondsBig).multiply(oneNanoBig));
		}
		public long toMillisecondsLong(BigDecimal decimalValue) {
			return PrologInteger.toLong(decimalValue.multiply(oneHourInSecondsBig).multiply(oneThousandBig));
		}
		public long toMillisecondsLong(double realValue) {
			return PrologInteger.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneHourInSecondsBig).multiply(oneThousandBig));
		}
		public int toMillisecondsInteger(BigDecimal decimalValue) {
			return PrologInteger.toInteger(decimalValue.multiply(oneHourInSecondsBig).multiply(oneThousandBig));
		}
		public int toMillisecondsInteger(double realValue) {
			return PrologInteger.toInteger(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneHourInSecondsBig).multiply(oneThousandBig));
		}
		public Term toTerm(Term argument) {
			return new PrologStructure(SymbolCodes.symbolCode_E_hours,new Term[]{argument});
		}
	},
	DAYS {
		public BigDecimal toNanosecondsBigDecimal(BigDecimal decimalValue) {
			return decimalValue.multiply(oneDayLengthInSecondsBigDecimal).multiply(oneNanoBig);
		}
		public BigDecimal toNanosecondsBigDecimal(double realValue) {
			return new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneDayLengthInSecondsBigDecimal).multiply(oneNanoBig);
		}
		public long toNanosecondsLong(BigDecimal decimalValue) {
			return PrologInteger.toLong(decimalValue.multiply(oneDayLengthInSecondsBigDecimal).multiply(oneNanoBig));
		}
		public long toNanosecondsLong(double realValue) {
			return PrologInteger.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneDayLengthInSecondsBigDecimal).multiply(oneNanoBig));
		}
		public long toMillisecondsLong(BigDecimal decimalValue) {
			return PrologInteger.toLong(decimalValue.multiply(oneDayLengthInSecondsBigDecimal).multiply(oneThousandBig));
		}
		public long toMillisecondsLong(double realValue) {
			return PrologInteger.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneDayLengthInSecondsBigDecimal).multiply(oneThousandBig));
		}
		public int toMillisecondsInteger(BigDecimal decimalValue) {
			return PrologInteger.toInteger(decimalValue.multiply(oneDayLengthInSecondsBigDecimal).multiply(oneThousandBig));
		}
		public int toMillisecondsInteger(double realValue) {
			return PrologInteger.toInteger(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneDayLengthInSecondsBigDecimal).multiply(oneThousandBig));
		}
		public Term toTerm(Term argument) {
			return new PrologStructure(SymbolCodes.symbolCode_E_days,new Term[]{argument});
		}
	},
	WEEKS {
		public BigDecimal toNanosecondsBigDecimal(BigDecimal decimalValue) {
			return decimalValue.multiply(oneWeekLengthInSecondsBigDecimal).multiply(oneNanoBig);
		}
		public BigDecimal toNanosecondsBigDecimal(double realValue) {
			return new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneWeekLengthInSecondsBigDecimal).multiply(oneNanoBig);
		}
		public long toNanosecondsLong(BigDecimal decimalValue) {
			return PrologInteger.toLong(decimalValue.multiply(oneWeekLengthInSecondsBigDecimal).multiply(oneNanoBig));
		}
		public long toNanosecondsLong(double realValue) {
			return PrologInteger.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneWeekLengthInSecondsBigDecimal).multiply(oneNanoBig));
		}
		public long toMillisecondsLong(BigDecimal decimalValue) {
			return PrologInteger.toLong(decimalValue.multiply(oneWeekLengthInSecondsBigDecimal).multiply(oneThousandBig));
		}
		public long toMillisecondsLong(double realValue) {
			return PrologInteger.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneWeekLengthInSecondsBigDecimal).multiply(oneThousandBig));
		}
		public int toMillisecondsInteger(BigDecimal decimalValue) {
			return PrologInteger.toInteger(decimalValue.multiply(oneWeekLengthInSecondsBigDecimal).multiply(oneThousandBig));
		}
		public int toMillisecondsInteger(double realValue) {
			return PrologInteger.toInteger(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneWeekLengthInSecondsBigDecimal).multiply(oneThousandBig));
		}
		public Term toTerm(Term argument) {
			return new PrologStructure(SymbolCodes.symbolCode_E_weeks,new Term[]{argument});
		}
	},
	MONTHS {
		public BigDecimal toNanosecondsBigDecimal(BigDecimal decimalValue) {
			return decimalValue.multiply(oneMonthLengthInSecondsBigInteger).multiply(oneNanoBig);
		}
		public BigDecimal toNanosecondsBigDecimal(double realValue) {
			return new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneMonthLengthInSecondsBigInteger).multiply(oneNanoBig);
		}
		public long toNanosecondsLong(BigDecimal decimalValue) {
			return PrologInteger.toLong(decimalValue.multiply(oneMonthLengthInSecondsBigInteger).multiply(oneNanoBig));
		}
		public long toNanosecondsLong(double realValue) {
			return PrologInteger.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneMonthLengthInSecondsBigInteger).multiply(oneNanoBig));
		}
		public long toMillisecondsLong(BigDecimal decimalValue) {
			return PrologInteger.toLong(decimalValue.multiply(oneMonthLengthInSecondsBigInteger).multiply(oneThousandBig));
		}
		public long toMillisecondsLong(double realValue) {
			return PrologInteger.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneMonthLengthInSecondsBigInteger).multiply(oneThousandBig));
		}
		public int toMillisecondsInteger(BigDecimal decimalValue) {
			return PrologInteger.toInteger(decimalValue.multiply(oneMonthLengthInSecondsBigInteger).multiply(oneThousandBig));
		}
		public int toMillisecondsInteger(double realValue) {
			return PrologInteger.toInteger(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneMonthLengthInSecondsBigInteger).multiply(oneThousandBig));
		}
		public Term toTerm(Term argument) {
			return new PrologStructure(SymbolCodes.symbolCode_E_months,new Term[]{argument});
		}
	},
	YEARS {
		public BigDecimal toNanosecondsBigDecimal(BigDecimal decimalValue) {
			return decimalValue.multiply(oneYearLengthInSecondsBigInteger).multiply(oneNanoBig);
		}
		public BigDecimal toNanosecondsBigDecimal(double realValue) {
			return new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneYearLengthInSecondsBigInteger).multiply(oneNanoBig);
		}
		public long toNanosecondsLong(BigDecimal decimalValue) {
			return PrologInteger.toLong(decimalValue.multiply(oneYearLengthInSecondsBigInteger).multiply(oneNanoBig));
		}
		public long toNanosecondsLong(double realValue) {
			return PrologInteger.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneYearLengthInSecondsBigInteger).multiply(oneNanoBig));
		}
		public long toMillisecondsLong(BigDecimal decimalValue) {
			return PrologInteger.toLong(decimalValue.multiply(oneYearLengthInSecondsBigInteger).multiply(oneThousandBig));
		}
		public long toMillisecondsLong(double realValue) {
			return PrologInteger.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneYearLengthInSecondsBigInteger).multiply(oneThousandBig));
		}
		public int toMillisecondsInteger(BigDecimal decimalValue) {
			return PrologInteger.toInteger(decimalValue.multiply(oneYearLengthInSecondsBigInteger).multiply(oneThousandBig));
		}
		public int toMillisecondsInteger(double realValue) {
			return PrologInteger.toInteger(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(oneYearLengthInSecondsBigInteger).multiply(oneThousandBig));
		}
		public Term toTerm(Term argument) {
			return new PrologStructure(SymbolCodes.symbolCode_E_years,new Term[]{argument});
		}
	};
	//
	///////////////////////////////////////////////////////////////
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
	public static long oneDayLengthInSecondsLong				= 24 * 60 * 60; // 86_400
	public static long oneDayLengthInMillisecondsLong			= oneDayLengthInSecondsLong * 1000; // 86_400_000
	public static long oneWeekLengthInSecondsLong				= 7 * 24 * 60 * 60; // 604_800
	public static double oneYearLengthInSecondsDouble			= 31556925.9747;
	public static double oneMonthLengthInSecondsDouble			= 31556925.9747 / 12;
	//
	public static final BigDecimal oneMinuteInSecondsBig			= BigDecimal.valueOf(60);
	public static final BigDecimal oneHourInSecondsBig			= BigDecimal.valueOf(60*60); // 3_600
	public static BigInteger oneDayLengthInMillisecondsBigInteger		= BigInteger.valueOf(oneDayLengthInMillisecondsLong);
	public static BigDecimal oneDayLengthInSecondsBigDecimal		= BigDecimal.valueOf(oneDayLengthInSecondsLong);
	public static BigDecimal oneWeekLengthInSecondsBigDecimal		= BigDecimal.valueOf(oneWeekLengthInSecondsLong);
	public static final BigDecimal oneYearLengthInSecondsBigInteger		= new BigDecimal(oneYearLengthInSecondsDouble);
	public static final BigDecimal oneMonthLengthInSecondsBigInteger	= new BigDecimal(oneMonthLengthInSecondsDouble);
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public BigDecimal toNanosecondsBigDecimal(BigDecimal value);
	abstract public BigDecimal toNanosecondsBigDecimal(double value);
	abstract public long toNanosecondsLong(BigDecimal value);
	abstract public long toNanosecondsLong(double value);
	abstract public long toMillisecondsLong(BigDecimal value);
	abstract public long toMillisecondsLong(double value);
	abstract public int toMillisecondsInteger(BigDecimal value);
	abstract public int toMillisecondsInteger(double value);
	abstract public Term toTerm(Term argument);
}
