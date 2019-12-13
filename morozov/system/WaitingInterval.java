// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.system.converters.*;
import morozov.system.signals.*;

import java.math.BigDecimal;

public class WaitingInterval extends TimeInterval {
	//
	protected boolean isDefault= false;
	protected boolean isAny= false;
	//
	private static final long serialVersionUID= 0x296F8CC7491D1062L; // 2985759865522950242
	//
	// static {
	//	// SerialVersionChecker.check(serialVersionUID,"morozov.system","WaitingInterval");
	//	SerialVersionChecker.report("morozov.system","WaitingInterval");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public WaitingInterval(boolean d, boolean a) {
		super(false,0.0,null,null);
		isDefault= d;
		isAny= a;
	}
	public WaitingInterval(double value) {
		super(value);
	}
	public WaitingInterval(BigDecimal value) {
		super(value);
	}
	public WaitingInterval(TimeInterval timeInterval) {
		super(	timeInterval.hasRealValue(),
			timeInterval.getRealValue(),
			timeInterval.getDecimalValue(),
			timeInterval.getUnits());
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean isDefault() {
		return isDefault;
	}
	public boolean isAny() {
		return isAny;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public BigDecimal toNanosecondsOrDefault(BigDecimal defaultValue) {
		if (isDefault) {
			return defaultValue;
		} else if (isAny) {
			return WaitingIntervalConverters.decimalAnyActionPeriod;
		} else {
			return toNanosecondsBigDecimal();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int toMillisecondsIntegerOrDefault(int defaultWaitingInterval) {
		if (isDefault) {
			return defaultWaitingInterval;
		} else if (isAny) {
			return WaitingIntervalConverters.anyActionPeriod; // A timeout of zero is interpreted as an infinite timeout.
		} else {
			return toMillisecondsInteger();
		}
	}
	//
	public long toMillisecondsLongOrDefault(long defaultWaitingInterval) {
		if (isDefault) {
			return defaultWaitingInterval;
		} else if (isAny) {
			return WaitingIntervalConverters.anyActionPeriod; // A timeout of zero is interpreted as an infinite timeout.
		} else {
			return toMillisecondsLong();
		}
	}
	//
	public long toMillisecondsLongOrDefault() throws UseDefaultInterval, UseAnyInterval {
		if (isDefault) {
			throw UseDefaultInterval.instance;
		} else if (isAny) {
			throw UseAnyInterval.instance;
		} else {
			return toMillisecondsLong();
		}
	}
}
