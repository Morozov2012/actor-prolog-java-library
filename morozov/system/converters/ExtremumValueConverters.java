// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.converters;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.errors.*;
import morozov.system.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.math.BigInteger;

public class ExtremumValueConverters {
	//
	public static void refine(ExtremumValue extremum, ChoisePoint iX, MultiArgumentArithmeticOperation operation, Term argument) {
		try {
			BigInteger givenIntegerValue= argument.getIntegerValue(iX);
			if (extremum.isInitiated()) {
				if (extremum.hasRealValue()) {
					double givenDoubleValue= givenIntegerValue.doubleValue();
					if (operation==MultiArgumentArithmeticOperation.MAX) {
						extremum.refineRealMaximum(givenDoubleValue);
					} else {
						extremum.refineRealMinimum(givenDoubleValue);
					}
				} else {
					if (operation==MultiArgumentArithmeticOperation.MAX) {
						extremum.refineIntegerMaximum(givenIntegerValue);
					} else {
						extremum.refineIntegerMinimum(givenIntegerValue);
					}
				}
			} else {
				extremum.setIsInitiated(true);
				extremum.setIntegerValue(givenIntegerValue);
				extremum.setHasRealValue(false);
			}
		} catch (TermIsNotAnInteger e1) {
			try {
				double givenDoubleValue= argument.getRealValue(iX);
				if (extremum.isInitiated()) {
					if (!extremum.hasRealValue()) {
						extremum.setRealValue(extremum.getIntegerValue().doubleValue());
						extremum.setHasRealValue(true);
					};
					if (operation==MultiArgumentArithmeticOperation.MAX) {
						extremum.refineRealMaximum(givenDoubleValue);
					} else {
						extremum.refineRealMinimum(givenDoubleValue);
					}
				} else {
					extremum.setIsInitiated(true);
					extremum.setRealValue(givenDoubleValue);
					extremum.setHasRealValue(true);
				}
			} catch (TermIsNotAReal e2) {
				Term nextHead;
				Term currentTail= argument;
				try {
					while (true) {
						nextHead= currentTail.getNextListHead(iX);
						refine(extremum,iX,operation,nextHead);
						currentTail= currentTail.getNextListTail(iX);
					}
				} catch (EndOfList e3) {
				} catch (TermIsNotAList e3) {
					throw new WrongArgumentIsNotAList(argument);
				}
			}
		}
	}
	//
	public static Term value(ExtremumValue extremum) {
		if (extremum.isInitiated()) {
			if (extremum.hasRealValue()) {
				return new PrologReal(extremum.getRealValue());
			} else {
				return new PrologInteger(extremum.getIntegerValue());
			}
		} else {
			throw new NoArgumentsAreProvided();
		}
	}
}
