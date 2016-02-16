// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.worlds.*;
import morozov.terms.*;

import java.math.BigDecimal;
import java.math.MathContext;

public class SystemUtils {
	public static int indexOf(String text, String target, int currentPosition, boolean caseSensitivity) {
		if (caseSensitivity) {
			return text.indexOf(target,currentPosition);
		} else {
			int targetLength= target.length();
			if (targetLength==0) {
				if (currentPosition <= text.length()) {
					return currentPosition;
				} else {
					return -1;
				}
			} else {
				for (int n=currentPosition; n < text.length()-targetLength+1; n++) {
					if (text.substring(n,n+targetLength).compareToIgnoreCase(target) == 0) {
						return n;
					}
				};
				return -1;
			}
		}
	}
	public static void sleep(BigDecimal nanos, ActiveWorld currentProcess) {
		try {
			BigDecimal milliseconds= nanos.divideToIntegralValue(TimeUnits.oneMillionBig,MathContext.DECIMAL128);
			BigDecimal remainder= nanos.subtract(milliseconds.multiply(TimeUnits.oneMillionBig));
			int delayInMilliseconds= PrologInteger.toInteger(milliseconds);
			int delayInNanos= PrologInteger.toInteger(remainder);
			if (delayInMilliseconds >= 0) {
				if (delayInNanos > 0) {
					if (delayInNanos <= 999999) {
						currentProcess.thread.sleep(delayInMilliseconds,delayInNanos);
					} else {
						delayInMilliseconds++;
						currentProcess.thread.sleep(delayInMilliseconds);
					}
				} else {
					currentProcess.thread.sleep(delayInMilliseconds);
				}
			}
		} catch (InterruptedException e) {
		}
	}
}
