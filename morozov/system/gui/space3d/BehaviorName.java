// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d;

import morozov.run.*;
import morozov.system.gui.space3d.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.math.BigInteger;

public class BehaviorName {
	//
	public String textName= "";
	public BigInteger numericalName;
	public boolean isTextName= true;
	//
	public BehaviorName(String text) {
		textName= text;
		isTextName= true;
	}
	public BehaviorName(BigInteger number) {
		numericalName= number;
		isTextName= false;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static BehaviorName termToBehaviorName(Term value, ChoisePoint iX) {
		try {
			BigInteger number= value.getIntegerValue(iX);
			return new BehaviorName(number);
		} catch (TermIsNotAnInteger e1) {
			try {
				String text= value.getStringValue(iX);
				return new BehaviorName(text);
			} catch (TermIsNotAString e2) {
				throw new WrongArgumentIsNotBehaviorName(value);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		if (isTextName) {
			return new PrologString(textName);
		} else {
			return new PrologInteger(numericalName);
		}
	}
}
