// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system;

import target.*;

import morozov.run.*;
import morozov.system.errors.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.math.BigDecimal;

public class WaitingInterval extends TimeInterval {
	//
	protected boolean isDefault= false;
	protected boolean isAny= false;
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
	protected static Term termAny= new PrologSymbol(SymbolCodes.symbolCode_E_any);
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	protected static int anyActionPeriod= 0;
	protected static BigDecimal decimalAnyActionPeriod= BigDecimal.valueOf(anyActionPeriod);
	//
	///////////////////////////////////////////////////////////////
	//
	public static WaitingInterval argumentToWaitingInterval(Term a, ChoisePoint iX) {
		try {
			long code= a.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_any) {
				return new WaitingInterval(false,true);
			} else if (code==SymbolCodes.symbolCode_E_default) {
				return new WaitingInterval(true,false);
			} else {
				throw new WrongArgumentIsNotWaitingInterval(a);
			}
		} catch (TermIsNotASymbol b) {
			return new WaitingInterval(argumentSecondsToTimeInterval(a,iX));
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public BigDecimal toNanosecondsOrDefault(BigDecimal defaultValue) {
		if (isDefault) {
			return defaultValue;
		} else if (isAny) {
			return decimalAnyActionPeriod;
		} else {
			return toNanosecondsBigDecimal();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int toMillisecondsIntegerOrDefault(Term defaultWaitingInterval, ChoisePoint iX) {
		if (isDefault) {
			return TimeInterval.argumentSecondsToTimeInterval(defaultWaitingInterval,iX).toMillisecondsInteger();
		} else if (isAny) {
			return anyActionPeriod; // A timeout of zero is interpreted as an infinite timeout.
		} else {
			return toMillisecondsInteger();
		}
	}
	//
	public long toMillisecondsLongOrDefault(Term defaultWaitingInterval, ChoisePoint iX) {
		if (isDefault) {
			return TimeInterval.argumentSecondsToTimeInterval(defaultWaitingInterval,iX).toMillisecondsLong();
		} else if (isAny) {
			return anyActionPeriod; // A timeout of zero is interpreted as an infinite timeout.
		} else {
			return toMillisecondsLong();
		}
	}
	//
	public int toMillisecondsIntegerOrDefault(int defaultWaitingInterval) {
		if (isDefault) {
			return defaultWaitingInterval;
		} else if (isAny) {
			return anyActionPeriod; // A timeout of zero is interpreted as an infinite timeout.
		} else {
			return toMillisecondsInteger();
		}
	}
	//
	public long toMillisecondsLongOrDefault(long defaultWaitingInterval) {
		if (isDefault) {
			return defaultWaitingInterval;
		} else if (isAny) {
			return anyActionPeriod; // A timeout of zero is interpreted as an infinite timeout.
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
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		if (isDefault) {
			return termDefault;
		} else if (isAny) {
			return termAny;
		} else {
			return super.toTerm();
		}
	}
}
