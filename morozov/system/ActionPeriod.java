// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system;

import target.*;

import morozov.run.*;
import morozov.system.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.math.BigDecimal;

public class ActionPeriod extends TimeInterval {
	//
	protected boolean isDefault= false;
	protected boolean isNone= false;
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
	protected static Term termNone= new PrologSymbol(SymbolCodes.symbolCode_E_none);
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	protected static int noActionPeriod= -1;
	protected static BigDecimal decimalNoActionPeriod= BigDecimal.valueOf(noActionPeriod);
	//
	///////////////////////////////////////////////////////////////
	//
	public static ActionPeriod argumentToActionPeriod(Term a, ChoisePoint iX) {
		try {
			long code= a.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_none) {
				return new ActionPeriod(false,true);
			} else if (code==SymbolCodes.symbolCode_E_default) {
				return new ActionPeriod(true,false);
			} else {
				throw new WrongArgumentIsNotActionPeriod(a);
			}
		} catch (TermIsNotASymbol b) {
			return new ActionPeriod(argumentSecondsToTimeInterval(a,iX));
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public BigDecimal toNanosecondsOrDefault(Term alternativeValue, BigDecimal defaultValue, ChoisePoint iX) {
		if (isDefault) {
			return ActionPeriod.argumentToActionPeriod(alternativeValue,iX).toNanosecondsOrDefault(defaultValue);
		} else if (isNone) {
			return decimalNoActionPeriod;
		} else {
			return toNanosecondsBigDecimal();
		}
	}
	//
	public BigDecimal toNanosecondsOrDefault(BigDecimal defaultValue) {
		if (isDefault) {
			return defaultValue;
		} else if (isNone) {
			return decimalNoActionPeriod;
		} else {
			return toNanosecondsBigDecimal();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int toMillisecondsOrDefault(Term defaultActionPeriod, ChoisePoint iX) {
		if (isDefault) {
			return TimeInterval.argumentSecondsToTimeInterval(defaultActionPeriod,iX).toMillisecondsInteger();
		} else if (isNone) {
			return noActionPeriod;
		} else {
			return toMillisecondsInteger();
		}
	}
	//
	public int toMillisecondsOrDefault(int defaultActionPeriod) {
		if (isDefault) {
			return defaultActionPeriod;
		} else if (isNone) {
			return noActionPeriod;
		} else {
			return toMillisecondsInteger();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		if (isDefault) {
			return termDefault;
		} else if (isNone) {
			return termNone;
		} else {
			return super.toTerm();
		}
	}
}
