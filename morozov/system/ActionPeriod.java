// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.system.converters.*;

import java.math.BigDecimal;

public class ActionPeriod extends TimeInterval {
	//
	protected boolean isDefault= false;
	protected boolean isNone= false;
	//
	private static final long serialVersionUID= 0x67DAC621296A337L; // 467719473535034167
	//
	// static {
	//	// SerialVersionChecker.check(serialVersionUID,"morozov.system","ActionPeriod");
	//	SerialVersionChecker.report("morozov.system","ActionPeriod");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public ActionPeriod(boolean d, boolean n) {
		super(false,0.0,null,null);
		isDefault= d;
		isNone= n;
	}
	public ActionPeriod(double value) {
		super(value);
	}
	public ActionPeriod(double value, TimeUnits u) {
		super(value,u);
	}
	public ActionPeriod(BigDecimal value) {
		super(value);
	}
	public ActionPeriod(BigDecimal value, TimeUnits u) {
		super(value,u);
	}
	public ActionPeriod(TimeInterval timeInterval) {
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
	public boolean isNone() {
		return isNone;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public BigDecimal toNanosecondsOrDefault(BigDecimal defaultValue) {
		if (isDefault) {
			return defaultValue;
		} else if (isNone) {
			return ActionPeriodConverters.decimalNoActionPeriod;
		} else {
			return toNanosecondsBigDecimal();
		}
	}
	//
	public int toMillisecondsOrDefault(int defaultActionPeriod) {
		if (isDefault) {
			return defaultActionPeriod;
		} else if (isNone) {
			return ActionPeriodConverters.noActionPeriod;
		} else {
			return toMillisecondsInteger();
		}
	}
}
