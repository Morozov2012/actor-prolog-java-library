/*
 * @(#)IntegerField.java 1.0 2009/11/23
 *
 * Copyright 2009 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

/*
 * IntegerField implementation for the Actor Prolog language
 * @version 1.0 2009/11/23
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.run.*;
import morozov.system.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.common.*;
import morozov.system.gui.dialogs.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.math.BigInteger;

public class IntegerField extends ActiveTextField {
	public IntegerField(AbstractDialog target, int columns) {
		this(target,"",columns);
	}
	public IntegerField(AbstractDialog target, String text, int columns) {
		super(target);
		component= new AIntegerField(target,this,text,columns);
	}
	//
	public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
		try {
			BigInteger number= Converters.termToRoundInteger(value,iX,false);
			return new PrologInteger(number);
		} catch (TermIsNotAnInteger e) {
			throw RejectValue.instance;
		}
	}
}