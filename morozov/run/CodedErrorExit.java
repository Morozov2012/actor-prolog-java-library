// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.terms.*;

import java.math.BigInteger;

public class CodedErrorExit extends ErrorExit {
	//
	protected BigInteger numericCode;
	//
	public CodedErrorExit(ChoisePoint cp, BigInteger number) {
		choisePoint= cp;
		numericCode= number;
	}
	//
	@Override
	public boolean isNormalTerminationOfTheProgram() {
		if (numericCode.compareTo(BigInteger.ZERO)==0) {
			return true;
		} else {
			return false;
		}
	}
	//
	@Override
	public Term createTerm() {
		return new PrologInteger(numericCode);
	}
	@Override
	public String toString() {
		return numericCode.toString();
	}
}
