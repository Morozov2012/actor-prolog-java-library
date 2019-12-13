// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.converters;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.math.BigDecimal;

public class ActionPeriodConverters extends TimeIntervalConverters {
	//
	public static final Term termNone= new PrologSymbol(SymbolCodes.symbolCode_E_none);
	public static final Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	public static final int noActionPeriod= -1;
	public static final BigDecimal decimalNoActionPeriod= BigDecimal.valueOf(noActionPeriod);
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
	public static BigDecimal toNanosecondsOrDefault(ActionPeriod period, Term alternativeValue, BigDecimal defaultValue, ChoisePoint iX) {
		if (period.isDefault()) {
			return ActionPeriodConverters.argumentToActionPeriod(alternativeValue,iX).toNanosecondsOrDefault(defaultValue);
		} else if (period.isNone()) {
			return decimalNoActionPeriod;
		} else {
			return period.toNanosecondsBigDecimal();
		}
	}
	//
	public static int toMillisecondsOrDefault(ActionPeriod period, Term defaultActionPeriod, ChoisePoint iX) {
		if (period.isDefault()) {
			return TimeIntervalConverters.argumentSecondsToTimeInterval(defaultActionPeriod,iX).toMillisecondsInteger();
		} else if (period.isNone()) {
			return noActionPeriod;
		} else {
			return period.toMillisecondsInteger();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(ActionPeriod period) {
		if (period.isDefault()) {
			return termDefault;
		} else if (period.isNone()) {
			return termNone;
		} else {
			return TimeIntervalConverters.toTerm(period);
		}
	}
}
