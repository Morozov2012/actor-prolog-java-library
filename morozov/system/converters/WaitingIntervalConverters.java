// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.converters;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.math.BigDecimal;

public class WaitingIntervalConverters extends TimeIntervalConverters {
	//
	public static final Term termAny= new PrologSymbol(SymbolCodes.symbolCode_E_any);
	public static final Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	public static final int anyActionPeriod= 0;
	public static final BigDecimal decimalAnyActionPeriod= BigDecimal.valueOf(anyActionPeriod);
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
	public static int toMillisecondsIntegerOrDefault(WaitingInterval interval, Term defaultWaitingInterval, ChoisePoint iX) {
		if (interval.isDefault()) {
			return argumentSecondsToTimeInterval(defaultWaitingInterval,iX).toMillisecondsInteger();
		} else if (interval.isAny()) {
			return anyActionPeriod; // A timeout of zero is interpreted as an infinite timeout.
		} else {
			return interval.toMillisecondsInteger();
		}
	}
	//
	public static long toMillisecondsLongOrDefault(WaitingInterval interval, Term defaultWaitingInterval, ChoisePoint iX) {
		if (interval.isDefault()) {
			return argumentSecondsToTimeInterval(defaultWaitingInterval,iX).toMillisecondsLong();
		} else if (interval.isAny()) {
			return anyActionPeriod; // A timeout of zero is interpreted as an infinite timeout.
		} else {
			return interval.toMillisecondsLong();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(WaitingInterval interval) {
		if (interval.isDefault()) {
			return termDefault;
		} else if (interval.isAny()) {
			return termAny;
		} else {
			return toTerm(interval);
		}
	}
}
