// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.run.*;
import morozov.system.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.math.BigInteger;

public class ExtremumValue {
	//
	protected boolean isInitiated;
	protected BigInteger integerValue;
	protected double realValue;
	protected boolean hasRealValue;
	//
	public void refine(ChoisePoint iX, MultiArgumentArithmeticOperation operation, Term argument) {
		try {
			BigInteger value= argument.getIntegerValue(iX);
			if (isInitiated) {
				if (hasRealValue) {
					double newValue= value.doubleValue();
					if (operation==MultiArgumentArithmeticOperation.MAX) {
						realValue= StrictMath.max(realValue,newValue);
					} else {
						realValue= StrictMath.min(realValue,newValue);
					}
				} else {
					if (operation==MultiArgumentArithmeticOperation.MAX) {
						integerValue= integerValue.max(value);
					} else {
						integerValue= integerValue.min(value);
					}
				}
			} else {
				isInitiated= true;
				integerValue= value;
				hasRealValue= false;
			}
		} catch (TermIsNotAnInteger e1) {
			try {
				double value= argument.getRealValue(iX);
				if (isInitiated) {
					if (!hasRealValue) {
						realValue= integerValue.doubleValue();
						hasRealValue= true;
					};
					if (operation==MultiArgumentArithmeticOperation.MAX) {
						realValue= StrictMath.max(realValue,value);
					} else {
						realValue= StrictMath.min(realValue,value);
					}
				} else {
					isInitiated= true;
					realValue= value;
					hasRealValue= true;
				}
			} catch (TermIsNotAReal e2) {
				Term nextHead;
				Term currentTail= argument;
				try {
					while (true) {
						nextHead= currentTail.getNextListHead(iX);
						refine(iX,operation,nextHead);
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
	public Term value() {
		if (isInitiated) {
			if (hasRealValue) {
				return new PrologReal(realValue);
			} else {
				return new PrologInteger(integerValue);
			}
		} else {
			throw new NoArgumentsAreProvided();
		}
	}
}
