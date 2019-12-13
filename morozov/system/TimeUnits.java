// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.system.converters.*;

import java.math.MathContext;
import java.math.BigDecimal;

public enum TimeUnits {
	//
	SECONDS {
		@Override
		public BigDecimal toNanosecondsBigDecimal(BigDecimal decimalValue) {
			return decimalValue.multiply(TimeUnitsConverters.oneNanoBig);
		}
		@Override
		public BigDecimal toNanosecondsBigDecimal(double realValue) {
			return new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneNanoBig);
		}
		@Override
		public long toNanosecondsLong(BigDecimal decimalValue) {
			return Arithmetic.toLong(decimalValue.multiply(TimeUnitsConverters.oneNanoBig));
		}
		@Override
		public long toNanosecondsLong(double realValue) {
			return Arithmetic.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneNanoBig));
		}
		@Override
		public long toMillisecondsLong(BigDecimal decimalValue) {
			return Arithmetic.toLong(decimalValue.multiply(TimeUnitsConverters.oneThousandBig));
		}
		@Override
		public long toMillisecondsLong(double realValue) {
			return Arithmetic.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneThousandBig));
		}
		@Override
		public int toMillisecondsInteger(BigDecimal decimalValue) {
			return Arithmetic.toInteger(decimalValue.multiply(TimeUnitsConverters.oneThousandBig));
		}
		@Override
		public int toMillisecondsInteger(double realValue) {
			return Arithmetic.toInteger(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneThousandBig));
		}
	},
	MILLISECONDS {
		@Override
		public BigDecimal toNanosecondsBigDecimal(BigDecimal decimalValue) {
			return decimalValue.multiply(TimeUnitsConverters.oneMillionBig);
		}
		@Override
		public BigDecimal toNanosecondsBigDecimal(double realValue) {
			return new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneMillionBig);
		}
		@Override
		public long toNanosecondsLong(BigDecimal decimalValue) {
			return Arithmetic.toLong(decimalValue.multiply(TimeUnitsConverters.oneMillionBig));
		}
		@Override
		public long toNanosecondsLong(double realValue) {
			return Arithmetic.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneMillionBig));
		}
		@Override
		public long toMillisecondsLong(BigDecimal decimalValue) {
			return Arithmetic.toLong(decimalValue);
		}
		@Override
		public long toMillisecondsLong(double realValue) {
			return Arithmetic.toLong(realValue);
		}
		@Override
		public int toMillisecondsInteger(BigDecimal decimalValue) {
			return Arithmetic.toInteger(decimalValue);
		}
		@Override
		public int toMillisecondsInteger(double realValue) {
			return Arithmetic.toInteger(realValue);
		}
	},
	MICROSECONDS {
		@Override
		public BigDecimal toNanosecondsBigDecimal(BigDecimal decimalValue) {
			return decimalValue.multiply(TimeUnitsConverters.oneThousandBig);
		}
		@Override
		public BigDecimal toNanosecondsBigDecimal(double realValue) {
			return new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneThousandBig);
		}
		@Override
		public long toNanosecondsLong(BigDecimal decimalValue) {
			return Arithmetic.toLong(decimalValue.multiply(TimeUnitsConverters.oneThousandBig));
		}
		@Override
		public long toNanosecondsLong(double realValue) {
			return Arithmetic.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneThousandBig));
		}
		@Override
		public long toMillisecondsLong(BigDecimal decimalValue) {
			return Arithmetic.toLong(decimalValue.divide(TimeUnitsConverters.oneThousandBig,MathContext.DECIMAL128));
		}
		@Override
		public long toMillisecondsLong(double realValue) {
			return Arithmetic.toLong(realValue / 1000.0);
		}
		@Override
		public int toMillisecondsInteger(BigDecimal decimalValue) {
			return Arithmetic.toInteger(decimalValue.divide(TimeUnitsConverters.oneThousandBig,MathContext.DECIMAL128));
		}
		@Override
		public int toMillisecondsInteger(double realValue) {
			return Arithmetic.toInteger(realValue / 1000.0);
		}
	},
	NANOSECONDS {
		@Override
		public BigDecimal toNanosecondsBigDecimal(BigDecimal decimalValue) {
			return decimalValue;
		}
		@Override
		public BigDecimal toNanosecondsBigDecimal(double realValue) {
			return new BigDecimal(realValue,MathContext.DECIMAL128);
		}
		@Override
		public long toNanosecondsLong(BigDecimal decimalValue) {
			return Arithmetic.toLong(decimalValue);
		}
		@Override
		public long toNanosecondsLong(double realValue) {
			return Arithmetic.toLong(realValue);
		}
		@Override
		public long toMillisecondsLong(BigDecimal decimalValue) {
			return Arithmetic.toLong(decimalValue.divide(TimeUnitsConverters.oneMillionBig,MathContext.DECIMAL128));
		}
		@Override
		public long toMillisecondsLong(double realValue) {
			return Arithmetic.toLong(realValue / 1_000_000.0);
		}
		@Override
		public int toMillisecondsInteger(BigDecimal decimalValue) {
			return Arithmetic.toInteger(decimalValue.divide(TimeUnitsConverters.oneMillionBig,MathContext.DECIMAL128));
		}
		@Override
		public int toMillisecondsInteger(double realValue) {
			return Arithmetic.toInteger(realValue / 1_000_000.0);
		}
	},
	PICOSECONDS {
		@Override
		public BigDecimal toNanosecondsBigDecimal(BigDecimal decimalValue) {
			return decimalValue.divide(TimeUnitsConverters.oneThousandBig,MathContext.DECIMAL128);
		}
		@Override
		public BigDecimal toNanosecondsBigDecimal(double realValue) {
			return new BigDecimal(realValue / 1000,MathContext.DECIMAL128);
		}
		@Override
		public long toNanosecondsLong(BigDecimal decimalValue) {
			return Arithmetic.toLong(decimalValue.divide(TimeUnitsConverters.oneThousandBig,MathContext.DECIMAL128));
		}
		@Override
		public long toNanosecondsLong(double realValue) {
			return Arithmetic.toLong(realValue / 1000.0);
		}
		@Override
		public long toMillisecondsLong(BigDecimal decimalValue) {
			return Arithmetic.toLong(decimalValue.divide(TimeUnitsConverters.oneNanoBig,MathContext.DECIMAL128));
		}
		@Override
		public long toMillisecondsLong(double realValue) {
			return Arithmetic.toLong(realValue / 1_000_000_000.0);
		}
		@Override
		public int toMillisecondsInteger(BigDecimal decimalValue) {
			return Arithmetic.toInteger(decimalValue.divide(TimeUnitsConverters.oneNanoBig,MathContext.DECIMAL128));
		}
		@Override
		public int toMillisecondsInteger(double realValue) {
			return Arithmetic.toInteger(realValue / 1_000_000_000.0);
		}
	},
	FEMTOSECONDS {
		@Override
		public BigDecimal toNanosecondsBigDecimal(BigDecimal decimalValue) {
			return decimalValue.divide(TimeUnitsConverters.oneMillionBig,MathContext.DECIMAL128);
		}
		@Override
		public BigDecimal toNanosecondsBigDecimal(double realValue) {
			return new BigDecimal(realValue / 1_000_000,MathContext.DECIMAL128);
		}
		@Override
		public long toNanosecondsLong(BigDecimal decimalValue) {
			return Arithmetic.toLong(decimalValue.divide(TimeUnitsConverters.oneMillionBig,MathContext.DECIMAL128));
		}
		@Override
		public long toNanosecondsLong(double realValue) {
			return Arithmetic.toLong(realValue / 1_000_000.0);
		}
		@Override
		public long toMillisecondsLong(BigDecimal decimalValue) {
			return Arithmetic.toLong(decimalValue.divide(TimeUnitsConverters.onePicoBig,MathContext.DECIMAL128));
		}
		@Override
		public long toMillisecondsLong(double realValue) {
			return Arithmetic.toLong(realValue / 1_000_000_000_000.0);
		}
		@Override
		public int toMillisecondsInteger(BigDecimal decimalValue) {
			return Arithmetic.toInteger(decimalValue.divide(TimeUnitsConverters.onePicoBig,MathContext.DECIMAL128));
		}
		@Override
		public int toMillisecondsInteger(double realValue) {
			return Arithmetic.toInteger(realValue / 1_000_000_000_000.0);
		}
	},
	MINUTES {
		@Override
		public BigDecimal toNanosecondsBigDecimal(BigDecimal decimalValue) {
			return decimalValue.multiply(TimeUnitsConverters.oneMinuteInSecondsBig).multiply(TimeUnitsConverters.oneNanoBig);
		}
		@Override
		public BigDecimal toNanosecondsBigDecimal(double realValue) {
			return new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneMinuteInSecondsBig).multiply(TimeUnitsConverters.oneNanoBig);
		}
		@Override
		public long toNanosecondsLong(BigDecimal decimalValue) {
			return Arithmetic.toLong(decimalValue.multiply(TimeUnitsConverters.oneMinuteInSecondsBig).multiply(TimeUnitsConverters.oneNanoBig));
		}
		@Override
		public long toNanosecondsLong(double realValue) {
			return Arithmetic.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneMinuteInSecondsBig).multiply(TimeUnitsConverters.oneNanoBig));
		}
		@Override
		public long toMillisecondsLong(BigDecimal decimalValue) {
			return Arithmetic.toLong(decimalValue.multiply(TimeUnitsConverters.oneMinuteInSecondsBig).multiply(TimeUnitsConverters.oneThousandBig));
		}
		@Override
		public long toMillisecondsLong(double realValue) {
			return Arithmetic.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneMinuteInSecondsBig).multiply(TimeUnitsConverters.oneThousandBig));
		}
		@Override
		public int toMillisecondsInteger(BigDecimal decimalValue) {
			return Arithmetic.toInteger(decimalValue.multiply(TimeUnitsConverters.oneMinuteInSecondsBig).multiply(TimeUnitsConverters.oneThousandBig));
		}
		@Override
		public int toMillisecondsInteger(double realValue) {
			return Arithmetic.toInteger(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneMinuteInSecondsBig).multiply(TimeUnitsConverters.oneThousandBig));
		}
	},
	HOURS {
		@Override
		public BigDecimal toNanosecondsBigDecimal(BigDecimal decimalValue) {
			return decimalValue.multiply(TimeUnitsConverters.oneHourInSecondsBig).multiply(TimeUnitsConverters.oneNanoBig);
		}
		@Override
		public BigDecimal toNanosecondsBigDecimal(double realValue) {
			return new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneHourInSecondsBig).multiply(TimeUnitsConverters.oneNanoBig);
		}
		@Override
		public long toNanosecondsLong(BigDecimal decimalValue) {
			return Arithmetic.toLong(decimalValue.multiply(TimeUnitsConverters.oneHourInSecondsBig).multiply(TimeUnitsConverters.oneNanoBig));
		}
		@Override
		public long toNanosecondsLong(double realValue) {
			return Arithmetic.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneHourInSecondsBig).multiply(TimeUnitsConverters.oneNanoBig));
		}
		@Override
		public long toMillisecondsLong(BigDecimal decimalValue) {
			return Arithmetic.toLong(decimalValue.multiply(TimeUnitsConverters.oneHourInSecondsBig).multiply(TimeUnitsConverters.oneThousandBig));
		}
		@Override
		public long toMillisecondsLong(double realValue) {
			return Arithmetic.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneHourInSecondsBig).multiply(TimeUnitsConverters.oneThousandBig));
		}
		@Override
		public int toMillisecondsInteger(BigDecimal decimalValue) {
			return Arithmetic.toInteger(decimalValue.multiply(TimeUnitsConverters.oneHourInSecondsBig).multiply(TimeUnitsConverters.oneThousandBig));
		}
		@Override
		public int toMillisecondsInteger(double realValue) {
			return Arithmetic.toInteger(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneHourInSecondsBig).multiply(TimeUnitsConverters.oneThousandBig));
		}
	},
	DAYS {
		@Override
		public BigDecimal toNanosecondsBigDecimal(BigDecimal decimalValue) {
			return decimalValue.multiply(TimeUnitsConverters.oneDayLengthInSecondsBigDecimal).multiply(TimeUnitsConverters.oneNanoBig);
		}
		@Override
		public BigDecimal toNanosecondsBigDecimal(double realValue) {
			return new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneDayLengthInSecondsBigDecimal).multiply(TimeUnitsConverters.oneNanoBig);
		}
		@Override
		public long toNanosecondsLong(BigDecimal decimalValue) {
			return Arithmetic.toLong(decimalValue.multiply(TimeUnitsConverters.oneDayLengthInSecondsBigDecimal).multiply(TimeUnitsConverters.oneNanoBig));
		}
		@Override
		public long toNanosecondsLong(double realValue) {
			return Arithmetic.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneDayLengthInSecondsBigDecimal).multiply(TimeUnitsConverters.oneNanoBig));
		}
		@Override
		public long toMillisecondsLong(BigDecimal decimalValue) {
			return Arithmetic.toLong(decimalValue.multiply(TimeUnitsConverters.oneDayLengthInSecondsBigDecimal).multiply(TimeUnitsConverters.oneThousandBig));
		}
		@Override
		public long toMillisecondsLong(double realValue) {
			return Arithmetic.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneDayLengthInSecondsBigDecimal).multiply(TimeUnitsConverters.oneThousandBig));
		}
		@Override
		public int toMillisecondsInteger(BigDecimal decimalValue) {
			return Arithmetic.toInteger(decimalValue.multiply(TimeUnitsConverters.oneDayLengthInSecondsBigDecimal).multiply(TimeUnitsConverters.oneThousandBig));
		}
		@Override
		public int toMillisecondsInteger(double realValue) {
			return Arithmetic.toInteger(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneDayLengthInSecondsBigDecimal).multiply(TimeUnitsConverters.oneThousandBig));
		}
	},
	WEEKS {
		@Override
		public BigDecimal toNanosecondsBigDecimal(BigDecimal decimalValue) {
			return decimalValue.multiply(TimeUnitsConverters.oneWeekLengthInSecondsBigDecimal).multiply(TimeUnitsConverters.oneNanoBig);
		}
		@Override
		public BigDecimal toNanosecondsBigDecimal(double realValue) {
			return new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneWeekLengthInSecondsBigDecimal).multiply(TimeUnitsConverters.oneNanoBig);
		}
		@Override
		public long toNanosecondsLong(BigDecimal decimalValue) {
			return Arithmetic.toLong(decimalValue.multiply(TimeUnitsConverters.oneWeekLengthInSecondsBigDecimal).multiply(TimeUnitsConverters.oneNanoBig));
		}
		@Override
		public long toNanosecondsLong(double realValue) {
			return Arithmetic.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneWeekLengthInSecondsBigDecimal).multiply(TimeUnitsConverters.oneNanoBig));
		}
		@Override
		public long toMillisecondsLong(BigDecimal decimalValue) {
			return Arithmetic.toLong(decimalValue.multiply(TimeUnitsConverters.oneWeekLengthInSecondsBigDecimal).multiply(TimeUnitsConverters.oneThousandBig));
		}
		@Override
		public long toMillisecondsLong(double realValue) {
			return Arithmetic.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneWeekLengthInSecondsBigDecimal).multiply(TimeUnitsConverters.oneThousandBig));
		}
		@Override
		public int toMillisecondsInteger(BigDecimal decimalValue) {
			return Arithmetic.toInteger(decimalValue.multiply(TimeUnitsConverters.oneWeekLengthInSecondsBigDecimal).multiply(TimeUnitsConverters.oneThousandBig));
		}
		@Override
		public int toMillisecondsInteger(double realValue) {
			return Arithmetic.toInteger(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneWeekLengthInSecondsBigDecimal).multiply(TimeUnitsConverters.oneThousandBig));
		}
	},
	MONTHS {
		@Override
		public BigDecimal toNanosecondsBigDecimal(BigDecimal decimalValue) {
			return decimalValue.multiply(TimeUnitsConverters.oneMonthLengthInSecondsBigInteger).multiply(TimeUnitsConverters.oneNanoBig);
		}
		@Override
		public BigDecimal toNanosecondsBigDecimal(double realValue) {
			return new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneMonthLengthInSecondsBigInteger).multiply(TimeUnitsConverters.oneNanoBig);
		}
		@Override
		public long toNanosecondsLong(BigDecimal decimalValue) {
			return Arithmetic.toLong(decimalValue.multiply(TimeUnitsConverters.oneMonthLengthInSecondsBigInteger).multiply(TimeUnitsConverters.oneNanoBig));
		}
		@Override
		public long toNanosecondsLong(double realValue) {
			return Arithmetic.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneMonthLengthInSecondsBigInteger).multiply(TimeUnitsConverters.oneNanoBig));
		}
		@Override
		public long toMillisecondsLong(BigDecimal decimalValue) {
			return Arithmetic.toLong(decimalValue.multiply(TimeUnitsConverters.oneMonthLengthInSecondsBigInteger).multiply(TimeUnitsConverters.oneThousandBig));
		}
		@Override
		public long toMillisecondsLong(double realValue) {
			return Arithmetic.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneMonthLengthInSecondsBigInteger).multiply(TimeUnitsConverters.oneThousandBig));
		}
		@Override
		public int toMillisecondsInteger(BigDecimal decimalValue) {
			return Arithmetic.toInteger(decimalValue.multiply(TimeUnitsConverters.oneMonthLengthInSecondsBigInteger).multiply(TimeUnitsConverters.oneThousandBig));
		}
		@Override
		public int toMillisecondsInteger(double realValue) {
			return Arithmetic.toInteger(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneMonthLengthInSecondsBigInteger).multiply(TimeUnitsConverters.oneThousandBig));
		}
	},
	YEARS {
		@Override
		public BigDecimal toNanosecondsBigDecimal(BigDecimal decimalValue) {
			return decimalValue.multiply(TimeUnitsConverters.oneYearLengthInSecondsBigInteger).multiply(TimeUnitsConverters.oneNanoBig);
		}
		@Override
		public BigDecimal toNanosecondsBigDecimal(double realValue) {
			return new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneYearLengthInSecondsBigInteger).multiply(TimeUnitsConverters.oneNanoBig);
		}
		@Override
		public long toNanosecondsLong(BigDecimal decimalValue) {
			return Arithmetic.toLong(decimalValue.multiply(TimeUnitsConverters.oneYearLengthInSecondsBigInteger).multiply(TimeUnitsConverters.oneNanoBig));
		}
		@Override
		public long toNanosecondsLong(double realValue) {
			return Arithmetic.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneYearLengthInSecondsBigInteger).multiply(TimeUnitsConverters.oneNanoBig));
		}
		@Override
		public long toMillisecondsLong(BigDecimal decimalValue) {
			return Arithmetic.toLong(decimalValue.multiply(TimeUnitsConverters.oneYearLengthInSecondsBigInteger).multiply(TimeUnitsConverters.oneThousandBig));
		}
		@Override
		public long toMillisecondsLong(double realValue) {
			return Arithmetic.toLong(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneYearLengthInSecondsBigInteger).multiply(TimeUnitsConverters.oneThousandBig));
		}
		@Override
		public int toMillisecondsInteger(BigDecimal decimalValue) {
			return Arithmetic.toInteger(decimalValue.multiply(TimeUnitsConverters.oneYearLengthInSecondsBigInteger).multiply(TimeUnitsConverters.oneThousandBig));
		}
		@Override
		public int toMillisecondsInteger(double realValue) {
			return Arithmetic.toInteger(new BigDecimal(realValue,MathContext.DECIMAL128).multiply(TimeUnitsConverters.oneYearLengthInSecondsBigInteger).multiply(TimeUnitsConverters.oneThousandBig));
		}
	};
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
}
