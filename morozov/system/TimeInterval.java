// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system;

import java.math.BigDecimal;
import java.io.Serializable;

public class TimeInterval implements Serializable {
	//
	protected boolean hasRealValue= false;
	protected double realValue;
	protected BigDecimal decimalValue;
	protected TimeUnits units;
	//
	private static final long serialVersionUID= 0x77BB4E89B95F1947L; // 8627575864585820487
	//
	// static {
	//	// SerialVersionChecker.check(serialVersionUID,"morozov.system","TimeInterval");
	//	SerialVersionChecker.report("morozov.system","TimeInterval");
	// }
	//
	///////////////////////////////////////////////////////////////
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
			return units.toNanosecondsLong(realValue);
		} else {
			return units.toNanosecondsLong(decimalValue);
		}
	}
	//
	public long toMillisecondsLong() {
		if (hasRealValue) {
			return units.toMillisecondsLong(realValue);
		} else {
			return units.toMillisecondsLong(decimalValue);
		}
	}
	//
	public int toMillisecondsInteger() {
		if (hasRealValue) {
			return units.toMillisecondsInteger(realValue);
		} else {
			return units.toMillisecondsInteger(decimalValue);
		}
	}
}
