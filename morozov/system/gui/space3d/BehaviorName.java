// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d;

import morozov.terms.*;

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
	public Term toTerm() {
		if (isTextName) {
			return new PrologString(textName);
		} else {
			return new PrologInteger(numericalName);
		}
	}
}
